package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import me.anwarshahriar.calligrapher.Calligrapher;

public class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNav;
    private static final String ANONYMOUS = "ANONYMOUS";
    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;

    CreateYourStoryFragment createYourStoryFragment = new CreateYourStoryFragment();
    SuggestedFragment suggestedFragment = new SuggestedFragment();
    ExploreMoreFragment exploreMoreFragment = new ExploreMoreFragment();
    private FrameLayout baseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        initViews();
        setListeners();
        setFontType();
        inflateDefaultView();
    }

    private void initViews(){
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);

    }

    private void inflateDefaultView(){
        FragmentTransaction exploreFragTransaction = getSupportFragmentManager().beginTransaction();
        exploreFragTransaction.replace(R.id.base_frame_Layout,exploreMoreFragment);
        exploreFragTransaction.commit();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
