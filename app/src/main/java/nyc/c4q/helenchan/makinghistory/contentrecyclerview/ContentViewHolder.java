package nyc.c4q.helenchan.makinghistory.contentrecyclerview;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

    public ContentViewHolder(View itemView) {
        super(itemView);
        contentImageView = (ImageView) itemView.findViewById(R.id.content_image);
        contentTitleTextView = (TextView) itemView.findViewById(R.id.content_title);
        contentDescriptionTextView = (TextView) itemView.findViewById(R.id.content_description);
        contentDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        contentYearTextView = (TextView) itemView.findViewById(R.id.content_year);
        Typeface titleFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "ArimaMadurai-Regular.ttf");
        Typeface bodyFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "Raleway-Regular.ttf" );
        contentTitleTextView.setTypeface(titleFont);
        contentDescriptionTextView.setTypeface(bodyFont);
        contentYearTextView.setTypeface(bodyFont);
        expandedImage = (ImageView) itemView.findViewById(R.id.expanded_IV);
    }

    public void bind(final Content c) {
        Glide.with(itemView.getContext())
                .load(c.getUrl())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(contentImageView);

        contentTitleTextView.setText(c.getName());
        contentDescriptionTextView.setText(c.getText());
        contentYearTextView.setText(c.getYear());

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

}
