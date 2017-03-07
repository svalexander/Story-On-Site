package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.leigh.AddConentActivity;

public class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNav;
    private static final String ANONYMOUS = "ANONYMOUS";
    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        initViews();
        setListeners();
        setFontType();
    }

    private void initViews() {
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
    }

    private void setListeners() {
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.bottom_nav_view), "Raleway-Regular.ttf");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.suggestedIcon:
                Intent suggestedIntent = new Intent(getBaseContext(), PointOfInterestActivity.class);
                startActivity(suggestedIntent);
                break;
            case R.id.createIcon:
                Intent createIntent = new Intent(getBaseContext(), AddConentActivity.class);
                startActivity(createIntent);
                break;
            case R.id.exploreIcon:
                Intent exploreIntent = new Intent(getBaseContext(), ExploreMoreActivity.class);
                startActivity(exploreIntent);
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
