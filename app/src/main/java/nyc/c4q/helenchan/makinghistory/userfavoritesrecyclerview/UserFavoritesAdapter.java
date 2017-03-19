package nyc.c4q.helenchan.makinghistory.userfavoritesrecyclerview;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.UserFavoriteDetailActivity;
import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.usercontentrecyclerview.UserContentViewHolder;

/**
 * Created by leighdouglas on 3/18/17.
 */

public class UserFavoritesAdapter extends RecyclerView.Adapter<UserContentViewHolder> {

    private List<Content> userFavoritesList = new ArrayList<>();

    public static String USER_PHOTO_URI = "UserPhotoUri";

    @Override
    public UserContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_content_item, parent, false);
        return new UserContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserContentViewHolder holder, int position) {
        Content content = userFavoritesList.get(position);
        final String userPhotoUrl = userFavoritesList.get(position).getUrl();
        holder.bind(content);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoDetailIntent = new Intent(view.getContext(), UserFavoriteDetailActivity.class);
                photoDetailIntent .putExtra(USER_PHOTO_URI, userPhotoUrl);
                view.getContext().startActivity(photoDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userFavoritesList.size();
    }

    public void setUserFavoritesList(List<Content> userFavoritesList) {
//        if(this.userFavoritesList != null){
//            this.userFavoritesList.clear();
//        }
        this.userFavoritesList = userFavoritesList;
        notifyDataSetChanged();
    }
}
