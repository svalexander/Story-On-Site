package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.leigh.AddConentActivity;

public class LandingPageActivity extends BaseActivity implements View.OnClickListener {

    private static final String ANONYMOUS = "ANONYMOUS";
    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    private Button loginBtn;
    private TextView welcomeText;
    private FrameLayout pntOfInterestLayout;
    private FrameLayout exploreLayout;
    private FrameLayout createLayout;
    private FrameLayout loginLayout;
    private FrameLayout baseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);
        getLayoutInflater().inflate(R.layout.activity_landing_page, baseLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUsername = ANONYMOUS;
        setAuthenticationListener();
        initViews();
        setListeners();
        setFontType();


    }


    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.loginFrameLayout), "Raleway-Regular.ttf");
    }

    private void initViews() {
        pntOfInterestLayout = (FrameLayout) findViewById(R.id.point_interest_layout);
        exploreLayout = (FrameLayout) findViewById(R.id.explore_layout);
        createLayout = (FrameLayout) findViewById(R.id.create_layout);
        loginLayout = (FrameLayout) findViewById(R.id.loginFrameLayout);
        loginBtn = (Button) findViewById(R.id.login_Btn);
        welcomeText = (TextView) findViewById(R.id.HelloTV);
    }

    private void setListeners() {
        exploreLayout.setOnClickListener(this);
        createLayout.setOnClickListener(this);
        pntOfInterestLayout.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.explore_layout:
                Intent exploreIntent = new Intent(LandingPageActivity.this, ExploreMoreActivity.class);
                startActivity(exploreIntent);
                break;
            case R.id.create_layout:
                Intent createIntent = new Intent(LandingPageActivity.this, AddConentActivity.class);
                startActivity(createIntent);
                break;
            case R.id.point_interest_layout:
                Intent pntInterestIntent = new Intent(LandingPageActivity.this, PointOfInterestActivity.class);
                startActivity(pntInterestIntent);
                break;
            case R.id.login_Btn:
                getLoginScreen();
                break;
        }
    }


    private void setAuthenticationListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUsername = user.getDisplayName();
                    welcomeText.setText("Hello " + mUsername);

                } else {
                    welcomeText.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void getLoginScreen() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(LandingPageActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(LandingPageActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}
