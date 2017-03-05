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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nyc.c4q.helenchan.makinghistory.BaseActivity;
import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Coordinate;

public class AddConentActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    static int REQUEST_IMAGE_CAPTURE = 1;
    static int REQUEST_VIDEO_CAPTURE = 2;

    private DatabaseReference mFirebaseDatabase;

    private Button takePhoto;
    private Button addLocation;
    private Button takeVideo;
    private ImageView imagePreview;
    private VideoView videoView;
    private boolean isConnected = false;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient mGoogleApiClient;
    private FrameLayout baseLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);
        getLayoutInflater().inflate(R.layout.activity_addcontent, baseLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

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
        addLocation = (Button) findViewById(R.id.bttn_addLatLng);
        addLocation.setOnClickListener(this);
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
        Coordinate currLocation = new Coordinate(lastLocation.getLatitude(), lastLocation.getLongitude());
        mFirebaseDatabase.child("locations").push().setValue(currLocation);
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
            case R.id.bttn_addLatLng:
                if (isConnected) {
                    Log.d("success", "successful connection");
                    Toast.makeText(getApplicationContext(), "location connected made", Toast.LENGTH_LONG);
                    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    saveToFirebase(mLastLocation);
                }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        isConnected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isConnected = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        isConnected = true;
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