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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

/**
 * <code>IGSToggleButtonPanel</code> is a <code>IGSDynamicButtonPanel</code> that 
 * creates dynamically a panel containing <code>JToggleButton</code>s.
 * @author The MFS Development Team
 */
public abstract class IGSToggleButtonPanel extends IGSDynamicButtonPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -630769473678541323L;

	/** The toggle buttons type */
	protected Class<?> buttonType;	

	/** Creates the layout for the <code>IGSToggleButtonPanel</code> */
	protected void createLayout() 
		throws IllegalAccessException, InstantiationException
	{	    
		// Variables are inherited
		
		// Initialize bgButtonGroup and group
		abButtonNames = new JToggleButton[buttonNames.length];
		
		if(hasGroup)
		{
			bgButtonGroup = new ButtonGroup();
		}
		
		// Set panel layout and title
		setLayout(new GridBagLayout());
		
		if(hasBorder)
		{
			setBorder(BorderFactory.createTitledBorder(title));
		}
		
		// Vertical alignment is DEFAULT  
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.RELATIVE,
				new Insets(0, 0, 0, 0), 0, 0);								

		if(HORIZONTAL_ALIGN == align)
		{
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.gridwidth = 1;
		}
		
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		// Create a button for each buttonName
		for(int buttonNumber = 0; buttonNumber < buttonNames.length; buttonNumber++)
		{
			if(HORIZONTAL_ALIGN == align)
			{
				gbc.gridx++;
			}
			else
			{
				gbc.gridy++;
			}
			
		    gbc.insets = new Insets(0, 10, 0, 0);
		    
		    // Create the button
		    abButtonNames[buttonNumber] = (JToggleButton) buttonType.newInstance();
		    abButtonNames[buttonNumber].setName(buttonNames[buttonNumber]);
		    abButtonNames[buttonNumber].setText(buttonNames[buttonNumber]);
		    
		    if(hasGroup)
		    {
		    	bgButtonGroup.add(abButtonNames[buttonNumber]);
		    }		    		   
		    
		    // Add the button to the Panel
		    add(abButtonNames[buttonNumber], gbc);
		}		
	}
}
