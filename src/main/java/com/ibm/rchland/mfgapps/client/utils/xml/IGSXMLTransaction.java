/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-01-12      39619JL  R Prechel        -Initial version
 * 2008-02-07   ~1 30635SE  R Prechel        -Add support for use by MFS Client
 *              ~1 40845MZ                   (Checked in using MFSCOMFUNC feature)
 * 2008-02-13   ~2 37616JL  R Prechel        -Support piped output
 * 2008-03-15   ~3 37616JL  R Prechel        -Fix receivePiped and add toString
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>IGSXMLTransaction</code> is used to call a server side program using
 * a socket connection. The class encapsulates the program's input, output, and
 * return code, along with an optional message that can be displayed while the
 * program executes.
 * @author The Process Profile Client Development Team
 */
public class IGSXMLTransaction
	extends IGSXMLDocument
	implements Runnable
{
	/** The return code indicating the end of a transaction's output. */
	public static final int END_OF_TRANSMISSION = 9999; //~1A

	/** The <code>Logger</code> used by <code>IGSXMLTransaction</code>. */
	public static final Logger LOGGER = Logger.getLogger(IGSXMLTransaction.class.getName());

	/** The size of the buffer used to send/receive meta data. */
	public static final int METADATA_LENGTH = 10;

	/** The default server port. */
	private static int defaultPort;

	/** The default server host name. */
	private static String defaultServer;

	static
	{
		try
		{
			defaultServer = System.getProperty("com.ibm.rchland.mfgapps.MFSSRV"); //$NON-NLS-1$
			Integer p = Integer.getInteger("com.ibm.rchland.mfgapps.MFSRTR"); //$NON-NLS-1$
			if (p != null)
			{
				defaultPort = p.intValue();
			}
		}
		catch (Exception exception)
		{
			String erms = "Error setting defaultPort and/or defaultServer."; //$NON-NLS-1$
			LOGGER.log(Level.INFO, erms, exception); //$NON-NLS-1$
		}
	}

	/** Used to create unique <code>Thread</code> names. */
	private static int threadNumber = 0; //~2A

	/** The message displayed while the transaction executes. */
	private String fieldActionMessage;

	/** The transaction's error message if an error occurred. */
	private String fieldErms; //~1A

	/** The transaction's input. */
	private String fieldInput;

	/** The transaction's name. */
	private final String fieldName;

	/** The transaction's output. */
	private String fieldOutput;

	/**
	 * The <code>PipedOutputStream</code> to which the XML with a root element
	 * of {@link #fieldPipedRootElement} is output.
	 */
	private PipedOutputStream fieldPipedOut; //~2A

	/**
	 * The name of the XML root element for the XML that is output to
	 * {@link #fieldPipedOut}.
	 */
	private String fieldPipedRootElement; //~2A

	/**
	 * The <code>Runnable</code> that reads from the {@link PipedInputStream}
	 * connected to {@link #fieldPipedOut}. The <code>Runnable</code> will be
	 * started on a new <code>Thread</code> before output is written to
	 * {@link #fieldPipedOut}.
	 */
	private Runnable fieldPipedRunnable; //~2A

	/** The <code>Thread</code> used to run {@link #fieldPipedRunnable}. */
	private Thread fieldPipedThread; //~2A

	/** The transaction's server port. */
	private int fieldPort = defaultPort;

	/** The transaction's return code. */
	private int fieldReturnCode;

	/** The transaction's server host name. */
	private String fieldServer = defaultServer;

	/**
	 * Constructs a new <code>IGSXMLTransaction</code>.
	 * @param name the name of the transaction
	 */
	public IGSXMLTransaction(String name)
	{
		super();
		this.fieldName = name;
	}

	/**
	 * Returns the default server port.
	 * @return the default server port
	 */
	public static int getDefaultPort()
	{
		return IGSXMLTransaction.defaultPort;
	}

	/**
	 * Sets the default server port.
	 * @param defaultPort the new default server port
	 */
	public static void setDefaultPort(int defaultPort)
	{
		IGSXMLTransaction.defaultPort = defaultPort;
	}

	/**
	 * Returns the default server host name.
	 * @return the default server host name
	 */
	public static String getDefaultServer()
	{
		return IGSXMLTransaction.defaultServer;
	}

	/**
	 * Sets the default server host name.
	 * @param defaultServer the new default server host name
	 */
	public static void setDefaultServer(String defaultServer)
	{
		IGSXMLTransaction.defaultServer = defaultServer;
	}

	//~1A New method
	/**
	 * Closes a <code>Socket</code> and its streams.
	 * @param socket the <code>Socket</code>
	 * @param in the <code>InputStream</code> used to read from the
	 *        <code>Socket</code>
	 * @param out the <code>OutputStream</code> used to write to the
	 *        <code>Socket</code>
	 */
	public static void closeConnection(Socket socket, InputStream in, OutputStream out)
	{
		try
		{
			if (in != null)
			{
				in.close();
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.INFO, "Error closing socket input.", e); //$NON-NLS-1$
		}

		try
		{
			if (out != null)
			{
				out.close();
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.INFO, "Error closing socket output.", e); //$NON-NLS-1$
		}

		try
		{
			if (socket != null)
			{
				socket.close();
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.INFO, "Error closing socket.", e); //$NON-NLS-1$
		}
	}

	/**
	 * Reads data from the <code>DataInputStream</code>.
	 * @param dataInput the <code>DataInputStream</code> from which to read
	 * @return the data read from the <code>DataInputStream</code>
	 * @throws IOException as thrown by the <code>DataInputStream</code>
	 */
	public static String readData(DataInputStream dataInput)
		throws IOException
	{
		byte[] data = new byte[METADATA_LENGTH];
		dataInput.readFully(data, 0, METADATA_LENGTH);
		String bufferString = new String(data, "US-ASCII"); //$NON-NLS-1$
		int length = Integer.parseInt(bufferString.trim());
		data = new byte[length];
		dataInput.readFully(data, 0, length);
		bufferString = new String(data, "US-ASCII"); //$NON-NLS-1$
		LOGGER.fine(bufferString);
		return bufferString;
	}

	/**
	 * Writes data to the <code>DataOutputStream</code>.
	 * @param dataOutput the <code>DataOutputStream</code> to which to write
	 * @param output the data to write to the <code>DataOutputStream</code>
	 * @throws IOException as thrown by the <code>DataOutputStream</code>
	 */
	public static void writeData(DataOutputStream dataOutput, String output)
		throws IOException
	{
		String length = Integer.toString(output.length()) + "          "; //$NON-NLS-1$
		length = length.substring(0, METADATA_LENGTH);
		dataOutput.write(length.getBytes());
		dataOutput.write(output.getBytes());
		//The OutputStream may be buffered, so it needs to be flushed
		dataOutput.flush();
		LOGGER.fine(output);
	}

	//~1A New method
	/**
	 * Appends the specified <code>data</code> to the transaction's input.
	 * This method should only be used for positional based input and should not
	 * be used for XML based input.
	 * @param data the data to append to the transaction's input
	 */
	public void append(Object data)
	{
		this.buffer.append(data);
	}

	/** {@inheritDoc} */
	public void startDocument()
	{
		super.startDocument();
		this.buffer.append("<PGM><TRX>"); //$NON-NLS-1$
		this.buffer.append(this.fieldName);
		this.buffer.append("</TRX>"); //$NON-NLS-1$
		this.buffer.append("<DATA>"); //$NON-NLS-1$
	}

	/** {@inheritDoc} */
	public void endDocument()
	{
		this.buffer.append("</DATA>"); //$NON-NLS-1$
		this.buffer.append("</PGM>"); //$NON-NLS-1$
		super.endDocument();
	}

	/** Executes the transaction. */
	public void run()
	{
		Socket socket = null;
		DataInputStream dataIn = null;
		DataOutputStream dataOut = null;
		try
		{
			socket = new Socket(this.fieldServer, this.fieldPort);
			InputStream in = new BufferedInputStream(socket.getInputStream());
			dataIn = new DataInputStream(in);
			OutputStream out = new BufferedOutputStream(socket.getOutputStream());
			dataOut = new DataOutputStream(out);
			this.fieldInput = this.buffer.toString();
			this.buffer.setLength(0);

			writeData(dataOut, this.fieldName);
			writeData(dataOut, "         0" + this.fieldInput); //$NON-NLS-1$

			//~1A Change processing of Socket output
			String data = readData(dataIn);
			if (data.charAt(0) == '<')
			{
				receiveXML(data, dataIn);
			}
			//~2C Check if fieldPipedOut is null
			else if (this.fieldPipedOut == null)
			{
				receivePositional(data, dataIn);
			}
			else
			{
				receivePiped(data, dataIn); //~2A
			}
			this.fieldOutput = this.buffer.toString();
		}
		catch (Exception e)
		{
			String erms = "Communication Error. Please contact support."; //$NON-NLS-1$
			setError(erms, -10, e);
		}

		//~1C Call closeConnection to close the Socket
		closeConnection(socket, dataIn, dataOut);
		closePipe(); //~2A
	}

	//~1A New method
	/**
	 * Processes the output of a transaction which uses an RRET XML element in
	 * each buffer of data to send the return code for the buffer of data.
	 * @param data the first buffer of data
	 * @param dataIn the <code>DataInputStream</code> used to read the
	 *        transaction's output
	 * @throws IOException as thrown by {@link #readData(DataInputStream)}
	 */
	private void receiveXML(String data, DataInputStream dataIn)
		throws IOException
	{
		StringBuffer rretBuffer = new StringBuffer(data);
		IGSXMLBoundary boundary = new IGSXMLBoundary(0, data.length());
		int rc = Integer.parseInt(getElement("RRET", rretBuffer, boundary).trim()); //$NON-NLS-1$
		while (rc != END_OF_TRANSMISSION)
		{
			this.fieldReturnCode = rc;
			this.buffer.append(data);
			data = readData(dataIn);
			rretBuffer.setLength(0);
			rretBuffer.append(data);
			boundary = new IGSXMLBoundary(0, data.length());
			rc = Integer.parseInt(getElement("RRET", rretBuffer, boundary).trim()); //$NON-NLS-1$
		}

		if (this.fieldReturnCode != 0)
		{
			this.fieldErms = getFirstElement("ERMS"); //$NON-NLS-1$
		}
	}

	//~1A New method
	/**
	 * Processes the output of a transaction which uses the first
	 * {@link #METADATA_LENGTH} characters of a buffer of data to send the
	 * return code for the buffer of data.
	 * @param data the first buffer of data
	 * @param dataIn the <code>DataInputStream</code> used to read the
	 *        transaction's output
	 * @throws IOException as thrown by {@link #readData(DataInputStream)}
	 */
	private void receivePositional(String data, DataInputStream dataIn)
		throws IOException
	{
		String lastDataBuffer = null;
		int rc = Integer.parseInt(data.substring(0, METADATA_LENGTH).trim());
		while (rc != END_OF_TRANSMISSION)
		{
			this.fieldReturnCode = rc;
			lastDataBuffer = data.substring(METADATA_LENGTH);
			this.buffer.append(lastDataBuffer);
			data = readData(dataIn);
			rc = Integer.parseInt(data.substring(0, METADATA_LENGTH).trim());
		}

		if (this.fieldReturnCode != 0)
		{
			this.fieldErms = getFirstElement("ERMS"); //$NON-NLS-1$
			if (this.fieldErms == null)
			{
				this.fieldErms = lastDataBuffer;
			}
		}
	}

	//~2A New method
	/**
	 * Processes the output of a transaction for which part of the output is
	 * piped to a <code>PipedOutputStream</code> for processing. The first
	 * {@link #METADATA_LENGTH} characters of a buffer of data are used to send
	 * the return code for the buffer of data.
	 * @param data the first buffer of data
	 * @param dataIn the <code>DataInputStream</code> used to read the
	 *        transaction's output
	 * @throws IOException as thrown by {@link #readData(DataInputStream)}
	 */
	private void receivePiped(String data, DataInputStream dataIn)
		throws IOException
	{
		// If this method is called by run, fieldPipedOut is not null,
		// which means fieldPipedRootElement cannot be null.
		final String endTag = "</" + this.fieldPipedRootElement + '>'; //$NON-NLS-1$

		// The output from the server will contain data before and after the
		// piped output. The following constants and the streamPosition variable
		// are used to indicate the position of the current buffer of data,
		// since the buffer's position determines how the data is parsed.
		final int BEFORE_PIPED_OUTPUT = 0;
		final int DURING_PIPED_OUTPUT = 1;
		final int AFTER_PIPED_OUTPUT = 2;
		int streamPosition = BEFORE_PIPED_OUTPUT;
		int rc = Integer.parseInt(data.substring(0, METADATA_LENGTH).trim());
		while (rc != END_OF_TRANSMISSION)
		{
			this.fieldReturnCode = rc;
			data = data.substring(METADATA_LENGTH);
			switch (streamPosition)
			{
				case AFTER_PIPED_OUTPUT:
				{
					this.buffer.append(data);
					break;
				}
				case DURING_PIPED_OUTPUT:
				{
					int end = data.indexOf(endTag);
					if (end == -1)
					{
						this.fieldPipedOut.write(data.getBytes());
					}
					else
					{
						end += endTag.length();
						this.fieldPipedOut.write(data.substring(0, end).getBytes());
						this.buffer.append(data.substring(end));
						streamPosition = AFTER_PIPED_OUTPUT;
					}
					break;
				}
				default:
				{
					//~3C Use findStartTag to search for start tag
					int start = findStartTag(this.fieldPipedRootElement,
							new StringBuffer(data), 0);
					int end = data.indexOf(endTag);
					if (start == -1)
					{
						this.buffer.append(data);
					}
					else
					{
						// The current buffer is the first buffer with data that
						// must be piped. Thus, the Runnable used to handle the
						// piped input must be started. The piped input must be
						// read from a different Thread than the Thread used to
						// write the piped output, so a new Thread is created.
						if (this.fieldPipedRunnable != null)
						{
							String n = "IGSXMLTransaction-" + threadNumber++; //$NON-NLS-1$
							this.fieldPipedThread = new Thread(this.fieldPipedRunnable, n);
							this.fieldPipedThread.start();
						}

						if (end == -1)
						{
							this.buffer.append(data.substring(0, start));
							this.fieldPipedOut.write(data.substring(start).getBytes());
							streamPosition = DURING_PIPED_OUTPUT;
						}
						else
						{
							end += endTag.length();
							this.buffer.append(data.substring(0, start));
							this.fieldPipedOut.write(data.substring(start, end).getBytes());
							this.buffer.append(data.substring(end));
							streamPosition = AFTER_PIPED_OUTPUT;
						}
					}
					break;
				}
			}
			data = readData(dataIn);
			rc = Integer.parseInt(data.substring(0, METADATA_LENGTH).trim());
		}

		if (this.fieldReturnCode != 0)
		{
			this.fieldErms = getFirstElement("ERMS"); //$NON-NLS-1$
		}
	}

	//~2A New method
	/** Closes {@link #fieldPipedOut} and joins {@link #fieldPipedThread}. */
	private void closePipe()
	{
		if (this.fieldPipedOut != null)
		{
			try
			{
				this.fieldPipedOut.close();
			}
			catch (Exception e)
			{
				String message = "Error closing PipedOutputStream."; //$NON-NLS-1$
				LOGGER.log(Level.WARNING, message, e);
			}

			if (this.fieldPipedThread != null)
			{
				while (this.fieldPipedThread.isAlive())
				{
					try
					{
						this.fieldPipedThread.join();
					}
					catch (Exception e)
					{
						String message = "Error joining Thread."; //$NON-NLS-1$
						LOGGER.log(Level.WARNING, message, e);
					}
				}
			}
		}
	}

	/**
	 * Returns the transaction's action message.
	 * @return the message displayed while the transaction executes
	 */
	public String getActionMessage()
	{
		return this.fieldActionMessage;
	}

	/**
	 * Returns the transaction's error message if an error occurred.
	 * @return the transaction's error message if an error occurred
	 */
	public String getErms()
	{
		//~1C Store error message in instance variable
		return this.fieldErms;
	}

	/**
	 * Returns the transaction's input.
	 * @return the transaction's input
	 */
	public String getInput()
	{
		return this.fieldInput;
	}

	/**
	 * Returns the transaction's output.
	 * @return the transaction's output
	 */
	public String getOutput()
	{
		return this.fieldOutput;
	}

	/**
	 * Returns the transaction's server port.
	 * @return the transaction's server port
	 */
	public int getPort()
	{
		return this.fieldPort;
	}

	/**
	 * Returns the transaction's return code.
	 * @return the transaction's return code
	 */
	public int getReturnCode()
	{
		return this.fieldReturnCode;
	}

	/**
	 * Returns the transaction's server host name.
	 * @return the transaction's server host name
	 */
	public String getServer()
	{
		return this.fieldServer;
	}

	/**
	 * Returns the transaction's name.
	 * @return the transaction's name
	 */
	public String getTransactionName()
	{
		return this.fieldName;
	}

	//~1A New method
	/**
	 * Sets the transaction's action message.
	 * @param actionMessage the message displayed while the transaction executes
	 */
	public void setActionMessage(String actionMessage)
	{
		this.fieldActionMessage = actionMessage;
	}

	//~1A New method
	/**
	 * Used to set the transaction's error message, return code, and output if
	 * an error occurred.
	 * @param erms a message describing the error
	 * @param returnCode the return code for the transaction
	 * @param exception the <code>Exception</code> for the error. May be
	 *        <code>null</code>.
	 */
	public void setError(String erms, int returnCode, Exception exception)
	{
		this.fieldErms = erms;
		this.fieldReturnCode = returnCode;

		LOGGER.log(Level.WARNING, this.fieldErms, exception);
		this.buffer.setLength(0);
		startDocument();
		addElement("ERMS", this.fieldErms); //$NON-NLS-1$
		endDocument();
		this.fieldOutput = this.buffer.toString();
	}

	//~2A New method
	/**
	 * Sets the <code>Runnable</code> that reads from the
	 * {@link PipedInputStream} connected to {@link #fieldPipedOut}. The
	 * <code>Runnable</code> will be started on a new <code>Thread</code>
	 * before output is written to {@link #fieldPipedOut}.
	 * @param runnable the <code>Runnable</code>
	 * @throws IllegalStateException if {@link #setupPipedStreams(String)} has
	 *         not been invoked to setup {@link #fieldPipedOut}
	 */
	public void setPipedRunnable(Runnable runnable)
	{
		if (this.fieldPipedOut == null)
		{
			String msg = "fieldPipedOut == null"; //$NON-NLS-1$
			throw new IllegalStateException(msg);
		}
		this.fieldPipedRunnable = runnable;
	}

	/**
	 * Sets the transaction's server port.
	 * @param port the new server port
	 */
	public void setPort(int port)
	{
		this.fieldPort = port;
	}

	/**
	 * Sets the transaction's server host name.
	 * @param server the new server host name
	 */
	public void setServer(String server)
	{
		this.fieldServer = server;
	}

	//~2A New method
	/**
	 * Sets up the transaction so that the XML with a root element of
	 * <code>pipedRootElement</code> is output to a
	 * <code>PipedOutputStream</code>.
	 * @param pipedRootElement the name of the XML root element for the XML that
	 *        is output to the <code>PipedOutputStream</code>
	 * @return the <code>PipedInputStream</code> from which the transaction's
	 *         piped output can be read
	 * @throws IOException if there is a problem connecting the piped streams
	 * @throws IllegalArgumentException if <code>pipedRootElement</code> is
	 *         <code>null</code>
	 */
	public PipedInputStream setupPipedStreams(String pipedRootElement)
		throws IOException
	{
		if (pipedRootElement == null)
		{
			String msg = "pipedRootElement == null"; //$NON-NLS-1$
			throw new IllegalArgumentException(msg);
		}
		this.fieldPipedRootElement = pipedRootElement;
		PipedInputStream pipedIn = new PipedInputStream();
		this.fieldPipedOut = new PipedOutputStream(pipedIn);
		return pipedIn;
	}

	//~3A New method
	/**
	 * Returns a <code>String</code> representation of the transaction.
	 * @return an XML <code>String</code> containing an XML comment with the
	 *         field values followed by the XML from the XML document
	 */
	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<!--"); //$NON-NLS-1$
		stringBuffer.append("\nAction Message: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldActionMessage);
		stringBuffer.append("\nErms: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldErms);
		stringBuffer.append("\nInput: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldInput);
		stringBuffer.append("\nName: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldName);
		stringBuffer.append("\nOutput: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldOutput);
		stringBuffer.append("\nPiped Root Element: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldPipedRootElement);
		stringBuffer.append("\nPort: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldPort);
		stringBuffer.append("\nReturn Code: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldReturnCode);
		stringBuffer.append("\nServer: "); //$NON-NLS-1$
		stringBuffer.append(this.fieldServer);
		stringBuffer.append("\n-->\n"); //$NON-NLS-1$
		stringBuffer.append(toXMLString());
		return stringBuffer.toString();
	}
}
