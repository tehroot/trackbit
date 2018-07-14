public class Coordinate {

    public double Latitude;
    public double Longitude;
    public long Timestamp;

    public Coordinate(){}

    public Coordinate(double Latitude, double Longitude, long Timestamp) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Timestamp = Timestamp;
    }

    public void set_latitude(double Latitude){ this.Latitude = Latitude; }

    public double get_latitude(){
        return Latitude;
    }

    public void set_longitude(double Longitude){
        this.Longitude = Longitude;
    }

    public double get_longitude(){
        return Longitude;
    }

    public void set_timestamp(long Timestamp) {this.Timestamp = Timestamp; }

    public double get_timestamp() { return Timestamp; }

    public String toString(){
        return "Coordinate -- Latitude: "+Latitude+", Longitude: "+Longitude+". Timestamp:"+Timestamp;
    }
}
