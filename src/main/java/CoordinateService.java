import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CoordinateService {
    private static Map<Integer, coordinate> coordinateMap = new HashMap<>();
    private static final AtomicInteger count = new AtomicInteger(0);
    private static JSONArray polyArray = new JSONArray();
    public coordinate add(double latitude, double longitude) {
        int currentId = count.incrementAndGet();
        coordinate coordinate = new coordinate(latitude, longitude);
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

    public CoordinateService(){

    }

    public void constructPolyLine(Map<Integer, coordinate> coordinateMap, JSONArray array){
        array.clear();
        coordinateMap.forEach((key, value) -> {
            ArrayList<Double> temp = new ArrayList<>();
            coordinate MapCoordinate = value;
            temp.add(MapCoordinate.get_latitude());
            temp.add(MapCoordinate.get_longitude());
            array.add(temp);
        });
    }
}
