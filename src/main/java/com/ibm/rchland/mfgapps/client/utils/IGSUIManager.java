/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 * 2006-09-28   ~1 35394PL  R Prechel        -Changes for JTable grid color
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.UIManager;

/**
 * <code>IGSUIManager</code> allows the user to change <code>Color</code>s
 * and the <code>LookAndFeel</code> via a <code>JMenu</code>.
 * @author The External Fulfillment Client Development Team
 */
public class IGSUIManager
	implements ActionListener
{
	/** The key suffix for a name. */
	public static final String NAME_SUFFIX = "Name"; //$NON-NLS-1$

	/** The key suffix for a mnemonic. */
	public static final String MNEMONIC_SUFFIX = "Mnemonic"; //$NON-NLS-1$

	/** The key suffix for an int RGB color value. */
	public static final String VALUE_SUFFIX = "Value"; //$NON-NLS-1$

	/** The key root for the main menu. */
	public static final String MENU = "menu"; //$NON-NLS-1$

	/** The key root for the change look and feel menu item. */
	public static final String CHANGE_LAF = "changeLaf"; //$NON-NLS-1$

	/** The key root for the change color menu item. */
	public static final String CHANGE_COLOR = "changeColor"; //$NON-NLS-1$

	/** The key root for the toggle default color scheme menu item. */
	public static final String TOGGLE_COLORS = "toggleColors"; //$NON-NLS-1$;  

	/**
	 * The key for the <code>String</code> of comma-delimited color menu item
	 * key roots. For each color menu item key root, there must be two
	 * corresponding resource bundle keys:
	 * <ul>
	 * <li>root+Name: the <code>String</code> text for the menu item</li>
	 * <li>root+Value: the <code>int</code> RGB value of the color</li>
	 * </ul>
	 * For example: <br>
	 * colors=foregroundColor,backgroundColor <br>
	 * foregroundColorName=Foreground Color <br>
	 * foregroundColorValue=-16777216 <br>
	 * backgroundColorName=Background Color <br>
	 * backgroundColorValue=-1 <br>
	 */
	public static final String COLORS = "colors"; //$NON-NLS-1$

	/** The sole instance of <code>IGSUIManager</code>. */
	private static final IGSUIManager INSTANCE = new IGSUIManager();

	/** The <code>ResourceBundle</code> for an <code>IGSUIManager</code>. */
	private final ResourceBundle bundle = createBundle();

	/** The <code>IGSPreferences</code> used to store UI preferences. */
	private IGSPreferences preferences;

	//Must be a Map that uses weak references
	/** The <code>Map</code> of <code>Component</code>s to color names. */
	@SuppressWarnings("rawtypes")
	private Map componentMap = new WeakHashMap();

	//~1A Must be a Map that uses weak references
	/** The <code>Map</code> of <code>JTable</code>s to color names. */
	@SuppressWarnings("rawtypes")
	private Map tableMap = new WeakHashMap();
	
	/** The <code>Map</code> of color names to <code>Color</code>s. */
	@SuppressWarnings("rawtypes")
	private Map colorMap = new Hashtable();

	/** <code>true</code> iff the look and feel default colors should be used. */
	private boolean defaultColors = false;

	/**
	 * Returns the sole instance of <code>IGSUIManager</code>.
	 * @return the sole instance of <code>IGSUIManager</code>
	 */
	public static IGSUIManager getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Creates and returns the <code>ResourceBundle</code> used to create the
	 * <code>JMenu</code> and <code>Color</code> scheme. The name of the
	 * <code>ResourceBundle</code> is the value of the
	 * &quot;ibmgs.uimanager.properties&quot; system property or
	 * &quot;IGSUIManagerResources&quot;.
	 * @return the <code>ResourceBundle</code> used to create the
	 *         <code>JMenu</code> and <code>Color</code> scheme.
	 * @throws MissingResourceException as thrown by
	 *         {@link ResourceBundle#getBundle(java.lang.String, java.util.Locale, java.lang.ClassLoader)}
	 */
	private static ResourceBundle createBundle()
	{
		String name = System.getProperty("ibmgs.uimanager.properties", "IGSUIManagerResources"); //$NON-NLS-1$ //$NON-NLS-2$
		Locale l = Locale.getDefault();
		ClassLoader cl = IGSUIManager.class.getClassLoader();
		return ResourceBundle.getBundle(name, l, cl);
	}

	/**
	 * Constructs a new <code>IGSUIManager</code>. This class implements the
	 * <cite>Singleton </cite> design pattern. To ensure only one instance of
	 * <code>IGSUIManager</code> exists, the only constructor has
	 * <code>private</code> visibility.
	 */
	private IGSUIManager()
	{
		this.preferences = new IGSPreferences("_ui"); //$NON-NLS-1$
		this.preferences.loadPreferences();

		String preference = this.preferences.getPreference(TOGGLE_COLORS);
		if (preference != null && preference.equals(Boolean.toString(true)))
		{
			this.defaultColors = true;
			updateColors();
		}

		try
		{
			preference = this.preferences.getPreference(CHANGE_LAF);
			if (preference == null)
			{
				preference = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(preference);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the sole instance of <code>IGSUIManager</code>. Used by Java
	 * serialization; ensures only one instance of <code>IGSUIManager</code> exists.
	 * @return the sole instance of <code>IGSUIManager</code>
	 */
	protected Object readResolve()
	{
		return INSTANCE;
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		//Validity check on action command
		String ac = ae.getActionCommand();
		if (ac == null || (ac = ac.trim()).length() == 0)
		{
			return;
		}

		StringTokenizer tokenizer = new StringTokenizer(ac, " "); //$NON-NLS-1$
		String token = tokenizer.nextToken();

		if (token.equals(TOGGLE_COLORS))
		{
			this.defaultColors = !this.defaultColors;
			updateColors();
			this.preferences.setPreference(TOGGLE_COLORS, Boolean.toString(this.defaultColors));
			this.preferences.storePreferences();
		}

		else if (token.equals(CHANGE_COLOR) && tokenizer.hasMoreElements())
		{
			changeColor(tokenizer.nextToken());
			updateColors();
		}

		else if (token.equals(CHANGE_LAF) && tokenizer.hasMoreElements())
		{
			changeLookAndFeel(tokenizer.nextToken());
		}
	}

	/**
	 * Creates a <code>JMenu</code> that allows a user to interact with the
	 * <code>IGSUIManager</code>.
	 * @return the <code>JMenu</code>
	 */
	public JMenu createMenu()
	{
		JMenu result = new JMenu(this.bundle.getString(MENU + NAME_SUFFIX));
		result.setMnemonic(this.bundle.getString(MENU + MNEMONIC_SUFFIX).charAt(0));
		IGSAccessibility.setDescription(result);

		result.add(createLAFMenu());
		result.add(createChangeColorMenu());

		JMenuItem menuItem = new JMenuItem(this.bundle.getString(TOGGLE_COLORS + NAME_SUFFIX));
		menuItem.setMnemonic(this.bundle.getString(TOGGLE_COLORS + MNEMONIC_SUFFIX).charAt(0));
		IGSAccessibility.setDescription(menuItem, result);
		menuItem.setActionCommand(TOGGLE_COLORS);
		menuItem.addActionListener(this);
		result.add(menuItem);

		return result;
	}

	/**
	 * Creates the <code>JMenu</code> for changing the look and feel.
	 * @return the <code>JMenu</code> for changing the look and feel
	 */
	private JMenu createLAFMenu()
	{
		JMenu result = new JMenu(this.bundle.getString(CHANGE_LAF + NAME_SUFFIX));
		result.setMnemonic(this.bundle.getString(CHANGE_LAF + MNEMONIC_SUFFIX).charAt(0));
		IGSAccessibility.setDescription(result);
		UIManager.LookAndFeelInfo[] infos;
		infos = UIManager.getInstalledLookAndFeels();

		for (int i = 0; i < infos.length; i++)
		{
			String fullName = infos[i].getClassName();
			String className = fullName;
			int index = fullName.lastIndexOf('.');
			if (index > 0 && fullName.length() > index)
			{
				className = fullName.substring(index + 1);
			}
			StringBuffer actionCommand = new StringBuffer(64);
			actionCommand.append(CHANGE_LAF);
			actionCommand.append(" "); //$NON-NLS-1$
			actionCommand.append(fullName);

			JMenuItem menuItem = new JMenuItem(className);
			IGSAccessibility.setDescription(menuItem, result);
			menuItem.setActionCommand(actionCommand.toString());
			menuItem.addActionListener(this);
			result.add(menuItem);
		}
		return result;
	}

	/**
	 * Creates the <code>JMenu</code> for changing a color.
	 * @return the <code>JMenu</code> for changing a color
	 */
	private JMenu createChangeColorMenu()
	{
		JMenu result = new JMenu(this.bundle.getString(CHANGE_COLOR + NAME_SUFFIX));
		result.setMnemonic(this.bundle.getString(CHANGE_COLOR + MNEMONIC_SUFFIX).charAt(0));
		IGSAccessibility.setDescription(result);

		String colors = this.bundle.getString(COLORS);
		StringTokenizer tokenizer = new StringTokenizer(colors, ","); //$NON-NLS-1$
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken().trim();
			String name = this.bundle.getString(token + NAME_SUFFIX);
			StringBuffer actionCommand = new StringBuffer(64);
			actionCommand.append(CHANGE_COLOR);
			actionCommand.append(" "); //$NON-NLS-1$
			actionCommand.append(token);

			JMenuItem menuItem = new JMenuItem(name);
			IGSAccessibility.setDescription(menuItem, result);
			menuItem.setActionCommand(actionCommand.toString());
			menuItem.addActionListener(this);
			result.add(menuItem);
		}
		return result;
	}

	/** Updates the colors of all registered <code>Component</code>s. */
	@SuppressWarnings("rawtypes")
	public void updateColors()
	{
		Iterator it = this.componentMap.keySet().iterator();
		if (this.defaultColors)
		{
			while (it.hasNext())
			{
				Component next = (Component) it.next();
				next.setBackground(null);
				next.setForeground(null);
			}
		}
		else
		{
			while (it.hasNext())
			{
				Component next = (Component) it.next();
				String[] keys = (String[]) this.componentMap.get(next);
				next.setBackground(colorLookup(keys[0]));
				next.setForeground(colorLookup(keys[1]));
			}
		}
		
		//~1A Start new code to set grid color
		it = this.tableMap.keySet().iterator();
		if (this.defaultColors)
		{
			while (it.hasNext())
			{
				JTable next = (JTable) it.next();
				next.setBackground(null);
				next.setForeground(null);
				next.setGridColor(SystemColor.textText);
			}
		}
		else
		{
			while (it.hasNext())
			{
				JTable next = (JTable) it.next();
				String[] keys = (String[]) this.componentMap.get(next);
				Color foreground = colorLookup(keys[1]);
				next.setBackground(colorLookup(keys[0]));
				next.setForeground(foreground);
				next.setGridColor(foreground);
			}
		}
		//~1A End new code to set grid color
	}

	/**
	 * Displays a <code>JColorChooser</code> to allow the user to change the
	 * <code>Color</code> mapped to the specified key.
	 * @param key the key for a <code>Color</code>
	 */
	public void changeColor(String key)
	{
		Color color = colorLookup(key);
		String title = ""; //$NON-NLS-1$
		try
		{
			title = this.bundle.getString(key + NAME_SUFFIX);
		}
		catch (MissingResourceException mre)
		{
			mre.printStackTrace();
		}

		color = JColorChooser.showDialog(null, title, color);

		if (color != null)
		{
			this.mapColor(key, color);
		}
	}

	/**
	 * Changes the look and feel.
	 * @param className the fully-qualified class name of the look and feel
	 */
	public void changeLookAndFeel(String className)
	{
		try
		{
			UIManager.setLookAndFeel(className);
			IGSWindowsManager.getInstance().updateWindowUI();
			this.preferences.setPreference(CHANGE_LAF, className);
			this.preferences.storePreferences();
		}
		catch (Exception ex)
		{
			//Nothing to do
		}
	}

	/**
	 * Maps the specified <code>Color</code> to the specified <code>key</code>.
	 * @param key the key for the specified <code>color</code>
	 * @param color the <code>Color</code> that will be mapped to the
	 *        specified <code>key</code>
	 */
	@SuppressWarnings("unchecked")
	public void mapColor(String key, Color color)
	{
		this.colorMap.put(key, color);
		this.preferences.setPreference(key + VALUE_SUFFIX, Integer.toString(color.getRGB()));
		this.preferences.storePreferences();
	}

	/**
	 * Returns the <code>Color</code> mapped to the specified <code>key</code>;
	 * returns <code>null</code> if <code>key</code> is <code>null</code>
	 * or not mapped to a <code>Color</code>.
	 * @param key the key for the <code>Color</code>
	 * @return the <code>Color</code> mapped to the specified <code>key</code>;
	 *         <code>null</code> if <code>key</code> is <code>null</code>
	 *         or not mapped to a <code>Color</code>
	 */
	@SuppressWarnings("unchecked")
	public Color colorLookup(String key)
	{
		if (key == null)
		{
			return null;
		}

		Object result = this.colorMap.get(key);
		if (result == null)
		{
			String valueKey = key + VALUE_SUFFIX;

			//Try getting the color from the user's preferences
			try
			{
				String pref = this.preferences.getPreference(valueKey);
				if (pref != null)
				{
					result = new Color(Integer.parseInt(pref));
				}
			}
			catch (NumberFormatException nfe)
			{
				result = null;
			}

			//If not in preferences, try the ResourceBundle
			if (result == null)
			{
				try
				{
					result = this.bundle.getObject(valueKey);
					if (!(result instanceof Color))
					{
						result = new Color(Integer.parseInt(result.toString()));
					}
				}
				catch (RuntimeException re)
				{
					result = null;
				}
			}

			if (result != null)
			{
				this.colorMap.put(key, result);
			}
		}
		return (Color) result;
	}

	/**
	 * Sets the background and foreground color of the specified <code>component</code>.
	 * @param component the <code>Component</code>
	 * @param foreground the key for the foreground color
	 * @param background the key for the background color
	 */
	@SuppressWarnings("unchecked")
	public void setComponentColors(Component component, String background, String foreground)
	{
		if (this.defaultColors)
		{
			component.setBackground(null);
			component.setForeground(null);
		}
		else
		{
			component.setBackground(colorLookup(background));
			component.setForeground(colorLookup(foreground));
		}
		this.componentMap.put(component, new String[] {background, foreground});
	}
	
	
	/**
	 * Sets the background, foreground, and grid color of the specified
	 * <code>table</code>. This method overload exists because a generic
	 * <code>Component</code> does not have a grid color property.
	 * @param table the <code>JTable</code>
	 * @param foreground the key for the foreground color
	 * @param background the key for the background color
	 */
	@SuppressWarnings("unchecked")
	public void setComponentColors(JTable table, String background, String foreground)
	{	//~1 New method
		if (this.defaultColors)
		{
			table.setBackground(null);
			table.setForeground(null);
			table.setGridColor(SystemColor.textText);
		}
		else
		{
			Color foregroundColor = colorLookup(foreground);
			table.setBackground(colorLookup(background));
			table.setForeground(foregroundColor);
			table.setGridColor(foregroundColor);
		}
		table.setShowGrid(true);
		this.tableMap.put(table, new String[] {background, foreground});
	}
}
