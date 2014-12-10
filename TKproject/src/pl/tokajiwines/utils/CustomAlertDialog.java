
package pl.tokajiwines.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pl.tokajiwines.R;

public class CustomAlertDialog extends AlertDialog {

    /**
     * Constructor for <code>CustomDialog</code>
     * 
     * @param context
     *            The {@link Context} to use
     */
    public CustomAlertDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Resources res = getContext().getResources();
        final int filter = res.getColor(R.color.filter_text);

        // Title
        final int titleId = res.getIdentifier("alertTitle", "id", "android");
        final View title = findViewById(titleId);
        if (title != null) {
            ((TextView) title).setTextColor(filter);
        }

        // Title divider
        final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
        final View titleDivider = findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(filter);
        }
    }

}
