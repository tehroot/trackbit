import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CoordinateService {
    static PsqlConnector psqlConnector = new PsqlConnector();
    private static Map<Integer, Coordinate> coordinateMap = new HashMap<>();
    private static Map<String, HashMap> storedRoutes = new HashMap<>();
    private static int count = 0;
    private static JSONArray polyArray = new JSONArray();
    protected void add(double latitude, double longitude, long timestamp, ArrayList<String> arguments) {
        int currentId = count++;
        Coordinate coordinate = new Coordinate(latitude, longitude, timestamp);
        coordinateMap.put(currentId, coordinate);
        try {
            PreparedStatement insert = psqlConnector.insertDB(psqlConnector.initConnection(arguments), currentId, objectToByteArray(coordinateMap.get(currentId)), coordinateMap.get(currentId).Timestamp);
            boolean result = psqlConnector.transactDB(insert);
            if(result != true){
                throw new SQLException("Unknown SQL exception, likely an error writing values, look at DB.");
            } else {
                throw new SQLException("Unknown SQL exception, no known issue, check DB.");
            }
        } catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }

    protected void createNewRoute(Map coordinateMap, Map storedRoutes) throws IOException{
        storedRoutes.put(shaHash(objectToByteArray(coordinateMap)), coordinateMap);
        coordinateMap.clear();
    }

    protected String finishRoute() throws IOException{
        createNewRoute(coordinateMap, storedRoutes);
        return "";
    }

    protected List findall(){
        return new ArrayList(coordinateMap.values());
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

    protected String shaHash(byte[] array) {
        String digest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            digest =  byteArrayToHex(md.digest(array));
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return digest;
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
        //TODO -- Buffout
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

    protected byte[] objectToByteArray(Object object) throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutput objectOutput = null;
        try {
            objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(object);
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
