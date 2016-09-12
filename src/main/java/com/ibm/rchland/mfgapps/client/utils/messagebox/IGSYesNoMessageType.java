/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-30      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.messagebox;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * <code>IGSYesNoMessageType</code> is the <code>IGSMessageType</code> for
 * an <code>IGSMessageBox</code> with yes/no buttons.
 * @author The MFS Client Development Team
 */
public class IGSYesNoMessageType
	extends IGSMessageType
{
	/** Constructs a new <code>IGSYesNoMessageType</code>. */
	public IGSYesNoMessageType()
	{
		super();
	}

	/** {@inheritDoc} */
	public JComponent createButtonComponent(IGSMessageBox messageBox)
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 40, 0));
		panel.add(createButton(IGSMessageTypeResources.YES, JComponent.WHEN_FOCUSED, messageBox));
		panel.add(createButton(IGSMessageTypeResources.NO, JComponent.WHEN_FOCUSED, messageBox));
		return panel;
	}
}
