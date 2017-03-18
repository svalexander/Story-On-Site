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
                    new SuggestedItem(R.drawable.coneyisland_suggested, "Coney Island"),
                    new SuggestedItem(R.drawable.avalon, "The Avalon"),
                    new SuggestedItem(R.drawable.idp_button_background_facebook, "The Highline")));

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
                        Intent i = new Intent(view.getContext(), ViewContentActivity.class);
                        i.putExtra("Latitude", 40.574933);
                        i.putExtra("Longitude", -73.98593);
                        view.getContext().startActivity(i);
                        break;
                    case 1:
                        break;
                    case 2:
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
