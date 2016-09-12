/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.icon;

import java.awt.IllegalComponentStateException;
import java.io.Serializable;
import java.util.Locale;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;
import javax.swing.Icon;

/**
 * <code>IGSAccessibleIcon</code> is a base class for creating concrete
 * <code>AccessibleIcon</code>s. Concrete subclasses must implement
 * {@link #paintIcon(java.awt.Component, java.awt.Graphics, int, int)}.
 * @author The External Fulfillment Client Development Team
 */
public abstract class IGSAccessibleIcon
	extends AccessibleContext
	implements Accessible, AccessibleIcon, Icon, Serializable
{
	private static final long serialVersionUID = 1L;

	/** The width of the <code>IGSAccessibleIcon</code>. */
	private int width;

	/** The height of the <code>IGSAccessibleIcon</code>. */
	private int height;

	/**
	 * Constructs a new <code>IGSAccessibleIcon</code>.
	 * @param width the width of the <code>IGSAccessibleIcon</code>
	 * @param height the height of the <code>IGSAccessibleIcon</code>
	 */
	public IGSAccessibleIcon(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the <code>AccessibleRole</code> of the
	 * <code>IGSAccessibleIcon</code>.
	 * @return {@link AccessibleRole#ICON}
	 */
	public AccessibleRole getAccessibleRole()
	{
		return AccessibleRole.ICON;
	}

	/**
	 * Returns the <code>AccessibleStateSet</code> of the
	 * <code>IGSAccessibleIcon</code>.
	 * @return <code>null</code>
	 */
	public AccessibleStateSet getAccessibleStateSet()
	{
		return null;
	}

	/**
	 * Returns the <code>Accessible</code> parent of the
	 * <code>IGSAccessibleIcon</code>.
	 * @return <code>null</code>
	 */
	public Accessible getAccessibleParent()
	{
		return null;
	}

	/**
	 * Returns the 0-based index of the <code>IGSAccessibleIcon</code> in its
	 * accessible parent or -1 if the <code>IGSAccessibleIcon</code> does not
	 * have an accessible parent.
	 * @return -1
	 */
	public int getAccessibleIndexInParent()
	{
		return -1;
	}

	/**
	 * Returns the number of accessible children of the
	 * <code>IGSAccessibleIcon</code>.
	 * @return 0
	 */
	public int getAccessibleChildrenCount()
	{
		return 0;
	}

	/**
	 * Returns the <code>Accessible</code> child at the specified
	 * <code>index</code>.
	 * @param index the 0-based index of the child
	 * @return <code>null</code>
	 */
	public Accessible getAccessibleChild(int index)
	{
		return null;
	}

	/**
	 * Returns the <code>Locale</code> of the <code>IGSAccessibleIcon</code>.
	 * @return <code>null</code>
	 * @throws java.awt.IllegalComponentStateException never
	 */
	public Locale getLocale()
		throws IllegalComponentStateException
	{
		return null;
	}

	/**
	 * Returns the <code>AccessibleContext</code> associated with the
	 * <code>IGSAccessibleIcon</code>.
	 * @return <code>this</code>
	 */
	public AccessibleContext getAccessibleContext()
	{
		return this;
	}

	/**
	 * Returns the description of the <code>IGSAccessibleIcon</code>.
	 * @return the description of the <code>IGSAccessibleIcon</code>
	 */
	public String getAccessibleIconDescription()
	{
		return getAccessibleDescription();
	}

	/**
	 * Sets the description of the <code>IGSAccessibleIcon</code>.
	 * @param description the description of the <code>IGSAccessibleIcon</code>
	 */
	public void setAccessibleIconDescription(String description)
	{
		setAccessibleDescription(description);
	}

	/**
	 * Returns the width of the <code>IGSAccessibleIcon</code>.
	 * @return the width of the <code>IGSAccessibleIcon</code>
	 */
	public int getAccessibleIconWidth()
	{
		return this.width;
	}

	/**
	 * Returns the height of the <code>IGSAccessibleIcon</code>.
	 * @return the height of the <code>IGSAccessibleIcon</code>
	 */
	public int getAccessibleIconHeight()
	{
		return this.height;
	}

	/**
	 * Returns the width of the <code>IGSAccessibleIcon</code>.
	 * @return the width of the <code>IGSAccessibleIcon</code>
	 */
	public int getIconWidth()
	{
		return this.width;
	}

	/**
	 * Returns the height of the <code>IGSAccessibleIcon</code>.
	 * @return the height of the <code>IGSAccessibleIcon</code>
	 */
	public int getIconHeight()
	{
		return this.height;
	}
}
