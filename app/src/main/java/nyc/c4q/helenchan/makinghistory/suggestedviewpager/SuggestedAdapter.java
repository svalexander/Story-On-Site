package nyc.c4q.helenchan.makinghistory.suggestedviewpager;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;
import nyc.c4q.helenchan.makinghistory.ViewContentActivity;
import nyc.c4q.helenchan.makinghistory.suggestedviewpager.models.SuggestedItem;

/**
 * Created by leighdouglas on 3/11/17.
 */

public class SuggestedAdapter extends PagerAdapter implements View.OnClickListener{
    private List<SuggestedItem> suggestedBttnList =
            new ArrayList<>(Arrays.asList(
                    new SuggestedItem(R.drawable.coneyisland_suggested, "Coney Island"),
                    new SuggestedItem(R.drawable.com_facebook_auth_dialog_cancel_background, "The Avalon"),
                    new SuggestedItem(R.drawable.idp_button_background_facebook, "The Highline")));

    @Override
    public int getCount() {
        return suggestedBttnList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ViewGroup layout = (ViewGroup) LayoutInflater.from(container.getContext()).inflate(R.layout.suggestedviewpagerlayout, container, false);
        container.addView(layout);
        ImageButton imgBttn = (ImageButton) layout.findViewById(R.id.suggested_bttn);
        TextView imgTxt = (TextView) layout.findViewById(R.id.suggested_text);

        Typeface maduraBld = Typeface.createFromAsset(imgTxt.getContext().getApplicationContext().getAssets(), "ArimaMadurai-Bold.ttf");
        imgTxt.setTypeface(maduraBld);

        switch(position){
            case 0:
                imgBttn.setBackground(container
                        .getContext()
                        .getResources()
                        .getDrawable(suggestedBttnList.get(position).getDrawable()));
                imgBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), ViewContentActivity.class);
                        i.putExtra("Latitude", 40.574933);
                        i.putExtra("Longitude", -73.98593);
                        view.getContext().startActivity(i);
                    }
                });
                imgTxt.setText("Coney \n Island");
                break;

            case 1:
                ImageButton imgBttn2 = (ImageButton) layout.findViewById(R.id.suggested_bttn);
                imgBttn.setBackground(container.getContext().getResources().getDrawable(suggestedBttnList.get(position).getDrawable()));
                break;
            case 2:
                ImageButton imgBttn3 = (ImageButton) layout.findViewById(R.id.suggested_bttn);
                imgBttn3.setBackground(container.getContext().getResources().getDrawable(suggestedBttnList.get(position).getDrawable()));
                break;
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public void onClick(View view) {

    }
}
