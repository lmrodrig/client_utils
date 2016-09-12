/* @ Copyright IBM Corporation 2007, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-23      34242JR  R Prechel        -Initial version
 * 2007-04-02   ~1 38166JM  R Prechel        -Call setLocationRelativeTo after size is determined
 * 2007-11-06   ~2 40104PB  R Prechel        -Do not append "Program Exception" 
 *                                            if isProgramException returns false
 * 2008-01-11   ~3 39619JL  R Prechel        -Use Logger API.         
 * 2008-01-12   ~4 39619JL  R Prechel        -Fix background color and standard out/err
 *                                            issues for use with Print Server
 * 2010-03-08  ~05 47595MZ  Toribio H.       -Efficiency changes
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.messagebox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSMessageableException;
import com.ibm.rchland.mfgapps.client.utils.io.IGSFileUtils;
import com.ibm.rchland.mfgapps.client.utils.layout.IGSCenterLayout;
import com.ibm.rchland.mfgapps.client.utils.media.IGSSound;

/**
 * <code>IGSMessageBox</code> displays a message using a modal dialog window.
 * If the use of the {@link java.util.logging.Logger} API is enabled, the text
 * of the message and any {@link java.lang.Throwable} are logged. Otherwise, the
 * text of the message is output using {@link IGSFileUtils#getOut()} and the
 * {@link java.lang.Throwable#printStackTrace()} method is called on all
 * <code>Throwable</code>s for which a message is displayed.
 * <p>
 * <code>IGSMessageBox</code> was designed using the <cite>Strategy</cite>
 * design pattern. An <code>IGSMessageBox</code> is constructed with an
 * instance of <code>IGSMessageType</code> that determines what buttons are
 * displayed on the message box and the behavior of the buttons.
 * <p>
 * <code>IGSMessageBox</code> contains static convenience methods used to
 * display escape, ok, and yes/no message boxes. Other types of message boxes
 * can be displayed by subclassing <code>IGSMessageType</code>, constructing
 * an <code>IGSMessageBox</code> with the new <code>IGSMessageType</code>,
 * and calling the {@link #display()} method.
 * @author The MFS Client Development Team
 */
public class IGSMessageBox
{
	/** The separator printed to standard out before and after each message. */
	public static final String SEPARATOR = "\n--------------------------------------------------\n"; //$NON-NLS-1$

	/**
	 * The <code>Font</code> used by the components on an
	 * <code>IGSMessageBox</code>.
	 */
	protected static final Font FONT = new Font("Dialog", Font.BOLD, 14); //$NON-NLS-1$

	/** The number of characters after which a line will wrap on the next space. */
	private static final int WRAP_WIDTH = 60;

	/** The padding on the left and right of the message box. */
	private static final int HORIZONTAL_PAD = 30;

	/** The padding on the top and bottom of the message box. */
	private static final int VERTICAL_PAD = 20;

	/** The vertical padding between text lines. */
	private static final int LINE_PAD = 1;

	/** The minimum width of an <code>IGSMessageBox</code>. */
	private static final int MIN_WIDTH = 400;

	/** The <code>Logger</code> used by <code>IGSMessageBox</code>. */
	private static Logger logger; //~3A
	
	//~4A
	/** The default panel background <code>Color</code>. */
	protected final static Color PANEL_BACKGROUND = 
		UIManager.getDefaults().getColor("Panel.background"); //$NON-NLS-1$

	/** The <code>JDialog</code> used to display the message box. */
	private JDialog dialog;

	/** The <code>Object</code> that closed the message box. */
	private Object closerObject;

	//~3A New method
	/**
	 * Indicates if <code>IGSMessageBox</code> should use the
	 * <code>Logger</code> API or {@link IGSFileUtils#getOut()} and
	 * {@link IGSFileUtils#getErr()}.
	 * @param logging <code>true</code> if <code>IGSMessageBox</code> should
	 *        use the {@link Logger} API; false if <code>IGSMessageBox</code>
	 *        should use {@link IGSFileUtils#getOut()} and
	 *        {@link IGSFileUtils#getErr()}
	 * @return the <code>Logger</code> if the use of the {@link Logger} API is
	 *         enabled; <code>null</code> otherwise
	 */
	public static Logger setUseLogger(boolean logging)
	{
		if (logging)
		{
			IGSMessageBox.logger = Logger.getLogger(IGSMessageBox.class.getName());
		}
		else
		{
			IGSMessageBox.logger = null;
		}
		return IGSMessageBox.logger;
	}

	/**
	 * Displays an ok message box that displays the specified
	 * <code>message</code>, displays the specified <code>throwable</code>'s
	 * message, and prints the <code>throwable</code>'s stack trace. If
	 * the specified <code>throwable</code>'s message is <code>null</code>,
	 * the fully qualified class name of the <code>throwable</code> is
	 * displayed.
	 * @param parent the parent <code>Component</code> of the message box
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param throwable the <code>Throwable</code> to output
	 */
	public static void showOkMB(Component parent, String title, String message, Throwable throwable)
	{
		showOkMB(parent, title, message, throwable, false);
	}

