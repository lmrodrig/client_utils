/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-11-02      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * <code>IGSInputStreamUtils</code> contains utility methods useful when
 * working with <code>InputStream</code>s.
 * <p>
 * Note: The <code>readFully</code> methods are derived from the
 * {@link java.io.DataInputStream#readFully(byte[], int, int)} method.
 * @author The MFS Client Development Team
 */
public class IGSInputStreamUtils
{
	/**
	 * Reads <code>b.length</code> bytes from <code>in</code> and stores
	 * them in the <code>byte</code> array <code>b</code>.
	 * <p>
	 * This method blocks until one of the following occurs:
	 * <ul>
	 * <li><code>b.length</code> bytes of input data are available.</li>
	 * <li>End of file is detected, in which case an <code>EOFException</code>
	 * is thrown.</li>
	 * <li>An I/O error occurs, in which case an <code>IOException</code> is
	 * thrown.</li>
	 * </ul>
	 * @param in the <code>InputStream</code> from which bytes are read
	 * @param b the buffer used to store the bytes that are read
	 * @exception EOFException if the end of stream is reached before
	 *            <code>b.length</code> bytes are read from <code>in</code>
	 * @exception IOException if an I/O error occurs
	 * @exception NullPointerException if <code>in == null</code> or
	 *            <code>b == null</code>
	 */
	public static void readFully(InputStream in, byte[] b)
		throws IOException, EOFException, NullPointerException
	{
		int len = b.length;
		int n = 0;
		while (n < len)
		{
			int count = in.read(b, n, len - n);
			if (count < 0)
			{
				throw new EOFException();
			}
			n += count;
		}
	}

	/**
	 * Reads <code>len</code> bytes from <code>in</code> and stores them in
	 * the <code>byte</code> array <code>b</code>.
	 * <p>
	 * This method blocks until one of the following occurs:
	 * <ul>
	 * <li><code>len</code> bytes of input data are available.</li>
	 * <li>End of file is detected, in which case an <code>EOFException</code>
	 * is thrown.</li>
	 * <li>An I/O error occurs, in which case an <code>IOException</code> is
	 * thrown.</li>
	 * </ul>
	 * @param in the <code>InputStream</code> from which bytes are read
	 * @param b the buffer used to store the bytes that are read
	 * @param off the offset into the buffer
	 * @param len the number of bytes to read
	 * @exception EOFException if the end of stream is reached before
	 *            <code>len</code> bytes are read from <code>in</code>
	 * @exception IOException if an I/O error occurs
	 * @exception NullPointerException if <code>in == null</code> or
	 *            <code>b == null</code>
	 * @exception IndexOutOfBoundsException if <code>len</code> or
	 *            <code>off</code> are negative or if <code>len + off</code>
	 *            is greater than <code>b.length</code>
	 */
	public static void readFully(InputStream in, byte[] b, int off, int len)
		throws IOException, EOFException, NullPointerException, IndexOutOfBoundsException
	{
		if (len < 0 || off < 0 || len + off > b.length)
		{
			throw new IndexOutOfBoundsException();
		}

		int n = 0;
		while (n < len)
		{
			int count = in.read(b, off + n, len - n);
			if (count < 0)
			{
				throw new EOFException();
			}
			n += count;
		}
	}

	/**
	 * Constructs a new <code>IGSInputStreamUtils</code>. This class only has
	 * static variables and static methods and does not have any instance
	 * variables or instance methods. Thus, there is no reason to create an
	 * instance of <code>IGSInputStreamUtils</code>, so the only constructor
	 * is declared <code>private</code>.
	 */
	private IGSInputStreamUtils()
	{
		//Nothing to do
	}
}
