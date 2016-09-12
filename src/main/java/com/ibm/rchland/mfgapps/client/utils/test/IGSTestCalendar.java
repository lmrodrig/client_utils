/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 * 2006-09-22   ~1 35394PL  R Prechel        -Changes to use IGSCalendarPanel
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.IGSCalendarPanel;
import com.ibm.rchland.mfgapps.client.utils.IGSCalendar;
import com.ibm.rchland.mfgapps.client.utils.event.IGSDateSelectedEvent;
import com.ibm.rchland.mfgapps.client.utils.event.IGSDateSelectedListener;

/**
 * <code>IGSTestCalendar</code> contains a main method to test the
 * functionality of the <code>IGSCalendar</code> and
 * <code>IGSCalendarPanel</code> classes.
 * @author Ryan Prechel
 * @version 2.0
 */
class IGSTestCalendar
	extends JFrame
	implements ActionListener, IGSDateSelectedListener
{
	private static final long serialVersionUID = 1L;
	/** The <code>IGSCalendar</code>. */
	private IGSCalendar calendar;

	/** The <code>JTextField</code> that displays the selected <code>Date</code>. */
	private JTextField textField;

	/** The <code>JButton</code> used to show the <code>IGSCalendar</code>. */
	private JButton showButton;

	/** The <code>JButton</code> used to select a pseudorandom date. */
	private JButton changeDateButton;

	/** The <code>JDialog</code> used to display a popup calendar. */
	private JDialog calendarDialog; //~1A

	/** Constructs a new <code>IGSTestCalendar</code>. */
	public IGSTestCalendar()
	{
		super("Calendar Test"); //$NON-NLS-1$
		this.calendar = new IGSCalendar();
		this.textField = new JTextField(25);
		this.showButton = new JButton("Show Calendar"); //$NON-NLS-1$
		this.changeDateButton = new JButton("Select Random Date"); //$NON-NLS-1$

		this.calendar.addDateSelectedListener(this);
		this.showButton.addActionListener(this);
		this.changeDateButton.addActionListener(this);

		JPanel contentPane = new JPanel(new FlowLayout());
		contentPane.add(this.textField);
		contentPane.add(this.showButton);
		contentPane.add(this.changeDateButton);
		//~1A Added an IGSCalendarPanel to the content pane
		IGSCalendarPanel cp = new IGSCalendarPanel(this.calendar, Locale.getDefault(),
				"Previous Month", "Next Month"); //$NON-NLS-1$, //$NON-NLS-2$
		contentPane.add(cp);
		this.setContentPane(contentPane);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Displays the <code>IGSCalendar</code> if the show calendar button was
	 * pushed. Otherwise, sets the text of the text field to a pseudorandom
	 * <code>Data</code>.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource().equals(this.showButton))
		{
			//~1C Start change to use IGSCalendarPanel
			this.calendarDialog = new JDialog();
			this.calendarDialog.setContentPane(new IGSCalendarPanel(this.calendar, Locale
					.getDefault(), "Previous Month", "Next Month")); //$NON-NLS-1$, //$NON-NLS-2$
			this.calendarDialog.setUndecorated(false);
			this.calendarDialog.setModal(true);
			this.calendarDialog.setLocationRelativeTo(this);
			this.calendarDialog.pack();
			this.calendarDialog.setVisible(true);
			//~1C End change to use CalendarPanel
		}
		else
		{
			Calendar randomCalendar = Calendar.getInstance();
			randomCalendar.set((int) (System.currentTimeMillis() % 40) + 1990, (int) (System
					.currentTimeMillis() % 12), (int) (System.currentTimeMillis() % 28));
			this.calendar.setSelectedDate(randomCalendar.getTime());
		}
	}

	/**
	 * Sets the text of the text field to the selected <code>Date</code>.
	 * @param e the <code>IGSDateSelectedEvent</code>
	 */
	public void dateSelected(IGSDateSelectedEvent e)
	{
		//~1A Start add close of calendarDialog
		if (this.calendarDialog != null)
		{
			this.calendarDialog.setVisible(false);
			this.calendarDialog.dispose();
		}
		//~1A End add close of calendarDialog
		this.textField.setText(DateFormat.getDateInstance().format(e.getDate()));
	}

	/**
	 * Main method to test functionality.
	 * @param args the command line arguments for this application
	 */
	public static void main(String[] args)
	{
		IGSTestCalendar testCalendar = new IGSTestCalendar();
		testCalendar.pack();
		testCalendar.setVisible(true);
	}
}
