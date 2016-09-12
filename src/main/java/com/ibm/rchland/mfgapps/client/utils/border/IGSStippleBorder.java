/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.io.Serializable;

import javax.swing.border.Border;

/**
 * <code>IGSStippleBorder</code> renders a line stipple <code>Border</code>.
 * @author The External Fulfillment Client Development Team
 */
public class IGSStippleBorder
	implements Border, Serializable
{
	/** Identifies the original class version for which this class 
	 * is capable of writing streams and from which it can read. */
	private static final long serialVersionUID = 1L;

	/** The <code>Stroke</code> used to draw the line stipple. */
	private static final Stroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 1.0f, new float[] {1.0f}, 0.0f);

	/** The thickness of the <code>Border</code>. */
	private int thickness;

	/**
	 * Constructs a new <code>IGSStippleBorder</code>.
	 * @param thickness the thickness of the <code>Border</code>
	 */
	public IGSStippleBorder(int thickness)
	{
		this.thickness = thickness;
	}

	/**
	 * Paints the <code>Border</code> for the specified <code>Component</code>
	 * with the specified position and size.
	 * @param c the <code>Component</code> for which this <code>Border</code>
	 *        is being painted
	 * @param g the paint <code>Graphics</code>
	 * @param x the x position of the painted <code>Border</code>
	 * @param y the y position of the painted <code>Border</code>
	 * @param width the width of the painted <code>Border</code>
	 * @param height the height of the painted <code>Border</code>
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		Color oldColor = g.getColor();
		g.setColor(c.getForeground());

		if (g instanceof Graphics2D)
		{
			Graphics2D g2d = (Graphics2D) g;
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(stroke);
			for (int i = 0; i < this.thickness; i++)
			{
				g.drawRect(x + i, y + i, width - i - i - 1, height - i - i - 1);
			}
			g2d.setStroke(oldStroke);
		}
		else
		{
			for (int i = 0; i < this.thickness; i++)
			{
				g.drawRect(x + i, y + i, width - i - i - 1, height - i - i - 1);
			}
		}
		g.setColor(oldColor);
	}

	/**
	 * Returns the <code>Insets</code> of the <code>Border</code>.
	 * @param c the <code>Component</code> for which the <code>Insets</code>
	 *        value applies
	 * @return the <code>Insets</code>
	 */
	public Insets getBorderInsets(Component c)
	{
		return new Insets(this.thickness, this.thickness, this.thickness, this.thickness);
	}

	/**
	 * Returns whether or not the <code>Border</code> is opaque. An opaque
	 * <code>Border</code> is responsible for filling in its own background
	 * when painting.
	 * @return <code>false</code>
	 */
	public boolean isBorderOpaque()
	{
		return false;
	}
}
