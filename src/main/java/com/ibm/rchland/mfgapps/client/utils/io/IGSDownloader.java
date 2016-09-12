/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-08-01      38768JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * <code>IGSDownloader</code> downloads/updates remote files specified by a
 * <code>URL</code> to a location specified by a <code>File</code>.
 * @author The MFS Print Server Development Team
 */
public class IGSDownloader
{
	/**
	 * Constructs a new <code>IGSDownloader</code>. This class only has
	 * static variables and static methods and does not have any instance
	 * variables or instance methods. Thus, there is no reason to create an
	 * instance of <code>IGSDownloader</code>, so the only constructor is
	 * declared <code>private</code>.
	 */
	private IGSDownloader()
	{
		super();
	}

	/**
	 * Downloads the remote file represented by <code>url</code> if it is
	 * newer than the file represented by <code>file</code> or if
	 * <code>file</code> represents a nonexistent local file.
	 * @param url the <code>URL</code> representing a remote file
	 * @param file the <code>File</code> representing a local file
	 * @return <code>true</code> iff the remote file represented by
	 *         <code>url</code> was successfully downloaded
	 * @throws IOException as thrown by the Java I/O and network APIs
	 */
	public static boolean updateFile(URL url, File file)
		throws IOException
	{
		boolean download = true;
		URLConnection connection = url.openConnection();
		
		if (file.exists())
		{
			connection.setIfModifiedSince(file.lastModified());
			if (connection instanceof HttpURLConnection)
			{
				int response = ((HttpURLConnection) connection).getResponseCode();
				if (response == HttpURLConnection.HTTP_NOT_MODIFIED)
				{
					download = false;
				}
			}

			// The HTTP_NOT_MODIFIED check will not work if the server does
			// not support the If-Modified-Since request-header. However,
			// the HTTP_NOT_MODIFIED check is a performance enhancement if
			// the server does support the If-Modified-Since request-header
			// because it decreases transaction overhead if the requested
			// file has not been modified.

			if (download)
			{
				if (file.lastModified() >= connection.getLastModified())
				{
					download = false;
				}
			}
		}
		
		if (download)
		{
			downloadFile(connection, file);
		}
		return download;
	}

	/**
	 * Downloads the file corresponding to the specified <code>connection</code>.
	 * @param connection a <code>URLConnection</code> for a file
	 * @param file the destination <code>File</code>
	 * @throws IOException as thrown by the Java I/O and network APIs
	 */
	public static void downloadFile(URLConnection connection, File file)
		throws IOException
	{
		BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		int c;
		while (-1 != (c = in.read()))
		{
			out.write(c);
		}
		in.close();
		out.close();
	}
}
