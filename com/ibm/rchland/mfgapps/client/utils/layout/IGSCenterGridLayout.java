/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-01-11   ~1 39619JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.layout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

/**
 * <code>IGSCenterGridLayout</code> is a variant of <code>GridLayout</code>
 * that centers the <code>Component</code>s in the last row if there are
 * fewer <code>Component</code>s than columns and centers the entire grid in
 * the parent <code>Container</code>.
 * @author The Process Profile Client Development Team
 */
public class IGSCenterGridLayout
	extends IGSGridLayout
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new <code>IGSCenterGridLayout</code> with the specified
	 * number of rows and columns. All components in the grid layout are given
	 * equal size. The components in the last row are centered if there are
	 * fewer components than columns.
	 * <p>
	 * Horizontal and vertical gaps are set to the specified values. Horizontal
	 * gaps are placed between each of the columns. Vertical gaps are placed
	 * between each of the rows.
	 * <p>
	 * One, but not both, of <code>rows</code> and <code>cols</code> can be
	 * zero, which means that any number of components can be placed in a row or
	 * in a column.
	 * @param rows the minimum number of rows, with zero meaning any number of
	 *        rows
	 * @param cols the minimum number of columns, with zero meaning any number
	 *        of columns
	 * @param hgap the horizontal gap
	 * @param vgap the vertical gap
	 * @throws IllegalArgumentException if both <code>rows</code> and
	 *         <code>cols</code> are nonpositive or if either
	 *         <code>hgap</code> or <code>vgap</code> is negative
	 */
	public IGSCenterGridLayout(int rows, int cols, int hgap, int vgap)
	{
		super(rows, cols, hgap, vgap);
	}

	/** {@inheritDoc} */
	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			final int componentCount = parent.getComponentCount();

			if (componentCount == 0)
			{
				return;
			}

			final int[] dimensions = calculateGridSize(parent);
			final int rowCount = dimensions[0];
			final int colCount = dimensions[1];
			final Insets insets = parent.getInsets();
			final Dimension pref = preferredLayoutSize(parent);
			final Dimension size = parent.getSize();
			size.width -= (insets.left + insets.right);
			size.height -= (insets.top + insets.bottom);

			int w = size.width;
			int h = size.height;

			if (size.width > pref.width)
			{
				w = pref.width;
			}
			if (size.width > pref.height)
			{
				h = pref.height;
			}

			w = (w - (colCount - 1) * this.hgap) / colCount;
			h = (h - (rowCount - 1) * this.vgap) / rowCount;

			if (parent.getComponentOrientation().isLeftToRight())
			{
				int x = insets.left;
				if (size.width > pref.width)
				{
					x += (size.width - pref.width) / 2;
				}

				int initY = insets.top;
				if (size.height > pref.height)
				{
					initY += (size.height - pref.height) / 2;
				}

				for (int c = 0; c < colCount; c++, x += w + this.hgap)
				{
					for (int r = 0, y = initY; r < rowCount; r++, y += h + this.vgap)
					{
						int i = r * colCount + c;
						if (i < componentCount)
						{
							int remainder = componentCount - (r * colCount);
							if (remainder < colCount)
							{
								float amount = (colCount - remainder) / 2.0f;
								int leftPad = (int) (amount * w + amount * this.hgap);
								parent.getComponent(i).setBounds(x + leftPad, y, w, h);
							}
							else
							{
								parent.getComponent(i).setBounds(x, y, w, h);
							}
						}
					}
				}
			}
			else
			{
				int x = parent.getWidth() - insets.right - w;
				if (size.width > pref.width)
				{
					x -= (size.width - pref.width) / 2;
				}

				int initY = insets.top;
				if (size.height > pref.height)
				{
					initY += (size.height - pref.height) / 2;
				}

				for (int c = 0; c < colCount; c++, x -= w + this.hgap)
				{
					for (int r = 0, y = initY; r < rowCount; r++, y += h + this.vgap)
					{
						int i = r * colCount + c;
						if (i < componentCount)
						{
							int remainder = componentCount - (r * colCount);
							if (remainder < colCount)
							{
								float amount = (colCount - remainder) / 2.0f;
								int rightPad = (int) (amount * w + amount * this.hgap);
								parent.getComponent(i).setBounds(x - rightPad, y, w, h);
							}
							else
							{
								parent.getComponent(i).setBounds(x, y, w, h);
							}
						}
					}
				}
			}
		}
	}
}
