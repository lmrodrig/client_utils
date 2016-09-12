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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * <code>IGSGRFImageReader</code> is an <code>ImageReader</code> for GRF
 * images that does NOT support subsampling, clipping, or shifting the decoded
 * image through the use of <code>ImageReadParam</code> parameters.
 * @author The Process Profile Client Development Team
 */
public class IGSGRFImageReader
	extends ImageReader
{
	/** The <code>ImageInputStream</code> to read from. */
	private ImageInputStream stream = null;

	/** <code>true</code> when the header has been read. */
	private boolean gotHeader = false;

	/** The total number of data bytes. */
	private int totalBytes = 0;

	/** The number of bytes in a row (width / 8). */
	private int bytesPerRow = 0;

	/** The width of the image. */
	private int width = 0;

	/** The height of the image. */
	private int height = 0;

	/**
	 * Constructs a new <code>IGSGRFImageReader</code>.
	 * @param originatingProvider the <code>ImageReaderSpi</code> that is
	 *        invoking this constructor or <code>null</code>
	 */
	public IGSGRFImageReader(ImageReaderSpi originatingProvider)
	{
		super(originatingProvider);
	}

	/** {@inheritDoc} */
	public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata)
	{
		super.setInput(input, seekForwardOnly, ignoreMetadata);
		this.stream = (ImageInputStream) input;
		this.gotHeader = false;
	}

	/**
	 * {@inheritDoc}
	 * @return 1
	 */
	public int getNumImages(boolean allowSearch)
		throws IOException
	{
		return 1;
	}

	/**
	 * Checks if the requested image index is in range and the
	 * <code>ImageInputStream</code> is not <code>null</code>.
	 * @param imageIndex the requested image index
	 * @throws IndexOutOfBoundsException if <code>imageIndex != 0</code>
	 * @throws IllegalStateException if the <code>ImageInputStream</code> is
	 *         <code>null</code>
	 */
	private void check(int imageIndex)
	{
		if (imageIndex != 0)
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append("Image index "); //$NON-NLS-1$
			buffer.append(imageIndex);
			buffer.append(" not equal to zero."); //$NON-NLS-1$
			throw new IndexOutOfBoundsException(buffer.toString());
		}
		if (this.stream == null)
		{
			throw new IllegalStateException("Image input stream is null."); //$NON-NLS-1$
		}
	}

	/** {@inheritDoc} */
	public int getWidth(int imageIndex)
		throws IOException
	{
		check(imageIndex);
		readHeader();
		return this.width;
	}

	/** {@inheritDoc} */
	public int getHeight(int imageIndex)
		throws IOException
	{
		check(imageIndex);
		readHeader();
		return this.height;
	}

	/**
	 * Reads the GRF header information.
	 * @throws IOException if an error occurs while reading the header
	 */
	private void readHeader()
		throws IOException
	{
		if (this.gotHeader)
		{
			return;
		}

		while (this.stream.readByte() != (byte) ',')
		{
			//Skip ~DG, device, and name
		}

		byte inputByte;
		StringBuffer total = new StringBuffer();
		StringBuffer perRow = new StringBuffer();
		while ((inputByte = this.stream.readByte()) != (byte) ',')
		{
			total.append((char) inputByte);
		}
		while ((inputByte = this.stream.readByte()) != (byte) ',')
		{
			perRow.append((char) inputByte);
		}

		try
		{
			this.totalBytes = Integer.parseInt(total.toString());
			this.bytesPerRow = Integer.parseInt(perRow.toString());
		}
		catch (NumberFormatException nfe)
		{
			IOException ioe = new IOException();
			ioe.initCause(nfe);
			throw ioe;
		}
		this.height = this.totalBytes / this.bytesPerRow;
		this.width = 8 * this.bytesPerRow;
		this.gotHeader = true;
	}

	/** {@inheritDoc} */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Iterator getImageTypes(int imageIndex)
		throws IOException
	{
		// Call checkIndex and readHeader to throw Exceptions
		// as indicated by the ImageReader API
		check(imageIndex);
		readHeader();
		ArrayList list = new ArrayList(1);
		list.add(ImageTypeSpecifier.createGrayscale(1, DataBuffer.TYPE_BYTE, false));
		return list.iterator();
	}

	/**
	 * {@inheritDoc}
	 * @return <code>null</code>
	 */
	public IIOMetadata getStreamMetadata()
		throws IOException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @return <code>null</code>
	 */
	public IIOMetadata getImageMetadata(int imageIndex)
		throws IOException
	{
		return null;
	}

	/** {@inheritDoc} */
	public BufferedImage read(int imageIndex, ImageReadParam param)
		throws IOException
	{
		check(imageIndex);
		clearAbortRequest();
		processImageStarted(imageIndex);
		readHeader();

		if (param == null)
		{
			param = getDefaultReadParam();
		}
		Rectangle src = new Rectangle(0, 0, 0, 0);
		Rectangle dest = new Rectangle(0, 0, 0, 0);
		computeRegions(param, this.width, this.height, param.getDestination(), src, dest);

		BufferedImage bi = param.getDestination();
		if (bi == null)
		{
			int w = dest.x + dest.width;
			int h = dest.y + dest.height;
			bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
		}

		/* If no transformation is needed. */
		if (dest.x == 0 && dest.y == 0 && dest.width == this.width
				&& dest.height == this.height && this.width == bi.getWidth()
				&& this.height == bi.getHeight())
		{
			if (abortRequested())
			{
				processReadAborted();
				return bi;
			}

			int byteCount = 0;
			byte streamByte = 0;
			WritableRaster raster = bi.getRaster();
			byte[] dataBuffer = ((DataBufferByte) raster.getDataBuffer()).getData();
			byte[] rowBuffer = new byte[this.bytesPerRow * 2];
			int rowBufferLengthMinusOne = rowBuffer.length - 1;
			int row = 0;

			while (byteCount < this.totalBytes)
			{
				//Ignore the end of line characters from the previous line
				streamByte = this.stream.readByte();
				while (!isImageData(streamByte))
				{
					streamByte = this.stream.readByte();
				}

				//Read the row of data
				rowBuffer[0] = streamByte;
				this.stream.read(rowBuffer, 1, rowBufferLengthMinusOne);
				for (int i = 0; i < rowBuffer.length; i += 2)
				{
					dataBuffer[byteCount] = (byte) (convertNibble(rowBuffer[i]) << 4);
					dataBuffer[byteCount] |= convertNibble(rowBuffer[i + 1]);
					byteCount++;
				}

				if (abortRequested())
				{
					processReadAborted();
					return bi;
				}
				processImageUpdate(bi, 0, row++, this.width, 1, 1, 1, new int[] {0});
				processImageProgress(100.0F * byteCount / this.totalBytes);
			}
		}
		else
		{
			throw new IllegalArgumentException("Transformation not supported."); //$NON-NLS-1$
		}

		if (abortRequested())
		{
			processReadAborted();
		}
		else
		{
			processImageComplete();
		}
		return bi;
	}

	/**
	 * {@inheritDoc}
	 * @return <code>true</code>
	 */
	public boolean canReadRaster()
	{
		return true;
	}

	/** {@inheritDoc} */
	public Raster readRaster(int imageIndex, ImageReadParam param)
		throws IOException
	{
		return read(imageIndex, param).getData();
	}

	/** {@inheritDoc} */
	public void reset()
	{
		super.reset();
		this.stream = null;
		this.gotHeader = false;
		this.totalBytes = 0;
		this.bytesPerRow = 0;
		this.width = 0;
		this.height = 0;
	}

	/**
	 * Checks if the specified <code>data</code> corresponds to image data.
	 * @param data the nibble value from the GRF input
	 * @return <code>true</code> if the specified <code>nibble</code>
	 *         corresponds to image data; <code>false</code> otherwise
	 */
	private boolean isImageData(byte data)
	{
		boolean isNumeric = '0' <= data && data <= '9';
		boolean isAlpha = 'A' <= data && data <= 'F';
		return isNumeric || isAlpha;
	}

	/**
	 * Converts a GRF ASCII nibble (half byte) into the binary value used by the
	 * <code>DataBufferByte</code> of a <code>BufferedImage</code>.
	 * <p>
	 * Implementation Note: A GRF file stores an ASCII hexadecimal
	 * representation of an image where each character represents a horizontal
	 * nibble of four dots with the binary value 0 representing white and the
	 * binary value 1 representing black. A <code>BufferedImage</code> of type
	 * {@link BufferedImage#TYPE_BYTE_BINARY} stores a binary representation of
	 * an image where each bit represents a single pixel with the binary value 0
	 * representing black and the binary value 1 representing white. Thus, the
	 * conversion from GRF input inverts the hexadecimal nibble value.
	 * @param nibble the ASCII nibble value from the GRF input
	 * @return the binary value
	 * @throws IOException if <code>nibble</code> is not a valid ASCII
	 *         hexadecimal value
	 */
	private byte convertNibble(byte nibble)
		throws IOException
	{
		switch (nibble)
		{
			case '0':
				return 0xF;
			case '1':
				return 0xE;
			case '2':
				return 0xD;
			case '3':
				return 0xC;
			case '4':
				return 0xB;
			case '5':
				return 0xA;
			case '6':
				return 0x9;
			case '7':
				return 0x8;
			case '8':
				return 0x7;
			case '9':
				return 0x6;
			case 'A':
				return 0x5;
			case 'B':
				return 0x4;
			case 'C':
				return 0x3;
			case 'D':
				return 0x2;
			case 'E':
				return 0x1;
			case 'F':
				return 0x0;
			default:
				throw new IOException("Invalid nibble in image input."); //$NON-NLS-1$
		}
	}
}
