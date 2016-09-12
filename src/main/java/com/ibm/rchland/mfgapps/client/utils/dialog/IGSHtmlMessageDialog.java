/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-06-10       45878MS Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The <code>IGSHtmlMessageDialog</code> is a <code>JDialog</code> to display a
 * html message.
 * @author The External Fulfillment Client Development Team
 */
public class IGSHtmlMessageDialog extends JDialog implements ActionListener, KeyListener
{
	/**
	 * Creates a <code>EFDocumentStatusDialog</code> with a <code>EFCardCoverFrame</code>
	 * as its parent and a header title.
	 * @param ccf the <code>EFCardCoverFrame</code>
	 * @param headerTitle title of the <code>EFHEaderPanel</code>
	 */
	private static final long serialVersionUID = 4516194973912078719L;

	/** The the html for the message area */
	protected String htmlText;

	/** The <code>JTextArea</code> to display the documents satus */
	protected JEditorPane messageArea;

	/** The <code>JButton</code> to cancel the operation */
	protected JButton pbOk;
	
	/** The <code>JFrame</code> parent of this dialog */
	private JFrame parentFrame;
	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		
		if (source == this.pbOk)
		{
			this.dispose();
		}		
	}
	
	/** Adds the listeners to this panel's <code>Component</code>s. */
	protected void addMyListeners()
	{
		messageArea.addKeyListener(this);
		
		pbOk.addActionListener(this);
		pbOk.addKeyListener(this);
	}
	
	/**
	 * Creates the layout for this dialog.
	 */
	protected void createLayout()
	{
		messageArea = new JEditorPane();
		messageArea.setContentType("text/html");
		messageArea.setText(htmlText);
		messageArea.setEditable(false);
		
		// Buttons
		pbOk = new JButton("OK");

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(pbOk);			
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(new JScrollPane(messageArea), BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		//EF Document Dialog settings
		setTitle(getClass().getSimpleName() + " - About..");
		setContentPane(contentPane);
		setPreferredSize(new Dimension(380, 180)); 
		setModal(true);

		pack();		
		
		// Set location in main screen
		this.setLocationRelativeTo(parentFrame);		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Creates a <code>IGSHtmlMessageDialog</code> with a <code>JFrame</code>
	 * as its parent.
	 * @param parentFrame the <code>JFrame</code> parent of this dialog
	 */
	public IGSHtmlMessageDialog(JFrame parentFrame, String htmlText)
	{
		super(parentFrame);
		
		this.parentFrame = parentFrame;
		this.htmlText = htmlText;
		
		createLayout();
		
		addMyListeners();

		pbOk.requestFocusInWindow();	
		
		setVisible(true);
	}
	
	/**
	 * Creates a <code>IGSHtmlMessageDialog</code> with a <code>JFrame</code>
	 * as its parent.
	 * @param parentFrame the <code>JFrame</code> parent of this dialog
	 */
	public IGSHtmlMessageDialog(JFrame parentFrame)
	{
		super(parentFrame);
		
		this.parentFrame = parentFrame;
	}
	
	/**
	 * Get the parent frame of this dialog
	 * @return this dialog parent frame
	 */
	public JFrame getParentFrame()
	{
		return parentFrame;
	}
	
	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
				
		if (keyCode == KeyEvent.VK_ENTER)
		{
			Object object = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			
			if(object instanceof JButton)
			{
				((JButton) object).doClick();
			}			
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbOk.requestFocusInWindow();
			this.pbOk.doClick();
		}		
	}
	
	/**
	 * Invoked when a key is released.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyReleased(KeyEvent ke)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Invoked when a key is typed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyTyped(KeyEvent ke) 
	{
		// TODO Auto-generated method stub
	}
}
