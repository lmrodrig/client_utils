/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-01-16      39619JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.imageio.grf;

import java.util.Locale;

import javax.imageio.ImageWriteParam;

/**
 * <code>IGSGRFImageWriteParam</code> is the default subclass of
 * <code>ImageWriteParam</code> used by {@link IGSGRFImageWriter} to support
 * specifying the end of line character, image name, and image rotation.
 * @author The Process Profile Client Development Team
 */
public class IGSGRFImageWriteParam
	extends ImageWriteParam
{
	/** The newline end of line character mode. */
	public static final int EOL_MODE_NEWLINE = 0;

	/** The underscore end of line character mode. */
	public static final int EOL_MODE_UNDERSCORE = 1;

	/** Determines the character used to signal the end of a line. */
	private int eolMode = EOL_MODE_NEWLINE;

	/** The name of the image. */
	private String name;

	/** The number of degrees the image should be rotated. */
	private int rotation = 0;

	/**
	 * Constructs a new <code>IGSGRFImageWriteParam</code>.
	 * @param name the name of the image
	 * @throws NullPointerException if name is <code>null</code>
	 */
	public IGSGRFImageWriteParam(String name)
	{
		super();
		this.name = name.toUpperCase(Locale.US);
	}

	/**
	 * Constructs a new <code>IGSGRFImageWriteParam</code>.
	 * @param name the name of the image
	 * @param locale the <code>Locale</code> used to perform localization
	 * @throws NullPointerException if name is <code>null</code>
	 */
	public IGSGRFImageWriteParam(String name, Locale locale)
	{
		super(locale);
		this.name = name.toUpperCase(Locale.US);
	}

	/**
	 * Returns the end of line characters to use when writing the GRF image.
	 * @return the end of line characters
	 */
	public byte[] getEOLCharacter()
	{
		switch (this.eolMode)
		{
			case EOL_MODE_NEWLINE:
			default:
				return new byte[] {13, 10};
			case EOL_MODE_UNDERSCORE:
				return new byte[] {(byte) '_'};
		}
	}

	/**
	 * Returns the end of line character mode.
	 * @return the end of line character mode
	 */
	public int getEOLMode()
	{
		return this.eolMode;
	}

	/**
	 * Returns the name of the image.
	 * @return the name of the image
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Returns the number of degrees the image should be rotated.
	 * @return the number of degrees the image should be rotated
	 */
	public int getRotation()
	{
		return this.rotation;
	}

	/**
	 * Sets the end of line character mode.
	 * @param mode the new end of line character mode
	 */
	public void setEOLMode(int mode)
	{
		this.eolMode = mode;
	}

	/**
	 * Sets the name of the image.
	 * @param name the name of the image
	 * @throws NullPointerException if name is <code>null</code>
	 */
	public void setName(String name)
	{
		this.name = name.toUpperCase(Locale.US);
	}

	/**
	 * Sets the number of degrees the image should be rotated.
	 * @param rotation the number of degrees the image should be rotated
	 * @throws IllegalArgumentException if the specified rotation is not
	 *         equivalent to 0, 90, 180, or 270 degrees
	 */
	public void setRotation(int rotation)
		throws IllegalArgumentException
	{
		while (rotation < 0)
		{
			rotation += 360;
		}
		while (rotation > 270)
		{
			rotation -= 360;
		}

		if (rotation == 0 || rotation == 90 || rotation == 180 || rotation == 270)
		{
			this.rotation = rotation;
		}
		else
		{
			throw new IllegalArgumentException("Illegal rotation."); //$NON-NLS-1$
		}
	}
}
