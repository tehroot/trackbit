import com.fasterxml.jackson.databind.JsonNode;
import spark.Request;

import java.util.ArrayList;

public class LoginService {
    //static UtilityMethods utilityMethods = new UtilityMethods();
    //Implementation of sessioning for users inside the login service in order to
    //facilitate session management and cookie generation required

    protected static Boolean createUser(JsonNode jsonNode){
        ArrayList<byte[]> hashed;
        ArrayList<JsonNode> collect = new ArrayList<>();
        //JSONnode shit in here, need to fix in javascript, server no problem
        String email = jsonNode.findValue("email").asText();
        String password = jsonNode.findValue("password").asText();
        hashed = UtilityMethods.PBKDF2(password, UtilityMethods.generateSalt());
        try{
            if(checkUser(jsonNode) == false){
                return PsqlConnector.writeUserToDb(hashed, email);
            } else {
                return false;
            }
            //TODO -- catch propagated nulls
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected static Boolean checkUser(JsonNode jsonNode){
        //TODO - include provision for keepalive token generation
        ArrayList result = PsqlConnector.returnUserFromDb(jsonNode.get("email").asText());
        if(result == null){
            return false;
        } else {
            return true;
        }
     }

    protected static Boolean checkUserLogin(JsonNode jsonNode, Request req){
        if(jsonNode.get("stayLoggedIn").asText() != null){
            //need to check for an additional json body payload inclusion
            //of a token?
            //if no token, re-issue token? generate new info
            //not sure about best practices exactly on inclusion in payload body
        } else {
            ArrayList result = PsqlConnector.returnUserFromDb(jsonNode.get("email").asText());
            if(result == null){
                return false;
            } else if (UtilityMethods.validatePBKDF2(jsonNode.get("password").asText(), (byte[])result.get(1), (byte[])result.get(2)))  {
                if(getSessionAuth(req) == null){
                    addSessionAuth(req, new User(jsonNode.get("email").asText()));
                } else {
                    removeSessionauth(req);
                    //reissue session auth
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    //CURRENT TODO
    //token for session managment, remember me stuff
    protected static String issueToken(Request req){
        if(getSessionAuth(req) != null){
           //512bit token to reduce possible generation attacks for authentication of data??
           //no user login binding so it's not super necessary but it shouldn't be that performance difficult
            byte[] token =  UtilityMethods.generateSalt();

        }
    }


    //OH LOOK THE GOOD SHIT IS HERE
    //yay sessions -> caching -> ????????

    protected static void addSessionAuth(Request req, User u){
        req.session().attribute("username", u);
    }

    protected static void removeSessionauth(Request req){
        req.session().removeAttribute("username");
    }

    protected static User getSessionAuth(Request req){
        return req.session().attribute("username");
    }

    protected static void reissueSessionAuth(Request req){
        req.session().invalidate();
        //request re-authorization for sessionauth
    }
}
