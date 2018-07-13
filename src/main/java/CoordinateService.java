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

    public CoordinateService(){

    }

}
