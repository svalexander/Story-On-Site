package nyc.c4q.helenchan.makinghistory.usercontentrecyclerview;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.UserPhotoDetailActivity;
import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContentAdapter extends RecyclerView.Adapter<UserContentViewHolder> {

    private List<Content> userPhotoList = new ArrayList<>();

    public static String USER_PHOTO_URI = "UserPhotoUri";


    @Override
    public UserContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_content_item, parent, false);
        return new UserContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserContentViewHolder holder, int position) {
        Content content = userPhotoList.get(position);
        final String userPhotoUrl = userPhotoList.get(position).getUrl();
        holder.bind(content);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoDetailIntent = new Intent(view.getContext(), UserPhotoDetailActivity.class);
                photoDetailIntent .putExtra(USER_PHOTO_URI, userPhotoUrl);
                view.getContext().startActivity(photoDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userPhotoList.size();
    }

    public void setUserPhotoContent(List<Content> userPhotoList) {
        this.userPhotoList = userPhotoList;
        notifyDataSetChanged();
    }
}

