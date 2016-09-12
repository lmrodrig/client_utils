/* @ Copyright IBM Corporation 2007, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-08-01      38768JL  R Prechel        -Initial version
 * 2008-04-29  ~1  41258JM  D Pietrasik      -extend javax.swing.filechooser.FileFilter
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>IGSFileExtensionFilter</code> is a <code>FileFilter</code> that
 * filters files based on acceptable file extensions.
 * @author The MFS Print Server Development Team
 */
public class IGSFileExtensionFilter extends javax.swing.filechooser.FileFilter
	implements java.io.FileFilter
{
	/** The <code>List</code> of acceptable file extensions. */
	@SuppressWarnings("rawtypes")
	private List extensions;
	
	/** The <code>String</code> to use for description. */
	private String description;  // ~1A
	
	/** Whether or not to allow directories */
	private boolean allowDir;    // ~1A

	/**
	 * Constructs a new <code>IGSFileExtensionFilter</code>.
	 * @param number the number of file extensions the <code>FileFilter</code>
	 *        will accept
	 */
	public IGSFileExtensionFilter(int number)
	{
		this(number, null, false);  //~1C
	}

	// ~1A
	/**
	 * Constructs a new <code>IGSFileExtensionFilter</code>.
	 * @param number the number of file extensions the <code>FileFilter</code>
	 *        will accept
	 * @param description Description for file chooser
	 * @param allowDirectories Should directories be accepted
	 */
	@SuppressWarnings("rawtypes")
	public IGSFileExtensionFilter(int number, String description, boolean allowDirectories)
	{
		this.extensions = new ArrayList(number);
		this.description = description;
		this.allowDir = allowDirectories;
	}

	/**
	 * Adds a new acceptable file extension to the <code>FileFilter</code>.
	 * @param extension the file extension
	 */
	@SuppressWarnings("unchecked")
	public void addExtension(String extension)
	{
		this.extensions.add(extension.toLowerCase());
	}

	/** {@inheritDoc} */
	public boolean accept(File pathname)
	{
		if (this.allowDir && pathname.isDirectory())   // ~1A
		{
			return true;
		}
		
		String name = pathname.getName();
		int index = name.lastIndexOf('.');
		String extension = null;
		if (index > 0 && index < name.length() - 1)
		{
			extension = name.substring(index + 1).toLowerCase();
		}
		return this.extensions.contains(extension);
	}

	// ~1A
	/** {@inheritDoc} */
	public String getDescription()
	{
		return this.description;
	}

}
