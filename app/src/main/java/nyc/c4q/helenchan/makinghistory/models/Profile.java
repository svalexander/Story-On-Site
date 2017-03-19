package nyc.c4q.helenchan.makinghistory.models;

/**
 * Created by helenchan on 3/18/17.
 */

public class Profile {
    private String picUrl;
    private String bio;


    public Profile(String picUrl, String bio) {
        this.picUrl = picUrl;
        this.bio = bio;
    }

    public Profile() {
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
