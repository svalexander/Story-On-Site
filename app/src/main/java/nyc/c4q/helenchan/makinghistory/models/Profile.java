package nyc.c4q.helenchan.makinghistory.models;

/**
 * Created by helenchan on 3/18/17.
 */

public class Profile {
    private String picUrl;
    private String userBio;


    public Profile(String picUrl, String userBio) {
        this.picUrl = picUrl;
        this.userBio = userBio;
    }

    public Profile() {
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }
}
