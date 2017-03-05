package nyc.c4q.helenchan.makinghistory.models;

/**
 * Created by leighdouglas on 3/4/17.
 */

public class Content {
    String name;
    String type;
    String text;
    String folder;
    String url;
    String year;


    public Content(String name, String type, String text, String folder, String url, String year) {
        this.name = name;
        this.type = type;
        this.text = text;
        this.folder = folder;
        this.url = url;
        this.year = year;
    }

    public Content() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
