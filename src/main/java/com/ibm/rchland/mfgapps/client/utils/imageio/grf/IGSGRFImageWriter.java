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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/**
 * <code>IGSGRFImageWriter</code> is an <code>IGSGRFImageWriter</code> for
 * GRF images that does NOT support subsampling, clipping, or shifting the
 * encoded image through the use of <code>ImageWriteParam</code> parameters
 * but does support specifying the end of line character, image name, and image
 * rotation using an <code>IGSGRFImageWriteParam</code>.
 * @author The Process Profile Client Development Team
 */
public class IGSGRFImageWriter
	extends ImageWriter
{
	/**
	 * The byte values for the characters 0-9 and A-F in reverse order.
	 * <p>
	 * This array is used to convert a binary nibble (half byte) from the
	 * <code>DataBufferByte</code> of a <code>Raster</code> to the
	 * corresponding GRF image data ASCII value.
	 */
	private static final byte[] ASCII_BYTES = {
			'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0'
	};

	/** The <code>ImageOutputStream</code> to read from. */
	private ImageOutputStream stream = null;

	/**
	 * Constructs a new <code>IGSGRFImageWriter</code>.
	 * @param originatingProvider the <code>ImageWriterSpi</code> that is
	 *        invoking this constructor or <code>null</code>
	 */
	public IGSGRFImageWriter(ImageWriterSpi originatingProvider)
	{
		super(originatingProvider);
	}

	/** {@inheritDoc} */
	public ImageWriteParam getDefaultWriteParam()
	{
		return new IGSGRFImageWriteParam("UNKNOWN.GRF", getLocale()); //$NON-NLS-1$
	}

	/** {@inheritDoc} */
	public void reset()
	{
		super.reset();
		this.stream = null;
	}

	/** {@inheritDoc} */
	public void setOutput(Object output)
	{
		super.setOutput(output);
		this.stream = (ImageOutputStream) output;
	}

	/**
	 * {@inheritDoc}
	 * @return <code>null</code>
	 */
	public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @return <code>null</code>
	 */
	public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType,
												ImageWriteParam param)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @return <code>null</code>
	 */
	public IIOMetadata convertStreamMetadata(IIOMetadata inData, ImageWriteParam param)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @return <code>null</code>
	 */
	public IIOMetadata convertImageMetadata(IIOMetadata inData,
											ImageTypeSpecifier imageType,
											ImageWriteParam param)
	{
		return null;
	}

	/** {@inheritDoc} */
	public void write(IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param)
		throws IOException
	{
		if (this.stream == null)
		{
			throw new IllegalStateException("Image output stream is null."); //$NON-NLS-1$
		}
		if(image == null)
		{
			throw new IllegalArgumentException("Image is null."); //$NON-NLS-1$
		}
		RenderedImage renderedImage = image.getRenderedImage();
		if (renderedImage == null)
		{
			throw new IllegalArgumentException("RenderedImage not available."); //$NON-NLS-1$
		}

		clearAbortRequest();
		processImageStarted(0);
		if (param == null)
		{
			param = getDefaultWriteParam();
		}
		IGSGRFImageWriteParam grfParam = (IGSGRFImageWriteParam) param;
		Raster raster = createSourceRaster(renderedImage, grfParam);
		writeImageHeader(raster, grfParam);
		writeImageData(raster, grfParam);

		if (abortRequested())
		{
			processWriteAborted();
		}
		else
		{
			processImageComplete();
			this.stream.flushBefore(this.stream.getStreamPosition());
		}
	}

	/**
	 * Creates a <code>Raster</code> from the specified {@link RenderedImage}
	 * and {@link IGSGRFImageWriteParam} that will be used to write the
	 * specified <code>RenderedImage</code> as a GRF image (i.e., the returned
	 * <code>Raster</code> will be used to provide the source image data). The
	 * returned <code>Raster</code> will be 1-bit byte-packed and the image
	 * data will correspond to an <code>IndexColorModel</code> with two colors
	 * in the default sRGB <code>ColorSpace</code>: black (0xFF000000) and
	 * white (0xFFFFFFFF).
	 * @param renderedImage the <code>RenderedImage</code> to output as a GRF
	 * @param grfParam the <code>IGSGRFImageWriteParam</code>
	 * @return the <code>Raster</code>
	 */
	private Raster createSourceRaster(RenderedImage renderedImage,
										IGSGRFImageWriteParam grfParam)
	{
		Raster raster = renderedImage.getTile(0, 0);
		int srcW = raster.getWidth();
		int srcH = raster.getHeight();
		SampleModel sampleModel = raster.getSampleModel();
		ColorModel colorModel = renderedImage.getColorModel();
		int rotation = grfParam.getRotation();
		if (rotation != 0 || sampleModel.getDataType() != DataBuffer.TYPE_BYTE
				|| sampleModel.getNumBands() != 1 || sampleModel.getSampleSize(0) != 1
				|| colorModel.getRGB(0) != 0xFF000000
				|| colorModel.getRGB(1) != 0xFFFFFFFF
				|| ((rotation == 0 || rotation == 180) && srcW % 8 != 0)
				|| ((rotation == 90 || rotation == 270) && srcH % 8 != 0))
		{
			final BufferedImage bi;
			final Graphics2D g2d;
			if (rotation == 0)
			{
				int mod = srcW % 8;
				if (mod == 0)
				{
					bi = new BufferedImage(srcW, srcH, BufferedImage.TYPE_BYTE_BINARY);
					g2d = bi.createGraphics();
				}
				else
				{
					int pad = 8 - mod;
					int destW = srcW + pad;
					bi = new BufferedImage(destW, srcH, BufferedImage.TYPE_BYTE_BINARY);
					g2d = bi.createGraphics();
					g2d.setColor(Color.WHITE);
					g2d.fillRect(0, 0, destW, srcH);
					g2d.translate((pad >> 1), 0);
				}
			}
			else
			{
				double angle = Math.toRadians(rotation);
				double xRot = srcW / 2.0;
				double yRot = srcH / 2.0;
				AffineTransform transform = AffineTransform.getRotateInstance(angle, xRot, yRot);
				AffineTransformOp op = new AffineTransformOp(transform,
						AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				Rectangle2D bounds = op.getBounds2D(raster);
				int destW = (int) bounds.getWidth();
				int destH = (int) bounds.getHeight();
				int mod = destW % 8;
				if (mod == 0)
				{
					bi = new BufferedImage(destW, destH, BufferedImage.TYPE_BYTE_BINARY);
					g2d = bi.createGraphics();
				}
				else
				{
					int pad = 8 - mod;
					destW += pad;
					bi = new BufferedImage(destW, destH, BufferedImage.TYPE_BYTE_BINARY);
					g2d = bi.createGraphics();
					g2d.setColor(Color.WHITE);
					g2d.fillRect(0, 0, destW, destH);
					g2d.translate((pad >> 1), 0);
				}
				g2d.translate(-bounds.getX(), -bounds.getY());
				g2d.transform(transform);
			}
			g2d.drawRenderedImage(renderedImage, new AffineTransform());
			g2d.dispose();
			renderedImage = bi;
			raster = bi.getRaster();
			sampleModel = raster.getSampleModel();
		}
		return raster;
	}

	/**
	 * Writes the GRF image header.
	 * @param raster the image data source <code>Raster</code>
	 * @param grfParam the <code>IGSGRFImageWriteParam</code>
	 * @throws IOException as thrown by the <code>ImageOutputStream</code>
	 */
	private void writeImageHeader(Raster raster, IGSGRFImageWriteParam grfParam)
		throws IOException
	{
		int bytesPerRow = raster.getWidth() / 8;
		int totalBytes = bytesPerRow * raster.getHeight();
		this.stream.writeBytes("~DG"); //$NON-NLS-1$
		this.stream.writeBytes(grfParam.getName());
		this.stream.writeByte(',');
		String string = Integer.toString(totalBytes);
		int append = 5 - string.length();
		while (append > 0)
		{
			this.stream.write('0');
			append--;
		}
		this.stream.writeBytes(string);
		this.stream.write(',');
		string = Integer.toString(bytesPerRow);
		append = 3 - string.length();
		while (append > 0)
		{
			this.stream.write('0');
			append--;
		}
		this.stream.writeBytes(string);
		this.stream.write(',');
	}

	/**
	 * Writes the GRF image data.
	 * @param raster the image data source <code>Raster</code>
	 * @param grfParam the <code>IGSGRFImageWriteParam</code>
	 * @throws IOException as thrown by the <code>ImageOutputStream</code>
	 */
	public void writeImageData(Raster raster, IGSGRFImageWriteParam grfParam)
		throws IOException
	{
		int height = raster.getHeight();
		int bytesPerRow = raster.getWidth() / 8;
		byte[] dataBuffer = ((DataBufferByte) raster.getDataBuffer()).getData();
		byte[] rowBuffer = new byte[bytesPerRow * 2];
		int byteCount = 0;
		byte[] eol = grfParam.getEOLCharacter();
		for (int row = 0; row < height; row++)
		{
			this.stream.write(eol);
			if (abortRequested())
			{
				break;
			}

			for (int i = 0; i < rowBuffer.length; i += 2)
			{
				rowBuffer[i] = ASCII_BYTES[(dataBuffer[byteCount] & 0xF0) >> 4];
				rowBuffer[i + 1] = ASCII_BYTES[(dataBuffer[byteCount] & 0X0F)];
				byteCount++;
			}
			this.stream.write(rowBuffer);
			processImageProgress(100.0F * row / height);
		}
	}
}
