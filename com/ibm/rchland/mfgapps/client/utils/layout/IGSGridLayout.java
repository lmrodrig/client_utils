/* © Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15      34242JR  R Prechel        -Initial version
 * 2008-01-11   ~1 39619JL  R Prechel        -Change private to protected
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/**
 * <code>IGSGridLayout</code> is a variant of <code>GridLayout</code> that
 * centers the <code>Component</code>s in the last row if there are fewer
 * <code>Component</code>s than columns.
 * @author The MFS Client Development Team
 */
public class IGSGridLayout
	implements LayoutManager, Serializable
{
	private static final long serialVersionUID = 1L;
	/** The horizontal gap (in pixels) between columns. */
	protected final int hgap; //~1C

	/** The vertical gap (in pixels) between columns. */
	protected final int vgap; //~1C

	/** The number of rows in the grid. */
	protected final int rows; //~1C

	/** The number of columns in the grid. */
	protected final int cols; //~1C

	/**
	 * Constructs a new <code>IGSGridLayout</code> with the specified number
	 * of rows and columns. All components in the grid layout are given equal
	 * size. The components in the last row are centered if there are fewer
	 * components than columns.
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
	public IGSGridLayout(int rows, int cols, int hgap, int vgap)
	{
		if ((rows <= 0) && (cols <= 0))
		{
			throw new IllegalArgumentException("rows and cols cannot both be nonpositive"); //$NON-NLS-1$
		}
		if ((hgap < 0) || (vgap < 0))
		{
			throw new IllegalArgumentException("hgap and vgap cannot be negative"); //$NON-NLS-1$
		}
		this.rows = rows;
		this.cols = cols;
		this.hgap = hgap;
		this.vgap = vgap;
	}

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

	/**
	 * Calculates the number of rows and columns in the grid used to determine
	 * the preferred and minimum layout size.
	 * @param parent the component to be laid out
	 * @return an int array containing the maximum number of rows and columns
	 */
	protected int[] calculateGridSize(Container parent) //~1C
	{
		final int componentCount = parent.getComponentCount();
		int rowCount = this.rows;
		int colCount = this.cols;
		if (rowCount > 0)
		{
			//Determine the minimum number of columns required
			//to display all components using rowCount rows.
			colCount = (componentCount + rowCount - 1) / rowCount;

			//colCount should be at most this.cols unless this.cols == 0
			if (colCount < this.cols)
			{
				colCount = this.cols;
			}
			//Increase the number of rows if necessary
			else if (this.cols > 0 && colCount > this.cols)
			{
				colCount = this.cols;
				rowCount = (componentCount + colCount - 1) / colCount;
			}
		}
		else
		{
			//rowCount is nonpositive so colCount is positive.
			//Determine the minimum number of rows required
			//to display all components using colCount columns.
			rowCount = (componentCount + colCount - 1) / colCount;
		}
		return new int[] {rowCount, colCount};
	}

	/** {@inheritDoc} */
	public Dimension preferredLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			final int componentCount = parent.getComponentCount();
			final int[] dimensions = calculateGridSize(parent);
			final int rowCount = dimensions[0];
			final int colCount = dimensions[1];

			//Determine the largest preferred width and height
			int w = 0;
			int h = 0;
			for (int i = 0; i < componentCount; i++)
			{
				Component comp = parent.getComponent(i);
				Dimension d = comp.getPreferredSize();
				if (w < d.width)
				{
					w = d.width;
				}
				if (h < d.height)
				{
					h = d.height;
				}
			}

			Insets insets = parent.getInsets();
			int width = insets.left + insets.right + (colCount * w)
					+ ((colCount - 1) * this.hgap);
			int height = insets.top + insets.bottom + (rowCount * h)
					+ ((rowCount - 1) * this.vgap);
			return new Dimension(width, height);
		}
	}

	/** {@inheritDoc} */
	public Dimension minimumLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			final int componentCount = parent.getComponentCount();
			final int[] dimensions = calculateGridSize(parent);
			final int rowCount = dimensions[0];
			final int colCount = dimensions[1];

			//Determine the largest minimum width and height
			int w = 0;
			int h = 0;
			for (int i = 0; i < componentCount; i++)
			{
				Component comp = parent.getComponent(i);
				Dimension d = comp.getMinimumSize();
				if (w < d.width)
				{
					w = d.width;
				}
				if (h < d.height)
				{
					h = d.height;
				}
			}

			Insets insets = parent.getInsets();
			int width = insets.left + insets.right + (colCount * w)
					+ ((colCount - 1) * this.hgap);
			int height = insets.top + insets.bottom + (rowCount * h)
					+ ((rowCount - 1) * this.vgap);
			return new Dimension(width, height);
		}
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

			//Determine the largest minimum width and height
			int w = parent.getWidth() - (insets.left + insets.right);
			int h = parent.getHeight() - (insets.top + insets.bottom);

			w = (w - (colCount - 1) * this.hgap) / colCount;
			h = (h - (rowCount - 1) * this.vgap) / rowCount;

			if (parent.getComponentOrientation().isLeftToRight())
			{
				for (int c = 0, x = insets.left; c < colCount; c++, x += w + this.hgap)
				{
					for (int r = 0, y = insets.top; r < rowCount; r++, y += h + this.vgap)
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
				//The x position for the rightmost grid component
				int x = parent.getWidth() - insets.right - w;
				for (int c = 0; c < colCount; c++, x -= w + this.hgap)
				{
					for (int r = 0, y = insets.top; r < rowCount; r++, y += h + this.vgap)
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
