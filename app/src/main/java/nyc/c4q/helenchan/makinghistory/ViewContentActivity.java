package nyc.c4q.helenchan.makinghistory;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.contentrecyclerview.ViewContentAdapter;
import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.models.MapPoint;
import nyc.c4q.helenchan.makinghistory.usercontentrecyclerview.RvCenterStart;

/**
 * Created by leighdouglas on 3/6/17.
 */

public class ViewContentActivity extends AppCompatActivity {

    private RecyclerView contentRV;
    private DatabaseReference databaseRefToMappoint;
    private DatabaseReference refToLocationOfPicTaken;
    private double lat;
    private double lng;
    private ViewContentAdapter viewContentAdapter;
    private ProgressDialog mProgressDialog;
    private Animator animator;
    private int shortAnimationLength;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcontent);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5e454b")));

        contentRV = (RecyclerView) findViewById(R.id.content_recycler_view);
        contentRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        viewContentAdapter = new ViewContentAdapter();
        contentRV.setAdapter(viewContentAdapter);
        contentRV.addItemDecoration(new RvCenterStart(40));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(contentRV);
        lat = getIntent().getDoubleExtra("Latitude", 0);
        lng = getIntent().getDoubleExtra("Longitude", 0);
        Log.d("lat", String.valueOf(lat));
        mProgressDialog = new ProgressDialog(this);

//        final ImageView contentImage = (ImageView) findViewById(R.id.content_image);
//        contentImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        setFontType();
    }

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
    }

    private void enlargeImage(final ImageView originalImage, int image) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(EditContentActivity.PICLATLONG)) {
                String location = extras.getString(EditContentActivity.PICLATLONG);
                refToLocationOfPicTaken = FirebaseDatabase.getInstance().getReference().child("MapPoint").child(location);
                refToLocationOfPicTaken.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MapPoint mapPoint = dataSnapshot.getValue(MapPoint.class);
                        lat = mapPoint.getLatitude();
                        lng = mapPoint.getLongitude();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }

        databaseRefToMappoint = FirebaseDatabase.getInstance().getReference().child("MapPoint");
        databaseRefToMappoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Content> tempList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MapPoint mp = ds.getValue(MapPoint.class);
                    if (mp.getLatitude() == lat && mp.getLongitude() == lng) {
                        HashMap<String, Content> contentList = mp.getContentList();
                        for (String s : contentList.keySet()) {
                            tempList.add(contentList.get(s));
                        }
                        break;
                    }
                }

                viewContentAdapter.setMapContent(tempList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
