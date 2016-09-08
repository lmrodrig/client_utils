/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 * 2006-09-28  ~1  35394PL  R Prechel        -Replace ?: with if/else
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.net.URL;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * <code>IGSTextFileViewer</code> is a plain text of html file viewer.
 * @author The External Fulfillment Client Development Team
 */
public class IGSTextFileViewer
{
	/** The parent <code>Window</code> of the displayed <code>JDialog</code>s. */
	private Window window = null;

	/** The size of the displayed <code>JDialog</code>s. */
	private Dimension dimension;

	/** The <code>Locale</code> used to resolve file names. */
	private Locale locale;

	/** Constructs a new <code>IGSFileViewer</code>. */
	public IGSTextFileViewer()
	{
		this(null, null, null);
	}

	/**
	 * Constructs a new <code>IGSFileViewer</code>.
	 * @param window the parent <code>Window</code> of the displayed
	 *        <code>JDialog</code>s
	 */
	public IGSTextFileViewer(Window window)
	{
		this(window, null, null);
	}

	/**
	 * Constructs a new <code>IGSFileViewer</code>.
	 * @param window the parent <code>Window</code> of the displayed
	 *        <code>JDialog</code>s
	 * @param dimension the size of the displayed <code>JDialog</code>s.
	 *        A default size is used if <code>null</code>.
	 * @param locale the <code>Locale</code> used to resolve file names.
	 *        The default <code>Locale</code> is used if <code>null</code>.
	 */
	public IGSTextFileViewer(Window window, Dimension dimension, Locale locale)
	{
		this.window = window;
		//~1 Replace ?: with if/else
		if (dimension == null)
		{
			if (window == null)
			{
				this.dimension = new Dimension(300, 400);
			}
			else
			{
				this.dimension = new Dimension(window.getWidth() / 2, window.getHeight() / 2);
			}
		}
		else
		{
			this.dimension = dimension;
		}

		if (locale == null)
		{
			this.locale = Locale.getDefault();
		}
		else
		{
			this.locale = locale;
		}
	}

	/**
	 * Sets the parent <code>Window</code> of the displayed <code>JDialog</code>s.
	 * @param window the parent <code>Window</code>
	 */
	public void setParentWindow(Window window)
	{
		this.window = window;
	}

	/**
	 * Sets the size of the displayed <code>JDialog</code>s.
	 * @param dimension the new size
	 */
	public void setSize(Dimension dimension)
	{
		this.dimension = dimension;
	}

	/**
	 * Sets the <code>Locale</code> used to resolve file names.
	 * @param locale the <code>Locale</code>
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * Displays a localized file. Using this object's <code>Locale</code>,
	 * the file name is resolved in the following order:
	 * <ol>
	 * <li>name + &quot;_&quot; + language + &quot;_&quot; + country +
	 * &quot;_&quot; + variant + extension</li>
	 * <li>name + &quot;_&quot; + language + &quot;_&quot; + country +
	 * extension</li>
	 * <li>name + &quot;_&quot; + language + extension</li>
	 * <li>name + extension</li>
	 * </ol>
	 * @param title the title of the <code>JDialog</code>
	 * @param name the base file name without an extension
	 * @param extension the file extension
	 * @return the <code>JDialog</code> used to display the file or
	 *         <code>null</code> if an error occurred
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JDialog displayLocalizedFile(String title, String name, String extension)
	{
		Vector names = new Vector();
		String language = this.locale.getLanguage();
		String country = this.locale.getCountry();
		String variant = this.locale.getVariant();

		names.add(name);
		StringBuffer buffer = new StringBuffer();
		if (language.length() > 0)
		{
			buffer.append(name);
			buffer.append('_');
			buffer.append(language);
			names.add(buffer.toString());
			if (country.length() > 0)
			{
				buffer.append('_');
				buffer.append(country);
				names.add(buffer.toString());
				if (variant.length() > 0)
				{
					buffer.append('_');
					buffer.append(variant);
					names.add(buffer.toString());
				}
			}
		}

		ClassLoader loader = IGSTextFileViewer.class.getClassLoader();
		URL url = null;
		for (int i = names.size(); i > 0 && url == null;)
		{
			url = loader.getResource(names.get(--i).toString() + extension);
		}

		return displayURL(title, url);
	}

	/**
	 * Displays the file with the specified <code>name</code>
	 * @param title the title of the <code>JDialog</code>
	 * @param name the file name
	 * @return the <code>JDialog</code> used to display the file or
	 *         <code>null</code> if an error occurred
	 */
	public JDialog displayFile(String title, String name)
	{
		return displayURL(title, IGSTextFileViewer.class.getClassLoader().getResource(name));
	}

	/**
	 * Displays the specified <code>url</code>.
	 * @param title the title of the <code>JDialog</code>
	 * @param url the <code>URL</code>
	 * @return the <code>JDialog</code> used to display the <code>url</code>
	 *         or <code>null</code> if an error occurred
	 */
	public JDialog displayURL(String title, URL url)
	{
		JDialog dialog = null;
		try
		{
			if (this.window instanceof Frame)
			{
				dialog = new JDialog((Frame) this.window, title);
			}
			else if (this.window instanceof Dialog)
			{
				dialog = new JDialog((Dialog) this.window, title);
			}
			else
			{
				dialog = new JDialog();
				dialog.setTitle(title);
			}

			IGSWindowsManager manager = IGSWindowsManager.getInstance();

			JEditorPane editorPane = new JEditorPane(url);
			editorPane.getAccessibleContext().setAccessibleName(title);
			editorPane.setEditable(false);
			JScrollPane pane = new JScrollPane(editorPane);
			IGSAccessibility.setNameAndDescription(pane, title+IGSAccessibility.TARGET_0);
			manager.registerKeyStroke(pane);
			dialog.setContentPane(pane);
			dialog.setSize(this.dimension);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.addWindowListener(manager);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			System.err.println("Could not display URL: " + url); //$NON-NLS-1$
			e.printStackTrace();
			dialog = null;
		}
		return dialog;
	}
}
