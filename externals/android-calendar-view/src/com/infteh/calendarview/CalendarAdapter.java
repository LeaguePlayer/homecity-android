package com.infteh.calendarview;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * calendar adapter.
 * @author Sazonov-adm
 * 
 */
public class CalendarAdapter extends BaseAdapter {

	/**
	 * references to our items.
	 */
	private DayCell[] days;

	/**
	 * context.
	 */
	private Context mContext;

	/**
	 * init month.
	 */
	private java.util.Calendar mCurrentMonth;
	/**
	 * today.
	 */
	private Calendar mToday;

	/**
	 * day that was picked.
	 */
	private Calendar mCurrentDay;
    private Calendar mInDay;
    private Calendar mLeaveDay;
    private DateSelectionType selectionType;
    private ArrayList<Integer> unselectableDays;
    private int closetRight;
    private int closetLeft;
    private int DAY_COUNT = 42;
    private CalendarMonthView.OnAllDaysRendered onAllDaysRendered;
    private int count;

    public void setUnselectableDays (ArrayList<Integer> unselectableDays){
        this.unselectableDays = unselectableDays;
    }

    /**
     * @param context context.
     * @param monthCalendar current month.
     */
	protected CalendarAdapter(Context context, Calendar monthCalendar, CalendarMonthView.OnAllDaysRendered onAllDaysRendered) {
        this.onAllDaysRendered = onAllDaysRendered;
        mToday = DateHelper.createCurrentBeginDayCalendar();
		mCurrentMonth = (Calendar) monthCalendar.clone();
		mContext = context;
		mCurrentMonth.set(Calendar.DAY_OF_MONTH, 1);
		refreshDays();
	}

	/**
	 * @param currentDay day that was picked.
	 */
	public void setCurrentDay(Calendar currentDay) {
		mCurrentDay = currentDay;
	}

    public void setInDay(Calendar inDay){
        mInDay = inDay;
    }

    public void setLeaveDay(Calendar leaveDay){
        mLeaveDay = leaveDay;
    }

	/**
	 * @param month current month.
	 */
	public final void setMonth(Calendar month) {
		mCurrentMonth = (Calendar) month.clone();
	}

	public int getCount() {
		return days.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View currentView = convertView;
		TextView dayViewTextView;
		DayCell dayCell = days[position];
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			currentView = vi.inflate(R.layout.calendar_item, null);
		}
		dayViewTextView = (TextView) currentView.findViewById(R.id.date);

        closetRight = mInDay != null && unselectableDays != null
                ? getClosetRight(mInDay.get(Calendar.DAY_OF_MONTH), unselectableDays) : -1;
        closetLeft = mLeaveDay != null && unselectableDays != null
                ? getClosetLeft(mLeaveDay.get(Calendar.DAY_OF_MONTH), unselectableDays) : -1;

		boolean isCurrentMonth = dayCell.mDate.get(Calendar.MONTH) == mCurrentMonth.get(Calendar.MONTH);
        boolean isSelectableDate = true;
        if (mToday != null && dayCell.mDate.getTime().before(mToday.getTime())) {
            isSelectableDate = false;
        } else if (selectionType == DateSelectionType.Start
                && closetLeft != -1 && dayCell.mDate.get(Calendar.DAY_OF_MONTH) < closetLeft) {
            isSelectableDate = false;
        } else if (selectionType == DateSelectionType.End
                && closetRight != -1 && dayCell.mDate.get(Calendar.DAY_OF_MONTH) > closetRight) {
            isSelectableDate = false;
        } else if (unselectableDays != null && unselectableDays.contains(dayCell.mDate.get(Calendar.DAY_OF_MONTH))){
            isSelectableDate = false;
        } else if (selectionType == DateSelectionType.End && mInDay != null) {
            isSelectableDate = !dayCell.mDate.getTime().before(mInDay.getTime());
        } else if (selectionType == DateSelectionType.Start && mLeaveDay != null) {
            isSelectableDate = !dayCell.mDate.getTime().after(mLeaveDay.getTime());
        }

        dayCell.isSelectable = isSelectableDate;

		if (!isCurrentMonth || !isSelectableDate) {
			dayViewTextView.setTextColor(mContext.getResources().getColor(R.color.calendar_secondary_font_color));
		} else {
			dayViewTextView.setTextColor(mContext.getResources().getColor(R.color.calendar_font_color));
		}

		if (DateHelper.isWeekendDay(dayCell.mDate)) {
			if (!isCurrentMonth || !isSelectableDate) {
                dayViewTextView.setTextColor(mContext.getResources().getColor(R.color.calendar_secondary_weekend_font_color));
			} else {
                dayViewTextView.setTextColor(mContext.getResources().getColor(R.color.calendar_weekend_font_color));
			}
		}

