/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-05       45878MS Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.panel.dynamic;

import javax.swing.JRadioButton;

/**
 * <code>IGSRadioButtonPanel</code> is a <code>IGSToggleButtonPanel</code> that 
 * creates dynamically a panel containing <code>JRadioButton</code>s.
 * @author The MFS Development Team
 */
public class IGSRadioButtonPanel extends IGSToggleButtonPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6979794833654484050L;	
	
	/**
	 * Builds an <code>IGSRadioButtonPanel</code> containing <code>JRadioButton</code>s.
	 * @param title the panel title
	 * @param buttonNames the name of the <code>JRadioButton</code>s
	 * @param align the alignment of the <code>JRadioButton</code>s
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSRadioButtonPanel(String title, String[] buttonNames, int align) 
		throws IllegalAccessException, InstantiationException
	{
		super.buttonType = JRadioButton.class;
		super.title = title;
		super.buttonNames = buttonNames;
		super.align = align;
		super.hasBorder = true;
		super.hasGroup = true;
		
		super.createLayout();
		
		// Select the first JRadioButton by Default
		super.abButtonNames[0].setSelected(true);
	}
	
	/**
	/**
	 * Builds an <code>IGSRadioButtonPanel</code> containing <code>JRadioButton</code>s.
	 * @param buttonNames the name of the <code>JRadioButton</code>s
	 * @param align the alignment of the <code>JRadioButton</code>s
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSRadioButtonPanel(String[] buttonNames, int align) 
		throws IllegalAccessException, InstantiationException
	{
		super.buttonType = JRadioButton.class;
		super.buttonNames = buttonNames;
		super.align = align;
		super.hasGroup = true;
		
		super.createLayout();
		
		// Select the first JRadioButton by Default
		super.abButtonNames[0].setSelected(true);
	}	
	
	/**
	 * Gets the <code>JRadioButton</code> by name
	 * @param buttonName the name of the <code>JRadioButton</code>
	 * @return a <code>JRadioButton</code>
	 */
	public JRadioButton getRadioButton(String buttonName)
	{
		return ((JRadioButton) super.getButton(buttonName));
	}
	
	/**
	 * Get the array of <code>JRadioButton</code>s
	 * @return the array of <code>JRadioButton</code>s.
	 */
	public JRadioButton[] getRadioButtons()
	{
		JRadioButton[] radioButtons = new JRadioButton[abButtonNames.length];
		
		for(int index = 0; index < radioButtons.length; index++)
		{
			radioButtons[index] = (JRadioButton) abButtonNames[index];
		}
		
		return radioButtons;
	}
}
