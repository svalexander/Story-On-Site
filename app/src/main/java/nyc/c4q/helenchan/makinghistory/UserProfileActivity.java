package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.userprofileviewpager.UserPicsFragment;
import nyc.c4q.helenchan.makinghistory.userprofileviewpager.UserViewPagerAdapter;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserProfileActivity extends AppCompatActivity implements UserPicsFragment.OnPictureCountListener {
    private String TAG = "User Profile Activity: ";

    private ImageView userProfilePhoto;
    private TextView userNameTv;
    private TextView userPhotoCountTv;
    private int numUserPhotos = 0;
    private RelativeLayout userContentLayout;

    private DatabaseReference photoRef;

    private UserViewPagerAdapter userViewPagerAdapter;
    private TabLayout tl;
    private ViewPager vp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        photoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("ContentList");

        setFontType();
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions()) {
                    requestCameraPermissions();
                } else {
                    openCamera();
                }
            }
        });

        String userName = SignInActivity.mUsername;
        userNameTv.setText(userName);

        tl = (TabLayout) findViewById(R.id.content_tabs);
        vp = (ViewPager) findViewById(R.id.content_vp);
        userViewPagerAdapter = new UserViewPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(userViewPagerAdapter);
        tl.setupWithViewPager(vp);
    }

    private void initViews() {
        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        userNameTv = (TextView) findViewById(R.id.user_profile_name);
        userPhotoCountTv = (TextView) findViewById(R.id.user_num_photos);
        userContentLayout = (RelativeLayout) findViewById(R.id.profileContent);
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
            //put whatever logic you guys would like here!
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        userProfilePhoto.setImageBitmap(imageBitmap);
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
}


