package nyc.c4q.helenchan.makinghistory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by akashaarcher on 3/16/17.
 */

public class UserPhotoDetailActivity extends AppCompatActivity {

    TextView usernameTextView;
    ImageView userPhotoDetailImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo_detail);

        usernameTextView = (TextView) findViewById(R.id.username);
        userPhotoDetailImageView = (ImageView) findViewById(R.id.user_photo_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("UserPhotoUri")) {
                String userPhotoDetailUrl = extras.getString("UserPhotoUri");

                usernameTextView.setText(SignInActivity.mUsername);
                Glide.with(getApplicationContext())
                        .load(userPhotoDetailUrl)
                        .override(1200, 1200)
                        .centerCrop()
                        .into(userPhotoDetailImageView);
            }
        }

    }



    /*
     public static void deleteUserPhoto(String userPhotoUrl) {
//
//        FirebaseStorage photoStorage = FirebaseStorage.getInstance();
//        StorageReference storageRef = photoStorage.getReference();
//
//        StorageReference photoToDeleteRef = storageRef.child(userPhotoUrl);
//
//        photoToDeleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.i("User Profile Activity: ", "photo deleted");
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.i("User Profile Activity: ", "Error occurred");
//            }
//        });
//
//    }


     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_photo_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.user_photo_share):
                //do stuff
                return true;

            case (R.id.user_photo_delete):
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this picture?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               // UserProfileActivity.deleteUserPhoto(userPhoto);
                                 dialogInterface.cancel();
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
        return super.onOptionsItemSelected(item);
    }
}
