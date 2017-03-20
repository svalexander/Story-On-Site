package nyc.c4q.helenchan.makinghistory.contentrecyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.models.Content;

/**
 * Created by leighdouglas on 3/6/17.
 */

public class ContentViewHolder extends RecyclerView.ViewHolder {
    private ImageView contentImageView;
    private TextView contentTitleTextView;
    private TextView contentDescriptionTextView;
    private TextView contentYearTextView;
    private ImageView expandedImage;
    private VideoView userVideoView;
    private ImageView likeBttn;
    boolean liked = false;

    public ContentViewHolder(View itemView) {
        super(itemView);

        contentImageView = (ImageView) itemView.findViewById(R.id.content_image);
        contentTitleTextView = (TextView) itemView.findViewById(R.id.content_title);
        contentDescriptionTextView = (TextView) itemView.findViewById(R.id.content_description);
        contentDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
        likeBttn = (ImageView) itemView.findViewById(R.id.like_bttn);
        contentYearTextView = (TextView) itemView.findViewById(R.id.content_year);
        Typeface titleFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "ArimaMadurai-Regular.ttf");
        Typeface bodyFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "Raleway-Regular.ttf");
        contentTitleTextView.setTypeface(titleFont);
        contentDescriptionTextView.setTypeface(bodyFont);
        contentYearTextView.setTypeface(bodyFont);
        expandedImage = (ImageView) itemView.findViewById(R.id.expanded_IV);
        userVideoView = (VideoView) itemView.findViewById(R.id.content_videoview);
    }

    public void bind(final Content c, List<Content> userFavorites) {

        if (userFavorites != null) {
            for (Content current : userFavorites)
                if (current.getUrl().equalsIgnoreCase(c.getUrl())) {
                    likeBttn.setColorFilter(Color.parseColor("#D32F2F"));
                    liked = true;
                }
        }

        if (userFavorites != null) {

        }
        likeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liked) {
                    ObjectAnimator anim = ObjectAnimator.ofArgb(v, "colorFilter", Color.parseColor("#9E9E9E"));
                    anim.setDuration(300);
                    anim.start();
                    final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserFavorites");
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                System.out.println(ds.getKey());
                                Content currentValue = ds.getValue(Content.class);
                                if (c.getUrl().equalsIgnoreCase(currentValue.getUrl())) {
                                    System.out.println(ds.getRef().child(ds.getKey()));
                                    ds.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    liked = false;

                } else if (!liked) {
                    ObjectAnimator anim = ObjectAnimator.ofArgb(v, "colorFilter", Color.parseColor("#D32F2F"));
                    anim.setDuration(300);
                    anim.start();
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                    ref1.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserFavorites").push().setValue(c);
                    liked = true;
                }
            }
        });
        contentTitleTextView.setText(c.getName());
        contentDescriptionTextView.setText(c.getText());
        checkIfYearIsListed(c);
        loadVideoOrPhoto(c);

        contentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentImageView.setVisibility(View.INVISIBLE);
                expandedImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(c.getUrl())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(expandedImage);
            }
        });

        expandedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandedImage.setVisibility(View.INVISIBLE);
                contentImageView.setVisibility(View.VISIBLE);
            }
        });

//        contentImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new TransferAnimation(contentImageView).setDestinationView(expandedImage).animate();
//            }
//        });
    }

    private void loadVideoOrPhoto(Content c) {
        if (c.getType().equals("video")) {
            userVideoView.setVisibility(View.VISIBLE);
            contentImageView.setVisibility(View.GONE);
            Uri videoUri = Uri.parse(c.getUrl());
            userVideoView.setMediaController(new MediaController(itemView.getContext()));
            userVideoView.setVideoURI(videoUri);
            userVideoView.requestFocus();
            userVideoView.start();

        } else {
            userVideoView.setVisibility(View.GONE);
            contentImageView.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext())
                    .load(c.getUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(contentImageView);
        }
    }

    private void checkIfYearIsListed(Content c) {
        if ((c.getYear()).equals("0")) {
            contentYearTextView.setText("Year Unknown");
        } else {
            contentYearTextView.setText(c.getYear());
        }
    }

}

