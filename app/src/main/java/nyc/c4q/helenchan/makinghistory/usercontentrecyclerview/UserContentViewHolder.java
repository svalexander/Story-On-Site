package nyc.c4q.helenchan.makinghistory.usercontentrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContentViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private ImageView userContentImageView;



    public UserContentViewHolder(View itemView) {
        super(itemView);
        userContentImageView = (ImageView) itemView.findViewById(R.id.user_photo_content);
    }


    public void bind(Content userContent) {

        final String userPhoto = userContent.getUrl();
        Glide.with(itemView.getContext())
                .load(userPhoto)
                .centerCrop()
                .into(userContentImageView);
    }


}