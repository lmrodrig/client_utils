/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.event;

import java.util.Date;
import java.util.EventObject;


/**
 * <code>IGSDateSelectedEvent</code> is an <code>EventObject</code>
 * which indicates the selection of a <code>Date</code>.
 * 
 * @author Ryan Prechel
 * @version 1.0
 */
public class IGSDateSelectedEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	/** The <code>Date</code> that was selected. */
	private Date date;
	
	
	/**
	 * Constructs a new <code>IGSDateSelectedEvent</code>.
	 * @param source the <code>Object</code> that originated the event
	 * @param date the <code>Date</code> that was selected
	 */
	public IGSDateSelectedEvent(Object source, Date date) {
		super(source);
		this.date = date;
	}
	
	
	/**
	 * Returns the <code>Date</code> that was selected.
	 * @return the <code>Date</code> that was selected
	 */
	public Date getDate() {
		return this.date;
	}
}