	/**
	 * Displays an ok message box that displays the specified
	 * <code>message</code>, displays the specified <code>throwable</code>'s
	 * message, and prints the <code>throwable</code>'s stack trace. If
	 * the specified <code>throwable</code>'s message is <code>null</code>,
	 * the fully qualified class name of the <code>throwable</code> is
	 * displayed.
	 * @param parent the parent <code>Component</code> of the message box
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param throwable the <code>Throwable</code> to output
	 * @param loudBeep <code>true</code> if want loud beep with message
	 */
	public static void showOkMB(Component parent, String title, String message, Throwable throwable, boolean loudBeep)
	{
		new IGSMessageBox(parent, title, message, throwable, new IGSOkMessageType(), 0).display(loudBeep);	 // ~05
	}

	/** ~05
	 * Displays an ok message box that displays the specified
	 * <code>message</code>, displays the specified <code>throwable</code>'s
	 * message, and prints the <code>throwable</code>'s stack trace. If
	 * the specified <code>throwable</code>'s message is <code>null</code>,
	 * the fully qualified class name of the <code>throwable</code> is
	 * displayed.
	 * @param parent the parent <code>Component</code> of the message box
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param throwable the <code>Throwable</code> to output
	 * @param timeForTimer the <code>integer</code> time for the timer if > than 0
	 */
	public static void showOkMBTimer(Component parent, String title, String message, Throwable throwable, String timeForTimer)
	{
		int intTimeForTimer = 0;
		try
		{
			intTimeForTimer = Integer.parseInt(timeForTimer);
		}
		catch (Exception e)
		{
			intTimeForTimer = 0;
		}
		if (intTimeForTimer < 0)
		{
			// do nothing
		}
		else if(timeForTimer != null && !timeForTimer.equals(""))  //$NON-NLS-1$
		{
			new IGSMessageBox(parent, title, message, throwable, new IGSOkMessageType(), intTimeForTimer).display();
		}
		else
		{
			new IGSMessageBox(parent, title, message, throwable, new IGSOkMessageType(), 0).display();
		}
	}

	/**
	 * Displays an escape message box that displays the specified
	 * <code>message</code>, displays the specified <code>throwable</code>'s
	 * message, and prints the <code>throwable</code>'s stack trace. If
	 * the specified <code>throwable</code>'s message is <code>null</code>,
	 * the fully qualified class name of the <code>throwable</code> is
	 * displayed.
	 * @param parent the parent <code>Component</code> of the message box
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param throwable the <code>Throwable</code> to output
	 * @param loudBeep <code>true</code> if want loud beep with message
	 */
	public static void showEscapeMB(Component parent, String title, String message, Throwable throwable, boolean loudBeep)
	{
		new IGSMessageBox(parent, title, message, throwable, new IGSEscapeMessageType(), 0).display(loudBeep); 	// ~05
	}

	/**
	 * Displays an escape message box that displays the specified
	 * <code>message</code>, displays the specified <code>throwable</code>'s
	 * message, and prints the <code>throwable</code>'s stack trace. If
	 * the specified <code>throwable</code>'s message is <code>null</code>,
	 * the fully qualified class name of the <code>throwable</code> is
	 * displayed.
	 * @param parent the parent <code>Component</code> of the message box
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param throwable the <code>Throwable</code> to output
	 */
	public static void showEscapeMB(Component parent, String title, String message, Throwable throwable)
	{
		new IGSMessageBox(parent, title, message, throwable, new IGSEscapeMessageType(), 0).display(false);
	}

	/**
	 * Displays a yes/no message box that displays the specified
	 * <code>message</code>, displays the specified <code>throwable</code>'s
	 * message, and prints the <code>throwable</code>'s stack trace. If
	 * the specified <code>throwable</code>'s message is <code>null</code>,
	 * the fully qualified class name of the <code>throwable</code> is
	 * displayed.
	 * @param parent the parent <code>Component</code> of the message box
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param throwable the <code>Throwable</code> to output
	 * @return <code>true</code> if the user selected the yes button;
	 *         <code>false</code> otherwise
	 */
	public static boolean showYesNoMB(Component parent, String title, String message, Throwable throwable)
	{
		Object d = new IGSMessageBox(parent, title, message, throwable, new IGSYesNoMessageType(), 0).display();
		if (d == null)
		{
			return false;
		}
		String yes = IGSMessageType.getString(IGSMessageTypeResources.YES_BUTTON_TEXT);
		return d.toString().equals(yes);
	}

