/* @ Copyright IBM Corporation 2007, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-23       34242JR R Prechel        -Initial version
 * 2010-04-09   ~1  45878MS Santiago SC      -Copy of MFSActionableDialog
 * 											 -Add getParentFrame() method
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.client.utils.interfaces.IGSActionable;
import com.ibm.rchland.mfgapps.client.utils.panel.action.IGSActionPanel;

/**
 * <code>IGSActionableDialog</code> is an abstract subclass of
 * <code>JDialog</code> that implements <code>IGSActionable</code>
 * and displays an <code>IGSActionPanel</code>. The setContentPane method is
 * overridden to add the <code>IGSActionPanel</code> to the bottom of
 * the dialog.
 * @author The MFS Client Development Team
 */
public abstract class IGSActionableDialog extends JDialog implements IGSActionable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2064402412596932779L;

	/**
	 * The <code>IGSActionPanel</code> used by this
	 * <code>IGSActionableDialog</code>.
	 */
	private IGSActionPanel actionIndicator = new IGSActionPanel();
	
	/**
	 * The <code>JFrame</code> that caused this <code>MFSDialog</code> to
	 * be displayed.
	 */
	private JFrame parent = null; //~1A
	
	
	/**
	 * Constructs a new modal <code>IGSActionableDialog</code> with the
	 * specified <code>parent</code> as owner.
	 * @param parent the <code>JFrame</code> that caused this
	 *        <code>IGSActionableDialog</code> to be displayed
	 * @see MFSDialog#MFSDialog(JFrame, String) for default settings
	 */
	public IGSActionableDialog(JFrame parent)
	{
		super(parent);
		
		this.parent = parent;
	}

	/**
	 * Constructs a new modal <code>IGSActionableDialog</code> with the
	 * specified <code>title</code> and the specified <code>parent</code> as
	 * owner.
	 * @param parent the <code>JFrame</code> that caused this
	 *        <code>IGSActionableDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @see MFSDialog#MFSDialog(JFrame, String) for default settings
	 */
	public IGSActionableDialog(JFrame parent, String title)
	{
		super(parent, title);
		
		this.parent = parent;
	}

	/**
	 * Returns the <code>IGSActionPanel</code> used by this dialog.
	 * @return the <code>IGSActionPanel</code> used by this dialog
	 */
	public final IGSActionPanel getActionIndicator()
	{
		return this.actionIndicator;
	}

	//~1A
	/**
	 * Returns the <code>JFrame</code> that caused this
	 * <code>MFSDialog</code> to be displayed.
	 * @return the <code>JFrame</code>
	 */
	public JFrame getParentFrame()
	{
		return this.parent;
	}

	/**
	 * Sets the content pane of the <code>IGSActionableDialog</code> to a new
	 * <code>Container</code> using a <code>BorderLayout</code> with the
	 * specified <code>Container</code> in the center and the
	 * <code>IGSActionPanel</code> in the south.
	 * @param contentPaneCenter the <code>Container</code> to add to the
	 *        center of the content pane
	 */
	public void setContentPane(Container contentPaneCenter)
	{
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(contentPaneCenter, BorderLayout.CENTER);
		contentPane.add(this.actionIndicator, BorderLayout.SOUTH);
		super.setContentPane(contentPane);
	}

	/** {@inheritDoc} */
	public void startAction(String message)
	{
		final Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		this.getParentFrame().setCursor(waitCursor);
		this.setCursor(waitCursor);
		this.actionIndicator.startAction(message);
		this.update(this.getGraphics());
	}

	/** {@inheritDoc} */
	public void stopAction()
	{
		final Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		this.actionIndicator.stopAction();
		this.setCursor(defaultCursor);
		this.getParentFrame().setCursor(defaultCursor);
		this.update(this.getGraphics());
	}
	
	/** {@inheritDoc} */
	public void updateAction(String message, int progress)
	{
		this.actionIndicator.startAction(message);
		this.update(this.getGraphics());
	}
}
