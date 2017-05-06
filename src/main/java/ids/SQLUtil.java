package ids;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkdulay on 5/6/17.
 */
public class SQLUtil {

    public static CreateTable parse(String sql) throws JSQLParserException {
        try {
            CCJSqlParserManager pm = new CCJSqlParserManager();
            return (CreateTable) pm.parse(new StringReader(sql));
        } catch (JSQLParserException e) {
            throw new JSQLParserException(sql, e);
        }
    }

    public static List<CreateTable> parse(File ddl) throws IOException, JSQLParserException {
        List<CreateTable> creates = new ArrayList<>();
        byte[] bytes = Files.readAllBytes(ddl.toPath());
        String contents = new String(bytes);
        String[] statements = contents.split(";");
        for (String stmt : statements) {
            if(stmt.trim().isEmpty()) continue;
            creates.add(parse(stmt));
        }
        return creates;
    }
}
