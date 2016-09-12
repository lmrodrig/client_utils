/* @ Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15      34242JR  R Prechel        -Initial version
 * 2007-05-01   ~1 38495JM  R Prechel        -Add null check; use append(char)
 * 2007-05-23   ~2 37676JM  R Prechel        -Trim based on padding location
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.io;

/**
 * <code>IGSPad</code> contains static methods for padding a
 * <code>String</code>.
 * @author The MFS Client Development Team
 */
public class IGSPad
{
	/**
	 * Indicates padding should occur before the contents of the
	 * <code>String</code> to be padded.
	 * @see #pad(String, int, char, int)
	 */
	public static final int LEADING = 1;

	/**
	 * Indicates padding should occur after the contents of the
	 * <code>String</code> to be padded.
	 * @see #pad(String, int, char, int)
	 */
	public static final int TRAILING = 0;

	/**
	 * Pads <code>s</code> with <code>padding</code> until the result is
	 * <code>length</code> characters long. This method trims <code>s</code>
	 * if it is longer than <code>length</code>.
	 * @param s the <code>String</code> to pad
	 * @param length the length to which <code>s</code> will be padded
	 * @param padding the <code>char</code> used to pad <code>s</code>
	 * @param location specifies where padding will occur. Must be one of the
	 *        following, or no padding occurs:
	 *        <ul>
	 *        <li>{@link #LEADING}</li>
	 *        <li>{@link #TRAILING}</li>
	 *        </ul>
	 * @return the padded <code>String</code>
	 */
	public static String pad(String s, int length, char padding, int location)
	{
		//~1A Add null check
		if (s == null)
		{
			s = ""; //$NON-NLS-1$
		}

		//~2C Trim based on padding location if s.length() > length
		if (s.length() > length)
		{
			//If TRAILING, trim end
			if (location == TRAILING)
			{
				return s.substring(0, length);
			}
			//Otherwise trim start
			return s.substring(s.length() - length);
		}

		StringBuffer result = new StringBuffer(length);
		int paddingRequired = length - s.length();
		if (location == TRAILING)
		{
			result.append(s);
		}
		for (int i = 0; i < paddingRequired; i++)
		{
			result.append(padding); //~1C
		}
		if (location == LEADING)
		{
			result.append(s);
		}

		return result.toString(); //~1C
	}

	/**
	 * Pads <code>s</code> with trailing spaces until the result is
	 * <code>length</code> characters long. This method trims <code>s</code>
	 * if it is longer than <code>length</code>.
	 * @param s the <code>String</code> to pad
	 * @param length the length to which <code>s</code> will be padded
	 * @return the padded <code>String</code>
	 */
	public static String pad(String s, int length)
	{
		return pad(s, length, ' ', TRAILING);
	}

	/**
	 * Constructs a new <code>IGSPad</code>. This class only has static
	 * variables and static methods and does not have any instance variables or
	 * instance methods. Thus, there is no reason to create an instance of
	 * <code>IGSPad</code>, so the only constructor is declared
	 * <code>private</code>.
	 */
	private IGSPad()
	{
		super();
	}
}
