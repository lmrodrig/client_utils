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

import javax.swing.JComponent;

/**
 * <code>IGSOkMessageType</code> is the <code>IGSMessageType</code> for an
 * <code>IGSMessageBox</code> with an ok button.
 * @author The MFS Client Development Team
 */
public class IGSOkMessageType
	extends IGSMessageType
{

	/** Constructs a new <code>IGSOkMessageType</code>. */
	public IGSOkMessageType()
	{
		super();
	}

	/** {@inheritDoc} */
	public JComponent createButtonComponent(IGSMessageBox messageBox)
	{
		return createButton(IGSMessageTypeResources.OK,
				JComponent.WHEN_IN_FOCUSED_WINDOW, messageBox);
	}
}
