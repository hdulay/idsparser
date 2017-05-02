import ids.Field;
import ids.IDSUtil;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

/**
 * Created by hkdulay on 4/29/17.
 */
public class IDSTest {

    @Test
    public void evaluatesExpression() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        String path = "ids/Clinical/Encounter_1.2.2.xml";

        IDSUtil.instance().getNodes(path, "//element[@type='OmniDate']")
                .stream()
                .map(node -> {
                    return getField(node, path);
                })
                .filter(field -> field.getDoc().toLowerCase().contains("patient"))
                .forEach(System.out::println);
    }

    private static Field getField(Node node, String file) {
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        Field f = new Field(name,  new File(file),null);
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
        return f;
    }
}
