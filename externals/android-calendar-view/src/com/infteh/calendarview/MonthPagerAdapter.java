package com.infteh.calendarview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author Sazonov-adm
 * 
 */
class MonthPagerAdapter extends PagerAdapter {

	/**
	 * max number of screens. 998 cause (998 mod 3) == 1
	 */
	public static final int INFINITE = 998;
	/**
	 * pages.
	 */
	private List<CalendarMonthView> pages = null;

	/**
	 * pager.
	 */
	private ViewPager mPager;
    private TaskFactory taskFactory;

    /**
	 * @param inflater inflater.
	 * @param pager 
	 */
	public MonthPagerAdapter(LayoutInflater inflater, ViewPager pager, TaskFactory taskFactory) {
		this.pages = new ArrayList<CalendarMonthView>();
		this.mPager = pager;
		Calendar cal1 = DateHelper.createCurrentBeginDayCalendar();
		Calendar cal2 = DateHelper.createCurrentBeginDayCalendar();
		Calendar cal3 = DateHelper.createCurrentBeginDayCalendar();

		CalendarMonthView view1 = (CalendarMonthView) getView(inflater);
		CalendarMonthView view2 = (CalendarMonthView) getView(inflater);
		CalendarMonthView view3 = (CalendarMonthView) getView(inflater);
		
		cal1.add(Calendar.MONTH, -1);
		cal2.add(Calendar.MONTH, 0);
		cal3.add(Calendar.MONTH, 1);

        view1.setTaskFactory(taskFactory);
        view2.setTaskFactory(taskFactory);
        view3.setTaskFactory(taskFactory);

        ContextThemeWrapper ctw = new ContextThemeWrapper(inflater.getContext(), R.style.Theme_DoNotDim);

        view1.findViewById(R.id.progressBar).setVisibility(view1.VISIBLE);
        view1.findViewById(R.id.content).setVisibility(view1.INVISIBLE);
        view2.findViewById(R.id.progressBar).setVisibility(view2.VISIBLE);
        view2.findViewById(R.id.content).setVisibility(view2.INVISIBLE);
        view3.findViewById(R.id.progressBar).setVisibility(view3.VISIBLE);
        view3.findViewById(R.id.content).setVisibility(view3.INVISIBLE);

        view1.setMonth(cal1);
		view2.setMonth(cal2);
		view3.setMonth(cal3);

		this.pages.add(view1);
		this.pages.add(view2);
		this.pages.add(view3);
	}
	
	/**
	 * @param currentDay current day.
	 */
	public void setCurrentDay(Calendar currentDay, Calendar inDate, Calendar leaveDate, DateSelectionType selectionType) {
		if (mPager == null) {
			return;
		}
		int pagerPosition = mPager.getCurrentItem();
		CalendarMonthView view1 = (CalendarMonthView) pages.get((pagerPosition - 1) % pages.size());
		CalendarMonthView view2 = (CalendarMonthView) pages.get(pagerPosition % pages.size());
		CalendarMonthView view3 = (CalendarMonthView) pages.get((pagerPosition + 1) % pages.size());

		view1.setAllDay(currentDay, inDate, leaveDate, selectionType);
		view2.setAllDay(currentDay, inDate, leaveDate, selectionType);
		view3.setAllDay(currentDay, inDate, leaveDate, selectionType);
	}

	/**
	 * @param month month.
	 */
	public final void setMonth(Calendar month) {
		if (mPager == null) {
			return;
		}
		Calendar cal1 = (Calendar) month.clone();
		Calendar cal2 = (Calendar) month.clone();
		Calendar cal3 = (Calendar) month.clone();
		int pagerPosition = mPager.getCurrentItem();
		CalendarMonthView view1 = (CalendarMonthView) pages.get((pagerPosition - 1) % pages.size());
		CalendarMonthView view2 = (CalendarMonthView) pages.get(pagerPosition % pages.size());
		CalendarMonthView view3 = (CalendarMonthView) pages.get((pagerPosition + 1) % pages.size());

        view1.findViewById(R.id.progressBar).setVisibility(view1.VISIBLE);
        view1.findViewById(R.id.content).setVisibility(view1.INVISIBLE);
        view2.findViewById(R.id.progressBar).setVisibility(view2.VISIBLE);
        view2.findViewById(R.id.content).setVisibility(view2.INVISIBLE);
        view3.findViewById(R.id.progressBar).setVisibility(view3.VISIBLE);
        view3.findViewById(R.id.content).setVisibility(view3.INVISIBLE);

		cal1.add(Calendar.MONTH, -1);
		cal2.add(Calendar.MONTH, 0);
		cal3.add(Calendar.MONTH, 1);
		view1.setMonth(cal1);
		view2.setMonth(cal2);
		view3.setMonth(cal3);
	}
	
