/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-25      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * <code>IGSWindowDisposeAL</code> is an <code>ActionListener</code> that
 * calls the {@link java.awt.Window#dispose()} method when an action occurs.
 * @author The External Fulfillment Client Development Team
 */
public class IGSWindowDisposeAL
	implements ActionListener, Serializable
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;
	
	/** The <code>Window</code> to dispose. */
	private Window window;

	/**
	 * Constructs a new <code>IGSWindowDisposeAL</code>.
	 * @param window the <code>Window</code> to dispose
	 */
	public IGSWindowDisposeAL(Window window)
	{
		this.window = window;
	}

	/**
	 * Invoked when an action occurs. Calls the {@link Window#dispose()} method.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		this.window.dispose();
	}
}
