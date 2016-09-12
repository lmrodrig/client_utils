/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-02-11   ~1 37616JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.xml;

/**
 * <code>IGSUserObject</code> is the interface for a non-<code>String</code>
 * class that serves as the user <code>Object</code> for a tree node.
 * @author The MFS Client Development Team
 */
public interface IGSUserObject
{
	/**
	 * Adds the specified <code>elementData</code> for the XML element with
	 * the specified <code>elementName</code> to this
	 * <code>IGSUserObject</code>.
	 * @param elementName the name of the XML element
	 * @param elementData the data for the XML element
	 */
	public void add(String elementName, String elementData);
}
