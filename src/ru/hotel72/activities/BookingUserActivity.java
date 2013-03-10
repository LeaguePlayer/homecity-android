package ru.hotel72.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.accessData.BookingTask;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 10.03.13
 * Time: 1:34
 * To change this template use File | Settings | File Templates.
 */
public class BookingUserActivity extends BaseHeaderActivity {
    private String mInDate;
    private String mLeaveDate;
    private int mFlatId;
    private int mGuests;
    private BookingUserActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setActivityView(R.layout.booking_user);
        fillUserData(mActivityLevelView);

        Intent intent = getIntent();
        mInDate = intent.getStringExtra(getString(R.string.date_stay_begin));
        mLeaveDate = intent.getStringExtra(getString(R.string.date_stay_finish));
        mFlatId = intent.getIntExtra(getString(R.string.id_hotel), 0);
        mGuests = intent.getIntExtra(getString(R.string.guests), 0);

        View btn = mActivityLevelView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView user = (TextView) mActivityLevelView.findViewById(R.id.user_name);
                TextView phone_number = (TextView) mActivityLevelView.findViewById(R.id.phone_number);

                new BookingTask(context,
                        user.getText().toString().trim(),
                        phone_number.getText().toString().trim(),
                        mInDate, mLeaveDate,
                        mGuests,
                        mFlatId).execute();
            }
        });
    }

    public void fillUserData(View view) {
        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);

        String userName = pref.getString(getString(R.string.user_name), "");
        EditText userNameEdit = (EditText) view.findViewById(R.id.user_name);
        userNameEdit.setText(userName);

        String phoneNumber = pref.getString(getString(R.string.phone_number), "");
        EditText phoneNumberEdit = (EditText) view.findViewById(R.id.phone_number);
        phoneNumberEdit.setText(phoneNumber);
    }

    public void booked() {
        BookingActivity.bookingActivity.finish();
        finish();
    }
}
