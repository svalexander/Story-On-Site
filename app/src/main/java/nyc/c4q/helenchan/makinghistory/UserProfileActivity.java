package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.usercontentrecyclerview.UserContentAdapter;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserProfileActivity extends AppCompatActivity {

    private String TAG = "User Profile Activity: ";

    private ImageView userProfilePhoto;
    private TextView userNameTv;
    private TextView userPhotoCountTv;
    private int numUserPhotos = 0;

    private RecyclerView userContentRV;
    private UserContentAdapter userContentAdapter;
    private DatabaseReference photoRef;


    private List<Content> userPhotoList = new ArrayList<>();


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


        userContentRV = (RecyclerView) findViewById(R.id.user_profile_recycler_view);
        userContentRV.setLayoutManager((new GridLayoutManager(this, 2)));
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
                Content userPhotoUrl = dataSnapshot.getValue(Content.class);
                userPhotoList.add(userPhotoUrl);
                userContentAdapter.setUserPhotoContent(userPhotoList);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void deleteUserPhoto(String userPhotoUrl) {

        FirebaseStorage photoStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = photoStorage.getReference();

        StorageReference photoToDeleteRef = storageRef.child(userPhotoUrl);

        photoToDeleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("User Profile Activity: ", "photo deleted");
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("User Profile Activity: ", "Error occurred");
            }
        });


    }

}

