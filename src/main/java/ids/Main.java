package ids;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by hkdulay on 4/30/17.
 */
public class Main {

    private static final NormalizedLevenshtein distance = new NormalizedLevenshtein();
    private static final double limit = .3; // 0 == exact match, 1 == completely different

    public static void main(String... args) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, ParseException, JSQLParserException {

        String[] ext = {"xml"};
        Collection<File> files = FileUtils.listFiles(new File("ids"), ext, true); // ids directory
        List<CreateTable> statements = SQLUtil.parse(new File("ddl/omni_tables.sql")); // sql file

        files.stream()
                .flatMap(file -> IDSUtil.getFileNodes(file, "//element[@type='OmniDate']").stream()) // OmniDates
                .map(Main::getField)
                .flatMap(field -> Arrays.stream(findTableColumn(field, statements)))
                .forEach(System.out::println);
    }

    private static TableColumn[] findTableColumn(Field field, List<CreateTable> statements) {
        Node parent = field.getNode().getParentNode();
        String parentName = parent.getAttributes().getNamedItem("name").getNodeValue();

        TableColumn[] tc = statements.stream().
                filter(create ->
                        distance.distance(create.getTable().getName().toLowerCase(), parentName.toLowerCase()) < limit
                ).
                flatMap(create ->
                        create.getColumnDefinitions().stream()
                                .filter(col -> distance.distance(col.getColumnName().toLowerCase(), field.getName().toLowerCase()) < limit)
                                .map(col -> new TableColumn(create, col, field))
                ).
                toArray(size -> new TableColumn[size]);

        return tc;
    }

    private static Field getField(FileNodes fn) {
        String name = fn.getNode().getAttributes().getNamedItem("name").getNodeValue();
        Field f = new Field(name, new File(fn.getPath()), null, fn.getNode());
        NamedNodeMap attributes = fn.getNode().getAttributes();
        Node dbColumn = attributes.getNamedItem("dbColumn");
        if (dbColumn != null)
            f.setDbColumn(attributes.getNamedItem("dbColumn").getNodeValue());
        else {
            String dbColumnName = fn.getNode().getAttributes().getNamedItem("name").getNodeValue();
            f.setDbColumn(dbColumnName);
        }

        NodeList kids = fn.getNode().getChildNodes();
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
        return f;
    }
}
