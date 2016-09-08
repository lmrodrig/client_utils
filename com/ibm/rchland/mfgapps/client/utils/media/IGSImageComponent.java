/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-01-19      39619JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.media;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * <code>IGSImageComponent</code> is a subclass of <code>JComponent</code>
 * that can display an image the user can zoom.
 * @author The Process Profile Client Development Team
 */
public class IGSImageComponent
	extends JComponent
{
	private static final long serialVersionUID = 1L;
	/** The key for the property that indicates the image changed. */
	public static final String IMAGE_PROPERTY = "image"; //$NON-NLS-1$

	/** The key for the property that indicates the max image scale changed. */
	public static final String MAX_SCALE_PROPERTY = "maxScale"; //$NON-NLS-1$

	/** The key for the property that indicates the image scale changed. */
	public static final String SCALE_PROPERTY = "scale"; //$NON-NLS-1$

	/** The key for the property that indicates the zoom factor changed. */
	public static final String ZOOM_FACTOR_PROPERTY = "zoomFactor"; //$NON-NLS-1$

	/** The <code>BufferedImage</code> to display. */
	private BufferedImage image = null;

	/** The maximum scale used to display {@link #image}. */
	private double maxScale = 4;

	/** The <code>RenderingHints</code>. */
	private final RenderingHints renderingHints = new RenderingHints(null);

	/** The current scale used to display {@link #image}. */
	private double scale = 1;

	/**
	 * The multiplicative factor used by {@link #zoomIn()} and
	 * {@link #zoomOut()} to change {@link #scale}.
	 */
	private double zoomFactor = 1.2;

	/**
	 * Constructs a new <code>IGSImageComponent</code>.
	 * @param image the <code>BufferedImage</code> to display
	 */
	public IGSImageComponent(BufferedImage image)
	{
		super();
		setOpaque(true);
		setImage(image);
	}

	/**
	 * Paints this <code>IGSImageComponent</code>.
	 * @param g the <code>Graphics</code> context
	 */
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		if (isOpaque())
		{
			g2d.setColor(this.getBackground());
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		Rectangle bounds = g2d.getClipBounds();
		int dx1 = bounds.x;
		int dx2 = bounds.x + bounds.width;
		int dy1 = bounds.y;
		int dy2 = bounds.y + bounds.height;
		int sx1 = (int) (dx1 / this.scale);
		int sx2 = (int) (dx2 / this.scale);
		int sy1 = (int) (dy1 / this.scale);
		int sy2 = (int) (dy2 / this.scale);
		RenderingHints hints = g2d.getRenderingHints();
		hints.add(this.renderingHints);
		g2d.setRenderingHints(hints);
		g2d.drawImage(this.image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
		g2d.dispose();
	}

	/**
	 * Sets the <code>BufferedImage</code> that is displayed.
	 * @param image the <code>BufferedImage</code> to display
	 */
	public void setImage(BufferedImage image)
	{
		BufferedImage oldImage = image;
		this.image = image;
		firePropertyChange(IMAGE_PROPERTY, oldImage, this.image);
		setScale(1);
	}

	/**
	 * Sets the maximum scale used to display the <code>BufferedImage</code>.
	 * The new value must be a positive number or the maximum scale is not
	 * changed.
	 * @param maxScale the maximum scale used to display the image
	 */
	public void setMaxScale(double maxScale)
	{
		if (maxScale > 0)
		{
			double oldMaxSacle = this.maxScale;
			this.maxScale = maxScale;
			firePropertyChange(MAX_SCALE_PROPERTY, oldMaxSacle, this.maxScale);

			if (this.maxScale < this.scale)
			{
				setScale(this.maxScale);
			}
		}
	}

	/**
	 * Sets the preferred size of this component.
	 * @param size the preferred size of the component
	 */
	public void setPreferredSize(Dimension size)
	{
		if (size != null)
		{
			double imgWidth = this.image.getWidth();
			double imgHeight = this.image.getHeight();
			setScaleImpl(Math.min(size.width / imgWidth, size.height / imgHeight));
			revalidate();
		}
		super.setPreferredSize(size);
	}

	/**
	 * Sets the value of a single preference for the rendering algorithms.
	 * @param key the rendering hint key
	 * @param value the rendering hint value
	 * @return the previous value of the specified key or <code>null</code> if
	 *         the key did not have a previous value
	 * @see RenderingHints
	 */
	public Object setRenderingHint(Key key, Object value)
	{
		Object result = this.renderingHints.put(key, value);
		if (isShowing())
		{
			repaint();
		}
		return result;
	}

	/**
	 * Sets the scale used to display the <code>BufferedImage</code>. The new
	 * value must be a positive number or the scale is not changed.
	 * @param scale the new scale used to display the image
	 */
	public void setScale(double scale)
	{
		if (scale > 0)
		{
			setScaleImpl(scale);
			int width = (int) Math.round(this.image.getWidth() * this.scale);
			int height = (int) Math.round(this.image.getHeight() * this.scale);
			super.setPreferredSize(new Dimension(width, height));

			revalidate();
			if (isVisible())
			{
				repaint();
			}
		}
	}

	/**
	 * Sets the scale used to display the image.
	 * @param scale the new scale used to display the image
	 */
	private void setScaleImpl(double scale)
	{
		double oldScale = this.scale;
		this.scale = Math.min(scale, this.maxScale);
		firePropertyChange(SCALE_PROPERTY, oldScale, this.scale);
	}

	/**
	 * Sets the zoom factor (the multiplicative factor used by {@link #zoomIn()}
	 * and {@link #zoomOut()} to change the scale). The new value must be
	 * greater than 1 and less than or equal to 2 or the value of the zoom
	 * factor is not changed.
	 * @param zoomFactor the multiplicative factor used by {@link #zoomIn()} and
	 *        {@link #zoomOut()} to change the scale
	 */
	public void setZoomFactor(double zoomFactor)
	{
		if (zoomFactor > 1 && zoomFactor <= 2)
		{
			double oldZoomFactor = this.zoomFactor;
			this.zoomFactor = zoomFactor;
			firePropertyChange(ZOOM_FACTOR_PROPERTY, oldZoomFactor, this.zoomFactor);
		}
	}

	/**
	 * Increases the scale to zooms in on the image. This method will not allow
	 * the {@link #scale scale} to exceed {@link #maxScale maxScale} (which can
	 * be changed via {@link #setMaxScale(double)}).
	 */
	public void zoomIn()
	{
		setScale(this.scale * this.zoomFactor);
	}

	/** Decreases the scale to zooms out on the image. */
	public void zoomOut()
	{
		setScale(this.scale / this.zoomFactor);
	}
}
