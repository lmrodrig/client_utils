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

import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;

/**
 * <code>IGSTransactionException</code> is a subclass of
 * <code>IGSException</code> thrown to indicate a server transaction failed.
 * @author The Process Profile Client Development Team
 */
public class IGSTransactionException
	extends IGSException
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** The <code>IGSTransaction</code> that failed. */
	private final IGSXMLTransaction transaction;

	/**
	 * Constructs a new <code>IGSTransactionException</code>.
	 * @param transaction the <code>IGSTransaction</code> that failed
	 * @param programException the value of the program exception property
	 */
	public IGSTransactionException(IGSXMLTransaction transaction, boolean programException)
	{
		super(transaction.getErms());
		this.transaction = transaction;
		setProgramException(programException);
	}

	/**
	 * Returns the transaction's error message.
	 * @return the transaction's error message
	 */
	public String getErms()
	{
		return this.transaction.getErms();
	}

	/**
	 * Returns the transaction's input.
	 * @return the transaction's input
	 */
	public String getInput()
	{
		return this.transaction.getInput();
	}

	/**
	 * Returns the transaction's output.
	 * @return the transaction's output
	 */
	public String getOutput()
	{
		return this.transaction.getOutput();
	}

	/**
	 * Returns the transaction's return code.
	 * @return the transaction's return code
	 */
	public int getReturnCode()
	{
		return this.transaction.getReturnCode();
	}

	/**
	 * Returns the transaction that failed.
	 * @return the transaction that failed
	 */
	public IGSXMLTransaction getTransaction()
	{
		return this.transaction;
	}

	/**
	 * Returns the transaction's name.
	 * @return the transaction's name
	 */
	public String getTransactionName()
	{
		return this.transaction.getTransactionName();
	}
}
