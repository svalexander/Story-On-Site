package nyc.c4q.helenchan.makinghistory.usercontentrecyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by leighdouglas on 3/12/17.
 */

public class RvCenterStart extends RecyclerView.ItemDecoration {
    private final int size;

    public RvCenterStart(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left += size;
        }
    }
}

