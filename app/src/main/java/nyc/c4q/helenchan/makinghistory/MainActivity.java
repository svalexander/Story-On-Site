package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AnimationListener {

    private static final String TAG = "Main Activity";
    private TextView skipBtn;
    private Animation fadeOutAnimation;
    private LinearLayout mainLayout;
    private Animation fadeInAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
        setAnimations();
        setFontType();

    }

    private void initViews() {
        skipBtn = (Button) findViewById(R.id.skipBtn);
        mainLayout = (LinearLayout) findViewById(R.id.activity_main);
    }

    private void setListeners() {
        skipBtn.setOnClickListener(this);
    }

    private void setAnimations() {
        fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipBtn:
              //  fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
                Intent skipIntent = new Intent(MainActivity.this, BaseActivity.class);
                startActivity(skipIntent);
               //     overridePendingTransition(fadeOutAnimation,fadeInAnimation );
        }
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.activity_main), "Raleway-Regular.ttf");
       // calligrapher.setFont(findViewById(R.id.main_card), "Raleway-Regular.ttf");

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
}
