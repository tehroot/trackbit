import com.fasterxml.jackson.databind.JsonNode;
import java.sql.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PsqlConnector {

    public static Connection initConnection() throws SQLException {
        JsonNode params = UtilityMethods.settingsRead();
        String user = params.get("user").asText();
        String password = params.get("password").asText();
        String databaseName = params.get("database").asText();
        String url = "jdbc:postgresql://localhost/"+databaseName+"?user="+user+"&password="+password;
        Connection connection = DriverManager.getConnection(url);
        return connection;
    }

    //MOVE THIS TO THE COORDINATE CONTROLLER SECTION
    //user_id generation -- required?
    public PreparedStatement writeCoordinateToDb(int user_id, String ID, String path_route_id, double latitude, double longitude, Timestamp timestamp){
        String coordinateInsert = "INSERT INTO route (id, path_route_id, latitude, longitude, timestamp) VALUES (?, ?, ?, ?, ?)";
        String checkRouteIDQuery = "SELECT (path_route_id) FROM routes WHERE (path_route_id) = ?";

        //TODO -- if the route id already exists in the database it needs to be skipped over for insertion
        //TODO -- if it has a duplicate id indicating duplicate coordinate
        //knock out table rules to determine when duplicates are possibly introduced? not worry about it?
        String routeInsert = "INSERT INTO ROUTES (user_id, id) VALUES (?, ?)";
        return null;
    }

    public static Boolean writeUserToDb(ArrayList<byte[]> hashes, String email){
        //insert checking for DB consistency, no duplicate emails, if duplicate detected
        //raise error above to raise to main to throw an error out to JS frontend...
        if (returnUserFromDb(email) == null){
            try{
                String writeUser = "INSERT INTO user_accounts (email, password_salt, password_hash) VALUES (?, ?, ?)";
                PreparedStatement userInsert = initConnection().prepareStatement(writeUser);
                userInsert.setString(1, email);
                userInsert.setBytes(2, hashes.get(0));
                userInsert.setBytes(3, hashes.get(1));
                return transactDB(userInsert);
            } catch (SQLException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    public static ArrayList returnUserFromDb(String email){
        try{
            String selectUser = "SELECT * FROM user_accounts WHERE email = ?";
            PreparedStatement userSelect = initConnection().prepareStatement(selectUser);
            userSelect.setString(1, email);
            ResultSet rs = returnDB(userSelect);
            if(rs == null){
                //TODO
                //stubbed here, need to figure out what to do if the resultset doesn't contain anything, return null?
                //if null, what to do above? null isn't a cure-all unfortunately
                return null;
            } else {
                //returns selected user from db, should only return 1 result, else handles...
                ArrayList list = new ArrayList();
                list.add(rs.getString(1));
                list.add(rs.getBytes(2));
                list.add(rs.getBytes(3));
                return list;
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static PreparedStatement writeRouteTodb(){

        return null;
    }

    public static Boolean transactDB(PreparedStatement statement) throws SQLException {
        int i = statement.executeUpdate();
        if(i == 1){
            return true;
        } else {
            return false;
        }
    }

    public static ResultSet returnDB(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs;
        } else {
            return null;
        }
    }

    protected static PreparedStatement returnStatement(Connection connection, Route route, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(null);
        return null;
    }


    protected static PreparedStatement argumentsToStatement(Route route) {

        return null;
    }

    protected static PreparedStatement deleteDB(Connection connection, int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("");
        return ps;
    }
}