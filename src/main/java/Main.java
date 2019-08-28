import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import static spark.Spark.*;

public class Main {
    static CoordinateService coordinateService = new CoordinateService();
    static LoginService loginService = new LoginService();
    //insert file reading to parse config
    //static UtilityMethods utilityMethods = new UtilityMethods();
    public static void main(String[] args) {
        final List<String> list;
        staticFileLocation("/public");
        port(6555);
        get("/", (req, res) -> {res.redirect("index.html"); return "";});

        //stub in authentication using //before in order to authorize posting routes/coordinates/other shit
        //general practical login methods

        post("/coordinate", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            response.type("application/json");

            return "";
        });

        get("/polyline",(request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            response.type("application/json");
            return coordinateService.returnPolyLine();

        });

        get("/token", (request, response) -> {
            response.type("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode requestNode = objectMapper.readTree(request.bodyAsBytes());
            return "";
        });

        post("/login", (request, response) -> {
           response.type("application/json");
           ObjectMapper objectMapper = new ObjectMapper();
           JsonNode postNode = objectMapper.readTree(request.bodyAsBytes());
            return "";
        });

        post("/register", (request, response) -> {
            //stub out generation here
            //user provides on registration page, username + password
            //need to stub out session shit as well but that needs to go in
            //login and transactional pages...

            response.type("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode postNode = objectMapper.readTree(request.bodyAsBytes());
            if(loginService.createUser(postNode)){
                response.redirect("index.html");
            } else {
                //stubbed to null for now?
                return null;
            }
            return "";
        });
    }
}