package ids;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkdulay on 4/30/17.
 */
public class Field {
    private String name;
    private File file;
    private String doc;
    private Type type;
    private List<String> links = new ArrayList<>();


    public Field(String name, File file, String doc) {
        this.name = name;
        this.file = file;
        this.doc = doc;
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
        String patient = doc != null && doc.toLowerCase().contains("patient") ? "patient" : "unknown";
        switch (type) {
            case OmniDate:
                return "[" + file.getName() + "] [" + name + "] [" + type + "] ["+patient+"] : " + doc;
            case OmniLink:
                return "[" + file.getName() + "] [" + name + "] [" + type + "] ["+patient+"] : " + links;
        }
        return "unknown field";
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

}
