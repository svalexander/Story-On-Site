package nyc.c4q.helenchan.makinghistory.models.nypl;

/**
 * Created by akashaarcher on 3/4/17.
 */

public class Geometry {

    private String type;

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    private double[] coordinates;

//    public Geometry(String type, List<Double> coordinates) {
//        this.type = type;
//        this.coordinates = coordinates;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
