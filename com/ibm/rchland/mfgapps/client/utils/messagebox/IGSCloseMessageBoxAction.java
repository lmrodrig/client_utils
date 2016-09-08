/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-30      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.messagebox;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * <code>IGSCloseMessageBoxAction</code> is an <code>Action</code> that
 * closes an <code>IGSMessageBox</code> when the <code>actionPerformed</code>
 * method is invoked.
 * @author The MFS Client Development Team
 */
public class IGSCloseMessageBoxAction
	extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	/** The <code>IGSMessageBox</code> to close. */
	private IGSMessageBox messageBox;

	/**
	 * Constructs a new <code>IGSCloseMessageBoxAction</code>.
	 * @param messageBox the <code>IGSMessageBox</code> to close
	 */
	public IGSCloseMessageBoxAction(IGSMessageBox messageBox)
	{
		this.messageBox = messageBox;
	}

	/**
	 * Invoked when an action occurs. Calls the
	 * {@link IGSMessageBox#close(Object)} method.
	 * @param e the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent e)
	{
		this.messageBox.close(this);
	}

	/**
	 * Returns a <code>String</code> representation of this
	 * <code>IGSCloseMessageBoxAction</code>.
	 * @return the value of the {@link Action#NAME} property
	 */
	public String toString()
	{
		return this.getValue(Action.NAME).toString();
	}
}
