import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;

public class LoginService {
    PsqlConnector psqlConnector = new PsqlConnector();
    //static UtilityMethods utilityMethods = new UtilityMethods();
    //Implementation of sessioning for users inside the login service in order to
    //facilitate session management and cookie generation required

    protected void login(JsonNode jsonNode){

    }

    protected Boolean createUser(JsonNode jsonNode){
        ArrayList<byte[]> hashed;
        ArrayList<JsonNode> collect = new ArrayList<>();
        //JSONnode shit in here, need to fix in javascript, server no problem
        String email = jsonNode.findValue("email").asText();
        String password = jsonNode.findValue("password").asText();
        hashed = UtilityMethods.PBKDF2(password, UtilityMethods.generateSalt());
        try{
            if(checkUser(jsonNode) == false){
                return psqlConnector.writeUserToDb(hashed, email);
            } else {
                return false;
            }
            //TODO -- catch propagated nulls
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected Boolean checkUser(JsonNode jsonNode){
        ArrayList result = psqlConnector.returnUserFromDb(jsonNode.get("email").asText());
        if(result == null){
            return false;
        } else {
            return true;
        }
     }

    protected Boolean checkUserLogin(JsonNode jsonNode){
        ArrayList result = psqlConnector.returnUserFromDb(jsonNode.get("email").asText());
        if(result == null){
            return false;
        } else if (UtilityMethods.validatePBKDF2(jsonNode.get("password").asText(), (byte[])result.get(1), (byte[])result.get(2)))  {
            return true;
        } else {
            return false;
        }
    }
}
