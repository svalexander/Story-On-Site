package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import me.anwarshahriar.calligrapher.Calligrapher;

public class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNav;
    CreateYourStoryFragment createYourStoryFragment = new CreateYourStoryFragment();
    SuggestedFragment suggestedFragment = new SuggestedFragment();
    private FrameLayout baseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initViews();
        setListeners();
        setFontType();
    }

    private void initViews(){
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);
    }

    private void setListeners(){
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.bottom_nav_view), "Raleway-Regular.ttf");
    }

    private void removeView(){
        baseLayout.removeAllViewsInLayout();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.suggestedIcon:
               removeView();
               FragmentTransaction suggestedFragTransaction = getSupportFragmentManager().beginTransaction();
               suggestedFragTransaction.replace(R.id.base_frame_Layout, suggestedFragment);
               suggestedFragTransaction.commit();
               break;
           case R.id.createIcon:
               removeView();
               FragmentTransaction createFragTransaction = getSupportFragmentManager().beginTransaction();
               createFragTransaction.replace(R.id.base_frame_Layout, createYourStoryFragment);
               createFragTransaction.commit();
               break;
           case R.id.exploreIcon:
               Intent exploreIntent = new Intent(getBaseContext(), ExploreMoreActivity.class);
               startActivity(exploreIntent);
       }
        return true;
    }

}
