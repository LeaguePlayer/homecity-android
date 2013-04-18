package com.infteh.calendarview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple dialog containing an {@link android.widget.DatePicker}.
 */
public class CalendarDatePickerDialog extends AlertDialog implements OnDateChangedListener {
	/**
	 * callback.
	 */
	private final OnDateSetListener mCallBack;
	/**
	 * current date.
	 */
	private final Calendar mCalendar;
	/**
	 * title format.
	 */
	private final java.text.DateFormat mTitleDateFormat;
	/**
	 * calendar view.
	 */
	private final CalendarView mCalendarMonthView;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {

		/**
		 * @param view The view associated with this listener.
		 * @param year The year that was set.
		 * @param monthOfYear The month that was set (0-11) for compatibility
		 *            with {@link java.util.Calendar}.
		 * @param dayOfMonth The day of the month that was set.
		 */
		void onDateSet(CalendarView view, int year, int monthOfYear, int dayOfMonth);
	}

	/**
     * @param context The context the dialog is to run in.
     * @param callBack How the parent is notified that the date is set.
     * @param date The initial date of the dialog, y,m,d.
     * @param inDate The in date of the order, y,m,d.
     * @param selectionType The type of selection, start or end of the interval
     */
	public CalendarDatePickerDialog(
            Context context,
            OnDateSetListener callBack,
            int[] date,
            final int[] inDate,
            int[] leaveDate,
            DateSelectionType selectionType,
            TaskFactory taskFactory) {
		super(context);

        final Calendar inDay = inDate != null
                ? DateHelper.createCalendar(inDate[0], inDate[1], inDate[2], TimeZone.getDefault().getID())
                : null;

        final Calendar leaveDay = leaveDate != null
                ? DateHelper.createCalendar(leaveDate[0], leaveDate[1], leaveDate[2], TimeZone.getDefault().getID())
                : null;

		mCalendarMonthView = new CalendarView(context, null, taskFactory);

        int[] curDate = date;
        if(inDate != null){
            curDate = inDate;
        } else if(leaveDate != null){
            curDate = leaveDate;
        }

        CalendarAdapter.resetCloset(); // зачищаем значения ближайших занятых дней перед инициализацией
		mCalendarMonthView.setAllDay(
                DateHelper.createCalendar(curDate[0], curDate[1], curDate[2], TimeZone.getDefault().getID()),
                inDay,
                leaveDay,
                selectionType);
		setView(mCalendarMonthView);

		mCallBack = callBack;

		setButton2(context.getText(R.string.cancel), (OnClickListener) null);
		mCalendarMonthView.registerCalendarDatePickObserver(new CalendarDatePick() {
			public void onDatePicked(CalendarAdapter.DayCell dayCell) {

                if(!dayCell.isSelectable)
                    return;

				Calendar picked = dayCell.getDate();
				mCallBack.onDateSet(mCalendarMonthView, picked.get(Calendar.YEAR), picked.get(Calendar.MONTH), picked.get(Calendar.DAY_OF_MONTH));
				dismiss();
			}
		});

        mCalendar = Calendar.getInstance();
        mTitleDateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL);
        updateDate(curDate[0], curDate[1], curDate[2]);

	}

    /**
	 * @param view view.
	 * @param year y.
	 * @param month m.
	 * @param day d. 
	 */
	public final void onDateChanged(DatePicker view, int year, int month, int day) {
		updateDate(year, month, day);
	}

	/**
	 * @param year y.
	 * @param monthOfYear m.
	 * @param dayOfMonth d.
	 */
	public final void updateDate(int year, int monthOfYear, int dayOfMonth) {
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, monthOfYear);
		mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		updateTitle();
		mCalendarMonthView.setMonth(mCalendar);
	}

	/**
	 * update title.
	 */
	private void updateTitle() {
		setTitle(mTitleDateFormat.format(mCalendar.getTime()));
	}
}
