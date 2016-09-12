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
 * <code>IGSVerticalArrowIcon</code> is a subclass of <code>Icon</code> used
 * to draw an up or down facing arrow head.
 * @author The External Fulfillment Client Development Team
 */
public class IGSVerticalArrowIcon
	extends IGSAccessibleIcon
{
	private static final long serialVersionUID = 1L;
	/**
	 * <code>true</code> if the <code>IGSVerticalArrowIcon</code> points up;
	 * <code>false</code> if the <code>IGSVerticalArrowIcon</code> points
	 * down.
	 */
	private boolean ascending;

	/**
	 * Constructs a new <code>IGSVerticalArrowIcon</code>.
	 * @param size the size of the <code>IGSVerticalArrowIcon</code>. Both
	 *        the height and width of the <code>IGSVerticalArrowIcon</code>
	 *        are equal to <code>size</code>.
	 * @param ascending <code>true</code> if the
	 *        <code>IGSVerticalArrowIcon</code> points up; <code>false</code>
	 *        if the <code>IGSVerticalArrowIcon</code> points down
	 */
	public IGSVerticalArrowIcon(int size, boolean ascending)
	{
		super(size, size);
		this.ascending = ascending;
	}

	/**
	 * Paints the <code>IGSVerticalArrowIcon</code> with the top-left corner
	 * drawn at the point (x, y) in the coordinate space of the specified
	 * <code>Graphics</code> context <code>g</code>.
	 * @param c a <code>Component</code> used to get properties useful for
	 *        painting (e.g., foreground or background color)
	 * @param g the <code>Graphics</code> used to paint the
	 *        <code>IGSVerticalArrowIcon</code>
	 * @param x the x coordinate of the <code>IGSVerticalArrowIcon</code>'s
	 *        top-left corner
	 * @param y the y coordinate of the <code>IGSVerticalArrowIcon</code>'s
	 *        top-left corner
	 */
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Color savedColor = g.getColor();
		Color color = (c == null ? Color.GRAY : c.getBackground());

		//Setup width, height, and location of arrow
		int size = getIconHeight(); //~1A
		int width = size / 2;
		int height = (this.ascending ? -width : width);
		y += (size * 5 / 6) + (this.ascending ? 0 : -height);
		g.translate(x, y);

		//Draw right diagonal
		g.setColor(color.darker());
		g.drawLine(width / 2, height, 0, 0);

		//Draw left diagonal
		g.setColor(color.brighter());
		g.drawLine(width / 2, height, width, 0);

		//Draw horizontal line
		g.setColor((this.ascending ? color.brighter() : color.darker()));
		g.drawLine(width, 0, 0, 0);

		//Undo changes to graphics
		g.translate(-x, -y);
		g.setColor(savedColor);
	}
}
