package nyc.c4q.helenchan.makinghistory;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.models.Coordinate;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by shannonalexander-navarro on 3/6/17.
 */

public class CreateYourStoryFragment extends Fragment implements View.OnClickListener, FindLocation.NearLocationListener {
    static int REQUEST_IMAGE_CAPTURE = 1;
    static int REQUEST_VIDEO_CAPTURE = 2;

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
    private FrameLayout baseLayout;
    private TextView titleTV;
    private LinearLayout btnLayout;
    private FrameLayout userPreviewLayout;
    private Bitmap imageBitmap;
    private Button saveContent;
    private String userLocationKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage = FirebaseStorage.getInstance();
        myStorageRef = mFirebaseStorage.getReference();

        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_addcontent, container, false);
        userPreviewLayout = (FrameLayout) root.findViewById(R.id.user_preview_layout);
        titleTV = (TextView) root.findViewById(R.id.create_title_tv);
        btnLayout = (LinearLayout) root.findViewById(R.id.user_actions_layout);
        imagePreview = (ImageView) root.findViewById(R.id.display_image);
        takePhoto = (Button) root.findViewById(R.id.bttn_takePic);
        takePhoto.setOnClickListener(this);
        addLocation = (Button) root.findViewById(R.id.bttn_addLatLng);
        addLocation.setOnClickListener(this);
        takeVideo = (Button) root.findViewById(R.id.bttn_takeVideo);
        takeVideo.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(getActivity());
        saveContent = (Button) root.findViewById(R.id.saveBtn);
        saveContent.setOnClickListener(this);


        setFontType(root);
        return root;
    }

    private void setFontType(View view) {
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(view.findViewById(R.id.user_actions_layout), "Raleway-Regular.ttf");
        calligrapher.setFont(view.findViewById(R.id.user_preview_layout), "Raleway-Regular.ttf");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imagePreview.setImageBitmap(imageBitmap);

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
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] photoByteArray = byteArrayOutputStream.toByteArray();

                    UploadTask uploadTask = photoStorageReference.putBytes(photoByteArray);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            downloadUri = taskSnapshot.getDownloadUrl();
                            Log.d("location key", userLocationKey);
                            addUserContentToDatabase(userLocationKey, downloadUri.toString());

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please take a photo!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    private void addUserContentToDatabase(String userLocationKey, String url) {
        mFirebaseDatabase.child("MapPoint").child(userLocationKey).child("ContentList").push().setValue(new Content(" ", "UserIdGoesHere", " ", "wash sq", url, "2017"));
    }

    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA}, 1);
        return checkPermissions();
    }

    private void openCamera() {
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCamera, REQUEST_IMAGE_CAPTURE);
    }

    private void openVideo() {
        Intent openVideoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(openVideoCapture, REQUEST_VIDEO_CAPTURE);
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


//    private void addContentToDatabase() {

        //first line adds a coordinate, second location adds content to list at that location
//        mFirebaseDatabase.child("MapPoint").child("Location3").setValue(new Coordinate(40.720398, -74.025452));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));


//        these two lines of code are super important. this is how you push new contents into the list of data at a point
//        mFirebaseDatabase.child("MapPoint").child("Location2").child("Content").push().setValue(new Content("Washington Sq", "Historical", "This was the washsq a long time ago", "wash sq", "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg", "1920"));
//        mFirebaseDatabase.child("MapPoint").child("Location2").child("Content").push().setValue(new Content("Washington Sq", "Historical", "This was the washsq a long time ago", "wash sq22", "http://oldnyc-assets.nypl.org/600px/707997f-a.jpg", "1920"));

//    }

//    private void addImagetomyapartment() {
//        mFirebaseDatabase.child("MapPoint")
//                .child("Location101")
//                .child("ContentList")
//                .push()
//                .setValue(new Content("Brooklyn: Broadway between Cornelia and Jefferson Street", "Historical", "Building behind the above ground train", "Broadway between Cornelia and Jefferson Street", "https://3rdeyesolation.files.wordpress.com/2012/02/tumblr_lp3lwpq4ay1qe3h33o1_500.jpg?w=500", "1990"));
//
//    }

//    private void saveToFirebase(Location lastLocation) {
//        Coordinate currLocation = new Coordinate(lastLocation.getLatitude(), lastLocation.getLongitude());
//        mFirebaseDatabase.child("locations").push().setValue(currLocation);
//    }

}
