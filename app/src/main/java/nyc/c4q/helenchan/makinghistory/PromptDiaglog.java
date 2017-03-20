package nyc.c4q.helenchan.makinghistory;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by leighdouglas on 3/20/17.
 */

public class PromptDiaglog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prompt_diagfrag, container, false);

        Typeface titleFont = Typeface.createFromAsset(view.getContext().getAssets(), "ArimaMadurai-Regular.ttf");
        Typeface bodyFont = Typeface.createFromAsset(view.getContext().getAssets(), "Raleway-Regular.ttf");

        TextView title = (TextView) view.findViewById(R.id.prompt_title);
        title.setTypeface(titleFont);

        TextView message = (TextView) view.findViewById(R.id.prompt_message);
        message.setTypeface(bodyFont);

        ImageButton close = (ImageButton) view.findViewById(R.id.xbttn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
