/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-12       42558JL Santiago SC      -Initial version, Java 5.0
 * 2010-04-12   ~1  45878MS Santiago SC      -Add removeKeyListenerToComponents()
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.panel.dynamic;

import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * <code>IGSDynamicPanel</code> contains useful methods for dynamic child panels
 * @author The MFS Development Team
 */
public abstract class IGSDynamicPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8438044152407332216L;

	
	/**
	 * Applies the given <code>KeyListener</code> to each <code>JComponent</code>
	 * passed in the array of <code>JComponent</code>
	 */
	protected void addKeyListener(JComponent[] comps, KeyListener keyListener)
	{
		for(JComponent comp : comps)
		{
			comp.addKeyListener(keyListener);
		}
	}
	
	/**
	 * Add the keyListener to components.
	 * Any subclass of <code>IGSDynamicPanel</code> should implement this method.
	 */
	public abstract void addKeyListenerToComponents(KeyListener keyListener);	
	
	//~1A
	/**
	 * Removes the given <code>KeyListener</code> to each <code>JComponent</code>
	 * passed in the array of <code>JComponent</code>
	 */
	protected void removeKeyListener(JComponent[] comps, KeyListener keyListener)
	{
		for(JComponent comp : comps)
		{
			comp.removeKeyListener(keyListener);
		}
	}
	
	//~1A
	/**
	 * Removes the keyListener to components.
	 * Any subclass of <code>IGSDynamicPanel</code> should implement this method.
	 */
	public abstract void removeKeyListenerToComponents(KeyListener keyListener);	
}
