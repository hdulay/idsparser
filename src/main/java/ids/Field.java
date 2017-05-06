package ids;

import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkdulay on 4/30/17.
 */
public class Field {
    private Node node;
    private String name;
    private File file;
    private String doc;
    private Type type;
    private List<String> links = new ArrayList<>();
    private String dbColumn;

    public Field(String name, File file, String doc, Node node) {
        this.name = name;
        this.file = file;
        this.doc = doc;
        this.node = node;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return file.getName() + "," + name + "," + dbColumn;
    }

    public File getFile() {
        return file;
    }

    public void setFilee(File file) {
        this.file = file;
    }

    public void addLink(String link) {
        links.add(link);
    }

    public List<String> getLinks() {
        return links;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDbColumn(String dbColumn) {
        this.dbColumn = dbColumn;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
