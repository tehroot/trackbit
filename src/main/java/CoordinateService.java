import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CoordinateService {
    private static Map<Integer, Coordinate> coordinateMap = new HashMap<>();
    private static final AtomicInteger count = new AtomicInteger(0);
    private static JSONArray polyArray = new JSONArray();
    public Coordinate add(double latitude, double longitude, long timestamp) {
        int currentId = count.incrementAndGet();
        Coordinate coordinate = new Coordinate(latitude, longitude, timestamp);
        coordinateMap.put(currentId, coordinate);
        return coordinate;
    }

    public List findall(){
        return new ArrayList(coordinateMap.values());
    }

    public JSONObject returnPolyLine(){
        constructPolyLine(coordinateMap, polyArray);
        JSONObject obj = new JSONObject();
        obj.put("coordinates", polyArray);
        return obj;
    }

    public String clearPolyLine(){
        clearPolyLineData(coordinateMap, polyArray);
        if(coordinateMap.size() == 0 && polyArray.size() == 0){
            return "clear";
        }
        return "";
    }

    public void constructPolyLine(Map<Integer, Coordinate> coordinateMap, JSONArray array){
        array.clear();
        coordinateMap.forEach((key, value) -> {
            ArrayList<Double> temp = new ArrayList<>();
            Coordinate MapCoordinate = value;
            temp.add(MapCoordinate.get_latitude());
            temp.add(MapCoordinate.get_longitude());
            array.add(temp);
        });
    }

    public void clearPolyLineData(Map<Integer, Coordinate> coordinateMap, JSONArray array){
        array.clear();
        coordinateMap.clear();
    }

    public void calculateDistance(Map<Integer, Coordinate> coordinateMap){
        int remainder = coordinateMap.size() % 2;

    }

    public double Haversine(Coordinate coordinate1, Coordinate coordinate2){
        //radius of the earth in meters
        int radius = 6371000;
        //phi = latitude, lambda = longitude
        double Phi1 = Math.toRadians(coordinate1.get_latitude());
        double Phi2 = Math.toRadians(coordinate2.get_latitude());
        double DeltaPhi = Math.toRadians(coordinate2.get_latitude() - coordinate1.get_latitude());
        double DeltaLambda = Math.toRadians(coordinate2.get_longitude() - coordinate1.get_longitude());
        //take sin squared of both DeltaPhi and DeltaLambda
        double x = Math.pow(Math.sin(DeltaPhi / 2), 2);
        double y = Math.pow(Math.sin(DeltaLambda / 2), 2);
        //solve for a using above
        double a = x + Math.cos(Phi1) * Math.cos(Phi2) * y;
        //take the arctangent of the sqrt of the first coordinate, and the sqrt of 1 - first coordinate
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = radius * c;
        return d;
    }

    public CoordinateService(){

    }

}
