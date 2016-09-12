/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

/**
 * <code>IGSCalendarIcon</code> is an <code>Icon</code> of a calendar.
 * @author The External Fulfillment Client Development Team
 */
public class IGSCalendarIcon
	extends IGSAccessibleIcon
{
	private static final long serialVersionUID = 1L;
	/** The background <code>color</code>. */
	public Color background = Color.white;

	/** The foreground <code>color</code>. */
	public Color foreground = Color.black;

	/** The highlight <code>color</code>. */
	public Color highlight = Color.blue;

	/** Constructs a new <code>IGSCalendarIcon</code>. */
	public IGSCalendarIcon()
	{
		super(16, 15);
	}

	/**
	 * Paints the <code>IGSCalendarIcon</code> with the top-left corner drawn
	 * at the point (x, y) in the coordinate space of the specified
	 * <code>Graphics</code> context <code>g</code>.
	 * @param c a <code>Component</code> used to get properties useful for
	 *        painting (e.g., foreground or background color)
	 * @param g the <code>Graphics</code> used to paint the
	 *        <code>IGSAccessibleIcon</code>
	 * @param x the x coordinate of the <code>IGSAccessibleIcon</code>'s
	 *        top-left corner
	 * @param y the y coordinate of the <code>IGSAccessibleIcon</code>'s
	 *        top-left corner
	 */
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Color darker = this.background.getRGB() > this.foreground.getRGB() 
			? this.background.darker() 
			: this.foreground.darker();
		Color savedColor = g.getColor();
		g.translate(x, y);
		g.setColor(this.foreground);
		g.drawLine(15, 0, 15, 13); //right border
		g.drawLine(0, 14, 15, 14); //bottom border
		g.setColor(darker.darker());
		g.drawLine(0, 0, 0, 13); //left border
		g.drawLine(0, 0, 14, 0); //top border
		g.fillRect(8, 1, 7, 2); //2 top half lines
		g.drawLine(0, 4, 0, 14);
		g.setColor(this.highlight);
		g.fillRect(1, 1, 7, 2); //other half of 2 top half lines
		g.setColor(this.background);
		g.fillRect(1, 3, 14, 11); //calendar background

		g.setColor(darker);
		for (int i = 5; i <= 11; i += 3)
		{
			g.drawLine(1, i, 14, i); //calendar horizontal grid lines
		}
		for (int i = 3; i <= 12; i += 3)
		{
			g.drawLine(i, 3, i, 13); //calendar vertical grid lines
		}
		g.setColor(this.highlight); //calendar selected cell
		g.drawRect(9, 6, 3, 3);
		g.translate(-x, -y);
		g.setColor(savedColor);
	}
}
