package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserProfileActivity extends AppCompatActivity {


    private int numUserPhotos = 0;
    private Button editUserProfile;

    private RecyclerView userProfileRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

    }





}
