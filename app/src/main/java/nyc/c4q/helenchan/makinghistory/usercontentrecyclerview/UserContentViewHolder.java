package nyc.c4q.helenchan.makinghistory.usercontentrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContentViewHolder extends RecyclerView.ViewHolder {

    private ImageView userContentImageView;

    public UserContentViewHolder(View itemView) {
        super(itemView);
        userContentImageView = (ImageView) itemView.findViewById(R.id.user_photo_content);
    }

    public void bind(Content userContent) {
        if (!userContent.getType().equals("Historical"))
            Picasso.with(itemView.getContext())
                    .load(userContent.getUrl())
                    .into(userContentImageView);
        }

        /*
            userContentTextView.setVisibility(View.GONE);
            userContentImageView.setVisibility(View.VISIBLE);
         */

}