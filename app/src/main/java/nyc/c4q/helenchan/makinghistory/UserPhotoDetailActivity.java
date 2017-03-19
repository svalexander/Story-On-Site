package nyc.c4q.helenchan.makinghistory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by akashaarcher on 3/16/17.
 */

public class UserPhotoDetailActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private ImageView userPhotoDetailImageView;
    private ImageView userSharePhotoBtn;
    private ImageView userDeletePhotoBtn;

    private DatabaseReference ref;

    private String TAG = "UserPhotoDetailActivity";
    private String userPhotoDetailUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo_detail);

        usernameTextView = (TextView) findViewById(R.id.username);
        userPhotoDetailImageView = (ImageView) findViewById(R.id.user_photo_detail);
        userSharePhotoBtn = (ImageView) findViewById(R.id.user_photo_share);
        userDeletePhotoBtn = (ImageView) findViewById(R.id.user_photo_delete);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("UserPhotoUri")) {
                userPhotoDetailUrl = extras.getString("UserPhotoUri");

                usernameTextView.setText(SignInActivity.mUsername);
                Glide.with(getApplicationContext())
                        .load(userPhotoDetailUrl)
                        .override(1200, 1200)
                        .centerCrop()
                        .into(userPhotoDetailImageView);
            }
        }

        userDeletePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete this picture?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteUserPhotoFromStorage(userPhotoDetailUrl);
                                deleteUserPhoto();
                            }
                        });

                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog deleteAlert = builder.create();
                deleteAlert.show();
            }
        });
    }

    public void deleteUserPhoto() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "photo url: " + userPhotoDetailUrl);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query photoQuery = ref.child("Users").child(uid).child("ContentList").orderByChild("url").equalTo(userPhotoDetailUrl);
        photoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot photoSnapshot : dataSnapshot.getChildren()) {
                    photoSnapshot.getRef().removeValue();
                    Log.i(TAG, "photo removed " + userPhotoDetailUrl);

                    UserProfileActivity.userContentAdapter.notifyDataSetChanged();
                    Toast.makeText(UserPhotoDetailActivity.this, "Your photo has been deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(intent);
        finish();
    }

    // ******** DELETE PHOTO FROM STORAGE -- NEEDS FIXING!!!! // *******
    public void deleteUserPhotoFromStorage(String userPhotoUrl) {

        Log.i(TAG, "photo url in delete storage method: " + userPhotoDetailUrl);

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
