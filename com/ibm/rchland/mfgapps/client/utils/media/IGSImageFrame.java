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

import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * <code>IGSImageFrame</code> is a subclass of <code>JFrame</code> used to
 * display an {@link IGSImageComponent}.
 * @author The Process Profile Client Development Team
 */
public class IGSImageFrame
	extends JFrame
{
	private static final long serialVersionUID = 1L;
	/** The list of <code>IGSImageFrame</code>s that have not been closed. */
	@SuppressWarnings("rawtypes")
	private static final Vector imageFrames = new Vector();

	/** The <code>IGSImageComponent</code>. */
	private IGSImageComponent imageComponent;

	/** The <code>JScrollPane</code> used to display {@link #imageComponent}. */
	private JScrollPane scrollPane;

	/** The <code>JButton</code> the user can click to zoom in on the image. */
	private JButton pbZoomIn = null;

	/** The <code>JButton</code> the user can click to zoom out on the image. */
	private JButton pbZoomOut = null;

	/** Indicates if the image is resized to fit the available area. */
	private boolean resizeImageToFitArea = false;

	/**
	 * Constructs a new <code>IGSImageFrame</code>.
	 * @param title the title for the frame
	 * @param image the image to display
	 * @param max the maximum number of <code>IGSImageFrame</code>s that can
	 *        be open
	 * @param change <code>true</code> if the menu bar should allow the user
	 *        to change the picture that is currently displayed
	 * @return the <code>IGSImageFrame</code> or <code>null</code> if the
	 *         maximum number of <code>IGSImageFrame</code>s has been reached
	 */
	public static IGSImageFrame create(String title, BufferedImage image, int max,
										boolean change)
	{
		synchronized (imageFrames)
		{
			if (imageFrames.size() >= max)
			{
				return null;
			}

			BufferedImage compatibleImage = toCompatibleImage(image);
			IGSImageComponent imageComponent = new IGSImageComponent(compatibleImage);
			return new IGSImageFrame(title, imageComponent, change);
		}
	}

	/**
	 * Returns a <code>BufferedImage</code> based on the specified
	 * <code>image</code> that is compatible with the graphics hardware. The
	 * specified <code>image</code> is returned if it is compatible with the
	 * graphics hardware or the environment is headless.
	 * @param image a <code>BufferedImage</code>
	 * @return the compatible <code>BufferedImage</code>
	 */
	public static BufferedImage toCompatibleImage(BufferedImage image)
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return image;
		}

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();
		GraphicsConfiguration config = dev.getDefaultConfiguration();
		int transparency = image.getTransparency();
		if (image.getColorModel().equals(config.getColorModel(transparency)))
		{
			return image;
		}

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage result = config.createCompatibleImage(width, height, transparency);
		Graphics g = result.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return result;
	}

	/**
	 * Constructs a new <code>IGSImageFrame</code>.
	 * @param title the title for the frame
	 * @param imageComponent the <code>IGSImageComponent</code> to display
	 * @param change <code>true</code> if the menu bar should allow the user
	 *        to change the picture that is currently displayed
	 */
	@SuppressWarnings("unchecked")
	private IGSImageFrame(String title, IGSImageComponent imageComponent, boolean change)
	{
		super(title);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.imageComponent = imageComponent;
		createLayout();
		setJMenuBar(createMenuBar(change));
		imageFrames.add(this);
	}

	/** Adds this frame's components to its layout. */
	private void createLayout()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		this.scrollPane = new JScrollPane(this.imageComponent);
		contentPane.add(this.scrollPane, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = .5;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		this.pbZoomOut = new JButton(new IGSImageAction(this.imageComponent, "zoomOut")); //$NON-NLS-1$
		contentPane.add(this.pbZoomOut, gbc);

		gbc.gridx++;
		gbc.anchor = GridBagConstraints.EAST;

		this.pbZoomIn = new JButton(new IGSImageAction(this.imageComponent, "zoomIn")); //$NON-NLS-1$
		contentPane.add(this.pbZoomIn, gbc);
	}

	/**
	 * Creates the <code>JMenuBar</code> for the <code>IGSImageFrame</code>.
	 * @param change <code>true</code> if the menu bar should allow the user
	 *        to change the picture that is currently displayed
	 * @return the newly created <code>JMenuBar</code>
	 */
	private JMenuBar createMenuBar(boolean change)
	{
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu(new IGSImageAction(this.imageComponent, "image")); //$NON-NLS-1$
		menuBar.add(menu);
		if (change)
		{
			menu.add(new JMenuItem(new IGSImageAction(this.imageComponent, "changeImage"))); //$NON-NLS-1$
		}
		menu.add(new JMenuItem(new IGSImageAction(this.imageComponent, "changeImageScale"))); //$NON-NLS-1$
		menu.add(new JMenuItem(new IGSImageAction(this.imageComponent, "changeZoomFactor"))); //$NON-NLS-1$

		menu = new JMenu(new IGSImageAction(this.imageComponent, "renderingHints")); //$NON-NLS-1$
		menuBar.add(menu);

		ButtonGroup group = new ButtonGroup();
		JMenu subMenu = new JMenu(new IGSImageAction(this.imageComponent, "ANTIALIASING")); //$NON-NLS-1$
		menu.add(subMenu);
		addRadioMenuItem(subMenu, group, true, "ANTIALIAS_DEFAULT"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "ANTIALIAS_OFF"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "ANTIALIAS_ON"); //$NON-NLS-1$
		this.imageComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_DEFAULT);

		group = new ButtonGroup();
		subMenu = new JMenu(new IGSImageAction(this.imageComponent, "COLOR_RENDERING")); //$NON-NLS-1$
		menu.add(subMenu);
		addRadioMenuItem(subMenu, group, true, "COLOR_RENDER_DEFAULT"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "COLOR_RENDER_SPEED"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "COLOR_RENDER_QUALITY"); //$NON-NLS-1$
		this.imageComponent.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_DEFAULT);

		group = new ButtonGroup();
		subMenu = new JMenu(new IGSImageAction(this.imageComponent, "DITHERING")); //$NON-NLS-1$
		menu.add(subMenu);
		addRadioMenuItem(subMenu, group, true, "DITHER_DEFAULT"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "DITHER_DISABLE"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "DITHER_ENABLE"); //$NON-NLS-1$
		this.imageComponent.setRenderingHint(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_DEFAULT);

		group = new ButtonGroup();
		subMenu = new JMenu(new IGSImageAction(this.imageComponent, "INTERPOLATION")); //$NON-NLS-1$
		menu.add(subMenu);
		addRadioMenuItem(subMenu, group, false, "INTERPOLATION_BICUBIC"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "INTERPOLATION_BILINEAR"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, true, "INTERPOLATION_NEAREST_NEIGHBOR"); //$NON-NLS-1$
		this.imageComponent.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		group = new ButtonGroup();
		subMenu = new JMenu(new IGSImageAction(this.imageComponent, "RENDERING")); //$NON-NLS-1$
		menu.add(subMenu);
		addRadioMenuItem(subMenu, group, true, "RENDER_DEFAULT"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "RENDER_SPEED"); //$NON-NLS-1$
		addRadioMenuItem(subMenu, group, false, "RENDER_QUALITY"); //$NON-NLS-1$
		this.imageComponent.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_DEFAULT);

		return menuBar;
	}

	/**
	 * Creates and adds a new <code>JRadioButtonMenuItem</code> to the
	 * specified <code>JMenu</code> and <code>ButtonGroup</code>.
	 * @param menu a <code>JMenu</code>
	 * @param group a <code>ButtonGroup</code>
	 * @param selected <code>true</code> if the new
	 *        <code>JRadioButtonMenuItem</code> should be selected;
	 *        <code>false</code> if the new <code>JRadioButtonMenuItem</code>
	 *        should not be selected
	 * @param actionCommand the action command for the <code>Action</code>
	 *        used to create the <code>JRadioButtonMenuItem</code>
	 */
	private void addRadioMenuItem(JMenu menu, ButtonGroup group, boolean selected,
									String actionCommand)
	{
		Action a = new IGSImageAction(this.imageComponent, actionCommand);
		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(a);
		group.add(menuItem);
		menu.add(menuItem);
		menuItem.setSelected(selected);
	}

	/**
	 * Sets the size of this <code>IGSImageFrame</code> to display the image
	 * at normal size unless there is not enough screen space.
	 */
	public void autoSizeFrame()
	{
		pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		GraphicsConfiguration configuration = getGraphicsConfiguration();
		Rectangle bounds = configuration.getBounds();
		Insets insets = toolkit.getScreenInsets(configuration);
		Dimension size = this.getSize();
		int width = Math.min(size.width, bounds.width - insets.left - insets.right);
		int height = Math.min(size.height, bounds.height - insets.top - insets.bottom);
		this.setSize(new Dimension(width, height));
	}

	/**
	 * Sets whether the image is resized to fit the available area when the size
	 * of the <code>IGSImageComponent</code> changes.
	 * @param resize <code>true</code> if the image should be resized;
	 *        <code>false</code> if the image should not be resized.
	 */
	public void setResizeImageToFitArea(boolean resize)
	{
		this.resizeImageToFitArea = resize;
		if (this.resizeImageToFitArea)
		{
			enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
			if (isVisible())
			{
				Insets insets = this.scrollPane.getInsets();
				int width = this.scrollPane.getWidth() - insets.left - insets.right;
				int height = this.scrollPane.getHeight() - insets.top - insets.bottom;
				this.imageComponent.setPreferredSize(new Dimension(width, height));
			}
		}
		else
		{
			disableEvents(AWTEvent.COMPONENT_EVENT_MASK);
		}
	}

	/**
	 * Processes <code>ComponentEvent</code>s occurring on this component.
	 * @param ce the <code>ComponentEvent</code>
	 */
	protected void processComponentEvent(ComponentEvent ce)
	{
		final int id = ce.getID();
		if (id == ComponentEvent.COMPONENT_RESIZED
				|| id == ComponentEvent.COMPONENT_SHOWN)
		{
			if (this.resizeImageToFitArea)
			{
				Insets insets = this.scrollPane.getInsets();
				int width = this.scrollPane.getWidth() - insets.left - insets.right;
				int height = this.scrollPane.getHeight() - insets.top - insets.bottom;
				this.imageComponent.setPreferredSize(new Dimension(width, height));
			}
		}
		super.processComponentEvent(ce);
	}

	/**
	 * Processes <code>WindowEvent</code>s occurring on this component.
	 * @param we the <code>WindowEvent</code>
	 */
	protected void processWindowEvent(WindowEvent we)
	{
		super.processWindowEvent(we);
		if (we.getID() == WindowEvent.WINDOW_CLOSED)
		{
			imageFrames.remove(we.getWindow());
		}
	}
}