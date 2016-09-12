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
 * <code>IGSRuntimeException</code> is a subclass of
 * <code>RuntimeException</code>. Future exception classes developed for
 * projects that use the Client Utils project should extend
 * <code>IGSRuntimeException</code> when {@link RuntimeException} must be
 * extended. Otherwise, {@link IGSException} should be extended.
 * @author The Process Profile Client Development Team
 */
public class IGSRuntimeException
	extends RuntimeException
	implements IGSMessageableException
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;
	
	/** The value returned by {@link #isProgramException()}. */
	private boolean programException = true;

	/** Constructs a new <code>IGSRuntimeException</code>. */
	public IGSRuntimeException()
	{
		super();
	}

	/**
	 * Constructs a new <code>IGSRuntimeException</code>.
	 * @param message the detail message which can be retrieved using the
	 *        {@link Throwable#getMessage()} method
	 */
	public IGSRuntimeException(String message)
	{
		super(message);
	}

	/**
	 * Constructs a new <code>IGSRuntimeException</code>.
	 * @param message the detail message which can be retrieved using the
	 *        {@link Throwable#getMessage()} method
	 * @param cause the cause which can be retrieved using the
	 *        {@link Throwable#getCause()} method
	 */
	public IGSRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new <code>IGSRuntimeException</code>.
	 * @param cause the cause which can be retrieved using the
	 *        {@link Throwable#getCause()} method
	 */
	public IGSRuntimeException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Constructs a new <code>IGSRuntimeException</code>.
	 * @param message the detail message which can be retrieved using the
	 *        {@link Throwable#getMessage()} method
	 * @param programException the new value of the program exception property
	 */
	public IGSRuntimeException(String message, boolean programException)
	{
		super(message);
		this.programException = programException;
	}

	/**
	 * Constructs a new <code>IGSRuntimeException</code>.
	 * @param message the detail message which can be retrieved using the
	 *        {@link Throwable#getMessage()} method
	 * @param cause the cause which can be retrieved using the
	 *        {@link Throwable#getCause()} method
	 * @param programException the new value of the program exception property
	 */
	public IGSRuntimeException(String message, Throwable cause, boolean programException)
	{
		super(message, cause);
		this.programException = programException;
	}

	/**
	 * Constructs a new <code>IGSRuntimeException</code>.
	 * @param cause the cause which can be retrieved using the
	 *        {@link Throwable#getCause()} method
	 * @param programException the new value of the program exception property
	 */
	public IGSRuntimeException(Throwable cause, boolean programException)
	{
		super(cause);
		this.programException = programException;
	}

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
	public boolean isProgramException()
	{
		return this.programException;
	}

	/**
	 * Sets the value of the program exception property.
	 * @param programException the new value of the program exception property
	 */
	public void setProgramException(boolean programException)
	{
		this.programException = programException;
	}
}
