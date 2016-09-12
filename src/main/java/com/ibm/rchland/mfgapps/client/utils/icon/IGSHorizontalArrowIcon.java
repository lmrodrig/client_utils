/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 * 2006-09-22   ~1 35394PL  R Prechel        -Extend IGSAccessibleIcon
 *                                           -Removed getIconWidth and getIconHeight
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

/**
 * <code>IGSHorizontalArrowIcon</code> is a subclass of <code>Icon</code>
 * used to draw a left or right facing arrow head.
 * @author The External Fulfillment Client Development Team
 */
public class IGSHorizontalArrowIcon
	extends IGSAccessibleIcon
{
	private static final long serialVersionUID = 1L;
	/** The x points used to draw/fill the <code>IGSHorizontalArrowIcon</code>. */
	private int[] xArray;

	/** The y points used to draw/fill the <code>IGSHorizontalArrowIcon</code>. */
	private int[] yArray;

	/**
	 * Constructs a new <code>IGSHorizontalArrowIcon</code>.
	 * @param size the size of the <code>IGSHorizontalArrowIcon</code>. The
	 *        height of an <code>IGSHorizontalArrowIcon</code> is equal to
	 *        <code>size</code> and the width of an
	 *        <code>IGSHorizontalArrowIcon</code> is equal to
	 *        <code>size/2</code>.
	 * @param left <code>true</code> if the
	 *        <code>IGSHorizontalArrowIcon</code> points left;
	 *        <code>false</code> if the <code>IGSHorizontalArrowIcon</code>
	 *        points right
	 */
	public IGSHorizontalArrowIcon(int size, boolean left)
	{
		super(size / 2, size);
		int width = size / 2; //~1C changed from class variable to local
		// variable
		int height = size; //~1C changed from class variable to local variable

		this.xArray = new int[] {(left ? width : 0), (left ? width : 0), (left ? 0 : width)};
		this.yArray = new int[] {0, height, width};
	}

	/**
	 * Paints the <code>IGSHorizontalArrowIcon</code> with the top-left corner
	 * drawn at the point (x, y) in the coordinate space of the specified
	 * <code>Graphics</code> context <code>g</code>.
	 * @param c a <code>Component</code> used to get properties useful for
	 *        painting (e.g., foreground or background color)
	 * @param g the <code>Graphics</code> used to paint the
	 *        <code>IGSHorizontalArrowIcon</code>
	 * @param x the x coordinate of the <code>IGSHorizontalArrowIcon</code>'s
	 *        top-left corner
	 * @param y the y coordinate of the <code>IGSHorizontalArrowIcon</code>'s
	 *        top-left corner
	 */
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Color savedColor = g.getColor();
		g.translate(x, y);

		g.setColor(c == null ? Color.BLACK : c.getForeground());
		g.fillPolygon(this.xArray, this.yArray, 3);

		g.translate(-x, -y);
		g.setColor(savedColor);
	}
}
