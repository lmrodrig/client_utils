/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-01      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;

/**
 * <code>IGSTestMessageBox</code> contains a main method to test the
 * functionality of the <code>IGSMessageBox</code> related classes.
 * @author The MFS Client Development Team
 * @see com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox
 * @see com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageType
 */
public class IGSTestMessageBox
{
	/** The <code>JCheckBox</code> used to control the display of message text. */
	protected static JCheckBox messageCB = new JCheckBox("Display Message Text"); //$NON-NLS-1$S
	
	/** The <code>JCheckBox</code> used to control the output of an exception. */
	protected static JCheckBox exceptionCB = new JCheckBox("Display Exception"); //$NON-NLS-1$

	/**
	 * Main method to test functionality.
	 * @param args the command line arguments for this application
	 */
	public static void main(String[] args)
	{
		final JFrame frame = new JFrame();
		final String TITLE = "Title"; //$NON-NLS-1$
		JButton escape = new JButton("Escape"); //$NON-NLS-1$
		escape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String message = null;
				Exception exception = null;
				
				if(IGSTestMessageBox.messageCB.isSelected()) {
					message = "Escape message box.\nSecond line of text."; //$NON-NLS-1$
				}
				
				if(IGSTestMessageBox.exceptionCB.isSelected()) {
					exception = new RuntimeException();
				}
				IGSMessageBox.showEscapeMB(frame, TITLE, message, exception); //$NON-NLS-1$
			}
		});
		
		JButton ok = new JButton("OK"); //$NON-NLS-1$
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String message = null;
				Exception exception = null;
				
				if(IGSTestMessageBox.messageCB.isSelected()) {
					message = "OK message box.\nSecond line of text."; //$NON-NLS-1$
				}
				
				if(IGSTestMessageBox.exceptionCB.isSelected()) {
					exception = new RuntimeException();
				}
				IGSMessageBox.showOkMB(frame, TITLE, message, exception); //$NON-NLS-1$
			}
		});
		
		JButton okLongLine = new JButton("No Title OK with Long Line and Exception Cause"); //$NON-NLS-1$
		okLongLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String message = null;
				Exception exception = null;
				
				if(IGSTestMessageBox.messageCB.isSelected()) {
					message = "OK message box. "; //$NON-NLS-1$
					message += "The entire text was entered as one line, "; //$NON-NLS-1$
					message += "but should be wrapped by the message box."; //$NON-NLS-1$
				}
				
				if(IGSTestMessageBox.exceptionCB.isSelected()) {
					exception = new RuntimeException("Test exception."); //$NON-NLS-1$
				}
				IGSMessageBox.showOkMB(frame, null, message, exception); //$NON-NLS-1$
			}
		});
		
		JButton yesNo = new JButton("Yes/No"); //$NON-NLS-1$
		yesNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String message = null;
				Exception exception = null;
				
				if(IGSTestMessageBox.messageCB.isSelected()) {
					message = "Yes/No message box.\nSecond line of text."; //$NON-NLS-1$
				}
				
				if(IGSTestMessageBox.exceptionCB.isSelected()) {
					exception = new RuntimeException();
				}
				boolean b = IGSMessageBox.showYesNoMB(frame, TITLE, message, exception); //$NON-NLS-1$
				
				System.out.println((b ? "Yes" : "No") + " was pressed."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		});
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.PAGE_AXIS));
		checkBoxPanel.add(messageCB);
		checkBoxPanel.add(exceptionCB);
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(escape);
		buttonPanel.add(ok);
		buttonPanel.add(yesNo);
		buttonPanel.add(okLongLine);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(checkBoxPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		frame.setContentPane(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
