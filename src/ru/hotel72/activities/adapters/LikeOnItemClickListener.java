package ru.hotel72.activities.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ToggleButton;
import ru.hotel72.accessData.DBHelper;
import ru.hotel72.accessData.GetFlatHelper;
import ru.hotel72.domains.Flat;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class LikeOnItemClickListener implements View.OnClickListener {
    private Context context;
    private Flat flat;

    public LikeOnItemClickListener(Context context, FlatsListAdapter flatsListAdapter, Flat flat) {
        this.context = context;
        this.flat = flat;
    }

    @Override
    public void onClick(View view) {
        ToggleButton btn = (ToggleButton) view;

        if(btn.isChecked()){
            flat.isLiked = true;
            if(!GetFlatHelper.likedFlat.contains(flat.id)){
                GetFlatHelper.likedFlat.add(flat.id);
            }
            new DBHelper(context).addLickedFlat(flat.id);
        }
        else
        {
            flat.isLiked = false;
            if(GetFlatHelper.likedFlat.contains(flat.id)){
                GetFlatHelper.likedFlat.remove(flat.id);
            }
            new DBHelper(context).removeLickedFlat(flat.id);
        }
    }
}
