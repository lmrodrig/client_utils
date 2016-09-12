/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-01-11      39619JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.exception;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;

/**
 * <code>IGSMessageableException</code> is an interface for subclasses of
 * <code>Exception</code> that can indicate if the words "Program Exception"
 * should be prepended to the message displayed for the exception.
 * @author The Process Profile Client Development Team
 */
public interface IGSMessageableException
{
	/**
	 * Returns the value of the program exception property.
	 * <p>
	 * The default value is <code>true</code>.
	 * <p>
	 * The value of this method is used by {@link IGSMessageBox} to determine if
	 * the words "Program Exception" should be prepended to the message
	 * displayed for the exception.
	 * @return the value of the program exception property
	 */
	public boolean isProgramException();

	/**
	 * Sets the value of the program exception property.
	 * @param programException the new value of the program exception property
	 */
	public void setProgramException(boolean programException);
}
