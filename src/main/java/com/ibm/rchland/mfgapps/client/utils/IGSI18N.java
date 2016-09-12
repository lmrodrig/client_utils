/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-01-15   ~1 39619JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <code>IGSI18N</code> contains internationalization utility methods.
 * @author The Process Profile Client Development Team
 */
public class IGSI18N
{
	/**
	 * Returns the <code>ResourceBundle</code> for the specified
	 * <code>Class</code>. The base name of the <code>ResourceBundle</code>
	 * returned will be the fully qualified name of the <code>Class</code>
	 * with the word "Resources" appended at the end.
	 * @param clazz the <code>Class</code> for which a
	 *        <code>ResourceBundle</code> is being created
	 * @return the <code>ResourceBundle</code>
	 * @throws java.util.MissingResourceException if no
	 *         <code>ResourceBundle</code> for the specified
	 *         <code>Class</code> can be found
	 */
	@SuppressWarnings("rawtypes")
	public static ResourceBundle getBundle(Class clazz)
	{
		String name = clazz.getName() + "Resources"; //$NON-NLS-1$
		Locale locale = Locale.getDefault();
		ClassLoader loader = clazz.getClassLoader();
		return ResourceBundle.getBundle(name, locale, loader);
	}

	/**
	 * Gets a <code>String</code> for the given key from the
	 * <code>ResourceBundle</code> for the specified <code>Class</code>.
	 * The {@link #getBundle(Class)} method is used to obtain the
	 * <code>ResourceBundle</code> for the <code>Class</code>.
	 * @param key the key for the desired <code>String</code>
	 * @param clazz the <code>Class</code> used to obtain a
	 *        <code>ResourceBundle</code>
	 * @return the <code>String</code> for the given key
	 * @throws java.util.MissingResourceException if no value for the given key
	 *         can be found
	 * @see #getBundle(Class)
	 */
	@SuppressWarnings("rawtypes")
	public static String getString(String key, Class clazz)
	{
		ResourceBundle bundle = getBundle(clazz);
		return bundle.getString(key);
	}

	/**
	 * Constructs a new <code>IGSI18N</code>. This class only has static
	 * methods and does not have any instance variables or instance methods.
	 * Thus, there is no reason to create an instance of <code>IGSI18N</code>,
	 * so the only constructor is <code>private</code>.
	 */
	private IGSI18N()
	{
		super();
	}
}
