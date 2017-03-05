package nyc.c4q.helenchan.makinghistory.model;

/**
 * Created by akashaarcher on 3/4/17.
 */

public class Properties {

    private String id;
    private String type;
    private String name;
    private Data data;
    private int validSince;
    private int validUntil;

//    public Properties(String id, String type, String name, Data data, int validSince, int validUntil) {
//        this.id = id;
//        this.type = type;
//        this.name = name;
//        this.data = data;
//        this.validSince = validSince;
//        this.validUntil = validUntil;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getValidSince() {
        return validSince;
    }

    public void setValidSince(int validSince) {
        this.validSince = validSince;
    }

    public int getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(int validUntil) {
        this.validUntil = validUntil;
    }
}
