/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-27      34242JR  R Prechel        -Initial version
 * Note: This class was created for 34242JR (MFS Client JRE Upgrade), but was
 *       used by 35394PL (EF Client Accessibility) before 34242JR was finished
 * 2007-01-18   ~1 37313MZ  R Prechel        -Incorporate changes for MFS Client (34242JR)
 *                                            into new EF Client installer for DST JRE replacement
 * 2007-05-01   ~2 38495JM  R Prechel        -Adjustments for Linux and MFS Print Server
 * 2007-05-24   ~3 37676JM  R Prechel        -Change directory and file creation
 * 2007-08-21   ~4 38768JL  R Prechel        -Change getTempDirectoryPath to getTempDirectory
 * 2011-07-24   ~5 36802JM  Santiago SC      -New getProperties() method, copy from ElfUtils
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;

/**
 * <code>IGSFileUtils</code> provides a collection of file utility methods.
 * <p>
 * The {@link #setLocalDirectory(Class)} method must be called to initialize the
 * local directory used by this class before any other methods of this class are
 * executed.
 * @author The MFS Client Development Team
 */
public class IGSFileUtils
{
	/** The <code>PrintStream</code> for standard err. */
	private static PrintStream err = System.err; //~2A
	
	/** The <code>PrintStream</code> for standard out. */
	private static PrintStream out = System.out; //~2A

	/** The name of the logs directory. */
	private static final String LOGS_DIRECTORY_NAME = "logs"; //$NON-NLS-1$ //~2A

	/** The name of the temp directory. */
	private static final String TEMP_DIRECTORY_NAME = "temp"; //$NON-NLS-1$

	//~1C The value of directory is set by the setLocalDirectory method
	// instead of a static initialization block so an application can use
	// its project as the local directory instead of the client utils project
	// when executed in the development IDE
	/** The base local directory. */
	private static File directory = null;

	//~2C Made public; parse file name
	/**
	 * Archives the specified <code>file</code> by renaming it to:
	 * filename prefix + <i>timestamp</i> + filename suffix
	 * <p>
	 * Note: If the file could not be archived, it is deleted.
	 * @param file the <code>File</code> to archive
	 */
	public static void archive(File file)
	{
		String prefix = file.getName();
		String suffix = ""; //$NON-NLS-1$
		if (prefix.indexOf('.') != -1)
		{
			int index = prefix.lastIndexOf('.');
			suffix = prefix.substring(index);
			prefix = prefix.substring(0, index);
		}
		
		StringBuffer name = new StringBuffer();
		name.append(prefix);
		name.append(file.lastModified());
		name.append(suffix);
		boolean renamed = file.renameTo(new File(file.getParent(), name.toString()));
		if (!renamed)
		{
			file.delete();
		}
	}

	//~1A New method
	/**
	 * Returns the code source directory of the specified <code>Class</code>.
	 * If the code source directory could not be determined, the current user
	 * directory is returned.
	 * @param referenceClass a <code>Class</code>
	 * @return the code source directory or the current user directory
	 */
	@SuppressWarnings("rawtypes")
	public static File getDirectory(Class referenceClass)
	{
		File resultDirectory = null;
		try
		{
			//determine the location of the code source
			ProtectionDomain p = referenceClass.getProtectionDomain();
			String name = p.getCodeSource().getLocation().getFile();
			name = name.replaceAll("%20", " "); //$NON-NLS-1$ //$NON-NLS-2$
			resultDirectory = new File(name);

			//code source could be a jar file
			while (resultDirectory.isFile())
			{
				resultDirectory = resultDirectory.getParentFile();
			}
		}
		catch (Exception e)
		{
			resultDirectory = null;
			e.printStackTrace();
		}

		// If the code source directory could not be determined,
		// use the current user directory
		if (resultDirectory == null)
		{
			//~3A Call getAbsoluteFile
			resultDirectory = new File("").getAbsoluteFile(); //$NON-NLS-1$
		}

		return resultDirectory;
	}

	//~2A New method
	/**
	 * Returns the <code>PrintStream</code> for standard err.
	 * @return the <code>PrintStream</code> for standard err
	 */
	public static PrintStream getErr()
	{
		return IGSFileUtils.err;
	}

