package nyc.c4q.helenchan.makinghistory.suggestedrecyclerview;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.suggestedrecyclerview.models.SuggestedItem;

/**
 * Created by leighdouglas on 3/12/17.
 */

public class SuggestedViewholder extends RecyclerView.ViewHolder {

    private ImageView sggstdBttn;
    public TextView sggstdTitle;
    public View overlay;

    public SuggestedViewholder(View itemView) {
        super(itemView);
        sggstdBttn = (ImageView) itemView.findViewById(R.id.suggested_image);
        sggstdTitle = (TextView) itemView.findViewById(R.id.suggested_title);
        Typeface arimaMadurai = Typeface.createFromAsset(itemView.getContext().getApplicationContext().getAssets(), "ArimaMadurai-Regular.ttf");
        sggstdTitle.setTypeface(arimaMadurai);
        overlay = itemView.findViewById(R.id.suggestedOverlay);


    }

    public void bind(SuggestedItem suggestedItem) {
        sggstdBttn.setImageResource(suggestedItem.getDrawable());
        sggstdBttn.setScaleType(ImageView.ScaleType.CENTER_CROP);
        sggstdTitle.setText(suggestedItem.getCity());
    }

}

