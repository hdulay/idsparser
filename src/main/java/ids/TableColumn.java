package ids;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

/**
 * Created by hkdulay on 5/6/17.
 */
public class TableColumn {

    private CreateTable create;
    private ColumnDefinition col;
    private Field field;

    public TableColumn(CreateTable create, ColumnDefinition col, Field field) {
        this.create = create;
        this.col = col;
        this.field = field;
    }

    public ColumnDefinition getCol() {
        return col;
    }

    public void setCol(ColumnDefinition col) {
        this.col = col;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public CreateTable getCreate() {
        return create;
    }

    public void setCreate(CreateTable create) {
        this.create = create;
    }

    @Override
    public String toString() {
        String parent = field.getNode().getParentNode().getAttributes().getNamedItem("name").getNodeValue();
        return field.getFile().getName()+","+parent+","+field.getName()+","+create.getTable()+","+col.getColumnName()+","+field.getDoc();
    }
}
