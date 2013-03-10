package ru.hotel72.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.infteh.calendarview.*;
import net.simonvt.numberpicker.NumberPicker;
import ru.hotel72.R;
import ru.hotel72.accessData.GetBookingDatesTask;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.ImageHelper;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class BookingActivity extends BaseHeaderActivity implements View.OnClickListener, CalendarDatePickerDialog.OnDateSetListener {
    private Flat flat;
    private String flatId;
    private DateSelectionType dateSelectionType;
    private int[] mInDate;
    private int[] mLeaveDate;
    private int mVisitors = 1;
    private double cost;
    private BookingDaysTaskFactory taskFactory;
    public static BookingActivity bookingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookingActivity = this;

        setActivityView(R.layout.booking);

        flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        taskFactory = new BookingDaysTaskFactory(this, flat.post_id);

        setContent();
        setBtns();
    }

    private void setBtns() {

        View actionLayout = mActivityLevelView.findViewById(R.id.actionLayout);

        View inDate = actionLayout.findViewById(R.id.inDate);
        inDate.setOnClickListener(this);

        View leaveDate = actionLayout.findViewById(R.id.leaveDate);
        leaveDate.setOnClickListener(this);

        View visitors = actionLayout.findViewById(R.id.visitors);
        visitors.setOnClickListener(this);

        View booking = mActivityLevelView.findViewById(R.id.booking);
        booking.setOnClickListener(this);

    }

    private void setContent() {
        View subHeader = mActivityLevelView.findViewById(R.id.subHeader);

        setImg(subHeader);

        TextView rooms = (TextView) subHeader.findViewById(R.id.rooms);
        rooms.setText(flat.rooms.toString() + " комн.");

        TextView address = (TextView) subHeader.findViewById(R.id.address);
        address.setText(flat.street);

        TextView cost = (TextView) subHeader.findViewById(R.id.cost);
        cost.setText(flat.cost.toString());

        updateVisitorsValue(mVisitors);
    }

    private void updateVisitorsValue(Integer count) {
        TextView visitors = (TextView) mActivityLevelView
                .findViewById(R.id.actionLayout)
                .findViewById(R.id.visitors)
                .findViewById(R.id.visitorsValue);

        visitors.setText(count.toString());
    }

    private void setImg(View container) {
        if (flat.photos.size() == 0)
            return;

        ImageView image = (ImageView) container.findViewById(R.id.imageView);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String imgUrl = flat.photos.get(0).url;
        String url = String.format("http://hotel72.ru/index.php/api/GetFile?filename=%s&for=%s", imgUrl, "original");
        ImageHelper.getImageDownloader(this).DisplayImage(url, imgUrl, this, image);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.inDate: {
                dateSelectionType = DateSelectionType.Start;
                Calendar calendar = Calendar.getInstance();
                int[] date = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)};
                new CalendarDatePickerDialog(this, this, date, mInDate, mLeaveDate,
                        DateSelectionType.Start, taskFactory).show();
                break;
            }

            case R.id.leaveDate: {
                dateSelectionType = DateSelectionType.End;
                Calendar calendar = Calendar.getInstance();
                int[] date = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)};
                new CalendarDatePickerDialog(this, this, date, mInDate, mLeaveDate,
                        DateSelectionType.End, taskFactory).show();
                break;
            }

            case R.id.visitors: {
                ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.SampleTheme);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
                LayoutInflater inflater = (LayoutInflater) ctw.getSystemService(LAYOUT_INFLATER_SERVICE);
                View numeric = inflater.inflate(R.layout.numeric, null);
                final NumberPicker np = (NumberPicker) numeric.findViewById(R.id.numberPicker);
                np.setMaxValue(20);
                np.setMinValue(1);
                np.setFocusable(true);
                np.setFocusableInTouchMode(true);
                np.setValue(mVisitors);
                final AlertDialog dialog = builder.create();
                View done = numeric.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVisitors = np.getValue();
                        updateVisitorsValue(mVisitors);
                        dialog.dismiss();
                    }
                });
                dialog.setView(numeric);
                dialog.show();

                break;
            }

            case R.id.booking: {

                if(mInDate == null || mLeaveDate == null) {
                    Toast.makeText(this, "Необходимо заполнить все поля", 0).show();
                    return;
                }

                int[] beginDate = mInDate.clone();
                beginDate[1]++;

                int[] endDate = mLeaveDate.clone();
                endDate[1]++;

                Intent intent = new Intent(this, BookingUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(getString(R.string.id_hotel), flat.post_id);
                intent.putExtra(getString(R.string.date_stay_begin),
                        String.format("%s-%s-%s", beginDate[0],
                                beginDate[1] < 10 ? String.format("0%d", beginDate[1]) : beginDate[1],
                                beginDate[2] < 10 ? String.format("0%d", beginDate[2]) : beginDate[2]));
                intent.putExtra(getString(R.string.date_stay_finish),
                        String.format("%s-%s-%s", endDate[0],
                                endDate[1] < 10 ? String.format("0%d", endDate[1]) : endDate[1],
                                endDate[2] < 10 ? String.format("0%d", endDate[2]) : endDate[2]));
                intent.putExtra(getString(R.string.guests), mVisitors);
                startActivity(intent);
                break;
            }
        }


    }

    private void updatePrice() {

        if (mInDate == null || mLeaveDate == null || flat == null)
            return;

        TextView price = (TextView) mActivityLevelView
                .findViewById(R.id.actionLayout)
                .findViewById(R.id.cost)
                .findViewById(R.id.costValue);

        Calendar calIn = DateHelper.createCalendar(mInDate[0], mInDate[1], mInDate[2], TimeZone.getDefault().getID());
        Calendar calLeave = DateHelper.createCalendar(mLeaveDate[0], mLeaveDate[1], mLeaveDate[2], TimeZone.getDefault().getID());

        long diff = calLeave.getTimeInMillis() - calIn.getTimeInMillis();
        int days = (int) diff / (24 * 60 * 60 * 1000);
        days++;
        cost = days * flat.cost;

        price.setText(String.valueOf(cost));
    }

    @Override
    public void onDateSet(CalendarView view, int year, int monthOfYear, int dayOfMonth) {

        switch (dateSelectionType) {
            case Start: {
                TextView text = (TextView) mActivityLevelView
                        .findViewById(R.id.actionLayout)
                        .findViewById(R.id.inDate)
                        .findViewById(R.id.inDateValue);
                mInDate = new int[]{year, monthOfYear, dayOfMonth};
                monthOfYear++;
                text.setText(String.format("%s.%s.%d", dayOfMonth < 10 ? String.format("0%d", dayOfMonth) : dayOfMonth
                        , monthOfYear < 10 ? String.format("0%d", monthOfYear) : monthOfYear
                        , year));
                break;
            }
            case End: {
                TextView text = (TextView) mActivityLevelView
                        .findViewById(R.id.actionLayout)
                        .findViewById(R.id.leaveDate)
                        .findViewById(R.id.leaveDateValue);
                mLeaveDate = new int[]{year, monthOfYear, dayOfMonth};
                monthOfYear++;
                text.setText(String.format("%s.%s.%d", dayOfMonth < 10 ? String.format("0%d", dayOfMonth) : dayOfMonth
                        , monthOfYear < 10 ? String.format("0%d", monthOfYear) : monthOfYear
                        , year));
                break;
            }
        }

        updatePrice();
    }

    public class BookingDaysTaskFactory implements TaskFactory {

        private Context context;
        private int flatId;

        public BookingDaysTaskFactory(Context context, int flatId) {
            this.context = context;
            this.flatId = flatId;
        }

        @Override
        public GetUnselectableDaysTask getTask() {
            return new GetBookingDatesTask(context, flatId);
        }
    }
}
