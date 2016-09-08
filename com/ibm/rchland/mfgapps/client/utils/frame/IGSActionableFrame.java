/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-04-09       45878MS Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.client.utils.interfaces.IGSActionable;
import com.ibm.rchland.mfgapps.client.utils.panel.action.IGSActionPanel;

/**
 * <code>IGSActionableFrame</code> is an abstract subclass of
 * <code>JFrame</code> that implements <code>IGSActionable</code>
 * and displays an <code>IGSActionPanel</code>. The setContentPane method is
 * overridden to add the <code>IGSActionPanel</code> to the bottom of
 * the frame.
 * @author The MFS Client Development Team
 */
public class IGSActionableFrame extends JFrame implements IGSActionable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2746693372512344155L;
	
	/**
	 * The <code>IGSActionPanel</code> used by this
	 * <code>IGSActionableFrame</code>.
	 */
	private IGSActionPanel actionIndicator = new IGSActionPanel();
	
	/**
	 * Returns the <code>IGSActionPanel</code> used by this dialog.
	 * @return the <code>IGSActionPanel</code> used by this dialog
	 */
	public final IGSActionPanel getActionIndicator()
	{
		return this.actionIndicator;
	}

	/**
	 * Sets the content pane of the <code>IGSActionableFrame</code> to a new
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
		this.update(this.getGraphics());
	}
	
	/** {@inheritDoc} */
	public void updateAction(String message, int progress)
	{
		this.actionIndicator.startAction(message);
		this.update(this.getGraphics());
	}
}
