import java.util.Arrays;
import java.util.List;
import static spark.Spark.*;

public class Main {
    static CoordinateService coordinateService = new CoordinateService();
    static List<String> list;
    public static void main(String[] args) {
        staticFiles.location("/");
        port(8080);
        get("/", (req, res) -> {res.redirect("index.html"); return null;});
        post("/coordinate", (request, response) -> {
            try {
                list = Arrays.asList(request.body().split(","));
                double latitude = Math.round(Double.parseDouble(list.get(0).replaceAll("\\[|\\]", "")) * 100000d) / 100000d;
                double longitude = Math.round(Double.parseDouble(list.get(1).replaceAll("\\[|\\]", "")) * 100000d) / 100000d;
                coordinateService.add(latitude,longitude);
            } catch (ArrayIndexOutOfBoundsException aie) {
                aie.printStackTrace();
            }
            return "";
        });
        get("/coordinate",(request, response) -> coordinateService.returnPolyLine());
    }
}