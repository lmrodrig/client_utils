/* @ Copyright IBM Corporation 2007, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-08-20      38768JL  R Prechel        -Initial version
 * 2008-02-11   ~1 37616JL  R Prechel        -Remove toUpperCase
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.xml;

import org.xml.sax.helpers.DefaultHandler;

/**
 * <code>IGSDefaultHandler</code> is a subclass of <code>DefaultHandler</code>
 * that provides handler related utility methods.
 * @author The MFS Print Server Development Team
 */
public class IGSDefaultHandler
	extends DefaultHandler
{
	/** Constructs a new <code>IGSDefaultHandler</code>. */
	public IGSDefaultHandler()
	{
		super();
	}

	/**
	 * Returns the name of the XML element without the namespace prefix.
	 * @param localName the local name of the XML element (without namespace
	 *        prefix). May be the empty <code>String</code> if namespace
	 *        processing is not being performed.
	 * @param qName the qualified name of the XML element (with namespace
	 *        prefix). May be the empty <code>String</code> if qualified names
	 *        are not available.
	 * @return the name of the XML element without the namespace prefix
	 */
	public static final String getElementName(String localName, String qName)
	{
		String result = ""; //$NON-NLS-1$
		if (localName.length() != 0)
		{
			result = localName; //~1C
		}
		else if (qName.length() != 0)
		{
			int index = qName.lastIndexOf(':');
			if (index != -1 && index < qName.length() - 1)
			{
				result = qName.substring(index + 1); //~1C
			}
			else
			{
				result = qName; //~1C
			}
		}
		return result;
	}
}
