package nyc.c4q.helenchan.makinghistory.models;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContent {

    private String userId;
    private String userProfilePhoto;
    private String userImageUrl;
    private int numUserPhotos;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public int getNumUserPhotos() {
        return numUserPhotos;
    }

    public void setNumUserPhotos(int numUserPhotos) {
        this.numUserPhotos = numUserPhotos;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

}
