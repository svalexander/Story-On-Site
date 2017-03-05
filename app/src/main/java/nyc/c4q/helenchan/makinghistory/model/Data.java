package nyc.c4q.helenchan.makinghistory.model;

/**
 * Created by akashaarcher on 3/4/17.
 */

public class Data {

    private String uuid;
    private String text;
    private String folder;
    private String url;
    private String imageUrl;
    private String nyplUrl;


//    public Data(String uuid, String text, String folder, String url, String imageUrl, String nyplUrl) {
//        this.uuid = uuid;
//        this.text = text;
//        this.folder = folder;
//        this.url = url;
//        this.imageUrl = imageUrl;
//        this.nyplUrl = nyplUrl;
//    }



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNyplUrl() {
        return nyplUrl;
    }

    public void setNyplUrl(String nyplUrl) {
        this.nyplUrl = nyplUrl;
    }


}
