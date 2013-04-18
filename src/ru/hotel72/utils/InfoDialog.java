package ru.hotel72.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 3:01
 * To change this template use File | Settings | File Templates.
 */
public class InfoDialog {

    public static void showDialog(Context context, CharSequence title, CharSequence text) {
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;

        builder = new AlertDialog.Builder(context);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        builder.setMessage(text);
        alertDialog = builder.create();

        alertDialog.show();
        TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        messageText.setTextSize(15f);
    }
}
