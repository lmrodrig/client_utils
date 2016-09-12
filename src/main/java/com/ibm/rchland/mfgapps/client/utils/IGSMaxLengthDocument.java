/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <code>IGSMaxLengthDocument</code> is a subclass of
 * <code>PlainDocument</code> that imposes a limit on the maximum number of
 * characters allowed in the document.
 * @author The External Fulfillment Client Development Team
 */
public class IGSMaxLengthDocument
	extends PlainDocument
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** The maximum number of characters allowed in the document. */
	private int maxLength;

	/**
	 * Constructs a new <code>IGSMaxLengthDocument</code>.
	 * @param max the maximum number of characters allowed in the document
	 */
	public IGSMaxLengthDocument(int max)
	{
		this.maxLength = max;
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
		if (!(string == null || string.length() == 0 || getLength() + string.length() > this.maxLength))
		{
			super.insertString(offset, string, a);
		}
	}
}
