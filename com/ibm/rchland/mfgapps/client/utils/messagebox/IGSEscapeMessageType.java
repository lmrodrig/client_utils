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
 * <code>IGSEscapeMessageType</code> is the <code>IGSMessageType</code> for
 * an <code>IGSMessageBox</code> with an escape button.
 * @author The MFS Client Development Team
 */
public class IGSEscapeMessageType
	extends IGSMessageType
{
	/** Constructs a new <code>IGSEscapeMessageType</code>. */
	public IGSEscapeMessageType()
	{
		super();
	}

	/** {@inheritDoc} */
	public JComponent createButtonComponent(IGSMessageBox messageBox)
	{
		return createButton(IGSMessageTypeResources.ESCAPE,
				JComponent.WHEN_IN_FOCUSED_WINDOW, messageBox);
	}
}