	/**
	 * Creates a new modal <code>JDialog</code> with the specified
	 * <code>parent</code>. The default close operation of the
	 * <code>JDialog</code> is set to
	 * {@link WindowConstants#DO_NOTHING_ON_CLOSE}.
	 * @param parent the <code>JDialog</code>'s parent <code>Component</code>
	 * @param title the title of the <code>JDialog</code>
	 * @return the new <code>JDialog</code>
	 */
	public static JDialog createDialog(Component parent, String title)
	{
		JDialog dialog = null;
		while (dialog == null)
		{
			if (parent instanceof Dialog)
			{
				dialog = new JDialog((Dialog) parent, title, true);
			}
			else if (parent instanceof Frame)
			{
				dialog = new JDialog((Frame) parent, title, true);
			}
			else if (parent == null)
			{
				dialog = new JDialog();
				dialog.setTitle(title);
				dialog.setModal(true);
			}
			else
			{
				parent = parent.getParent();
			}
		}
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		return dialog;
	}

	/**
	 * Constructs a new <code>IGSMessageBox</code> that displays the specified
	 * <code>message</code>, displays the specified <code>throwable</code>'s
	 * message, and prints the <code>throwable</code>'s stack trace. If
	 * the specified <code>throwable</code>'s message is <code>null</code>,
	 * the fully qualified class name of the <code>throwable</code> is
	 * displayed.
	 * @param parent the parent <code>Component</code> of the message box
	 * @param title the title of the message box
	 * @param message the message to display
	 * @param throwable the <code>Throwable</code> to output
	 * @param messageType the type of message displayed
	 */
	public IGSMessageBox(Component parent, String title, String message,
							Throwable throwable, IGSMessageType messageType,
							int timeForTimer)
	{
		// Construct the display message based on the message and throwable
		StringBuffer messageBuffer = new StringBuffer();
		if (throwable != null)
		{
			//~3A Only call printStackTrace if logger == null
			if (IGSMessageBox.logger == null)
			{
				throwable.printStackTrace(IGSFileUtils.getErr()); //~4C
			}
			if (message == null)
			{
				//~2C Do not append "Program Exception" if isProgramException returns false
				if ((throwable instanceof IGSMessageableException) == false
						|| ((IGSMessageableException) throwable).isProgramException())
				{
					messageBuffer.append(IGSMessageType.getString(IGSMessageTypeResources.PROGRAM_EXCEPTION));
				}
			}
			else
			{
				messageBuffer.append(message);
				messageBuffer.append(IGSMessageType.getString(IGSMessageTypeResources.CAUSED_BY));
			}

			String throwableMessage = throwable.getLocalizedMessage();
			if (throwableMessage == null)
			{
				throwableMessage = throwable.getClass().getName();
			}
			messageBuffer.append(throwableMessage);
		}
		else if (message != null)
		{
			messageBuffer.append(message);
		}

		// Replace message parameter with constructed message
		message = messageBuffer.toString();

		// Create and setup the JDialog
		this.dialog = createDialog(parent, title);
		Insets insets = new Insets(VERTICAL_PAD, HORIZONTAL_PAD, LINE_PAD, HORIZONTAL_PAD);
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0);
		final Container contentPane = this.dialog.getContentPane(); //~4A
		contentPane.setLayout(new GridBagLayout()); //~4C
		if(PANEL_BACKGROUND != null)
		{
			contentPane.setBackground(PANEL_BACKGROUND); //~4A
		}

		// Add the constructed message to the JDialog
		addMessage(message, gbc);

		// Add the button component to the JDialog
		JComponent component = messageType.createButtonComponent(this);
		contentPane.add(component, gbc); //~4C
		if(component instanceof JPanel && PANEL_BACKGROUND != null)
		{
			component.setBackground(PANEL_BACKGROUND); //~4A
		}
		gbc.gridy++;

