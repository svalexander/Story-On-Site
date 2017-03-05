package nyc.c4q.helenchan.makinghistory.model;

/**
 * Created by akashaarcher on 3/4/17.
 */

public class Feature {

    private String type;
    private Properties properties;
    private Geometry geometry;

//    public Feature(String type, Properties properties, Geometry geometry) {
//        this.type = type;
//        this.properties = properties;
//        this.geometry = geometry;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }




}
