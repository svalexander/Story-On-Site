package nyc.c4q.helenchan.makinghistory;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.contentrecyclerview.ViewContentAdapter;
import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.models.MapPoint;
import nyc.c4q.helenchan.makinghistory.models.UserContent;
import nyc.c4q.helenchan.makinghistory.usercontentrecyclerview.UserContentAdapter;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserProfileActivity extends AppCompatActivity {

    private ImageView userProfilePhoto;
    private TextView userNameTv;
    private TextView userPhotoCountTv;
    private int numUserPhotos = 0;    // size of list ?
    private Button editUserProfile;
    private Content userContent;   // use type (userId) to count amount of pictures a user has

    private RecyclerView userProfileRv;
    private UserContentAdapter userContentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        userNameTv = (TextView) findViewById(R.id.user_profile_name);
        userPhotoCountTv = (TextView) findViewById(R.id.user_num_photos);

        String userName = SignInActivity.mUsername;
        userNameTv.setText(userName);
        userPhotoCountTv.setText(String.valueOf(numUserPhotos));


//        userProfileRv = (RecyclerView) findViewById(R.id.user_profile_recycler_view);
//        userProfileRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
//        userContentAdapter = new UserContentAdapter();
//        userProfileRv.setAdapter(userContentAdapter);
    }


    //    private void attachDatabaseReadListener() {
//        // These events will trigger only when one of the children of the messages node changes
//        if (mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                /* We added a listener to our messages list, and when a new message is added, it triggers onChildAdded, which
//                takes the newly added message, converts it into a FriendlyMessage object and adds it to the adapter to be displayed
//                in the list view
//
//                SEE DOCS: Retrieving Firebase Data
//                */
//
//                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
//                    mMessageAdapter.add(friendlyMessage);
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            };
//            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
//        }
//    }



}
