package ids;

import org.w3c.dom.Node;

/**
 * Created by hkdulay on 5/6/17.
 */
public class FileNodes {

    private final String path;
    private final Node node;

    public FileNodes(String path, Node node) {
        this.path = path;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
    public String getPath() {
        return path;
    }
}
