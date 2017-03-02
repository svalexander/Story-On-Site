package nyc.c4q.helenchan.makinghistory.leigh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import nyc.c4q.helenchan.makinghistory.R;

/**
 * Created by leighdouglas on 2/27/17.
 */

public class AddConentActivity extends AppCompatActivity implements View.OnClickListener {
    static int REQUEST_IMAGE_CAPTURE = 1;
    static int REQUEST_VIDEO_CAPTURE = 2;

    private Button takePhoto;
    private Button addText;
    private Button takeVideo;
    private ImageView imagePreview;
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontent);

        initViews();
    }

    private void initViews(){
        imagePreview = (ImageView) findViewById(R.id.display_image);
        videoView = (VideoView) findViewById(R.id.display_video);
        takePhoto = (Button) findViewById(R.id.bttn_takePic);
        takePhoto.setOnClickListener(this);
        addText = (Button) findViewById(R.id.bttn_addText);
        takeVideo = (Button) findViewById(R.id.bttn_takeVideo);
        takeVideo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            videoView.setVisibility(View.GONE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagePreview.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bttn_takePic:
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCamera, REQUEST_IMAGE_CAPTURE);
                break;
            case R.id.bttn_takeVideo:
                Intent openVideoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(openVideoCapture, REQUEST_VIDEO_CAPTURE);
                break;
            default:
        }
    }
}
