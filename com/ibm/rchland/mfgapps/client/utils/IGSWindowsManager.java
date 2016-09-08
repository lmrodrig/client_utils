/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * <code>IGSWindowsManager</code> is used to switch among <code>Window</code>s
 * and update the <code>LookAndFeel</code>. To know about a
 * <code>Window</code>, the <code>IGSWindowsManager</code> class must be
 * added as a <code>WindowListener</code> before the <code>Window</code> is
 * made visible for the first time.
 * @author The External Fulfillment Client Development Team
 */
public class IGSWindowsManager
	extends AbstractAction
	implements WindowListener
{
	private static final long serialVersionUID = 1L;

	/** The sole instance of <code>IGSWindowsManager</code>. */
	private static final IGSWindowsManager INSTANCE = new IGSWindowsManager();

	/** The list of visible <code>Window</code>s. */
	@SuppressWarnings("rawtypes")
	private List windows = new Vector();

	/** The currently active <code>Window</code>. */
	private Window activeWindow;

	/**
	 * Returns the sole instance of <code>IGSWindowsManager</code>.
	 * @return the sole instance of <code>IGSWindowsManager</code>
	 */
	public static IGSWindowsManager getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Constructs a new <code>IGSWindowsManager</code>. This class implements
	 * the <cite>Singleton</cite> design pattern. To ensure only one instance
	 * of <code>IGSWindowsManager</code> exists, the only constructor has
	 * <code>private</code> visibility.
	 */
	private IGSWindowsManager()
	{
		//Nothing to do
	}

	/**
	 * Returns the sole instance of <code>IGSWindowsManager</code>. Used by
	 * Java serialization; ensures only one instance of
	 * <code>IGSWindowsManager</code> exists.
	 * @return the sole instance of <code>IGSWindowsManager</code>
	 */
	protected Object readResolve()
	{
		return INSTANCE;
	}

	/**
	 * Adds a (F3 <code>KeyStroke</code>, <code>this</code>) binding to
	 * the specified <code>component</code>'s
	 * {@link JComponent#WHEN_IN_FOCUSED_WINDOW} <code>InputMap</code> with
	 * <code>this</code> as the <code>Action</code>.
	 * @param component the <code>JComponent</code> (usually should be the
	 *        content pane of a top-level <code>Window</code>)
	 */
	public void registerKeyStroke(JComponent component)
	{
		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
		component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, this);
		component.getActionMap().put(this, this);
	}

	/**
	 * Executes {@link SwingUtilities#updateComponentTreeUI(java.awt.Component)}
	 * for each active <code>Window</code>.
	 */
	@SuppressWarnings("rawtypes")
	public void updateWindowUI()
	{
		Iterator iterator = this.windows.iterator();
		while (iterator.hasNext())
		{
			SwingUtilities.updateComponentTreeUI((Window) iterator.next());
		}
	}

	/**
	 * Invoked when an action occurs. Changes the focused <code>Window</code>.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		int size = this.windows.size();
		if (size > 1)
		{
			int index = this.windows.indexOf(this.activeWindow) + 1;
			if (index < size)
			{
				((Window) this.windows.get(index)).requestFocus();
			}
			else
			{
				((Window) this.windows.get(0)).requestFocus();
			}
		}
	}

	/**
	 * Invoked the first time a <code>Window</code> is made visible.
	 * @param we the <code>WindowEvent</code>
	 */
	@SuppressWarnings("unchecked")
	public void windowOpened(WindowEvent we)
	{
		this.windows.add(we.getWindow());
	}

	/**
	 * Invoked when the user attempts to close the <code>Window</code> from
	 * the system menu.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowClosing(WindowEvent we)
	{
		//Nothing to do
	}

	/**
	 * Invoked when a <code>Window</code> has been closed.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowClosed(WindowEvent we)
	{
		this.windows.remove(we.getWindow());

	}

	/**
	 * Invoked when a <code>Window</code> is changed from a normal to a
	 * minimized state.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowIconified(WindowEvent we)
	{
		this.windows.remove(we.getWindow());
	}

	/**
	 * Invoked when a <code>Window</code> is changed from a minimized to a
	 * normal state.
	 * @param we the <code>WindowEvent</code>
	 */
	@SuppressWarnings("unchecked")
	public void windowDeiconified(WindowEvent we)
	{
		this.windows.add(we.getWindow());
	}

	/**
	 * Invoked when a <code>Window</code> becomes the active
	 * <code>Window</code>.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowActivated(WindowEvent we)
	{
		this.activeWindow = we.getWindow();
	}

	/**
	 * Invoked when a <code>Window</code> is no longer the active
	 * <code>Window</code>.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowDeactivated(WindowEvent we)
	{
		//Nothing to do
	}
}
