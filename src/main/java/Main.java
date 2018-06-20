import java.util.Arrays;
import java.util.List;
import static spark.Spark.*;

public class Main {
    static CoordinateService coordinateService = new CoordinateService();
    static List<String> list;
    public static void main(String[] args) {
        port(8080);
        get("/", (req, res) -> "Hello World");
        post("/coordinate/add", (request, response) -> {
            try {
                list = Arrays.asList(request.body().split(","));
                double latitude = Double.parseDouble(list.get(0).replaceAll("\\[|\\]", ""));
                double longitude = Double.parseDouble(list.get(1).replaceAll("\\[|\\]", ""));
                coordinateService.add(latitude,longitude);
            } catch (ArrayIndexOutOfBoundsException aie) {
                aie.printStackTrace();
            }
            return "";
        });
        get("/coordinate",(request, response) -> coordinateService.findall().toString());
        get("/listall",(request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            return coordinateService.returnPolyLine();
        });
    }
}