package nyc.c4q.helenchan.makinghistory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.Content;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AnimationListener {

    private static final String TAG = "Main Activity";
    private TextView skipBtn;
    private Animation fadeOutAnimation;
    private LinearLayout mainLayout;
    private Animation fadeInAnimation;
    private ImageView iconIV;
    private ImageView oldPic;
    private ImageView newPic;
    private int shortAnimLength;
    private int medAnimLength;
    private Handler handler;
    private RelativeLayout slidingLogo;


    List<Content> picsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // slidingLogo.setVisibility(View.VISIBLE);
                slidingLogo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));
                slidingLogo.setVisibility(View.GONE);
                 crossfadeAnimation();
            }
        }, 2000);


        newPic.setVisibility(View.GONE);
        shortAnimLength = getResources().getInteger(android.R.integer.config_shortAnimTime);
        // setFontType();
        medAnimLength = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        // crossfadeAnimation();
    }

    private void initViews() {

        skipBtn = (Button) findViewById(R.id.skipBtn);
        mainLayout = (LinearLayout) findViewById(R.id.activity_main);
        iconIV = (ImageView) findViewById(R.id.logoIV);
        oldPic = (ImageView) findViewById(R.id.old_pic);
        newPic = (ImageView) findViewById(R.id.new_pic);
        slidingLogo = (RelativeLayout) findViewById(R.id.sliding_logo);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#1E000D"));
        }
    }

    private void crossfadeAnimation() {

        newPic.setAlpha(0f);
        newPic.setVisibility(View.VISIBLE);
        newPic.animate().alpha(1f).setDuration(medAnimLength).setStartDelay(1000).setListener(null);

        oldPic.animate().alpha(0f).setDuration(medAnimLength).setStartDelay(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                oldPic.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // setAnimations();
        return super.onCreateView(parent, name, context, attrs);

    }

    private void setListeners() {
        skipBtn.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
    }

    private void setAnimations() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipBtn:
                crossfadeAnimation();
                break;
            case R.id.activity_main:
                Intent goToBaseActivityIntent = new Intent(MainActivity.this, BaseActivity.class);
                startActivity(goToBaseActivityIntent);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
     //   crossfadeAnimation();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
