package nyc.c4q.helenchan.makinghistory.usercontentrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.UserContent;

/**
 * Created by Akasha on 3/8/17.
 */

public class UserContentAdapter  extends RecyclerView.Adapter<UserContentViewHolder> {

    private List<UserContent> userContentList = new ArrayList<>();

    @Override
    public UserContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_content_item, parent, false);
        return new UserContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserContentViewHolder holder, int position) {
        UserContent userContent = userContentList.get(position);
        holder.bind(userContent);
    }

    @Override
    public int getItemCount() {
        return userContentList.size();
    }

//    public void setMapContent(List<Content> mapContent) {
//        this.mapContent = mapContent;
//        notifyDataSetChanged();
//    }
}
