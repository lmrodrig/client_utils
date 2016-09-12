/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-15       45878MS Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.panel.dynamic;

import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

/**
 * <code>IGSCheckBoxPanel</code> is a <code>IGSToggleButtonPanel</code> that 
 * creates dynamically a panel containing <code>JCheckBox</code>es.
 * @author The MFS Development Team
 */
public class IGSCheckBoxPanel extends IGSToggleButtonPanel
{		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1901248256560240868L;		
	
	
	/**
	 * Builds an <code>IGSCheckBoxPanel</code> containing <code>JCheckBox</code>es.
	 * @param title the panel title
	 * @param buttonNames the name of the <code>JCheckBox</code>es
	 * @param align the alignment of the <code>JCheckBox</code>es
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSCheckBoxPanel(String title, String[] buttonNames, int align) 
		throws IllegalAccessException, InstantiationException
	{
		super.buttonType = JCheckBox.class;
		super.title = title;
		super.buttonNames = buttonNames;
		super.align = align;
		super.hasBorder = true;
		super.hasGroup = false;
		
		super.createLayout();
	}
	
	
	/**
	 * Builds an <code>IGSCheckBoxPanel</code> containing <code>JCheckBox</code>es.
	 * @param buttonNames the name of the <code>JCheckBox</code>es
	 * @param align the alignment of the <code>JCheckBox</code>es
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSCheckBoxPanel(String[] buttonNames, int align) 
		throws IllegalAccessException, InstantiationException
	{
		super.buttonType = JCheckBox.class;
		super.buttonNames = buttonNames;
		super.align = align;
		super.hasGroup = false;

		super.createLayout();
	}

	/**
	 * Checks if all <code>JCheckBox</code>es are selected
	 * @return true if all <code>JCheckBox</code>es are selected, false otherwise
	 */
	public boolean areAllSelected()
	{
		boolean areSelected = true;
		
		for(AbstractButton ab : abButtonNames)
		{
			if(!ab.isSelected())
			{
				areSelected = false;
				break;
			}
		}
		
		return areSelected;
	}
	
	/**
	 * Checks if all <code>JCheckBox</code>es are selected except the <code>JCheckBox</code>
	 * specified by the <code>String</code> buttonName.
	 * @param buttonName the <code>JCheckBox</code> that should not be selected.
	 * @return true if all <code>JCheckBox</code>es are selected except the given name.
	 */
	public boolean areAllSelectedExcept(String buttonName)
	{
		boolean found = false;
		boolean areSelectedExcept = true;
		
		for(int buttonNumber = 0; buttonNumber < buttonNames.length; buttonNumber++)
		{
			if(buttonName.equals(buttonNames[buttonNumber]))
			{
				found = true;
				
				if(abButtonNames[buttonNumber].isSelected())
				{
					areSelectedExcept = false;
					break;
				}
			}
			else
			{
				if(!abButtonNames[buttonNumber].isSelected())
				{
					areSelectedExcept = false;
					break;
				}
			}
		}
		
		// The buttonName was not found in the abButtonNames array
		if(!found)
		{
			areSelectedExcept = false;
		}

		return areSelectedExcept;
	}
	
	/**
	 * Gets the <code>JCheckBox</code> by name
	 * @param buttonName the name of the <code>JCheckBox</code>
	 * @return a <code>JCheckBox</code>
	 */
	public JCheckBox getCheckBox(String buttonName)
	{
		return ((JCheckBox) super.getButton(buttonName));
	}
	
	/**
	 * Get the array of <code>JCheckBox</code>es
	 * @return the array of <code>JCheckBox</code>es.
	 */
	public JCheckBox[] getCheckBoxes()
	{
		JCheckBox[] checkBoxes = new JCheckBox[abButtonNames.length];
		
		for(int index = 0; index < checkBoxes.length; index++)
		{
			checkBoxes[index] = (JCheckBox) abButtonNames[index];
		}
		
		return checkBoxes;
	}
	
	/**
	 * Gets an array of <code>String</code> containing the text/name for each selected
	 * <code>JCheckBox</code>.
	 * @return the <code>ArrayList</code> of selected buttons.
	 */
	public ArrayList<String> getSelectedButtonsTexts()
	{
		ArrayList<String> texts = new ArrayList<String>();
		
		for(AbstractButton ab : abButtonNames)
		{
			if(ab.isSelected())
			{
				texts.add(ab.getText());
			}
		}
		
		texts.trimToSize();
		
		return texts;
	}
	
	/**
	 * Gets an array of <code>Integer</code> containing the indexes of each selected
	 * button.
	 * @return the <code>ArrayList</code> of selected buttons indexes.
	 */
	public ArrayList<Integer> getSelectedButtonsIndexes()
	{
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		for(int index = 0; index < abButtonNames.length; index++)
		{
			if(abButtonNames[index].isSelected())
			{
				indexes.add(index);
			}
		}
		
		indexes.trimToSize();
		
		return indexes;
	}
	
	/**
	 * Select all buttons
	 * @param select option true or false
	 */
	public void selectAllButtons(boolean select)
	{
		for(AbstractButton ab : abButtonNames)
		{
			ab.setSelected(select);
		}
	}
}