		// choosing background
        if (mToday.equals(dayCell.mDate)) {
            currentView.setBackgroundResource(R.drawable.calendar_item_background_today);
        } else if (mInDay != null && DateHelper.equalsIgnoreTime(mInDay.getTime(), dayCell.mDate.getTime())) {
            currentView.setBackgroundResource(R.drawable.calendar_item_background_indate);
        } else if (mLeaveDay != null && DateHelper.equalsIgnoreTime(mLeaveDay.getTime(), dayCell.mDate.getTime())){
            currentView.setBackgroundResource(R.drawable.calendar_item_background_leavedate);
        } else if (mCurrentDay != null && DateHelper.equalsIgnoreTime(mCurrentDay.getTime(), dayCell.mDate.getTime())) {
            currentView.setBackgroundResource(R.drawable.calendar_item_background_current);
        }  else {
            if (!isCurrentMonth || !isSelectableDate) {
                currentView.setBackgroundResource(R.drawable.calendar_notcurrentmonth_item_selector);
            } else {
                currentView.setBackgroundResource(R.drawable.list_item_background);
            }
        }
        dayViewTextView.setText(dayCell.getDescr());

//		// create date string for comparison
//		String date = dayCell.getDescr();
//
//		if (date.length() == 1) {
//			date = "0" + date;
//		}
//
//		String monthStr = "" + (mCurrentMonth.get(Calendar.MONTH) + 1);
//		if (monthStr.length() == 1) {
//			monthStr = "0" + monthStr;
//		}

        count++;
        if(count == DAY_COUNT){
            count = 0;
            onAllDaysRendered.onAllDaysRendered(mCurrentMonth.get(Calendar.MONTH));
        }

		currentView.setTag(dayCell);
		return currentView;
	}

    private int getClosetRight(int desiredNumber, ArrayList<Integer> array){
        int nearest = -1;
        int bestDistanceFoundYet = 31;
        for (int i = 0; i < array.size(); i++) {
            // if we found the desired number, we return it.

            if(array.get(i) < desiredNumber){
                continue;
            }

            if (array.get(i) == desiredNumber) {
                return array.get(i);
            } else {
                // else, we consider the difference between the desired number and the current number in the array.
                int d = Math.abs(desiredNumber - array.get(i));
                if (d < bestDistanceFoundYet) {
                    bestDistanceFoundYet = d;
                    nearest = array.get(i);
                }
            }
        }
        return nearest;
    }

    private int getClosetLeft(int desiredNumber, ArrayList<Integer> array){
        int nearest = -1;
        int bestDistanceFoundYet = 31;
        for (int i = 0; i < array.size(); i++) {
            // if we found the desired number, we return it.

            if(array.get(i) > desiredNumber){
                continue;
            }

            if (array.get(i) == desiredNumber) {
                return array.get(i);
            } else {
                // else, we consider the difference between the desired number and the current number in the array.
                int d = Math.abs(desiredNumber - array.get(i));
                if (d < bestDistanceFoundYet) {
                    bestDistanceFoundYet = d;
                    nearest = array.get(i);
                }
            }
        }
        return nearest;
    }

	/**
	 * refresh.
	 */
	public final void refreshDays() {
		Calendar currentDate = DateHelper.fromDateToCalendar(DateHelper.clearTime(((Calendar) mCurrentMonth.clone()).getTime()));
		currentDate.set(Calendar.DAY_OF_MONTH, 1);

		int firstDay = currentDate.get(Calendar.DAY_OF_WEEK);
		int dayShift;
		if (DateHelper.isMondayFirst()) {
			if (firstDay == Calendar.SUNDAY) {
				dayShift = 6;
			} else {
				dayShift = firstDay - 2;
			}
		} else {
			dayShift = firstDay - 1;
		}
		currentDate.add(Calendar.DATE, -dayShift);
		days = new DayCell[DAY_COUNT];
		for (int i = 0; i < days.length; i++) {
			days[i] = new DayCell(currentDate, "" + currentDate.get(Calendar.DAY_OF_MONTH));
			DateHelper.increment(currentDate);
		}
	}

    public void setSelectionType(DateSelectionType selectionType) {
        this.selectionType = selectionType;
    }

    /**
	 * @author Sazonov-adm
	 * 
	 */
	public class DayCell {

        public boolean isSelectable;

		/**
		 * descr.
		 */
		private String mDescription;
		/**
		 * date.
		 */
		private Calendar mDate;

		/**
		 * @param currentDate date.
		 * @param text descr.
		 */
		public DayCell(Calendar currentDate, String text) {
			mDescription = text;
			mDate = (Calendar) currentDate.clone();
		}

		/**
		 * @return descr.
		 */
		public final String getDescr() {
			return mDescription;
		}

		/**
		 * @return date.
		 */
		public final Calendar getDate() {
			return mDate;
		}

		@Override
		public final int hashCode() {
			return super.hashCode();
		}

		@Override
		public final boolean equals(Object other) {
			// Not strictly necessary, but often a good optimization
			if (this == other) {
				return true;
			}
			if (!(other instanceof DayCell)) {
				return false;
			}
			DayCell otherA = (DayCell) other;
			return (mDate.equals(otherA.mDate)) && ((mDate == null) ? otherA.mDate == null : mDate.equals(otherA.mDate));
		}
	}
}