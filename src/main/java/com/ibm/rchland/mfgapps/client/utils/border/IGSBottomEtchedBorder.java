/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;

/**
 * <code>IGSBottomEtchedBorder</code> renders a raised or lowered etched
 * <code>Border</code> along the bottom of a <code>Component</code>.
 * @author The External Fulfillment Client Development Team
 */
public class IGSBottomEtchedBorder
	implements Border, Serializable
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** Lowered etched type. */
	public static final int LOWERED = 1;

	/** Raised etched type. */
	public static final int RAISED = 0;

	/** The etched type of this border. */
	private boolean etched;

	/** Constructs a new lowered <code>IGSBottomEtchedBorder</code>. */
	public IGSBottomEtchedBorder()
	{
		this.etched = true;
	}

	/**
	 * Constructs a new <code>IGSBottomEtchedBorder</code> with the specified
	 * etched <code>type</code>.
	 * @param type the type of etch to be drawn by the <code>Border</code>
	 */
	public IGSBottomEtchedBorder(int type)
	{
		this.etched = (type == LOWERED);
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
		Color savedColor = g.getColor();
		Color background = c.getBackground();
		g.translate(x, y);
		g.setColor(this.etched ? background.darker() : background.brighter());
		g.drawLine(0, height - 1, width - 1, height - 1);
		g.setColor(this.etched ? background.brighter() : background.darker());
		g.drawLine(0, height - 2, width - 1, height - 2);
		g.translate(-x, -y);
		g.setColor(savedColor);
	}

	/**
	 * Returns the <code>Insets</code> of the <code>Border</code>.
	 * @param c the <code>Component</code> for which the <code>Insets</code>
	 *        value applies
	 * @return the <code>Insets</code>
	 */
	public Insets getBorderInsets(Component c)
	{
		return new Insets(0, 0, 2, 0);
	}

	/**
	 * Returns whether or not the <code>Border</code> is opaque. An opaque
	 * <code>Border</code> is responsible for filling in its own background
	 * when painting.
	 * @return <code>true</code>
	 */
	public boolean isBorderOpaque()
	{
		return true;
	}
}
