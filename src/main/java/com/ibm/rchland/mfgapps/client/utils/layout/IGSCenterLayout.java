/* @ Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/**
 * <code>IGSCenterLayout</code> is a <code>LayoutManager</code> that centers
 * the single <code>Component</code> in the layout.
 * @author The MFS Client Development Team
 */
public class IGSCenterLayout
	implements LayoutManager, Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * Has no effect, since this layout manager does not use a per-component
	 * string.
	 * @param name the string to be associated with the component
	 * @param comp the component to be added
	 */
	public void addLayoutComponent(String name, Component comp)
	{
		//Does nothing
	}

	/**
	 * Has no effect, since this layout manager does not use
	 * {@link #addLayoutComponent(String, Component)}.
	 * @param comp the component to be removed
	 */
	public void removeLayoutComponent(Component comp)
	{
		//Does nothing
	}

	/** {@inheritDoc} */
	public Dimension preferredLayoutSize(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			Insets i = parent.getInsets();
			Dimension dim = new Dimension(i.left + i.right, i.top + i.bottom);
			if(parent.getComponentCount() > 0)
			{
				Dimension d = parent.getComponent(0).getPreferredSize();
				dim.height += d.height;
				dim.width += d.width;
			}
			return dim;
		}
	}

	/** {@inheritDoc} */
	public Dimension minimumLayoutSize(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			Insets i = parent.getInsets();
			Dimension dim = new Dimension(i.left + i.right, i.top + i.bottom);
			if(parent.getComponentCount() > 0)
			{
				Dimension d = parent.getComponent(0).getMinimumSize();
				dim.height += d.height;
				dim.width += d.width;
			}
			return dim;
		}
	}

	/** {@inheritDoc} */
	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			if (parent.getComponentCount() > 0)
			{
				final Component component = parent.getComponent(0);
				Insets insets = parent.getInsets();
				Dimension parentDim = parent.getSize();
				int availableHeight = parentDim.height - insets.top - insets.bottom;
				int availableWidth = parentDim.width - insets.left - insets.right;

				Dimension componentDim = component.getPreferredSize();
				if ((componentDim.width > availableWidth)
						|| (componentDim.height > availableHeight))
				{
					componentDim = component.getPreferredSize();
				}

				int x = insets.left;
				int y = insets.top;
				int layoutWidth = availableWidth;
				int layoutHeight = availableHeight;
				if ((componentDim.width <= availableWidth)
						&& (componentDim.height <= availableHeight))
				{
					x += ((availableWidth - componentDim.width) / 2);
					y += ((availableHeight - componentDim.height) / 2);
					layoutWidth = componentDim.width;
					layoutHeight = componentDim.height;
				}

				component.setBounds(x, y, layoutWidth, layoutHeight);
			}
		}
	}
}
