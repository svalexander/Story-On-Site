package nyc.c4q.helenchan.makinghistory.leigh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.BaseActivity;
import nyc.c4q.helenchan.makinghistory.FindLocation;
import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;

public class AddConentActivity extends BaseActivity implements View.OnClickListener, FindLocation.NearLocationListener {
    static int REQUEST_IMAGE_CAPTURE = 1;
    static int REQUEST_VIDEO_CAPTURE = 2;
    public final String APP_TAG = "Story";


    private DatabaseReference mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference myStorageRef;
    private Uri downloadUri;
    private ProgressDialog mProgressDialog;

    private Button takePhoto;
    private Button addLocation;
    private Button takeVideo;
    private ImageView imagePreview;
    private VideoView videoView;
    private boolean isConnected = false;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient mGoogleApiClient;
    private FrameLayout baseLayout;
    private TextView titleTV;
    private LinearLayout btnLayout;
    private FrameLayout userPreviewLayout;
    private Bitmap imageBitmap;
    private Button saveContent;
    private String userLocationKey;
    public String photoFileName = "photo.jpg";

    Uri takenPhotoUri;
    Uri fileUri;
    Bitmap takenImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);
        getLayoutInflater().inflate(R.layout.activity_addcontent, baseLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage = FirebaseStorage.getInstance();
        myStorageRef = mFirebaseStorage.getReference();

        initViews();
        setFontType();

        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    private void initViews() {
        userPreviewLayout = (FrameLayout) findViewById(R.id.user_preview_layout);
        titleTV = (TextView) findViewById(R.id.create_title_tv);
        btnLayout = (LinearLayout) findViewById(R.id.user_actions_layout);
        imagePreview = (ImageView) findViewById(R.id.display_image);
        takePhoto = (Button) findViewById(R.id.bttn_takePic);
        takePhoto.setOnClickListener(this);
        takeVideo = (Button) findViewById(R.id.bttn_takeVideo);
        takeVideo.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        saveContent = (Button) findViewById(R.id.saveBtn);
        saveContent.setOnClickListener(this);
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.user_actions_layout), "Raleway-Regular.ttf");
        calligrapher.setFont(findViewById(R.id.user_preview_layout), "Raleway-Regular.ttf");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            takenPhotoUri = data.getData();
//            takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
//            imagePreview.setImageBitmap(takenImage);

//            Bundle intentExtras = data.getExtras();
//            Bitmap picture = (Bitmap) intentExtras.get("data");
//            Uri pictureUri = data.getData();
//            Intent intentCam = new Intent(getApplicationContext(), EditActivity.class);
//            intentCam.putExtra("CameraPhotoUri", pictureUri.toString());
//            intentCam.putExtra("BitmapCamera", picture);
//            intentCam.putExtra("TypeOfPicture", 0);
//            startActivity(intentCam);

        } else {
            Toast.makeText(this, "Picture not taken", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bttn_takePic:
                mProgressDialog.setMessage("Checking user location");
                mProgressDialog.show();

                FindLocation findLocation = new FindLocation(getApplicationContext(), this);
                findLocation.buildGoogleApiClient();
                findLocation.connectApiClient();

                break;

            case R.id.bttn_takeVideo:
                if (checkPermissions()) {
                    openVideo();
                } else if (requestPermissions()) {
                    openVideo();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied by user", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.saveBtn:
                if (imageBitmap != null) {
                    mProgressDialog.setMessage("Uploading Image");
                    mProgressDialog.show();
                    String randomID = java.util.UUID.randomUUID().toString();
                    StorageReference photoStorageReference = myStorageRef.child("photos").child(randomID);
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    takenImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                    byte[] photoByteArray = byteArrayOutputStream.toByteArray();
//
//                    UploadTask uploadTask = photoStorageReference.putBytes(photoByteArray);
//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            mProgressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                            downloadUri = taskSnapshot.getDownloadUrl();
//                            addUserContentToDatabase(userLocationKey, downloadUri.toString());
//
//                        }
//                    });


//                    StorageReference riversRef = photoStorageReference.child("images/" + file.getLastPathSegment());

                    UploadTask uploadTask = photoStorageReference.putFile(takenPhotoUri);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });
                } else {
                    Toast.makeText(this, "Please take a photo!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    public void foundLocation(String userLocationkey, boolean foundLocation) {
        this.userLocationKey = userLocationkey;
        mProgressDialog.cancel();
        clickedButton(foundLocation);
    }

    private void clickedButton(boolean foundLocation) {
        Log.d("nearby", String.valueOf(foundLocation));
        if (!foundLocation) {
            Toast.makeText(getApplicationContext(), "Sorry, you're currently not near a location", Toast.LENGTH_LONG).show();
        } else if (foundLocation && checkPermissions()) {
            openCamera();
        } else if (foundLocation && requestPermissions()) {
            openCamera();
        } else {
            Toast.makeText(getApplicationContext(), "Permission denied by user", Toast.LENGTH_LONG).show();
        }
    }

    private void addUserContentToDatabase(String userLocationKey, String url) {
        mFirebaseDatabase.child("MapPoint").child(userLocationKey).child("ContentList").push().setValue(new Content(" ", "UserIdGoesHere", " ", "wash sq", url, "2017"));
    }


    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA}, 1);
        return checkPermissions();
    }

    private void openCamera() {
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        openCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (openCamera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(openCamera, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openVideo() {
        Intent openVideoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(openVideoCapture, REQUEST_VIDEO_CAPTURE);
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdir()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.pathSeparator + "IMG" + timeStamp + ".jpg");

        return mediaFile;
    }

    private void addContentToDatabase() {


        //first line adds a coordinate, second location adds content to list at that location
//        mFirebaseDatabase.child("MapPoint").child("Location3").setValue(new Coordinate(40.720398, -74.025452));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//

//        these two lines of code are super important. this is how you push new contents into the list of data at a point
//        mFirebaseDatabase.child("MapPoint").child("Location2").child("ContentList").push().setValue(new Content("Washington Sq", "Historical", "This was the washsq a long time ago", "wash sq", "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location2").child("ContentList").push().setValue(new Content("Washington Sq", "Historical", "This was the washsq a long time ago", "wash sq22", "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg", "1920"));
    }

    private void addImagetomyapartment() {
        mFirebaseDatabase.child("MapPoint")
                .child("Location101")
                .child("ContentList")
                .push()
                .setValue(new Content("Brooklyn: Broadway between Cornelia and Jefferson Street", "Historical", "Building behind the above ground train", "Broadway between Cornelia and Jefferson Street", "https://3rdeyesolation.files.wordpress.com/2012/02/tumblr_lp3lwpq4ay1qe3h33o1_500.jpg?w=500", "1990"));

    }
}