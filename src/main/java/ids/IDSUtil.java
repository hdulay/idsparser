package ids;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkdulay on 4/29/17.
 */
public class IDSUtil {

    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static XPathFactory xPathfactory = XPathFactory.newInstance();
    private static IDSUtil instance = null;

    public synchronized static IDSUtil instance() {
        if (instance == null)
            instance = new IDSUtil();
        return instance;
    }

    public static List<Node> getNodes(String path, String xpathStr) throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {

        List<Node> list = new ArrayList<>();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(path));
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile(xpathStr);
        NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            list.add(node);
        }

        return list;
    }

}
