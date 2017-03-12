package nyc.c4q.helenchan.makinghistory;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

/**
 * Created by helenchan on 3/11/17.
 */

public class EditContentActivity extends AppCompatActivity {
    private ImageView portraitIV;
    private ImageView horizontalIV;
    private Uri photoUri;
    private Bitmap cameraBitmap;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        intiViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("PHOTOURI")) {
            String stringUri = extras.getString("PHOTOURI");
            cameraBitmap = getBitmapFromUri(stringUri);
            photoUri = Uri.parse(stringUri);
            settingImage(cameraBitmap);
        }
    }

    private void intiViews() {
        portraitIV = (ImageView) findViewById(R.id.preview_portrait_iv);
        horizontalIV = (ImageView) findViewById(R.id.preview_landscape_iv);
    }

    private Bitmap getBitmapFromUri(String uriString) {
        Bitmap imageBitmap = null;
        Uri photoUri = Uri.parse(uriString);
        try {
            imageBitmap = MediaStore.Images.Media
                    .getBitmap(getApplicationContext()
                            .getContentResolver(), photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }

    private void settingImage(Bitmap imageBitmap){
        if(imageBitmap.getWidth() > imageBitmap.getHeight()) {
            horizontalIV.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(photoUri.getPath())
                    .into(horizontalIV);
        }else {
            portraitIV.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(photoUri.getPath())
                    .override(portraitIV.getMaxWidth(), portraitIV.getMaxHeight())
                    .into(portraitIV);
        }
    }

}

