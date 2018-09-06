import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CoordinateService {
    static PsqlConnector psqlConnector = new PsqlConnector();
    private static Map<Integer, Coordinate> coordinateMap = new HashMap<>();
    private static Map<Integer, HashMap> storedRoutes = new HashMap<>();
    private static int count = 0;
    private static JSONArray polyArray = new JSONArray();
    public Coordinate add(double latitude, double longitude, long timestamp, ArrayList<String> arguments) {
        int currentId = count++;
        Coordinate coordinate = new Coordinate(latitude, longitude, timestamp);
        coordinateMap.put(currentId, coordinate);
        try {
            psqlConnector.insertDB(psqlConnector.initConnection(arguments), currentId, coordinateToByteArray(coordinateMap.get(currentId)), coordinateMap.get(currentId).Timestamp);
        } catch (IOException |SQLException e){
            e.printStackTrace();
        }

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

    protected Stream returnDistance(){
        //TODO -- Buffout
        return calculateDistance(coordinateMap);
    }

    public Stream calculateDistance(Map<Integer, Coordinate> coordinateMap){
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

    protected static List setToList(Map map){
        //entry set to list for window purposes
        List returnList = new ArrayList();
        returnList.addAll(map.keySet());
        return returnList;
    }

    protected static <T> Stream<List<T>> sliding(List<T> list, int size){
        //TODO -- Revisit
        if (size > list.size()){
            return Stream.empty();
        } else {
            //returns a ranged intstream of the list(window)
            return IntStream.range(0, list.size() - size + 1).mapToObj(start -> list.subList(start, start + size));
        }
    }

    protected static byte[] coordinateToByteArray(Coordinate coordinate) throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutput objectOutput = null;
        try {
            objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(coordinate);
            objectOutput.flush();
            byte[] coordinateSerialized = byteOutput.toByteArray();
            return coordinateSerialized;
        } finally {
            try {
                byteOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CoordinateService(){

    }

}
