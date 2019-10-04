import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CoordinateService {
    static PsqlConnector psqlConnector = new PsqlConnector();
    private HashMap<Integer, Coordinate> coordinateMap = new HashMap<>();
    private HashMap<String, HashMap> storedRoutes = new HashMap<>();
    private int count = 0;
    private JSONArray polyArray = new JSONArray();

    protected void add(String path_route_id, double latitude, double longitude, long timestamp) {
        int currentId = count++;
        Coordinate coordinate = new Coordinate(path_route_id, latitude, longitude, timestamp);
        coordinateMap.put(currentId, coordinate);
    }

    protected String createNewRoute() throws IOException{
        //GENERATE UUID
        //PASSBACK UUID TO WATCH FOR USAGE
        //WHEN POSTING COORDINATES
        return "";
    }

    protected JSONObject getAllRoutes(Map<String, HashMap> storedRoutes){
        JSONObject obj = new JSONObject();
        storedRoutes.forEach((key, value) -> {
            HashMap<Integer, Coordinate> map = value;
            map.forEach((key1, value1) -> {
                Coordinate coordinate = value1;
                obj.put(key, coordinate.Timestamp);
            });
        });
        return obj;
    }

    protected String finishRoute(ArrayList<String> arguments) throws IOException{
        long now = Instant.now().toEpochMilli();
        //not sure what I remember that this is supposed to do exactly?
        //String hash = createNewRoute(coordinateMap, storedRoutes);
        return "";
    }

    protected JSONObject returnPolyLine(){
        constructPolyLine(coordinateMap, polyArray);
        JSONObject obj = new JSONObject();
        obj.put("coordinates", polyArray);
        return obj;
    }

    protected String clearPolyLine(){
        clearPolyLineData(coordinateMap, polyArray);
        if(coordinateMap.size() == 0 && polyArray.size() == 0){
            return "clear";
        }
        return "";
    }

    protected String byteArrayToHex(byte[] array) throws ArrayIndexOutOfBoundsException{
        Formatter formatter = new Formatter();
        for (byte b : array){
            formatter.format("%02X", b);
        }
        return formatter.toString();
    }

    protected void constructPolyLine(Map<Integer, Coordinate> coordinateMap, JSONArray array){
        array.clear();
        coordinateMap.forEach((key, value) -> {
            ArrayList<Double> temp = new ArrayList<>();
            Coordinate MapCoordinate = value;
            temp.add(MapCoordinate.get_latitude());
            temp.add(MapCoordinate.get_longitude());
            array.add(temp);
        });
    }

    protected void clearPolyLineData(Map<Integer, Coordinate> coordinateMap, JSONArray array){
        array.clear();
        coordinateMap.clear();
    }

    protected Stream returnDistance(){
        return calculateDistance(coordinateMap);
    }

    protected Stream calculateDistance(Map<Integer, Coordinate> coordinateMap){
        //TODO -- Implement windowing on hashmap for haversine calculation
        List keySet = setToList(coordinateMap);
        return sliding(keySet, 2);
    }

    public double Haversine(Coordinate coordinate1, Coordinate coordinate2){
        //TODO -- Comparison of Law of cosines in small distance accuracy discrepancies
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
        //calculate the distance between by using the product of the radius of earth and the value of c
        double d = radius * c;
        //return distance
        return d;
    }

    protected List setToList(Map map){
        //entry set to list for window purposes
        List returnList = new ArrayList();
        returnList.addAll(map.keySet());
        return returnList;
    }

    protected <T> Stream<List<T>> sliding(List<T> list, int size){
        //TODO -- Revisit
        if (size > list.size()){
            return Stream.empty();
        } else {
            //returns a ranged intstream of the list(window)
            return IntStream.range(0, list.size() - size + 1).mapToObj(start -> list.subList(start, start + size));
        }
    }

    public CoordinateService(){

    }

}