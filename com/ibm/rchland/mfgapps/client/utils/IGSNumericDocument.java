/* © Copyright IBM Corporation 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2011-04-14       50364MC Santiago SC      -Initial version
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <code>IGSNumericDocument</code> is a subclass of
 * <code>PlainDocument</code> that imposes a limit on the maximum number of
 * digits allowed in the document.
 * @author The External Fulfillment Client Development Team
 */
public class IGSNumericDocument extends PlainDocument
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** The regex to validate that the input is numeric */
	private String numericRegex = "";
	
	/** The maximum number of characters allowed in the document. */
	private int maxLength;
	
	/**
	 * Constructs a new <code>IGSNumericDocument</code>.
	 * @param max the maximum number of digits allowed in the document
	 */
	public IGSNumericDocument(int maxLength)
	{
		this.maxLength = maxLength;
		numericRegex = "[\\d]{0," + maxLength + "}";
	}
	
	/**
	 * If the maximum number of characters will not be exceeded, adds the
	 * content of <code>string</code> to this document.
	 * @param offset the starting offset
	 * @param string the <code>String</code> to insert. Does nothing with
	 *        <code>null</code> or empty <code>String</code>s
	 * @param a the attributes for the inserted content
	 * @throws BadLocationException as thrown by
	 *         {@link PlainDocument#insertString(int, String, AttributeSet)}
	 */
	public void insertString(int offset, String string, AttributeSet a)
		throws BadLocationException
	{
		if (!(string == null || string.length() == 0 || 
				getLength() + string.length() > this.maxLength || !string.matches(numericRegex)))
		{
			super.insertString(offset, string, a);
		}
	}
}
