import java.io.IOException;
import java.sql.*;
import java.util.*;
import org.slf4j.*;

public class PsqlConnector {

    public static Connection initConnection(String user, String password, String databaseName) throws SQLException{
        String url = "jdbc:postgresql://localhost/"+databaseName+'?'+user+"&"+password;
        Connection connection = DriverManager.getConnection(url);
        return connection;
    }

    public static void transactDB(Connection connection, PreparedStatement statement) throws SQLException{

    }

    public static void binaryStreamLength() throws IOException{

    }

}
