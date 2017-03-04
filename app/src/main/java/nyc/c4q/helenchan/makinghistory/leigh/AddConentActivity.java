package nyc.c4q.helenchan.makinghistory.leigh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import nyc.c4q.helenchan.makinghistory.R;

public class AddConentActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    static int REQUEST_IMAGE_CAPTURE = 1;
    static int REQUEST_VIDEO_CAPTURE = 2;

    private DatabaseReference mFirebaseDatabase;

    private Button takePhoto;
    private Button addText;
    private Button takeVideo;
    private ImageView imagePreview;
    private VideoView videoView;

    private String currentUid;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontent);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        initViews();

        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    private void initViews() {
        imagePreview = (ImageView) findViewById(R.id.display_image);
        takePhoto = (Button) findViewById(R.id.bttn_takePic);
        takePhoto.setOnClickListener(this);
        addText = (Button) findViewById(R.id.bttn_addText);
        takeVideo = (Button) findViewById(R.id.bttn_takeVideo);
        takeVideo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagePreview.setImageBitmap(imageBitmap);

        }
    }

    private void saveToFirebase(Location lastLocation) {
        Map mLocations = new HashMap();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        Date date = new Date();
        String mLastUpdateTime = dateFormat.format(date).toString();
        Toast.makeText(getApplicationContext(), "save firebase method evoked", Toast.LENGTH_LONG).show();
        mLocations.put("timestamp", mLastUpdateTime);
        Map mCoordinate = new HashMap();
        mCoordinate.put("latitude", lastLocation.getLatitude());
        mCoordinate.put("longitude", lastLocation.getLongitude());
        mLocations.put("location", mCoordinate);
        mFirebaseDatabase.push().setValue(mLocations);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bttn_takePic:
                if (checkPermissions()) {
                    openCamera();
                } else if (requestPermissions()) {
                    openCamera();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied by user", Toast.LENGTH_LONG).show();
                }
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

            default:
        }
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
        startActivityForResult(openCamera, REQUEST_IMAGE_CAPTURE);
    }

    private void openVideo() {
        Intent openVideoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(openVideoCapture, REQUEST_VIDEO_CAPTURE);
    }


    private void writeNewUser(String userId, String name, String message) {
        User user = new User(name, message);
        mFirebaseDatabase.child("users").child(userId).setValue(user);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("success", "successful connection");
        Toast.makeText(getApplicationContext(), "location connected made", Toast.LENGTH_LONG);
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        saveToFirebase(mLastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}