/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-15       45878MS Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.panel.dynamic;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/**
 * <code>IGSDynamicButtonPanel</code> is a <code>IGSDynamicPanel</code> that 
 * creates dynamically a panel containing <code>AbstractButton</code>s.
 * @author The MFS Development Team
 */
public abstract class IGSDynamicButtonPanel extends IGSDynamicPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7116755628692267763L;	
	
	/** Horizontal button alignment */
	public static final int HORIZONTAL_ALIGN = 0;
	
	/** Vertical button alignment */
	public static final int VERTICAL_ALIGN = 1;
	
	/** Default alignment */
	protected int align = 0;
	
	/** The border painted around the panel */
	protected boolean hasBorder;
	
	/** If all buttons belong to a button group */
	protected boolean hasGroup;
	
	/** The title in the border line */
	protected String title;	
	
	/** The name of the buttons */
	protected String[] buttonNames;
	
	/** The buttons */
	protected AbstractButton[] abButtonNames;
	
	/** The button group */
	protected ButtonGroup bgButtonGroup;
	
	/**
	 * Add the given actionListener to each <code>AbstractButton</code>
	 * @param actionListener
	 */
	public void addActionListenerToButtons(ActionListener actionListener)
	{
		for(AbstractButton ab : abButtonNames)
		{
			ab.addActionListener(actionListener);
		}
	}
	
	@Override
	/**
	 * Add the given keyListener to each <code>AbstractButton</code>
	 * @param keyListener
	 */
	public void addKeyListenerToComponents(KeyListener keyListener) 
	{
		super.addKeyListener(abButtonNames, keyListener);
	}
	
	/**
	 * Get the <code>AbstractButton</code> specified by the buttonName
	 * @param buttonName the name of the button
	 * @return the <code>AbstractButton</code>
	 */
	protected AbstractButton getButton(String buttonName)
	{
		AbstractButton button = null;
		
		for(int buttonNumber = 0; buttonNumber < buttonNames.length; buttonNumber++)
		{
			if(buttonName.equals(buttonNames[buttonNumber]))
			{
				button = abButtonNames[buttonNumber];
			}
		}
		
		return button;
	}
	
	/**
	 * Gets the text of the selected <code>AbstractButton</code>
	 * @return the text of the selected button
	 */
	public String getSelectedButtonText()
	{
		String buttonName = null;
		
		for(AbstractButton ab : abButtonNames)
		{
			if(ab.isSelected())
			{
				buttonName = ab.getText();
			}
		}
		
		return buttonName;
	}
	
	
	
	/**
	 * Remove the given actionListener to each <code>AbstractButton</code>
	 * @param actionListener
	 */
	public void removeActionListenerToButtons(ActionListener actionListener)
	{
		for(AbstractButton ab : abButtonNames)
		{
			ab.removeActionListener(actionListener);
		}
	}
	
	@Override
	/**
	 * Remove the given keyListener to each <code>AbstractButton</code>
	 * @param keyListener
	 */
	public void removeKeyListenerToComponents(KeyListener keyListener) 
	{
		super.removeKeyListener(abButtonNames, keyListener);
	}
	
	/**
	 * Sets the selected button by text
	 * @param buttonName the button to be selected
	 * @param selected the selection
	 */
	public void setSelectedButton(String buttonName, boolean selected)
	{
		for(AbstractButton ab : abButtonNames)
		{
			if(buttonName.equals(ab.getText()))
			{
				ab.setSelected(selected);
			}
		}
	}
}