	/**
	 * @return month.
	 */
	public final Calendar getMonth() {
		int pagerPosition = mPager.getCurrentItem();
		CalendarMonthView view2 = (CalendarMonthView) pages.get(pagerPosition % pages.size());
		return view2.getMonth();
	}

	/**
	 * @param pickObserver observer.
	 */
	public final void setPickObserver(CalendarDatePick pickObserver) {
		for (CalendarMonthView view : this.pages) {
			view.registerCalendarDatePickObserver(pickObserver);
		}
	}

	/**
	 * @param inflater inflater.
	 * @return view.
	 */
	private CalendarMonthView getView(LayoutInflater inflater) {
		View p = inflater.inflate(R.layout.calendar_month, null, false);
		return (CalendarMonthView) p;
	}

	@Override
	public final Object instantiateItem(View collection, int position) {
		mPager = (ViewPager) collection;
		int pagerPosition = mPager.getCurrentItem();
		CalendarMonthView calView = pages.get(position % pages.size());
        calView.findViewById(R.id.progressBar).setVisibility(calView.VISIBLE);
        calView.findViewById(R.id.content).setVisibility(calView.INVISIBLE);
        calView.setTaskFactory(taskFactory);
		if (position == pagerPosition) {
			// first init. (occure then pager just created)
			Calendar cal = calView.getMonth();
			Calendar calBefore = (Calendar) cal.clone();
			Calendar calAfter = (Calendar) cal.clone();
			calBefore.add(Calendar.MONTH, -1);
			calAfter.add(Calendar.MONTH, 1);

			CalendarMonthView calViewBefore = pages.get((position - 1) % pages.size());
			CalendarMonthView calViewAfter = pages.get((position + 1) % pages.size());

            calViewBefore.setTaskFactory(taskFactory);
            calViewAfter.setTaskFactory(taskFactory);

			calViewAfter.setMonth(calAfter);
			calViewBefore.setMonth(calBefore);
		}
		if (mPager.getChildCount() == pages.size()) {
			Calendar cal = pages.get(pagerPosition % pages.size()).getMonth();
			if (position > pagerPosition) {
				// slide right
				Calendar calAfter = (Calendar) cal.clone();
				calAfter.add(Calendar.MONTH, 1);
				CalendarMonthView calViewAfter = pages.get((position) % pages.size());

                calViewAfter.findViewById(R.id.progressBar).setVisibility(calViewAfter.VISIBLE);
                calViewAfter.findViewById(R.id.content).setVisibility(calViewAfter.INVISIBLE);

                calViewAfter.setTaskFactory(taskFactory);
				calViewAfter.setMonth(calAfter);
			} else if (position < pagerPosition) {
				// slide left
				Calendar calBefore = (Calendar) cal.clone();
				calBefore.add(Calendar.MONTH, -1);
				CalendarMonthView calViewBefore = pages.get((position) % pages.size());

                calViewBefore.findViewById(R.id.progressBar).setVisibility(calViewBefore.VISIBLE);
                calViewBefore.findViewById(R.id.content).setVisibility(calViewBefore.INVISIBLE);

                calViewBefore.setTaskFactory(taskFactory);
				calViewBefore.setMonth(calBefore);
			}
		}

		if (mPager.getChildCount() < pages.size()) {
			// if views has not created.
			mPager.addView(calView, 0);
		}

		return calView;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
	}

	@Override
	public final int getCount() {
		return INFINITE;
	}

	@Override
	public final boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public final Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

    public void setTask(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }
}