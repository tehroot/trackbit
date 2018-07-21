import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import static spark.Spark.*;

public class Main {

    static CoordinateService coordinateService = new CoordinateService();
    public static void main(String[] args) {
        final List<String> list;
        staticFileLocation("/public");
        port(8080);
        get("/", (req, res) -> {res.redirect("index.html"); return "";});
        post("/coordinate", (request, response) -> {
            try {
                byte[] byteList = request.bodyAsBytes();
                ObjectMapper objectMapper = new ObjectMapper();
                Coordinate coordinate = objectMapper.readValue(byteList, Coordinate.class);
                coordinateService.add(coordinate.Latitude, coordinate.Longitude, coordinate.Timestamp);
            } catch (ArrayIndexOutOfBoundsException aie) {
                aie.printStackTrace();
            }
            return "";
        });
        get("/coordinate",(request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            response.type("application/json");
            return coordinateService.returnPolyLine();
        });

        post("/distance", (request, response) -> coordinateService.returnDistance());
        post("/clear", ((request, response) -> coordinateService.clearPolyLine()));
    }
}