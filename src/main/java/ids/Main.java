package ids;

import org.apache.commons.cli.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Created by hkdulay on 4/30/17.
 */
public class Main {

    public static void main(String... args) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, ParseException {

        Options options = new Options();
        options.addOption("d", true, "directory to search for IDS files");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        recurse(cmd.getOptionValue("d", "ids"), "Encounter", new ArrayList<>());
    }

    private static void recurse(String directory, String ids, List<String> visited) {
        try {
            List<String> paths = getFiles(directory, ids);
            for (String path : paths) {

                if (visited.contains(path)) continue;
                visited.add(path);

                Field[] fields = IDSUtil.instance().getNodes(path, "//element[@type='OmniDate' or @type='OmniLink']")
                        .stream()
                        .map(node -> {
                            return getField(node, path);
                        }).toArray(size -> new Field[size]);

                Field[] dates = Arrays.stream(fields).filter(field -> field.getType() == Type.OmniDate
                        //&& field.getDoc().toLowerCase().contains("patient")
                ).toArray(size -> new Field[size]);
                Arrays.stream(dates).forEach(System.out::println);
                String[] lines = Arrays.stream(dates).map(date -> date.toString()).toArray(size -> new String[size]);
                Files.write(new File("output.txt").toPath(), Arrays.asList(lines), StandardOpenOption.APPEND, StandardOpenOption.CREATE);

                Field[] links = Arrays.stream(fields).filter(field -> field.getType() == Type.OmniLink).toArray(size -> new Field[size]);
                Arrays.stream(links).forEach(link -> link.getLinks().stream().forEach(l -> recurse(directory, l, visited)));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    static class Matcher implements BiPredicate<Path, BasicFileAttributes> {

        private final String ids;

        public Matcher(String ids) {
            this.ids = ids;
        }

        @Override
        public boolean test(Path path, BasicFileAttributes atts) {
            String name = path.toFile().getName();
            return name.startsWith(ids + "_") && name.endsWith(".xml");
        }
    }

    private static List<String> getFiles(String dir, String ids) throws IOException {
        Matcher matcher = new Matcher(ids);
        List<String> files = new ArrayList<>();
        Files.find(Paths.get(dir), 4, matcher).forEach(path -> files.add(path.toFile().getAbsolutePath()));
        return files;
    }

    private static Field getField(Node node, String file) {
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        Field f = new Field(name, new File(file), null);
        NamedNodeMap attributes = node.getAttributes();
        if (attributes.getNamedItem("type").getNodeValue().equals("OmniLink")) {
            f.addLink(attributes.getNamedItem("contains").getNodeValue());
            f.setType(Type.OmniLink);

        } else {
            f.setType(Type.OmniDate);
            NodeList kids = node.getChildNodes();
            for (int i = 0; i < kids.getLength(); i++) {
                Node kid = kids.item(i);
                switch (kid.getNodeType()) {
                    case Node.ELEMENT_NODE:
                        if (kid.getNodeName().equals("documentation")) {
                            Node text = kid.getFirstChild();
                            f.setDoc(text.getNodeValue().trim());
                        }
                }
                if (f.getDoc() != null) break;
            }
        }
        return f;
    }
}
