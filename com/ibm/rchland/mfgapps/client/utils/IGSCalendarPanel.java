/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 * 2006-09-24   ~1 35394PL  R Prechel        -One instance of IGSButtonClickKeyListener
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ibm.rchland.mfgapps.client.utils.border.IGSBottomEtchedBorder;
import com.ibm.rchland.mfgapps.client.utils.icon.IGSHorizontalArrowIcon;
import com.ibm.rchland.mfgapps.client.utils.listener.IGSButtonClickKeyListener;

/**
 * <code>IGSCalendarPanel</code> is a subclass of <code>JPanel</code> that
 * displays an <code>IGSCalendar</code> one month at a time.
 * @author The External Fulfillment Client Development Team
 */
public class IGSCalendarPanel
	extends JPanel
	implements ChangeListener
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A <code>KeyListener</code> that invokes the
	 * {@link javax.swing.AbstractButton#doClick()} method when the enter key is
	 * pressed on an <code>AbstractButton</code>.
	 */
	private static final KeyListener listener = new IGSButtonClickKeyListener(); //~1A

	/**
	 * The number of rows in the calendar for dates. For 7 days in a week
	 * (DATE_COLUMNS == 7) and a max of 31 days in a month, this value should be
	 * 6 to account for 31-day months that start on a Friday or Saturday and for
	 * 30-day months that start on a Saturday.
	 */
	private static final int DATE_ROWS = 6;

	/** The number of columns in the calendar (i.e., the number of days in a week). */
	private static final int DATE_COLUMNS = 7;

	/** Array of <code>String</code>s for the days in a month. */
	private static final String[] DATE_STRINGS = 
		new String[Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)];
	
	static
	{
		for (int i = 0; i < DATE_STRINGS.length; i++)
		{
			DATE_STRINGS[i] = Integer.toString(i + 1);
		}
	}

	/** The <code>JButton</code>s used to display the calendar dates. */
	private JButton[] dateButtons;

	/** The <code>JButton</code> used to display the selected date. */
	private JButton selectedDateButton;

	/** The background <code>Color</code> of an unselected date <code>JButton</code>. */
	private Color background;

	/** The foreground <code>Color</code> of an unselected date <code>JButton</code>. */
	private Color foreground;

	/** The <code>long</code> millisecond value of the displayed <code>Date</code>. */
	private long displayedMillis;

	/** The {@link DateFormat} used to format the name of the month that is displayed. */
	private DateFormat monthFormat;

	/** The {@link DateFormat} used to format the tooltip of the date <code>JButton</code>s. */
	private DateFormat buttonFormat;

	/** The <code>Calendar</code> used to create the button format <code>String</code>s. */
	private Calendar buttonCalendar;

	/** The <code>JLabel</code> used to indicate the currently displayed month. */
	private JLabel monthLabel;

	/** The <code>IGSCalendar</code> to display. */
	private IGSCalendar calendar;

	/**
	 * Constructs a new <code>CalendarPanel</code>.
	 * @param calendar the <code>IGSCalendar</code> to display
	 * @param locale the locale used to display the <code>IGSCalendar</code>
	 * @param previous the text for the previous month <code>JButton</code>
	 * @param next the text for the next month <code>JButton</code>
	 */
	public IGSCalendarPanel(IGSCalendar calendar, Locale locale, String previous, String next)
	{
		this.calendar = calendar;
		this.calendar.addChangeListener(this);
		this.monthFormat = new SimpleDateFormat("MMMM, yyyy", locale); //$NON-NLS-1$
		this.buttonFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);

		JPanel monthSelector = createMonthSelector(previous, next);
		JPanel calendarGrid = createCalendarGrid(locale);
		this.monthLabel.setLabelFor(calendarGrid);

		this.foreground = this.dateButtons[0].getForeground();
		this.background = this.dateButtons[0].getBackground();

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(monthSelector);
		this.add(calendarGrid);

		this.displayedMillis = this.calendar.getDisplayedDate().getTime();
	}

	/**
	 * Creates the month selector <code>JPanel</code>.
	 * @param previous the text for the previous month <code>JButton</code>
	 * @param next the text for the next month <code>JButton</code>
	 * @return the month selector <code>JPanel</code>
	 */
	private JPanel createMonthSelector(String previous, String next)
	{
		JPanel result = new JPanel();
		this.monthLabel = new JLabel(this.monthFormat.format(this.calendar.getDisplayedDate()));

		int size = this.monthLabel.getFont().getSize();
		IGSHorizontalArrowIcon laIcon = new IGSHorizontalArrowIcon(size, true);
		laIcon.setAccessibleIconDescription(previous);
		IGSHorizontalArrowIcon raIcon = new IGSHorizontalArrowIcon(size, false);
		raIcon.setAccessibleDescription(next);
		
		JButton leftArrow = new JButton(laIcon);
		leftArrow.addKeyListener(listener); //~1C
		leftArrow.addActionListener(this.calendar);
		leftArrow.setActionCommand(IGSCalendar.INCREMENT_DISPLAYED_MONTH + "-1"); //$NON-NLS-1$
		leftArrow.getAccessibleContext().setAccessibleName(previous);
		leftArrow.setToolTipText(previous);
		
		JButton rightArrow = new JButton(raIcon);
		rightArrow.addKeyListener(listener); //~1C
		rightArrow.addActionListener(this.calendar);
		rightArrow.setActionCommand(IGSCalendar.INCREMENT_DISPLAYED_MONTH + "1"); //$NON-NLS-1$
		rightArrow.getAccessibleContext().setAccessibleName(next);
		rightArrow.setToolTipText(next);

		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

		result.add(leftArrow);
		result.add(Box.createGlue());
		result.add(this.monthLabel);
		result.add(Box.createGlue());
		result.add(rightArrow);

		result.setBorder(new IGSBottomEtchedBorder());
		return result;
	}

	/**
	 * Creates the calendar grid <code>JPanel</code>.
	 * @param locale the locale used to display the <code>IGSCalendar</code>
	 * @return the calendar grid <code>JPanel</code>
	 */
	private JPanel createCalendarGrid(Locale locale)
	{
		JPanel calendarGrid = new JPanel();
		calendarGrid.setLayout(new GridLayout(DATE_ROWS + 1, DATE_COLUMNS));

		// E -> Day in week
		DateFormat dayFormat = new SimpleDateFormat("E", locale); //$NON-NLS-1$
		DateFormat dayFormatLong = new SimpleDateFormat("EEEE", locale); //$NON-NLS-1$
		Calendar dayCalendar = Calendar.getInstance();

		for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++)
		{
			dayCalendar.set(Calendar.DAY_OF_WEEK, i);
			Date date = dayCalendar.getTime();
			JLabel label = new JLabel(dayFormat.format(date), SwingConstants.CENTER);
			label.setToolTipText(dayFormatLong.format(date));
			calendarGrid.add(label);
		}

		this.dateButtons = new JButton[DATE_ROWS * DATE_COLUMNS];

		Date time = this.calendar.getDisplayedDate();
		this.buttonCalendar = Calendar.getInstance();
		this.buttonCalendar.setTime(time);
		for (int row = 0; row < DATE_ROWS; row++)
		{
			for (int col = 0; col < DATE_COLUMNS; col++)
			{
				int location = row * DATE_COLUMNS + col;
				this.dateButtons[location] = createDateButton(location);
				calendarGrid.add(this.dateButtons[location]);
			}
		}

		return calendarGrid;
	}

	/**
	 * Paints this <code>Component</code>.
	 * @param g the <code>Graphics</code> object used to paint this
	 *        <code>Component</code>
	 */
	protected void paintComponent(Graphics g)
	{
		if (isOpaque())
		{
			Color savedColor = g.getColor();
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(savedColor);
		}

		String text = this.monthFormat.format(this.calendar.getDisplayedDate());
		this.monthLabel.setText(text);
		this.monthLabel.setToolTipText(text);

		Container parent = getParent();
		while (parent instanceof JLayeredPane || parent instanceof JRootPane)
		{
			parent = parent.getParent();
		}
		if (parent instanceof Window)
		{
			((Window) parent).getAccessibleContext().setAccessibleName(text);
		}

		//Only update the dateButtons if the displayedCalendar changed.
		Date displayedDate = this.calendar.getDisplayedDate();
		this.buttonCalendar = Calendar.getInstance();
		this.buttonCalendar.setTime(displayedDate);
		if (this.displayedMillis != displayedDate.getTime())
		{
			for (int i = 0; i < this.dateButtons.length; i++)
			{
				updateButton(this.dateButtons[i], i);
			}
			this.displayedMillis = displayedDate.getTime();
		}

		if (this.selectedDateButton != null)
		{
			this.selectedDateButton.setBackground(this.background);
			this.selectedDateButton.setForeground(this.foreground);
			this.selectedDateButton = null;
		}

		if (this.calendar.isSelectedMonthDisplayed())
		{
			this.selectedDateButton = this.dateButtons[this.calendar.getSelectedDayIndex()];
			this.selectedDateButton.setBackground(this.background.darker());
			this.selectedDateButton.setForeground(Color.YELLOW);
		}
	}

	/**
	 * Creates a date <code>JButton</code> for the specified grid location.
	 * @param location the date <code>JButton</code>'s location in the
	 *        calendar grid
	 * @return the new date <code>JButton</code>
	 */
	private JButton createDateButton(int location)
	{
		JButton button = new JButton();
		button.setActionCommand(""); //$NON-NLS-1$
		button.addActionListener(this.calendar);
		button.addKeyListener(listener); //~1C
		button.setName(Integer.toString(location));
		updateButton(button, location);
		return button;
	}

	/**
	 * Updates the date <code>JButton</code> that has the specified
	 * <code>location</code>.
	 * @param button the date <code>JButton</code>
	 * @param location the date <code>JButton</code>'s location in the
	 *        calendar grid
	 */
	private void updateButton(JButton button, int location)
	{
		int day = this.calendar.getDayOfMonth(location);
		if (day > 0)
		{
			this.buttonCalendar.set(Calendar.DATE, day);
			Date time = this.buttonCalendar.getTime();
			day--; //Convert day to 0 based index
			button.setText(DATE_STRINGS[day]);
			button.setActionCommand(IGSCalendar.CHANGE_SELECTED_DATE + DATE_STRINGS[day]);
			String text = this.buttonFormat.format(time);
			button.getAccessibleContext().setAccessibleName(text);
			button.setToolTipText(text);
			button.setVisible(true);
		}
		else
		{
			button.setText(""); //$NON-NLS-1$
			button.setActionCommand(""); //$NON-NLS-1$
			button.setVisible(false);
		}
	}

	/**
	 * Invoked when the target of the listener has changed its state.
	 * @param e the <code>ChangeEvent</code>
	 */
	public void stateChanged(ChangeEvent e)
	{
		if (this.calendar.equals(e.getSource()) && this.isVisible())
		{
			this.repaint();
		}
	}
}