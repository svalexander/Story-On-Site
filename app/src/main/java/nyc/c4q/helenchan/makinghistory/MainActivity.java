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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AnimationListener {

    private static final String TAG = "Main Activity";
    private LinearLayout mainLayout;
    private ImageView oldPic;
    private ImageView newPic;
    private int shortAnimLength;
    private int medAnimLength;
    private Handler animationHandler;
    private Handler intentHandler;
    private RelativeLayout slidingLogo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();

        newPic.setVisibility(View.GONE);
        shortAnimLength = getResources().getInteger(android.R.integer.config_shortAnimTime);
        medAnimLength = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        animationHandler = new Handler();

        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slidingLogo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));
                slidingLogo.setVisibility(View.GONE);
                crossfadeAnimation();
            }
        }, 1500);

//        intentHandler = new Handler();
//
//        intentHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//                startActivity(intent);
//            }
//        }, 3000);


    }

    private void initViews() {

        mainLayout = (LinearLayout) findViewById(R.id.activity_main);
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
        newPic.animate().alpha(1f).setDuration(medAnimLength).setStartDelay(1500).setListener(null);

        oldPic.animate().alpha(0f).setDuration(medAnimLength).setStartDelay(2000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                oldPic.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void setListeners() {
        mainLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
