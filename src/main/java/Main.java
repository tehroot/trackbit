import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import static spark.Spark.*;

public class Main {

    static CoordinateService coordinateService = new CoordinateService();
    static UtilityMethods utilityMethods = new UtilityMethods();
    public static void main(String[] args) throws Exception{
        final List<String> list;
        staticFileLocation("/public");
        port(8080);

        final ArrayList<String> arguments = UtilityMethods.commandLineArgs(args);
        get("/", (req, res) -> {res.redirect("index.html"); return "";});
        post("/coordinate", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            response.type("application/json");
            try {
                byte[] byteList = request.bodyAsBytes();
                ObjectMapper objectMapper = new ObjectMapper();
                Coordinate coordinate = objectMapper.readValue(byteList, Coordinate.class);
                coordinateService.add(coordinate.Latitude, coordinate.Longitude, coordinate.Timestamp, arguments);
            } catch (ArrayIndexOutOfBoundsException aie) {
                aie.printStackTrace();
            }
            return "";
        });
        get("/polyline",(request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            response.type("application/json");
            return coordinateService.returnPolyLine();
        });
        /*
        get("/coordinates", (request, response) -> {
           response.header("Access-Control-Allow-Origin", "*");
           response.header("Access-Control-Allow-Methods", "GET");
           response.type("application/json");
           //TODO - return list of coordinates from postgres
        });
        */

        get("/routes", (request, response) -> coordinateService.allRoutes());
        post("/finished", (request, response) -> coordinateService.finishRoute());
        get("/distance", (request, response) -> coordinateService.returnDistance());
        post("/clear", (request, response) -> coordinateService.clearPolyLine());
    }
}