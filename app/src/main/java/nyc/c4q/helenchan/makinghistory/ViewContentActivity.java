package nyc.c4q.helenchan.makinghistory;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

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

/**
 * Created by leighdouglas on 3/6/17.
 */

public class ViewContentActivity extends AppCompatActivity {

    private RecyclerView contentRV;
    private DatabaseReference ref1;
    private double lat;
    private double lng;
    private ViewContentAdapter viewContentAdapter;
    private ProgressDialog mProgressDialog;
    private TextView titleTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcontent);


        contentRV = (RecyclerView) findViewById(R.id.content_recycler_view);
        contentRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        viewContentAdapter = new ViewContentAdapter();
        contentRV.setAdapter(viewContentAdapter);
        lat = getIntent().getDoubleExtra("Latitude", 0);
        lng = getIntent().getDoubleExtra("Longitude", 0);
        Log.d("lat", String.valueOf(lat));
        titleTV = (TextView) findViewById(R.id.content_title);
        setFontType();
        mProgressDialog = new ProgressDialog(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ref1 = FirebaseDatabase.getInstance().getReference().child("MapPoint");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.content_title), "Raleway-Regular.ttf");
    }


    //TODO: build retrofit call to NYTimes Api here
}
