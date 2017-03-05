package nyc.c4q.helenchan.makinghistory.leigh;

import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.UserPhoto;

/**
 * Created by leighdouglas on 3/2/17.
 */

public class User {
    String name;
    String message;
    List<UserPhoto> photos;

    public void setPhotos(List<UserPhoto> photos) {
        this.photos = photos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User(String name, String message){
        this.name = name;
        this.message = message;
    }

    public User(){

    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public List<UserPhoto> getPhotos(){
        return photos;
    }
}
