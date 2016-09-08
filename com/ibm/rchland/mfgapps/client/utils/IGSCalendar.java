/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 * 2006-09-22      35394PL  R Prechel        -Rewrite to make IGSCalendar a model only
 *                                           -Created IGSCalendarPanel and moved
 *                                            all view code to IGSCalendarPanel
 * 2006-09-28   ~1 35394PL  R Prechel        -Add comments
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.ibm.rchland.mfgapps.client.utils.event.IGSDateSelectedEvent;
import com.ibm.rchland.mfgapps.client.utils.event.IGSDateSelectedListener;

/**
 * <code>IGSCalendar</code> is a calendar model designed for views that
 * display a month at a time. The model remembers the last selected date.
 * @author The External Fulfillment Client Development Team
 */
public class IGSCalendar
	implements ActionListener
{
	/** The action command for changing the selected date. */
	public static final String CHANGE_SELECTED_DATE = "changeSelectedDate"; //$NON-NLS-1$

	/** The action command for incrementing the displayed month. */
	public static final String INCREMENT_DISPLAYED_MONTH = "incrementDisplayedMonth"; //$NON-NLS-1$

	/** The <code>Calendar</code> representing the selected date. */
	private Calendar selectedCalendar;

	/** The <code>Calendar</code> representing the month that is displayed. */
	private Calendar displayedCalendar;

	/** The column the first day of the month occupies in the calendar. */
	private int firstDayColumn;

	/** The number of days in the displayed month. */
	private int numberOfDaysInMonth;

	/** Stores the listeners observing this <code>IGSCalendar</code>. */
	private EventListenerList listenerList = new EventListenerList();

	/** Constructs a new <code>IGSCalendar</code>. */
	public IGSCalendar()
	{
		this.selectedCalendar = Calendar.getInstance();
		this.displayedCalendar = Calendar.getInstance();
		updateDisplayedMonth(); //sets firstDayColumn and numberOfDaysInMonth
	}

	/**
	 * Sets the date of the <code>displayedCalendar</code> to the first of the
	 * month and then calculates and sets both <code>firstDayColumn</code> and
	 * <code>numberOfDaysInMonth</code>.
	 */
	protected final void updateDisplayedMonth()
	{
		this.displayedCalendar.set(Calendar.DATE, 1);
		switch (this.displayedCalendar.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.SUNDAY:
				this.firstDayColumn = 0;
				break;
			case Calendar.MONDAY:
				this.firstDayColumn = 1;
				break;
			case Calendar.TUESDAY:
				this.firstDayColumn = 2;
				break;
			case Calendar.WEDNESDAY:
				this.firstDayColumn = 3;
				break;
			case Calendar.THURSDAY:
				this.firstDayColumn = 4;
				break;
			case Calendar.FRIDAY:
				this.firstDayColumn = 5;
				break;
			case Calendar.SATURDAY:
				this.firstDayColumn = 6;
				break;
			default:
				this.firstDayColumn = 0;
				break;
		}
		this.numberOfDaysInMonth = this.displayedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		ChangeEvent changeEvent = new ChangeEvent(this);
		//~1 getListenerList returns a non-null array of 
		//   listenerType, listener, listenerType, listener, etc.
		Object[] listeners = this.listenerList.getListenerList();
		//~1 Process the listeners last to first, notifying
		//   those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ChangeListener.class)
			{
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	/**
	 * Adds a <code>ChangeListener</code> to this <code>IGSCalendar</code>.
	 * @param listener the <code>ChangeListener</code> to add
	 */
	public void addChangeListener(ChangeListener listener)
	{
		this.listenerList.add(ChangeListener.class, listener);
	}

	/**
	 * Removes a <code>ChangeListener</code> from this <code>IGSCalendar</code>.
	 * @param listener the <code>ChangeListener</code> to remove
	 */
	public void removeChangeListener(ChangeListener listener)
	{
		this.listenerList.remove(ChangeListener.class, listener);
	}

	/**
	 * Adds an <code>IGSDateSelectedListener</code> to this <code>IGSCalendar</code>.
	 * @param listener the <code>IGSDateSelectedListener</code> to add
	 */
	public void addDateSelectedListener(IGSDateSelectedListener listener)
	{
		this.listenerList.add(IGSDateSelectedListener.class, listener);
	}

	/**
	 * Removes an <code>IGSDateSelectedListener</code> from this <code>IGSCalendar</code>.
	 * @param listener the <code>IGSDateSelectedListener</code> to remove
	 */
	public void removeDateSelectedListener(IGSDateSelectedListener listener)
	{
		this.listenerList.remove(IGSDateSelectedListener.class, listener);
	}

	/**
	 * Returns the displayed <code>Date</code>.
	 * @return the displayed <code>Date</code>
	 */
	public Date getDisplayedDate()
	{
		return this.displayedCalendar.getTime();
	}

	/**
	 * Returns the selected <code>Date</code>.
	 * @return the selected <code>Date</code>
	 */
	public Date getSelectedDate()
	{
		return this.selectedCalendar.getTime();
	}

	/**
	 * Sets the value of the selected <code>Date</code> and notifies all
	 * registered <code>IGSDateSelectedListener</code>s of the new value of
	 * the selected <code>Date</code>.
	 * @param date the new selected <code>Date</code>
	 */
	public void setSelectedDate(Date date)
	{
		this.selectedCalendar.setTime(date);

		IGSDateSelectedEvent dateEvent = new IGSDateSelectedEvent(this, date);
		//~1 getListenerList returns a non-null array of 
		//   listenerType, listener, listenerType, listener, etc.
		Object[] listeners = this.listenerList.getListenerList();
		//~1 Process the listeners last to first, notifying
		//   those that are interested in this event
		ChangeEvent changeEvent = new ChangeEvent(this);
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == IGSDateSelectedListener.class)
			{
				((IGSDateSelectedListener) listeners[i + 1]).dateSelected(dateEvent);
			}
			else if (listeners[i] == ChangeListener.class)
			{
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}

		this.displayedCalendar.setTime(this.selectedCalendar.getTime());
		updateDisplayedMonth();
	}

	/**
	 * Returns <code>true</code> iff the selected month is the displayed month.
	 * @return <code>true</code> iff the selected month is the displayed month
	 */
	public boolean isSelectedMonthDisplayed()
	{
		return this.displayedCalendar.get(Calendar.MONTH) == this.selectedCalendar.get(Calendar.MONTH)
				&& this.displayedCalendar.get(Calendar.YEAR) == this.selectedCalendar.get(Calendar.YEAR);
	}

	/**
	 * Returns the calendar model index of the selected day of the month.
	 * @return the calendar model index of the selected day of the month
	 */
	public int getSelectedDayIndex()
	{
		return this.selectedCalendar.get(Calendar.DATE) + this.firstDayColumn - 1;
	}

	/**
	 * Returns the day of the month at the specified calendar model <code>index</code>.
	 * @param index the calendar model index
	 * @return the day of the month at the specified calendar model
	 *         <code>index</code>. A negative value is returned if the index
	 *         does not correspond to a day.
	 */
	public int getDayOfMonth(int index)
	{
		int day = index - this.firstDayColumn;
		if (day >= 0 && day < this.numberOfDaysInMonth)
		{
			return ++day; //convert from 0 based to 1 based
		}
		return -1;
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		String command = ae.getActionCommand();
		if (command == null)
		{
			return;
		}

		if (command.startsWith(CHANGE_SELECTED_DATE))
		{
			try
			{
				int dayOfMonth = Integer.parseInt(command.substring(CHANGE_SELECTED_DATE.length()));
				this.displayedCalendar.set(Calendar.DATE, dayOfMonth);
				this.setSelectedDate(this.displayedCalendar.getTime());
			}
			catch (NumberFormatException nfe)
			{
				//Nothing to do
			}
		}
		else if (command.startsWith(INCREMENT_DISPLAYED_MONTH))
		{
			try
			{
				int value = Integer.parseInt(command.substring(INCREMENT_DISPLAYED_MONTH.length()));
				this.displayedCalendar.add(Calendar.MONTH, value);
				updateDisplayedMonth();
			}
			catch (NumberFormatException nfe)
			{
				//Nothing to do
			}
		}
	}
}
