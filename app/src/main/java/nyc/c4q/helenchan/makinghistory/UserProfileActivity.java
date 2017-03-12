package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.usercontentrecyclerview.UserContentAdapter;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserProfileActivity extends AppCompatActivity {

    private ImageView userProfilePhoto;
    private TextView userNameTv;
    private TextView userPhotoCountTv;
    private int numUserPhotos = 0;    // size of list ?

    private RecyclerView userContentRV;
    private UserContentAdapter userContentAdapter;
    private DatabaseReference photoRef;

    private List<Content> userPhotoList = new ArrayList<>();

    //    private Button editUserProfile;
    private Content userContent;   // use type (userId) to count amount of pictures a user has


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        photoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("ContentList");

        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        userNameTv = (TextView) findViewById(R.id.user_profile_name);
        userPhotoCountTv = (TextView) findViewById(R.id.user_num_photos);


        String userName = SignInActivity.mUsername;
        userNameTv.setText(userName);
        userPhotoCountTv.setText(String.valueOf(numUserPhotos));

        userContentRV = (RecyclerView) findViewById(R.id.user_profile_recycler_view);
        userContentRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        userContentAdapter = new UserContentAdapter();
        userContentRV.setAdapter(userContentAdapter);


        photoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Content userPhotoUrl = dataSnapshot.getValue(Content.class);
                userPhotoList.add(userPhotoUrl);
                userContentAdapter.setUserPhotoContent(userPhotoList);

                numUserPhotos = userPhotoList.size();
                userPhotoCountTv.setText(String.valueOf(numUserPhotos));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

