/* @ Copyright IBM Corporation 2007. All rights reserved.
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

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * <code>IGSGRFImageReaderSpi</code> is the <code>ImageReaderSpi</code> for
 * the GRF Java Image I/O plugin.
 * @author The Process Profile Client Development Team
 */
public class IGSGRFImageReaderSpi
	extends ImageReaderSpi
{
	/** Constructs a new <code>IGSGRFImageReaderSpi</code>. */
	public IGSGRFImageReaderSpi()
	{
		super(
				"IBM Global Services", //$NON-NLS-1$
				"1.0", //$NON-NLS-1$
				new String[] {"grf", "GRF"}, //$NON-NLS-1$ //$NON-NLS-2$
				new String[] {"grf"}, //$NON-NLS-1$
				null,
				"com.ibm.rchland.mfgapps.client.utils.imageio.grf.IGSGRFImageReader", //$NON-NLS-1$
				STANDARD_INPUT_TYPE,
				new String[] {"com.ibm.rchland.mfgapps.client.utils.imageio.grf.IGSGRFImageWriterSpi"}, //$NON-NLS-1$
				false, null, null, null, null, false, null, null, null, null);
	}

	/** {@inheritDoc} */
	public boolean canDecodeInput(Object source)
		throws IOException
	{
		if (!(source instanceof ImageInputStream))
		{
			return false;
		}

		byte[] b = new byte[3];
		ImageInputStream stream = (ImageInputStream) source;
		stream.mark();
		stream.readFully(b);
		stream.reset();

		return b[0] == (byte) '~' && b[1] == (byte) 'D' && b[2] == (byte) 'G';
	}

	/** {@inheritDoc} */
	public ImageReader createReaderInstance(Object extension)
		throws IOException
	{
		return new IGSGRFImageReader(this);
	}

	/** {@inheritDoc} */
	public String getDescription(Locale locale)
	{
		return "GRF Image Reader"; //$NON-NLS-1$
	}
}
