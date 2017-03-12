package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nyc.c4q.helenchan.makinghistory.suggestedrecyclerview.SuggestedAdapter;

/**
 * Created by shannonalexander-navarro on 3/6/17.
 */

public class SuggestedFragment extends Fragment {
    private RecyclerView suggestedRV;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.suggested_fragment, container, false);
        suggestedRV = (RecyclerView) root.findViewById(R.id.suggested_rv);
        suggestedRV.setLayoutManager(new LinearLayoutManager(getContext()));
        suggestedRV.setAdapter(new SuggestedAdapter());
        setActionBarTitle(root);
        return root;
    }

    private void setActionBarTitle(View v) {
        ((BaseActivity) v.getContext()).getSupportActionBar().setTitle(R.string.suggested);
    }
}
