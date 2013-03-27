package ru.hotel72.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.accessData.BookingTask;
import ru.hotel72.activities.adapters.Size;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.ImageHelper;
import ru.hotel72.utils.InfoDialog;

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
    private String flatId;
    private Flat flat;
    private Size imageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setActivityView(R.layout.booking_user);

        flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        setContent();

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

                TextView userView = (TextView) mActivityLevelView.findViewById(R.id.user_name);
                TextView phone_numberView = (TextView) mActivityLevelView.findViewById(R.id.phone_number);
//                TextView email = (TextView) mActivityLevelView.findViewById(R.id.email);

                String user = userView.getText().toString().trim();
                String phone = phone_numberView.getText().toString().trim();

                boolean hasUser = user != null && user != "";
                boolean hasPhone = phone != null && phone != "";
                if(!hasUser || !hasPhone){
                    String msg = !hasUser ? "Укажите имя" : "";
                    msg += !hasPhone
                            ? String.format("%s%s", !hasUser ? "\n" : "", "Укажите номер телефона")
                            : "";

                    InfoDialog.showDialog(BookingUserActivity.this, "Исправьте ошибки", msg);

                    return;
                }



                new BookingTask(context,
                        user,
                        phone,
                        mInDate, mLeaveDate,
                        mGuests,
                        mFlatId).execute();
            }
        });
    }

    private void setImage(View image){
        int w = image.getWidth();
        int h = image.getHeight();

        String imgUrl = flat.photos.get(0).url;
        String url = String.format("http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&w=%d&h=%d&zc=1&q=90", imgUrl, w, h);
        image.setTag(url);
        ImageHelper.getImageDownloader(this, ImageDownloaderType.Booking).DisplayImage(url, imgUrl, BookingUserActivity.this, (ImageView) image, true);
    }

    private void setContent() {
        View subHeader = mActivityLevelView.findViewById(R.id.subHeader);

        if (flat.photos.size() > 0){
            final ImageView image = (ImageView) subHeader.findViewById(R.id.icon_layout).findViewById(R.id.imageView);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if(imageSize == null) {
                image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        imageSize = new Size();
                        imageSize.width = image.getWidth();
                        imageSize.height = image.getHeight();

                        setImage(image);
                        image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
            } else {
                setImage(image);
            }
        }

        TextView rooms = (TextView) subHeader.findViewById(R.id.rooms);
        rooms.setText(flat.rooms.toString() + " комн.");

        TextView address = (TextView) subHeader.findViewById(R.id.address);
        address.setText(flat.street);

        TextView cost = (TextView) subHeader.findViewById(R.id.cost);
        cost.setText(flat.cost.toString());
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
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.showDialog), true);
        BookingActivity.bookingActivity.setResult(RESULT_OK, intent);
        BookingActivity.bookingActivity.finish();
        finish();
    }

    public void bookedError(String responseMsg) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.showDialog), true);
        intent.putExtra(getString(R.string.error), true);
        intent.putExtra(getString(R.string.errorMsg), responseMsg);
        BookingActivity.bookingActivity.setResult(RESULT_OK, intent);
        BookingActivity.bookingActivity.finish();
        finish();
    }
}
