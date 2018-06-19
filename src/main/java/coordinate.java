public class coordinate {

    double Latitude;
    double Longitude;

    public coordinate(){}

    public coordinate(double Latitude, double Longitude) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public void set_latitude(double Latitude){
        this.Latitude = Latitude;
    }

    public double get_latitude(){
        return Latitude;
    }

    public void set_longitude(double Longitude){
        this.Longitude = Longitude;
    }

    public double get_longitude(){
        return Longitude;
    }

    public String toString(){
        return "Coordinate -- Latitude: "+Latitude+", Longitude: "+Longitude;
    }
}