	/**
	 * Creates a <code>File</code> for the specified <code>fileName</code>
	 * resolved relative to the local base directory.
	 * @param fileName the name of the file
	 * @return the <code>File</code>
	 */
	public static File getFile(String fileName)
	{
		return new File(IGSFileUtils.directory, fileName);
	}

	//~2A New method
	/**
	 * Creates a <code>File</code> for the specified <code>fileName</code>
	 * resolved relative to the logs directory in the local base directory.
	 * @param fileName the name of the file
	 * @return the <code>File</code>
	 */
	public static File getLogFile(String fileName)
	{
		StringBuffer name = new StringBuffer();
		name.append(IGSFileUtils.LOGS_DIRECTORY_NAME);
		name.append(File.separator);
		name.append(fileName);
		return new File(IGSFileUtils.directory, name.toString());
	}

	//~2A New method
	/**
	 * Returns the <code>PrintStream</code> for standard out.
	 * @return the <code>PrintStream</code> for standard out
	 */
	public static PrintStream getOut()
	{
		return IGSFileUtils.out;
	}

	//~5A
	/**
	 * This method loads the content of the application properties 
	 * file to a Java Properties object for easy lookup.
	 * 
	 * @param propertyFile name for the application properties file
	 * @return theProps contains the parameter value pairs
	 * @throws ElfException
	 * 
	 * @author Tao He
	 */
	public static Properties getProperties(String propertyFile) throws IGSException
	{
		Properties theProps = new Properties();
		
		try
		{
			if (propertyFile.indexOf("\\") == -1 && propertyFile.indexOf("/") == -1)
			{
				propertyFile = System.getProperty("user.dir") + File.separator + propertyFile;
			}
			theProps.load(new BufferedInputStream(new FileInputStream(propertyFile)));
			
		}
		catch (Exception e)
		{
			String message = "Unable to find properties file " + propertyFile;
			IGSException ie = new IGSException(message);
			throw ie;
		}

		return theProps;
	}

	//~4C Change getTempDirectoryPath to getTempDirectory (return File instead of String)
	/**
	 * Returns a <code>File</code> corresponding to the temp directory created
	 * inside the local base directory.
	 * @return a <code>File</code> for the temp directory
	 */
	public static File getTempDirectory()
	{
		return new File(IGSFileUtils.directory, IGSFileUtils.TEMP_DIRECTORY_NAME);
	}

	/**
	 * Creates a <code>File</code> for the specified <code>fileName</code>
	 * resolved relative to the temp directory in the local base directory. The
	 * file will be deleted if the virtual machine is terminated normally.
	 * @param fileName the name of the file
	 * @return the <code>File</code>
	 * @see File#deleteOnExit()
	 */
	public static File getTempFile(String fileName)
	{
		StringBuffer name = new StringBuffer();
		name.append(IGSFileUtils.TEMP_DIRECTORY_NAME);
		name.append(File.separator);
		name.append(fileName);
		File result = new File(IGSFileUtils.directory, name.toString());
		result.deleteOnExit();
		return result;
	}

	//~1A New method
	/**
	 * Sets the base local directory to the code source directory of the
	 * specified <code>Class</code> and ensures the logs subdirectory exists.
	 * @param referenceClass a <code>Class</code>
	 */
	@SuppressWarnings("rawtypes")
	public static void setLocalDirectory(Class referenceClass)
	{
		IGSFileUtils.directory = getDirectory(referenceClass);

		//~3A Create the logs directory if it does not exist
		File logDirectory = new File(IGSFileUtils.directory, LOGS_DIRECTORY_NAME);
		if (!logDirectory.exists())
		{
			logDirectory.mkdir();
		}
	}

	/**
	 * Deletes any archived log files older than <code>numberOfHours</code>
	 * old, sets up the new &quot;log&quot; and &quot;err&quot; log files, and
	 * redirects standard out and standard err to the log files. All log files
	 * accessed by this method must reside in the &quot;logs&quot; subdirectory
	 * of the base local directory.
	 * @param numberOfHours the number of hours after which an archived log file
	 *        will be deleted
	 * @throws IOException if an I/O error occurs
	 */
	public static void setupLogFiles(int numberOfHours)
		throws IOException
	{
		//~2C Call setupLogFiles(int, boolean)
		setupLogFiles(numberOfHours, true);
	}

