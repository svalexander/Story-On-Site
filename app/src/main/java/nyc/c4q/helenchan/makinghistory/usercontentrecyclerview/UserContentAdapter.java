package nyc.c4q.helenchan.makinghistory.usercontentrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContentAdapter  extends RecyclerView.Adapter<UserContentViewHolder> {

    private List<Content> userPhotoList = new ArrayList<>();

    @Override
    public UserContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_content_item, parent, false);
        return new UserContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserContentViewHolder holder, int position) {
        Content content = userPhotoList.get(position);
        holder.bind(content);
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

