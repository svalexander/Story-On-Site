package nyc.c4q.helenchan.makinghistory.suggestedrecyclerview;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.ViewContentActivity;
import nyc.c4q.helenchan.makinghistory.suggestedrecyclerview.models.SuggestedItem;

/**
 * Created by leighdouglas on 3/11/17.
 */

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedViewholder> {
    private List<SuggestedItem> suggestedBttnList =
            new ArrayList<>(Arrays.asList(
                    new SuggestedItem(R.drawable.highlinebackground, "The Highline"),
                    new SuggestedItem(R.drawable.avalon, "The Avalon"),
                    new SuggestedItem(R.drawable.coneyisland_suggested, "Coney Island")));

    @Override
    public SuggestedViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_viewholder, parent, false);
        return new SuggestedViewholder(view);
    }

    @Override
    public void onBindViewHolder(SuggestedViewholder holder, int position) {
        final int temp = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (temp) {
                    case 0:
                        Log.d("Tag", "click registered");
                        Intent highline = new Intent(view.getContext(), ViewContentActivity.class);
                        highline.putExtra("Latitude", 40.743801);
                        highline.putExtra("Longitude", -74.007036);
                        view.getContext().startActivity(highline);
                        break;
                    case 1:
                        Log.d("Tag", "click registered");
                        Intent avalon = new Intent(view.getContext(), ViewContentActivity.class);
                        avalon.putExtra("Latitude",  40.741117);
                        avalon.putExtra("Longitude", -74.007036);
                        view.getContext().startActivity(avalon);
                        break;
                    case 2:
                        Log.d("Tag", "click registered");
                        Intent coney = new Intent(view.getContext(), ViewContentActivity.class);
                        coney.putExtra("Latitude", 40.574933);
                        coney.putExtra("Longitude", -73.98593);
                        view.getContext().startActivity(coney);
                        break;
                    default:
                }
            }
        });

        holder.bind(suggestedBttnList.get(position));

    }

    @Override
    public int getItemCount() {
        return suggestedBttnList.size();
    }
}
