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

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

/**
 * <code>IGSGRFImageWriterSpi</code> is the <code>ImageWriterSpi</code> for
 * the GRF Java Image I/O plugin.
 * @author The Process Profile Client Development Team
 */
public class IGSGRFImageWriterSpi
	extends ImageWriterSpi
{
	/** Constructs a new <code>IGSGRFImageWriterSpi</code>. */
	public IGSGRFImageWriterSpi()
	{
		super(
				"IBM Global Services", //$NON-NLS-1$
				"1.0", //$NON-NLS-1$
				new String[] {"grf", "GRF"}, //$NON-NLS-1$ //$NON-NLS-2$
				new String[] {"grf"}, //$NON-NLS-1$
				null,
				"com.ibm.rchland.mfgapps.client.utils.imageio.grf.IGSGRFImageWriter", //$NON-NLS-1$
				STANDARD_OUTPUT_TYPE,
				new String[] {"com.ibm.rchland.mfgapps.client.utils.imageio.grf.IGSGRFImageReaderSpi"}, //$NON-NLS-1$
				false, null, null, null, null, false, null, null, null, null);
	}

	/** {@inheritDoc} */
	public boolean canEncodeImage(ImageTypeSpecifier type)
	{
		return true;
	}

	/** {@inheritDoc} */
	public ImageWriter createWriterInstance(Object extension)
		throws IOException
	{
		return new IGSGRFImageWriter(this);
	}

	/** {@inheritDoc} */
	public String getDescription(Locale locale)
	{
		return "GRF Image Writer"; //$NON-NLS-1$
	}
}
