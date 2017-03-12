package nyc.c4q.helenchan.makinghistory.contentrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by leighdouglas on 3/6/17.
 */

public class ViewContentAdapter extends RecyclerView.Adapter<ContentViewHolder> {
    private List<Content> mapContent = new ArrayList<>();

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_viewholder, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        Content c = mapContent.get(position);
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return mapContent.size();
    }

    public void setMapContent(List<Content> mapContent) {
        this.mapContent = mapContent;
        notifyDataSetChanged();
    }
}
