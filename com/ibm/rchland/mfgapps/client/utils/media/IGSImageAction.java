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

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.ibm.rchland.mfgapps.client.utils.IGSI18N;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;

/**
 * <code>IGSImageAction</code> is a subclass of <code>AbstractAction</code>
 * used by {@link com.ibm.rchland.mfgapps.client.utils.media.IGSImageFrame}.
 * @author The Process Profile Client Development Team
 */
public class IGSImageAction
	extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	/** The <code>ResourceBundle</code> used for localized messages. */
	private static final ResourceBundle bundle = IGSI18N.getBundle(IGSImageAction.class);

	/** The <code>IGSImageComponent</code>. */
	private final IGSImageComponent imageComponent;

	/**
	 * Constructs a new <code>IGSImageAction</code>.
	 * @param component the <code>IGSImageComponent</code>
	 * @param actionCommand the action command for this <code>Action</code>
	 */
	public IGSImageAction(IGSImageComponent component, String actionCommand)
	{
		super();
		this.imageComponent = component;
		lookupAndPutValue(actionCommand, ACCELERATOR_KEY);
		putValue(ACTION_COMMAND_KEY, actionCommand);
		lookupAndPutValue(actionCommand, MNEMONIC_KEY);
		lookupAndPutValue(actionCommand, NAME);
		lookupAndPutValue(actionCommand, SHORT_DESCRIPTION);
	}

	/**
	 * Retrieves the value from the {@link ResourceBundle} that corresponds to
	 * the specified <code>actionCommand</code> and <code>key</code> and
	 * associates the value with the specified <code>key</code> via the
	 * {@link AbstractAction#putValue(String, Object)} method.
	 * @param actionCommand the action command for this <code>Action</code>
	 * @param key one of the <code>Action</code> keys
	 * @see #ACCELERATOR_KEY
	 * @see #MNEMONIC_KEY
	 * @see #NAME
	 * @see #SHORT_DESCRIPTION
	 */
	private void lookupAndPutValue(String actionCommand, String key)
	{
		try
		{
			String value = bundle.getString(actionCommand + '_' + key);
			if (value != null)
			{
				if (ACCELERATOR_KEY == key)
				{
					putValue(key, KeyStroke.getKeyStroke(value));
				}
				else if (MNEMONIC_KEY == key)
				{
					putValue(key, new Integer(value.charAt(0)));
				}
				else
				{
					putValue(key, value);
				}
			}
		}
		catch (MissingResourceException mre)
		{
			if (key != ACCELERATOR_KEY && key != MNEMONIC_KEY)
			{
				throw mre;
			}
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final String actionCommand = ae.getActionCommand();
		if (actionCommand.equals("zoomIn")) //$NON-NLS-1$
		{
			this.imageComponent.zoomIn();
		}
		else if (actionCommand.equals("zoomOut")) //$NON-NLS-1$
		{
			this.imageComponent.zoomOut();
		}
		else if (actionCommand.equals("changeImage")) //$NON-NLS-1$
		{
			changeImage();
		}
		else if (actionCommand.equals("changeImageScale")) //$NON-NLS-1$
		{
			changeImageScale();
		}
		else if (actionCommand.equals("changeZoomFactor")) //$NON-NLS-1$
		{
			changeZoomFactor();
		}
		else if (actionCommand.equals("ANTIALIAS_DEFAULT")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		}
		else if (actionCommand.equals("ANTIALIAS_OFF")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		else if (actionCommand.equals("ANTIALIAS_ON")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		else if (actionCommand.equals("COLOR_RENDER_DEFAULT")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
		}
		else if (actionCommand.equals("COLOR_RENDER_QUALITY")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		}
		else if (actionCommand.equals("COLOR_RENDER_SPEED")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_SPEED);
		}
		else if (actionCommand.equals("DITHER_DEFAULT")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_DITHERING,
					RenderingHints.VALUE_DITHER_DEFAULT);
		}
		else if (actionCommand.equals("DITHER_DISABLE")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_DITHERING,
					RenderingHints.VALUE_DITHER_DISABLE);
		}
		else if (actionCommand.equals("DITHER_ENABLE")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_DITHERING,
					RenderingHints.VALUE_DITHER_ENABLE);
		}
		else if (actionCommand.equals("INTERPOLATION_BICUBIC")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		else if (actionCommand.equals("INTERPOLATION_BILINEAR")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}
		else if (actionCommand.equals("INTERPOLATION_NEAREST_NEIGHBOR")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
		else if (actionCommand.equals("RENDER_DEFAULT")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_DEFAULT);
		}
		else if (actionCommand.equals("RENDER_QUALITY")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
		}
		else if (actionCommand.equals("RENDER_SPEED")) //$NON-NLS-1$
		{
			this.imageComponent.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_SPEED);
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Displays a <code>JFileChooser</code> from which the user can select the
	 * image that is displayed.
	 */
	private void changeImage()
	{
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(this.imageComponent);
		if (option == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File file = fileChooser.getSelectedFile();
				BufferedImage image = ImageIO.read(file);
				this.imageComponent.setImage(image);
			}
			catch (Exception e)
			{
				IGSMessageBox.showOkMB(this.imageComponent, null, null, e);
			}
		}
	}

	/** Displays a <code>JOptionPane</code> used to set the zoom factor. */
	private void changeImageScale()
	{
		String message = bundle.getString("changeImageScalePrompt"); //$NON-NLS-1$
		String scale = JOptionPane.showInputDialog(this.imageComponent, message);
		try
		{
			if (scale != null)
			{
				this.imageComponent.setScale(Double.parseDouble(scale));
			}
		}
		catch (NumberFormatException nfe)
		{
			//User entered bad text. Ignore.
		}
	}

	/** Displays a <code>JOptionPane</code> used to set the zoom factor. */
	private void changeZoomFactor()
	{
		String message = bundle.getString("changeZoomFactorPrompt"); //$NON-NLS-1$
		String zoomFactor = JOptionPane.showInputDialog(this.imageComponent, message);
		try
		{
			if (zoomFactor != null)
			{
				this.imageComponent.setZoomFactor(Double.parseDouble(zoomFactor));
			}
		}
		catch (NumberFormatException nfe)
		{
			//User entered bad text. Ignore.
		}
	}
}
