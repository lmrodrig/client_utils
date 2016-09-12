/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-05-04   ~1 31214TB  R Prechel        -Commented out code used to 
 *                                            backup one directory 
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 * 2006-09-22   ~2 35394PL  R Prechel        -Changes to allow multiple preferences files
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <code>IGSPreferences</code> is a wrapper around
 * {@link java.util.Properties} designed to store preferences in a properties
 * file. When created using the no argument default constructor, the properties
 * file's location is based on the directory containing the package from which
 * this class's class file was loaded and either the value of the system
 * property specified by {@link #FILE_NAME_SYSTEM_PROPERTY} or the value of
 * {@link #DEFAULT_FILE_NAME} if an error occurred reading the system property
 * of the property does not exist.
 * @author The External Fulfillment Client Development Team
 */
public class IGSPreferences
{
	/** The default file name for the properties file. */
	private static final String DEFAULT_FILE_NAME = "preferences"; //$NON-NLS-1$

	/** The default file name extension for the properties file. */
	private static final String DEFAULT_FILE_NAME_EXTENSION = ".properties"; //$NON-NLS-1$

	/**
	 * The system property used to create the file name for the properties file.
	 * The selected property should return a unique value for each user (e.g., user.name).
	 */
	private static final String FILE_NAME_SYSTEM_PROPERTY = "user.name"; //$NON-NLS-1$

	/** The <code>File</code> representing the properties file. */
	private File preferencesFile;

	/** The suffix appended to the properties file filename before the file extension. */
	private String suffix; //~2A

	/** The <code>Properties</code> used to store the user preferences. */
	private Properties properties;

	/** Constructs a new <code>IGSPreferences</code>. */
	public IGSPreferences()
	{
		this.properties = new Properties();
	}

	/**
	 * Constructs a new <code>IGSPreferences</code>.
	 * @param suffix the suffix appended to the properties file filename before
	 *        the file extension
	 */
	public IGSPreferences(String suffix)
	{
		//~2A new constructor
		this.properties = new Properties();
		this.suffix = suffix;
	}

	/**
	 * Constructs a new <code>IGSPreferences</code>.
	 * @param file the properties <code>File</code> used to load and store the
	 *        preferences referenced by this <code>IGSPreferences</code>
	 */
	public IGSPreferences(File file)
	{
		this.properties = new Properties();
		this.preferencesFile = file;
	}

	/**
	 * Searches for and returns the value of the preference with the specified
	 * name. If a preference with the specified name is not found, returns
	 * <code>null</code>.
	 * @param name the name of the preference for which a value should be returned
	 * @return the value of the preference with the specified name or
	 *         <code>null</code> if such a preference could not be found
	 */
	public String getPreference(String name)
	{
		return this.properties.getProperty(name);
	}

	/**
	 * Searches for and returns the value of the preference with the specified
	 * name. If a preference with the specified name is not found, returns
	 * <code>defaultValue</code>.
	 * @param name the name of the preference for which a value should be returned
	 * @param defaultValue a default value for the preference
	 * @return the value of the preference with the specified name or
	 *         <code>defaultValue</code> if such a preference could not be found
	 */
	public String getPreference(String name, String defaultValue)
	{
		return this.properties.getProperty(name, defaultValue);
	}

	/**
	 * Sets the value of the preference with the specified name to the specified value.
	 * @param name the name of the preference for which a value should be set
	 * @param value the value to which the preference is set
	 * @return the previous value of the specified preference
	 */
	public Object setPreference(String name, String value)
	{
		return this.properties.setProperty(name, value);
	}

	/**
	 * Loads the preferences from the file corresponding to
	 * {@link #getPropertiesFile()}.
	 */
	public void loadPreferences()
	{
		try
		{
			FileInputStream in = new FileInputStream(getPropertiesFile());
			this.properties.load(in);
			in.close();
		}
		catch (IOException ioe)
		{
			displayErrorMessage(ioe);
		}
	}

	/**
	 * Stores the preferences to the file corresponding to
	 * {@link #getPropertiesFile()}.
	 */
	public void storePreferences()
	{
		try
		{
			File file = getPropertiesFile();
			FileOutputStream out = new FileOutputStream(file);
			this.properties.store(out, file.toString());
			out.close();
		}
		catch (IOException ioe)
		{
			displayErrorMessage(ioe);
		}
	}

	/**
	 * Returns the properties <code>File</code> used to load and store the
	 * preferences referenced by this <code>IGSPreferences</code>.
	 * Responsible for lazy creation of the <code>File</code>.
	 * @return the properties <code>File</code> used to load and store the
	 *         preferences referenced by this <code>IGSPreferences</code>
	 * @throws IOException if the <code>preferencesFile</code> could not be created
	 */
	private File getPropertiesFile()
		throws IOException
	{
		try
		{
			if (this.preferencesFile == null)
			{
				// determine the location of the code source
				String directory = this.getClass().getProtectionDomain().getCodeSource()
						.getLocation().getFile().replaceAll("%20", " "); //$NON-NLS-1$ //$NON-NLS-2$
				this.preferencesFile = new File(directory);

				// code source could be a jar file
				while (this.preferencesFile.isFile())
				{
					this.preferencesFile = this.preferencesFile.getParentFile();
				}

				//~1C Commented out following code 
				//~1C Can be uncommented when executed in IDE

				// Backup one more directory to avoid issues
				// with writing files in an IDE project's directory
//				File temp = preferencesFile.getParentFile();
//				if(temp != null) {
//					preferencesFile = temp;
//				}
				
				
				// Try to create a unique filename for each user
				//~2C The creation of the file name was changed to include the suffix if non null
				//~2C A StringBuffer is used instead of String concatenation.
				StringBuffer filename = new StringBuffer();
				try
				{
					filename.append(System.getProperty(FILE_NAME_SYSTEM_PROPERTY, DEFAULT_FILE_NAME));
					if (this.suffix != null)
					{
						filename.append(this.suffix);
					}
					filename.append(DEFAULT_FILE_NAME_EXTENSION);
				}
				catch (Exception e)
				{
					filename = new StringBuffer();
					filename.append(DEFAULT_FILE_NAME);
					if (this.suffix != null)
					{
						filename.append(this.suffix);
					}
					filename.append(DEFAULT_FILE_NAME_EXTENSION);
				}
				filename.insert(0, File.separator);
				filename.insert(0, this.preferencesFile.getAbsolutePath());
				this.preferencesFile = new File(filename.toString());
				//~2C End changes for creation of the file name and use of StringBuffer
			}
		}
		//thrown by getProtectionDomain
		catch (SecurityException se)
		{
			throw new IOException(se.getMessage());
		}
		//code source can be null
		catch (NullPointerException npe)
		{
			throw new IOException(npe.getMessage());
		}

		if (!this.preferencesFile.exists())
		{
			this.preferencesFile.createNewFile();
		}

		return this.preferencesFile;
	}

	/**
	 * Helper method called to display a message when an <code>Exception</code> occurs.
	 * @param exception the <code>Exception</code> that occurred
	 */
	private static void displayErrorMessage(Exception exception)
	{
		System.out.println(exception.toString());
		exception.printStackTrace();
	}
}
