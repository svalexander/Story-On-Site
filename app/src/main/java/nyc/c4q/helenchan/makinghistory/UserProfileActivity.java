package nyc.c4q.helenchan.makinghistory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.userprofileviewpager.UserPicsFragment;
import nyc.c4q.helenchan.makinghistory.userprofileviewpager.UserViewPagerAdapter;
import nyc.c4q.helenchan.makinghistory.models.Profile;
import nyc.c4q.helenchan.makinghistory.usercontentrecyclerview.UserContentAdapter;

import static nyc.c4q.helenchan.makinghistory.R.id.user_bio_edittext;
import static nyc.c4q.helenchan.makinghistory.R.id.user_profile_bio;
import static nyc.c4q.helenchan.makinghistory.R.id.user_profile_photo;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserProfileActivity extends AppCompatActivity implements UserPicsFragment.OnPictureCountListener, View.OnClickListener {
    private String TAG = "User Profile Activity: ";

    private ImageView userProfilePhoto;
    private TextView userNameTv;
    private TextView userPhotoCountTv;
    private TextView userProfileBio;
    private EditText userProfileEdittext;
    private int numUserPhotos = 0;
    private RelativeLayout userContentLayout;

    public static UserContentAdapter userContentAdapter;

    private DatabaseReference contentRef;
    private DatabaseReference userProfileRef;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageProfilePicRef;
    private Uri uploadedPhotoUri;
    private String imageUrl;
    private String userBio;
    private String dbBio;

    private UserViewPagerAdapter userViewPagerAdapter;
    private TabLayout tl;
    private ViewPager vp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#5e454b"));
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        contentRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("ContentList");
        userProfileRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Profile");
        firebaseStorage = FirebaseStorage.getInstance();
        storageProfilePicRef = firebaseStorage.getReference().child("profilepic");
        setFontType();
        initViews();
        loadSavedPicAndText();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        String userName = SignInActivity.mUsername;
        userNameTv.setText(userName);

        tl = (TabLayout) findViewById(R.id.content_tabs);
        vp = (ViewPager) findViewById(R.id.content_vp);
        userViewPagerAdapter = new UserViewPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(userViewPagerAdapter);
        tl.setupWithViewPager(vp);
    }

    private void initViews() {
        userProfilePhoto = (ImageView) findViewById(user_profile_photo);
        userProfilePhoto.setOnClickListener(this);
        userNameTv = (TextView) findViewById(R.id.user_profile_name);
        userPhotoCountTv = (TextView) findViewById(R.id.user_num_photos);
        userContentLayout = (RelativeLayout) findViewById(R.id.profileContent);
        userProfileBio = (TextView) findViewById(user_profile_bio);
        userProfileBio.setOnClickListener(this);
        userProfileEdittext = (EditText) findViewById(user_bio_edittext);
        userProfileEdittext.setOnClickListener(this);
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.profileContent), "Raleway-Regular.ttf");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermissions() {
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA}, Constants.REQUEST_CODE_PROFILEPIC);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickPicFromGallery() {
        Intent getFromGallery = new Intent();
        getFromGallery.setType("image/*");
        getFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getFromGallery, "Select Profile Picture"), Constants.REQUEST_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ProgressDialog picuploadProgress = new ProgressDialog(this);
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            uploadingCamPicToStorage(picuploadProgress, byteArrayOutputStream);

            Glide.with(this)
                    .load(byteArrayOutputStream.toByteArray())
                    .asBitmap()
                    .centerCrop()
                    .into(userProfilePhoto);

        }
        if (requestCode == Constants.REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedUri = data.getData();
            uploadingGalleryPicToStorage(picuploadProgress, selectedUri);
            Glide.with(this)
                    .load(selectedUri)
                    .centerCrop()
                    .into(userProfilePhoto);
        }
    }

    private void uploadingCamPicToStorage(final ProgressDialog picuploadProgress, ByteArrayOutputStream byteArrayOutputStream) {
        String randomID = java.util.UUID.randomUUID().toString();
        storageProfilePicRef.child(randomID);
        picuploadProgress.setMessage("Setting Picture");
        picuploadProgress.show();
        byte[] photoByteArray = byteArrayOutputStream.toByteArray();
        final UploadTask uploadTask = storageProfilePicRef.putBytes(photoByteArray);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                picuploadProgress.dismiss();
                uploadedPhotoUri = taskSnapshot.getDownloadUrl();
                userProfileRef.child("picUrl").setValue(uploadedPhotoUri.toString());
            }
        });
    }

    private void uploadingGalleryPicToStorage(final ProgressDialog picuploadProgress, Uri picUri) {
        picuploadProgress.setMessage("Setting Picture");
        picuploadProgress.show();
        UploadTask uploadTask = storageProfilePicRef.child(picUri.getLastPathSegment()).putFile(picUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                picuploadProgress.dismiss();
                uploadedPhotoUri = taskSnapshot.getDownloadUrl();
                userProfileRef.child("picUrl").setValue(uploadedPhotoUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Upload Failed! Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSavedPicAndText() {
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                if (profile != null) {
                    imageUrl = profile.getPicUrl();
                    dbBio = profile.getBio();
                }
                if (imageUrl != null) {
                    Glide.with(UserProfileActivity.this)
                            .load(imageUrl)
                            .centerCrop()
                            .into(userProfilePhoto);
                } else {
                    Glide.with(UserProfileActivity.this)
                            .load(R.drawable.ic_camera_icon)
                            .into(userProfilePhoto);
                }
                if (dbBio != null) {
                    userProfileBio.setText(dbBio);
                } else {
                    userProfileBio.setText("Let's Explore");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Unable to Load Profile Picture", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CODE_PROFILEPIC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
        }
    }

    @Override
    public void updatePhotoCount(int count) {
        userPhotoCountTv.setText(String.valueOf(count));
    }

    private void whereToGetPicFromDialogueBox() {
        AlertDialog.Builder profilePicAlertDialogBuilder = new AlertDialog.Builder(this);
        profilePicAlertDialogBuilder.setTitle(R.string.get_profile_pic)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setItems(R.array.camera_or_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (!checkPermissions()) {
                                requestCameraPermissions();
                            } else {
                                openCamera();
                            }
                        } else if (which == 1) {
                            pickPicFromGallery();
                        }
                    }
                });
        profilePicAlertDialogBuilder.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case user_profile_photo:
                whereToGetPicFromDialogueBox();
                break;
            case user_profile_bio:
                userProfileBio.setVisibility(View.INVISIBLE);
                if (userProfileBio.length() != 0) {
                    userBio = userProfileBio.getText().toString();
                    userProfileEdittext.setText(userBio);
                }
                userProfileEdittext.setVisibility(View.VISIBLE);
                break;
            case user_bio_edittext:
                userProfileEdittext.setVisibility(View.INVISIBLE);
                userProfileBio.setVisibility(View.VISIBLE);
                if (userProfileEdittext.length() != 0) {
                    userBio = userProfileEdittext.getText().toString();
                    userProfileBio.setText(userBio);
                    userProfileRef.child("bio").setValue(userBio);
                }
                break;
        }
    }

}


