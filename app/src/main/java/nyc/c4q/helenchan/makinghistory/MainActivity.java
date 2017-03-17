package nyc.c4q.helenchan.makinghistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.models.Content;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AnimationListener {

    private static final String TAG = "Main Activity";
    private TextView skipBtn;
    private Animation fadeOutAnimation;
    private LinearLayout mainLayout;
    private Animation fadeInAnimation;
    private ImageView mainPicIV;
    private TextView mainPicTV;
    private CardView cardView;
    private ImageView iconIV;
    private ImageView oldPic;
    private ImageView newPic;
    private int shortAnimLength;

    List<Content> picsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();

       // setFontType();
    }

    private void initViews() {
        skipBtn = (Button) findViewById(R.id.skipBtn);
        mainLayout = (LinearLayout) findViewById(R.id.activity_main);
       // iconIV = (ImageView) findViewById(R.id.logoIV);
//        oldPic = (ImageView) findViewById(R.id.old_pic);
//        newPic= (ImageView) findViewById(R.id.new_pic);
        mainPicIV = (ImageView) findViewById(R.id.main_pic_iv);
        mainPicTV = (TextView) findViewById(R.id.main_pic_tv);
        cardView = (CardView) findViewById(R.id.main_card);
        getRandomImage();

        for (int i = 0; i < picsList.size(); i++) {
            String url = picsList.get(i).getUrl();
            String text = picsList.get(i).getText();
            Picasso.with(getBaseContext()).load(url).resize(1200, 1200).centerCrop().into(mainPicIV);
            mainPicTV.setText(text);
        }


    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
       // setAnimations();
        return super.onCreateView(parent, name, context, attrs);

    }

    private void setListeners() {
        skipBtn.setOnClickListener(this);
    }

    private void setAnimations() {

//        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.slide_down_anim);
//        iconIV.startAnimation(slideDown);
//
//        fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
//        fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
//        shortAnimLength = getResources().getInteger(
//                android.R.integer.config_shortAnimTime);
//
//        //not showing because they're both happening in oncreate
//        new FadeInAnimation(oldPic).animate();
//        new FadeOutAnimation(oldPic).animate();

        cardView.setEnabled(true);
        cardView.setVisibility(View.VISIBLE);
        cardView.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipBtn:
                fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
                Intent skipIntent = new Intent(MainActivity.this, BaseActivity.class);
                startActivity(skipIntent);
                //     overridePendingTransition(fadeOutAnimation,fadeInAnimation );
        }
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.activity_main), "Raleway-Regular.ttf");
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void getRandomImage() {

        picsList.add(new Content(
                "name null",
                "type null",
                "2866 West 6th Street, west side, between Neptune Ave., and Sheepshead Bay Road, showing The Harvest, formerly the Coney Island House, later the Oceanic Hotel. It formerly stood, prior to 1921, at Ne. 622 Neptune Ave.\nDecember 1923.\nEugene L. Armbruster Collection.\nThe same, also showing overhead the Culver (B.M.T) Line.\nDecember 1923.\nEugene L. Armbruster Collection.\nThe same, another view. Once a railroad ran from this Hotel to the beach and the Cars left every few minutes to accomodate the guests.\nDecember 1923.\nEugene L. Armbruster Collection.\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/702449f-a.jpg",
                "1923"));
        picsList.add(new Content(
                "name null",
                "type null",
                "43 Third Avenue, adjoining the S.E. corner of 10th Street.\nMay 18, 1934.\nP. L. Sperr.\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg",
                "1934"
        ));

        picsList.add(new Content(
                "name null",
                "type null",
                "East 36th Street, north side, between First and Second avenues, showing workmen clearing the former\nsite of St. Gabriel's Church and school.\nNote the corner stone of same in the foreground. To the left is the Second Avenue \"El\" and to the right, 37th Street tenements. View No. 3 shows the last mentioned\nfeature only.\nJune 28, 1939\nViews: 1-2-3\nP. L. SpeIr\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/712287f-b.jpg",
                "1939"
        ));
        picsList.add(new Content(
                "name null",
                "type null",
                "Ninth Avenue, north from W. 50th Street, as photographed from the downtown \"El\" platform. Shown are details of track and station at this point prior to demolition. Note the raised express portion. This was inclined to permit the shunting of 6th Avenue trains eastward on W. 53rd Street.\nJune 6, 1940\nViews 1,2,3\nP. L. Sperr\n",
                "folder null",
                "http://www.oldnyc.org/rotated-assets/600px/710415f-c.90.jpg",
                "1940"
        ));
        picsList.add(new Content(
                "name null",
                "type null",
                "152 Broadway, adjoining the N. E. corner of Liberty Street, showing Frank Hegger's Photographic Depot, the best known and most popular establishment of its kind in its day. The Williamsburg City Building is the corner structure. On the left is a portion of No. 154-8 Broadway. The three of these structures were replaced by the present 21-story Westinghouse Building.\n1883\nGift of New York Historical Society\nThru Mr. Vigilanti\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/717072f.jpg",
                "1883"
        ));
        picsList.add(new Content(
                "name null",
                "type null",
                "Grand Avenue, at the N.E. corner of 68th Street (left extending north), showing in fouu views at the corner,the home of Judge William Burcham in 1872 which was demolished in Jure 1925. It is said that this house is on the site of the John Firth house of 1850. One Dovle was an occupant of the house seen in these views. View No. four shows a rear of the house viewed across the B.R.T. tracks parallelking\nBorden Avenue.\nFebruary 1924\nViews 1 and 4\nViews 2S:3 1922\nEuene L. Armbruster Collection\n",
                "folder null",
                "http://oldnyc-assets.nypl.org/600px/726773f-c.jpg",
                "1922"
        ));

        Collections.shuffle(picsList);
    }

}
