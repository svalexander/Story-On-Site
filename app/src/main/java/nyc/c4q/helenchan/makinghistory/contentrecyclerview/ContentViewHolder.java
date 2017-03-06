package nyc.c4q.helenchan.makinghistory.contentrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by leighdouglas on 3/6/17.
 */

public class ContentViewHolder extends RecyclerView.ViewHolder {
    private ImageView contentImageView;
    public ContentViewHolder(View itemView) {
        super(itemView);
        contentImageView = (ImageView) itemView.findViewById(R.id.content_image);
    }

    public void bind(Content c){
        Picasso.with(itemView.getContext()).load(c.getUrl()).into(contentImageView);
    }
}