	//~2A New method
	/**
	 * Deletes any archived log files older than <code>numberOfHours</code>
	 * old, sets up the new &quot;log&quot; and &quot;err&quot; log files, and
	 * redirects standard out and standard err to the log files if
	 * <code>redirect</code> is <code>true</code>. All log files accessed
	 * by this method must reside in the &quot;logs&quot; subdirectory of the
	 * base local directory.
	 * @param numberOfHours the number of hours after which an archived log file
	 *        will be deleted
	 * @param redirect <code>true</code> if standard out and standard err
	 *        should be redirected to the log and err file
	 * @throws IOException if an I/O error occurs
	 */
	public static void setupLogFiles(int numberOfHours, boolean redirect)
		throws IOException
	{
		//~3C Moved mkdir for logs directory to setLocalDirectory

		//Delete old files
		File logDirectory = new File(IGSFileUtils.directory, LOGS_DIRECTORY_NAME);
		long time = new Date().getTime() - (3600000L * numberOfHours);
		File[] files = logDirectory.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].lastModified() < time)
			{
				files[i].delete();
			}
		}

		String f = "MMddkkmmss.'txt'"; //$NON-NLS-1$
		String suffix = new SimpleDateFormat(f).format(new Date()); //$NON-NLS-1$
		String logName = "log" + suffix; //$NON-NLS-1$
		String errName = "err" + suffix; //$NON-NLS-1$

		//Create File objects for log files
		File logFile = new File(logDirectory, logName); //$NON-NLS-1$
		File errFile = new File(logDirectory, errName); //$NON-NLS-1$
		System.out.println("Log file created at: " + logFile.getPath()); //$NON-NLS-1$
		System.out.println("Err file created at: " + errFile.getPath()); //$NON-NLS-1$

		//Archive log files if they exist
		if (logFile.exists())
		{
			archive(logFile);
			logFile = new File(logDirectory, logName);
		}
		if (errFile.exists())
		{
			archive(errFile);
			errFile = new File(logDirectory, errName);
		}

		//~3C Creating a FileOutputStream will create the file
		try
		{
			IGSFileUtils.out = new PrintStream(new FileOutputStream(logFile));
			IGSFileUtils.err = new PrintStream(new FileOutputStream(errFile));
		}
		catch (IOException ioe)
		{
			IGSFileUtils.out = System.out;
			IGSFileUtils.err = System.err;
			throw ioe;
		}

		if (redirect)
		{
			//Redirect standard out and standard err
			System.setOut(IGSFileUtils.out);
			System.setErr(IGSFileUtils.err);
		}
	}

	//~3C Rename to reflect usage
	/**
	 * Creates the temp directory if it does not exist or deletes the contents
	 * of the temp directory if it does exist.
	 * @throws IOException if the pathname for the temp directory represents an
	 *         existing file that is not a directory
	 */
	public static void setupTempDirectory()
		throws IOException
	{
		File file = new File(IGSFileUtils.directory, IGSFileUtils.TEMP_DIRECTORY_NAME);
		if (file.exists())
		{
			if (!file.isDirectory())
			{
				throw new IOException(IGSFileUtils.TEMP_DIRECTORY_NAME
						+ " is not a directory."); //$NON-NLS-1$
			}
			File[] files = file.listFiles();
			if (files != null)
			{
				for (int i = 0; i < files.length; i++)
				{
					files[i].delete();
				}
			}
		}
		else
		{
			file.mkdir();
		}
		//~2D Do not delete temp directory on exit
	}
	
	/**
	 * Constructs a new <code>IGSFileUtils</code>. This class only has static
	 * variables and static methods and does not have any instance variables or
	 * instance methods. Thus, there is no reason to create an instance of
	 * <code>IGSFileUtils</code>, so the only constructor is declared
	 * <code>private</code>.
	 */
	private IGSFileUtils()
	{
		super();
	}
}
