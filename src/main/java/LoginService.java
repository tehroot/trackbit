import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginService {
    PsqlConnector psqlConnector = new PsqlConnector();
    static UtilityMethods utilityMethods = new UtilityMethods();

    protected void login(JsonNode jsonNode){

    }

    protected Boolean createUser(JsonNode jsonNode){
        ArrayList<byte[]> hashed = new ArrayList<byte[]>();
        String email = jsonNode.get("email").asText();
        String password = jsonNode.get("password").asText();
        hashed = utilityMethods.PBKDF2(password);
        try{
            return psqlConnector.writeUserToDb(hashed, email);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected Boolean checkUser(JsonNode jsonNode){

    }
}
