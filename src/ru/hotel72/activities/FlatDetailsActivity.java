package ru.hotel72.activities;

import android.os.Bundle;
import android.widget.TextView;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.02.13
 * Time: 20:36
 * To change this template use File | Settings | File Templates.
 */
public class FlatDetailsActivity extends BaseInfoActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInfoView(R.layout.flat_details);
        
        setContent();
    }

    private void setContent() {
        TextView square = (TextView) infoView.findViewById(R.id.squareLayout).findViewById(R.id.textSquare);
        square.setText(flat.square.toString() + " кв.м.");

        TextView rooms = (TextView) infoView.findViewById(R.id.roomsLayout).findViewById(R.id.textRooms);
        rooms.setText(flat.rooms.toString());

        TextView cost = (TextView) infoView.findViewById(R.id.costLayout).findViewById(R.id.textCost);
        cost.setText(flat.cost.toString() + " р");
    }
}
