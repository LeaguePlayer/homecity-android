package com.infteh.calendarview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * one month view.
 * @author Sazonov-adm
 * 
 */
public class CalendarMonthView extends LinearLayout {

	/**
	 * expand change observer.
	 */
	private CalendarDatePick mObserver;

	/**
	 * date with which view was init.
	 */
	private Calendar mInitialMonth;
	/**
	 * adapter.
	 */
	private CalendarAdapter mDaysAdapter;

	/**
	 * context.
	 */
	private Context mContext;
    private TaskFactory taskFactory;
    private final CalendarMonthView mThis;

    /**
	 * Конструктор.
	 * 
	 * @param context контекст
	 */
	public CalendarMonthView(final Context context) {
		this(context, null);
	}

	/**
	 * Конструктор.
	 * 
	 * @param context контекст
	 * @param attrs атрибуты
	 */
	public CalendarMonthView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
        mThis = this;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.calendar, this, true);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mInitialMonth = Calendar.getInstance();

        initGrid();
	}

    private void initGrid() {
        mDaysAdapter = new CalendarAdapter(mContext, mInitialMonth, new OnAllDaysRendered() {

            @Override
            public void onAllDaysRendered(int month) {
                if (month == mInitialMonth.get(Calendar.MONTH)) {
                    mThis.findViewById(R.id.progressBar).setVisibility(INVISIBLE);
                    mThis.findViewById(R.id.content).setVisibility(VISIBLE);
                }
            }
        });

        GridView gridview = (GridView) findViewById(R.id.content).findViewById(R.id.calendar_days_gridview);
        gridview.setAdapter(mDaysAdapter);

        initDayCaptions(mContext);
        initMonthCaption();
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (v.getTag() != null) {
                    if (mObserver != null) {
                        mObserver.onDatePicked((CalendarAdapter.DayCell) v.getTag());
                    }
                }
            }
        });
    }

    public interface OnAllDaysRendered {
        void onAllDaysRendered(int i);
    }

    public void setTaskFactory(TaskFactory taskFactory){
        this.taskFactory = taskFactory;
    }
	
	/**
	 * set current.
	 */
	public final void setAllDay(Calendar currentDay, Calendar inDay, Calendar leaveDay, DateSelectionType selectionType) {
		//mInitialMonth = month;
        mDaysAdapter.setSelectionType(selectionType);
		mDaysAdapter.setCurrentDay(currentDay);
		if(inDay != null) mDaysAdapter.setInDay(inDay);
        if(leaveDay != null) mDaysAdapter.setLeaveDay(leaveDay);
		refreshCalendar(false);
	}
	
	/**
	 * set current.
	 * @param month month.
	 */
	public final void setMonth(Calendar month) {
		mInitialMonth = month;
		mDaysAdapter.setMonth(month);
        if(taskFactory != null){
            GetUnselectableDaysTask task = taskFactory.getTask();
            task.setMonth(mInitialMonth.get(Calendar.MONTH));
            task.setYear(mInitialMonth.get(Calendar.YEAR));
            task.setCallBack(new GetUnselectableDaysTask.OnDaysLoaded() {
                @Override
                public void onDaysLoaded(ArrayList<Integer> days, int month, int year) {
                    mDaysAdapter.setUnselectableDays(days, month, year);
                    refreshCalendar(true);
                }
            });
            task.execute();
        }
	}

	/**
	 * @return current month.
	 */
	public final Calendar getMonth() {
		return mInitialMonth;
	}

	/**
	 * init.
	 */
	private void initMonthCaption() {

		TextView title = (TextView) findViewById(R.id.content).findViewById(R.id.header).findViewById(R.id.title);
		String month;
		if (LocaleHelper.isRussianLocale(mContext)) {
			String[] months = mContext.getResources().getStringArray(R.array.months_long);
			month = months[mInitialMonth.get(Calendar.MONTH)];
		} else {
			month = android.text.format.DateFormat.format("MMMM", mInitialMonth).toString();
		}
		if (month.length() > 1) {
			// make first letter in upper case
			month = month.substring(0, 1).toUpperCase() + month.substring(1);
		}
		title.setText(String.format("%s %s", month, android.text.format.DateFormat.format("yyyy", mInitialMonth)));
	}

	/**
	 * @param context context.
	 */
	private void initDayCaptions(final Context context) {
		final int week = 7;
		String[] dayCaptions = new String[week];
		SimpleDateFormat weekDateFormat = new SimpleDateFormat("EE");
		Calendar weekDay = DateHelper.createCurrentBeginDayCalendar();

		if (DateHelper.isMondayFirst()) {
			weekDay.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else {
			weekDay.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}

		for (int i = 0; i < week; i++) {
			dayCaptions[i] = weekDateFormat.format(weekDay.getTime());
			DateHelper.increment(weekDay);
		}

		GridView captionsGridView = (GridView) findViewById(R.id.content).findViewById(R.id.calendar_captions_gridview);
		captionsGridView.setAdapter(new ArrayAdapter<String>(context, R.layout.calendar_caption_item, R.id.calendar_caption_date, dayCaptions));
	}

	/**
	 * refresh view.
	 */
	public final void refreshCalendar(boolean byTask) {
        findViewById(R.id.progressBar).setVisibility(VISIBLE);
        findViewById(R.id.content).setVisibility(INVISIBLE);

        mDaysAdapter.changedByTask = byTask;
		mDaysAdapter.refreshDays();
		mDaysAdapter.notifyDataSetChanged();
		initMonthCaption();
	}

	/**
	 * Зарегистрировать новый наблюдатель.
	 * 
	 * @param observer Наблюдатель.
	 */
	public final void registerCalendarDatePickObserver(final CalendarDatePick observer) {
		this.mObserver = observer;
	}

	/**
	 * Разрегистрировать новый наблюдатель.
	 */
	public final void unregisterCalendarDatePickObserver() {
		this.mObserver = null;
	}
}