		//~3A Use logger if it is not null 
		if (IGSMessageBox.logger != null)
		{
			IGSMessageBox.logger.log(Level.INFO, message, throwable);
		}
		else
		{
			//~4C Print timestamp and message to IGSFileUtils.getOut()
			StringBuffer outputBuffer = new StringBuffer();
			outputBuffer.append(SEPARATOR);
			outputBuffer.append(DateFormat.getDateTimeInstance().format(new Date()));
			outputBuffer.append("\n"); //$NON-NLS-1$
			outputBuffer.append(message);
			outputBuffer.append(SEPARATOR);

			IGSFileUtils.getOut().println(outputBuffer.toString()); //~4C
		}
		if(timeForTimer > 0)  //~05
		{
			final IGSMessageBox messageBox = this;
	        Timer timer = new Timer(timeForTimer, new ActionListener() { 
	            public void actionPerformed(ActionEvent e) { 
	            	messageBox.close(e.getSource());
	            } 
	        }); 
	        timer.setRepeats(false); 
	        timer.start();
		}
	}

	/**
	 * Displays the message box. Note: The message box is modal.
	 * @return the <code>Object</code> that closed the message box
	 * @see #close(Object)
	 */
	public Object display()
	{
		return display(false);
	}

	/**
	 * Displays the message box. Note: The message box is modal.
	 * @param loudBeep <code>true</code> if want loud beep with message
	 * @return the <code>Object</code> that closed the message box
	 * @see #close(Object)
	 */
	public Object display(boolean loudBeep)
	{
		this.dialog.pack();
		Dimension size = this.dialog.getContentPane().getSize();
		if (size.width < MIN_WIDTH)
		{
			JPanel contentPane = new JPanel(new IGSCenterLayout());
			if(PANEL_BACKGROUND != null)
			{
				contentPane.setBackground(PANEL_BACKGROUND); //~4C
			}
			contentPane.add(this.dialog.getContentPane());
			contentPane.setMinimumSize(new Dimension(MIN_WIDTH, size.height));
			contentPane.setPreferredSize(new Dimension(MIN_WIDTH, size.height));
			this.dialog.setContentPane(contentPane);
			this.dialog.pack();
		}
		this.dialog.setLocationRelativeTo(this.dialog.getParent()); //~1A
		
		if (loudBeep) // ~05
		{
	      	try
	      	{
		      	new IGSSound("buzzer.wav").start();
	      	}
	      	catch (Exception e)
	      	{
	      		System.out.println("error playing audio clip");
	      		e.printStackTrace();
	      	}
		}
		else
		{
			Toolkit.getDefaultToolkit().beep();
		}
		
		this.dialog.setVisible(true);
		
		return this.closerObject;
	}

	/**
	 * Closes the message box.
	 * @param closer the <code>Object</code> that closed the message box
	 */
	public void close(Object closer)
	{
		this.closerObject = closer;
		this.dialog.setVisible(false);
		this.dialog.dispose();
	}

	/**
	 * Adds the specified <code>message</code> to the message box.
	 * <p>
	 * If the specified <code>message</code> is <code>null</code> or has no
	 * length, a default message will be displayed.
	 * @param message the message to add to the message box
	 * @param gbc the <code>GridBagConstraints</code> used to layout the
	 *        contents of the message box
	 */
	private void addMessage(String message, GridBagConstraints gbc)
	{
		if (message == null || (message = message.trim()).length() == 0)
		{
			message = IGSMessageType.getString(IGSMessageTypeResources.NO_MESSAGE);

			// An IGSMessageBox was created with no message and no throwable.
			// Print a stack trace for debugging use.
			//~3A Use logger if it is not null
			if (IGSMessageBox.logger != null)
			{
				IGSMessageBox.logger.log(Level.FINE, message, new RuntimeException());
			}
			else
			{
				new RuntimeException().printStackTrace(IGSFileUtils.getErr()); //~4C
			}
		}

		Container contentPane = this.dialog.getContentPane();

		// The start index in message of the next displayed line
		int startIndex = 0;

		// The end index in message of the next displayed line
		int endIndex = message.indexOf(' ', WRAP_WIDTH);

		// The index in message of the next newline character
		int newlineIndex = message.indexOf('\n');

		// The while loop creates a new JLabel for each line in message
		// and adds it to the content pane of the message box JDialog.
		// Any line longer than WRAP_WIDTH will be broken into multiple lines.
		while (endIndex > 0 || newlineIndex > 0)
		{
			// Set endIndex to newlineIndex if the line is shorter than WRAP_WIDTH
			if (newlineIndex > 0 && (newlineIndex < endIndex || endIndex == -1))
			{
				endIndex = newlineIndex;
				newlineIndex = message.indexOf('\n', endIndex + 1);
			}

			// Create the line of text
			String line = message.substring(startIndex, endIndex);

			// Consecutive new lines will produce a line with no text.
			// A JLabel needs text to take up space, so set the line to a space.
			if (line.length() == 0)
			{
				line = " "; //$NON-NLS-1$
			}

			// Add a JLabel and update the layout constraints
			JLabel label = new JLabel(line);
			label.setFont(FONT);
			contentPane.add(label, gbc);
			gbc.insets = new Insets(LINE_PAD, HORIZONTAL_PAD, LINE_PAD, HORIZONTAL_PAD);
			gbc.gridy++;

			// Update startIndex and endIndex for the next line
			startIndex = endIndex + 1;
			endIndex = message.indexOf(' ', startIndex + WRAP_WIDTH);
		}

		// Add the last JLabel and update the layout constraints
		JLabel label = new JLabel(message.substring(startIndex));
		label.setFont(FONT);
		contentPane.add(label, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(VERTICAL_PAD, HORIZONTAL_PAD, VERTICAL_PAD, HORIZONTAL_PAD);
	}
}
