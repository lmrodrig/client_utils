/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.event;

import java.util.EventListener;


/**
 * <code>IGSDateSelectedListener</code> is the listener interface 
 * for receiving <code>IGSDateSelectedEvent</code>s.
 *
 * @author Ryan Prechel
 * @version 1.0
 */
public interface IGSDateSelectedListener extends EventListener {
	/**
	 * Invoked when a <code>Date</code> is selected.
	 * @param e the <code>IGSDateSelectedEvent</code>
	 */
	public void dateSelected(IGSDateSelectedEvent e);
}
