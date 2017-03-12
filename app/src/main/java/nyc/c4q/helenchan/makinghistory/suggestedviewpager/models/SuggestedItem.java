package nyc.c4q.helenchan.makinghistory.suggestedviewpager.models;

/**
 * Created by leighdouglas on 3/11/17.
 */

public class SuggestedItem {
    private int drawable;
    private String city;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SuggestedItem(int drawable, String city) {

        this.drawable = drawable;
        this.city = city;
    }
}

