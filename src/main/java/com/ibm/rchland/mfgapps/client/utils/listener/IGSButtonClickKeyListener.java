/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-24      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.AbstractButton;

/**
 * <code>IGSButtonClickKeyListener</code> is a <code>KeyListener</code> that
 * invokes the {@link javax.swing.AbstractButton#doClick()} method when the
 * enter key is pressed on an <code>AbstractButton</code>.
 * @author The External Fulfillment Client Development Team
 */
public class IGSButtonClickKeyListener
	extends KeyAdapter
	implements Serializable
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** Constructs a new <code>IGSButtonClickKeyListener</code>. */
	public IGSButtonClickKeyListener()
	{
		//Nothing to do
	}

	/**
	 * Invoked when a key has been pressed. Invokes the
	 * {@link javax.swing.AbstractButton#doClick()} method when the enter key is
	 * pressed on an <code>AbstractButton</code>.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		if (ke.getKeyCode() == KeyEvent.VK_ENTER)
		{
			Object source = ke.getSource();
			if (source instanceof AbstractButton)
			{
				((AbstractButton) source).doClick();
			}
		}
	}
}
