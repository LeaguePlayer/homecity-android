package ru.hotel72.activities.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.*;
import ru.hotel72.R;
import ru.hotel72.domains.Flat;
import ru.hotel72.domains.extension.FlatListExtension;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 27.01.13
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
public class FlatsListAdapter extends ArrayAdapter<Flat> {

    private Context context;
    private ArrayList<Flat> flats;
    private ViewHolder holder;
    private GalleryAdapter galleryAdapter;
    private HashMap<Integer, GalleryAdapter> galleryCache = new HashMap<Integer, GalleryAdapter>();

    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

    public FlatsListAdapter(Context context, int textViewResourceId, ArrayList<Flat> flats) {
        super(context, textViewResourceId, flats);
        this.context = context;
        this.flats = flats;
    }

    public void updateContext(Context context){
        this.context = context;
    }

    private static class ViewHolder{
        public TextView cost;
        public TextView rooms;
        public TextView address;
        public ToggleButton isLiked;
        public Gallery gallery;
        public View costLayout;
        public View tagPiece;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {
            final LayoutInflater mInflate = LayoutInflater.from(context);
            convertView = mInflate.inflate(R.layout.flat_list_item, parent, false);

            holder = new ViewHolder();
            holder.cost = (TextView) convertView.findViewById(R.id.tag_layout).findViewById(R.id.cost_layout).findViewById(R.id.cost);
            holder.costLayout = convertView.findViewById(R.id.tag_layout).findViewById(R.id.cost_layout);
            holder.rooms = (TextView) convertView.findViewById(R.id.gallery_item_footer).findViewById(R.id.rooms);
            holder.address = (TextView) convertView.findViewById(R.id.gallery_item_footer).findViewById(R.id.address);
            holder.isLiked = (ToggleButton) convertView.findViewById(R.id.gallery_item_footer).findViewById(R.id.isLiked);
            holder.gallery = (Gallery) convertView.findViewById(R.id.gallery);
            holder.tagPiece = convertView.findViewById(R.id.tag_piece);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Flat flat = flats.get(position);
        if (flat != null) {

            if(flat.rooms > 0){
                holder.rooms.setText(flat.rooms.toString() + " комн.");
            }
            holder.address.setText(flat.street);

            Object tag = holder.costLayout.getTag();
            if(tag == null){
                RotateAnimation rotate= (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotate_cost);
                holder.costLayout.setAnimation(rotate);
                holder.costLayout.setTag(true);
            }

            if (flat.cost != Double.NaN && holder.cost != null) {
                SpannableString spanString = new SpannableString(Utils.getCostString(flat.cost));
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

                holder.cost.setText(spanString);
            }
            if(galleryCache.containsKey(flat.id)){
                galleryAdapter = galleryCache.get(flat.id);
            }
            else {
                galleryAdapter = new GalleryAdapter(context, R.layout.gallary_item, flat.photos, ImageDownloaderType.LIST);
                galleryCache.put(flat.id, galleryAdapter);
            }

            holder.isLiked.setChecked(flat.isLiked);
            holder.isLiked.setOnClickListener(new LikeOnItemClickListener(context, this, flat));

            holder.gallery.setAdapter(galleryAdapter);
            holder.gallery.setOnItemClickListener(new FlatOnItemClickListener(context, flat));

            if(position < flats.size() - 1){
                holder.tagPiece.setVisibility(holder.tagPiece.VISIBLE);
            }
            else {
                holder.tagPiece.setVisibility(holder.tagPiece.INVISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public void add(Flat object) {
        if(flats == null){
            flats = new ArrayList<Flat>();
        }

        if(!FlatListExtension.Contains(flats, object)){
            flats.add(object);
        }
    }
}

