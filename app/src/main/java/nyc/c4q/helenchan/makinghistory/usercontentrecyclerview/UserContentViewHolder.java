package nyc.c4q.helenchan.makinghistory.usercontentrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.contentrecyclerview.ContentViewHolder;
import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.models.UserContent;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContentViewHolder extends RecyclerView.ViewHolder {

    private ImageView userContentImageView;

    public UserContentViewHolder(View itemView) {
        super(itemView);
        userContentImageView = (ImageView) itemView.findViewById(R.id.user_photo_content);
    }

    public void bind(UserContent userContent) {
            Picasso.with(itemView.getContext())
                    .load(userContent.getUserPhotoUrl())
                    .into(userContentImageView);
        }

        /*
            userContentTextView.setVisibility(View.GONE);
            userContentImageView.setVisibility(View.VISIBLE);
         */

}