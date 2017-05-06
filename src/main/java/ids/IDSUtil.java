package ids;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkdulay on 4/29/17.
 */
public class IDSUtil {

    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static XPathFactory xPathfactory = XPathFactory.newInstance();

    public static List<FileNodes> getFileNodes(File file, String xpathStr) {
        return getFileNodes(file.getAbsolutePath(), xpathStr);
    }

    public static List<FileNodes> getFileNodes(String path, String xpathStr) {

        List<FileNodes> list = new ArrayList<>();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile(xpathStr);
            NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                list.add(new FileNodes(path, node));
            }

            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
