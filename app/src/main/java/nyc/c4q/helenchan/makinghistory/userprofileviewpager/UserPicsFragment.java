package nyc.c4q.helenchan.makinghistory.userprofileviewpager;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.usercontentrecyclerview.UserContentAdapter;

/**
 * Created by leighdouglas on 3/18/17.
 */

public class UserPicsFragment extends Fragment {
    private RecyclerView userContentRV;
    private UserContentAdapter userContentAdapter;
    private DatabaseReference photoRef;
    private String uId;

    private OnPictureCountListener picListener;

    private List<Content> userPhotoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_stories_vp, container, false);
        userContentRV = (RecyclerView) view.findViewById(R.id.stories_rv);
        userContentRV.setLayoutManager((new GridLayoutManager(view.getContext(), 2)));
        userContentAdapter = new UserContentAdapter();
        userContentRV.setAdapter(userContentAdapter);

        picListener = (OnPictureCountListener) getActivity();

        uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(uId);

        photoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uId).child("ContentList");

        photoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Content userPhotoUrl = dataSnapshot.getValue(Content.class);
                userPhotoList.add(userPhotoUrl);
                userContentAdapter.setUserPhotoContent(userPhotoList);

                picListener.updatePhotoCount(userPhotoList.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Content userPhotoUrl = dataSnapshot.getValue(Content.class);
                userPhotoList.remove(userPhotoUrl);
                userContentAdapter.setUserPhotoContent(userPhotoList);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;


    }

    public interface OnPictureCountListener{
        void updatePhotoCount(int count);

    }

}
