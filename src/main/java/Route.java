import java.util.ArrayList;

public class Route {
    public String path_route_id;
    public ArrayList<Coordinate> coordinates;

    public Route(){}

    public Route(String id, String path_route_id, ArrayList coordinates){
        this.path_route_id = path_route_id;
        this.coordinates = coordinates;
    }

    //public void set_id(String id){ this.id = id; }

    //public String get_id() { return id; }

    public void set_path_route_id(String path_route_id) { this.path_route_id = path_route_id; }

    public String get_path_route_id() { return path_route_id; }

    public void add_Coordinates(Coordinate coordinate) { this.coordinates.add(coordinate); }

}