package nyc.c4q.helenchan.makinghistory;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by shannonalexander-navarro on 3/6/17.
 */

public class CreateYourStoryFragment extends Fragment implements View.OnClickListener, FindLocation.NearLocationListener {
    public static final String PHOTOURI = "PHOTOURI";
    public static final String VIDEOURI = "VIDEOURI";
    static int REQUEST_IMAGE_CAPTURE = 1;
    static int REQUEST_VIDEO_CAPTURE = 22;

    private ProgressDialog mProgressDialog;
    private ImageButton takePhoto;
    private ImageButton takeVideo;
    private ImageButton selectImage;

    private String userLocationKey;
    private String mCurrentPhotoPath;
    private String imageFileName;
    private Uri photoUri;
    private Uri videoUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mProgressDialog = new ProgressDialog(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_create_content, container, false);
        takePhoto = (ImageButton) root.findViewById(R.id.camera_button_create);
        takePhoto.setOnClickListener(this);
        takeVideo = (ImageButton) root.findViewById(R.id.video_button_create);
        takeVideo.setOnClickListener(this);
        selectImage = (ImageButton) root.findViewById(R.id.pic_image_create);
        selectImage.setOnClickListener(this);
        setActionBarTitle(root);
        return root;
    }

    private void setActionBarTitle(View v) {
        ((BaseActivity) v.getContext()).getSupportActionBar().setTitle(R.string.share_story);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent editIntent = new Intent(getApplicationContext(), EditContentActivity.class);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            addPicToGallery();
            editIntent.putExtra(PHOTOURI, photoUri.toString());
            editIntent.putExtra("userLocation", userLocationKey);
            startActivity(editIntent);
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Log.d(this.getClass().getSimpleName(), "onActivityResult: "+ requestCode);
            videoUri = data.getData();
            editIntent.putExtra(VIDEOURI, videoUri.toString());
            startActivity(editIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.camera_button_create:
                mProgressDialog.setMessage("Checking user location");
                mProgressDialog.show();
                FindLocation findLocation = new FindLocation(getApplicationContext(), this);
                findLocation.buildGoogleApiClient();
                findLocation.connectApiClient();
                break;

            case R.id.video_button_create:
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

    private void clickedButton(boolean foundLocation) {
        Log.d("nearby", String.valueOf(foundLocation));
        if (!foundLocation) {
            Toast.makeText(getApplicationContext(), "Sorry, you're currently not near a location", Toast.LENGTH_LONG).show();
        } else if (foundLocation && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
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

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void openCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "nyc.c4q.helenchan.makinghistory",
                        photoFile);
                List<ResolveInfo> resolvedIntentActivities = getContext().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;

                    getContext().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void addPicToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        photoUri = Uri.fromFile(file);
        galleryIntent.setData(photoUri);
        getApplicationContext().sendBroadcast(galleryIntent);

    }

    private void openVideo() {
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent openVideoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//            File videoFile = new File(
//                    Environment.getExternalStorageDirectory().getAbsolutePath() + ".mp4");
//            videoUri = Uri.fromFile(videoFile);
//            openVideoCapture.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(openVideoCapture, REQUEST_VIDEO_CAPTURE);
        } else {
            Toast.makeText(getContext(), "No camera found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void foundLocation(String userLocationkey, boolean foundLocation) {
        this.userLocationKey = userLocationkey;
        mProgressDialog.cancel();
        clickedButton(foundLocation);
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

