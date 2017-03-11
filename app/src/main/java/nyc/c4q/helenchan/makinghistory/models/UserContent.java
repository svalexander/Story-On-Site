package nyc.c4q.helenchan.makinghistory.models;

import java.util.Calendar;

import nyc.c4q.helenchan.makinghistory.SignInActivity;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContent {

    private String userName;
    private String userPhotoUrl;
    private String text;
    private Calendar year;

    public UserContent(String userName, String userPhotoUrl, String text, Calendar year) {
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.text = text;
        this.year = year;
    }

    public String getUserName() {
        return SignInActivity.mUsername;
        //return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Calendar getYear() {
        return year;
    }

    public void setYear(Calendar year) {
        this.year = year;
    }
}
