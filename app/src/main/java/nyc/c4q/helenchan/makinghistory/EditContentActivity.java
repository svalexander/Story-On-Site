package nyc.c4q.helenchan.makinghistory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by helenchan on 3/11/17.
 */

public class EditContentActivity extends AppCompatActivity {
    private ImageView portraitIV;
    private EditText storyEditText;
    private EditText locationEditText;
    private Uri photoUri;
    private Uri downloadUri;
    private String userLocationKey;
    private String userStory;
    private String photoLocationName;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference myStorageRef;
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        setActionBarTitle();
        initialize();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userLocationKey = extras.getString("userLocation");
            if (extras.containsKey("PHOTOURI")) {
                String stringUri = extras.getString("PHOTOURI");
                photoUri = Uri.parse(stringUri);
                Glide.with(getApplicationContext())
                        .load(photoUri.getPath())
                        .override(1200, 1200)
                        .centerCrop()
                        .into(portraitIV);
            }
        }
    }

    private void initialize() {
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage = FirebaseStorage.getInstance();
        myStorageRef = mFirebaseStorage.getReference();
        portraitIV = (ImageView) findViewById(R.id.preview_portrait_iv);
        storyEditText = (EditText) findViewById(R.id.user_story_edittext);
        locationEditText = (EditText) findViewById(R.id.location_name_edittext);
        mProgressDialog = new ProgressDialog(EditContentActivity.this);
    }

    private void setActionBarTitle() {
        (EditContentActivity.this).getSupportActionBar().setTitle(R.string.new_post);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_content_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.edit_content_save):
                if (checkifEdittextHasText()) {
                    userStory = String.valueOf(storyEditText.getText());
                    photoLocationName = String.valueOf(locationEditText.getText());
                    uploadingToFireBase();
                    return true;
                }
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    private Boolean checkifEdittextHasText() {
        if (storyEditText.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a short description of photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (locationEditText.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please complete the location field", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void uploadingToFireBase() {
        mProgressDialog.setMessage("Uploading Image");
        mProgressDialog.show();
        String photoID = photoUri.getLastPathSegment();
        StorageReference photoStorageReference = myStorageRef.child("photos").child(photoID);
        UploadTask uploadTask = photoStorageReference.putFile(photoUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_LONG).show();
                downloadUri = taskSnapshot.getDownloadUrl();
                addUserContentToDatabase(userLocationKey, downloadUri.toString(), photoLocationName, userStory);
                goToViewContentActivity();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Upload Failed! Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addUserContentToDatabase(String userLocationKey, String url, String locationName, String story) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase.child("MapPoint").child(userLocationKey).child("ContentList").push().setValue(new Content(locationName, "story", story, "wash sq", url, "2017"));
        mFirebaseDatabase.child("Users").child(uid).child("ContentList").push().setValue(new Content(" ", uid, " ", "wash sq", url, "2017"));
    }

    private void goToViewContentActivity() {
        Intent intent = new Intent(EditContentActivity.this, ViewContentActivity.class);
        intent.putExtra("PICLATLONG", userLocationKey);
        startActivity(intent);
    }
}

