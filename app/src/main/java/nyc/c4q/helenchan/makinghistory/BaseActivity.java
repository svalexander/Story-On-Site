package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.firebase.ui.auth.AuthUI;

import me.anwarshahriar.calligrapher.Calligrapher;

public class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNav;
    private FrameLayout baseLayout;
    CreateYourStoryFragment createYourStoryFragment = new CreateYourStoryFragment();
    SuggestedFragment suggestedFragment = new SuggestedFragment();
    ExploreMoreFragment exploreMoreFragment = new ExploreMoreFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initViews();
        setListeners();
        setFontType();
        inflateDefaultView();
    }

    private void initViews() {
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5e454b")));
        if(Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(Color.parseColor("#3fab9b"));
            getWindow().setStatusBarColor(Color.parseColor("#5e454b"));
        }
    }

    private void inflateDefaultView() {

        FragmentTransaction exploreFragTransaction = getSupportFragmentManager().beginTransaction();
        exploreFragTransaction.replace(R.id.base_frame_Layout, exploreMoreFragment);
        //  exploreFragTransaction.addToBackStack("exploreFrag");
        exploreFragTransaction.commit();
    }

    private void setListeners() {
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.bottom_nav_view), "Raleway-Regular.ttf");
        calligrapher.setFont(findViewById(R.id.base_frame_Layout), "Raleway-Regular.ttf");
    }

    private void removeView() {
        baseLayout.removeAllViewsInLayout();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.suggestedIcon:
                //removeView();
                FragmentTransaction suggestedFragTransaction = getSupportFragmentManager().beginTransaction();
                suggestedFragTransaction.replace(R.id.base_frame_Layout, suggestedFragment);
                //    suggestedFragTransaction.addToBackStack("suggestedFrag");
                suggestedFragTransaction.commit();
                break;
            case R.id.createIcon:
                //removeView();
                FragmentTransaction createFragTransaction = getSupportFragmentManager().beginTransaction();
                createFragTransaction.replace(R.id.base_frame_Layout, createYourStoryFragment);
                //   createFragTransaction.addToBackStack("createFrag");
                createFragTransaction.commit();
                break;
            case R.id.exploreIcon:
                inflateDefaultView();
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                Intent intent = new Intent(BaseActivity.this, SignInActivity.class);
                startActivity(intent);
                return true;
            case R.id.user_profile:
                Intent userProfileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(userProfileIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private boolean requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
        return checkPermissions();
    }

}
