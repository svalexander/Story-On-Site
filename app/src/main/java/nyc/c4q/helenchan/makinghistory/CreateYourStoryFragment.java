package nyc.c4q.helenchan.makinghistory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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
    private TextView title;
    private TextView prompt;

    private String userLocationKey;
    private String mCurrentPhotoPath;
    private String imageFileName;
    private boolean cameraPressed;
    private boolean videoPressed;
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
        title = (TextView) root.findViewById(R.id.create_title_tv);
        Typeface titleFont = Typeface.createFromAsset(root.getContext().getAssets(), "ArimaMadurai-Regular.ttf");
        title.setTypeface(titleFont);
        Typeface bodyFont = Typeface.createFromAsset(root.getContext().getAssets(), "Raleway-Regular.ttf");
        prompt = (TextView) root.findViewById(R.id.create_prompt_tv);
        prompt.setTypeface(bodyFont);
        takePhoto = (ImageButton) root.findViewById(R.id.camera_button_create);
        takePhoto.setOnClickListener(this);
        takeVideo = (ImageButton) root.findViewById(R.id.video_button_create);
        takeVideo.setOnClickListener(this);
        selectImage = (ImageButton) root.findViewById(R.id.pic_image_create);
        selectImage.setOnClickListener(this);
        setActionBarTitle(root);
        setHasOptionsMenu(true);
        return root;
    }

    private void setActionBarTitle(View v) {
        ((BaseActivity) v.getContext()).getSupportActionBar().setTitle(R.string.share_story);
        ((BaseActivity) v.getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            Log.d(this.getClass().getSimpleName(), "onActivityResult: " + requestCode);
            videoUri = data.getData();
            editIntent.putExtra(VIDEOURI, videoUri.toString());
            editIntent.putExtra("userLocation", userLocationKey);
            startActivity(editIntent);
        }
    }

    @Override
    public void onClick(View view) {
        if (!checkPermissions()) {
            requestCameraPermissions(Constants.REQUEST_CODE_CAMERAANDLOCATION);
        } else {
            setupLocationService();
        }

        switch (view.getId()) {

            case R.id.camera_button_create:
                cameraPressed = true;
                videoPressed = false;
                break;
            case R.id.video_button_create:
                videoPressed = true;
                cameraPressed = false;
                break;
            default:
        }
    }

    private void setupLocationService() {
        mProgressDialog.setMessage("Checking user location");
        mProgressDialog.show();
        FindLocation findLocation = new FindLocation(getApplicationContext(), this);
        findLocation.buildGoogleApiClient();
        findLocation.connectApiClient();
    }

    //temp workaround
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getContext(), BaseActivity.class);
                startActivity(intent);

                //this doesnt work because it knows it's in base and wants to find the parent of base
                //however what we need to do is remove the fragment but adding to backstack crashes b/c of map
//                NavUtils.navigateUpFromSameTask(getActivity());
//                return true;

                //this goes back to base
                // getActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickedButton(boolean foundLocation) {
        Log.d("nearby", String.valueOf(foundLocation));
        if (!foundLocation) {
            Toast.makeText(getApplicationContext(), "Sorry, you're currently not near a location", Toast.LENGTH_LONG).show();
        } else if (foundLocation) {
            if (cameraPressed) {
                openCamera();
            } else if (videoPressed) {
                openVideo();
            }
        }
    }

    private boolean checkPermissions() {
        return (
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermissions(int requestCode) {
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA}, requestCode);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CODE_CAMERAANDLOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupLocationService();
                }
        }
    }
}

