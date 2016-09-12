/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-05       42558JL Santiago SC      -Initial version, Java 5.0
 * 2010-04-12   ~1  45878MS Santiago SC      -Add removeKeyListenerToComponents()
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.panel.dynamic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.beans.IGSTextFieldPanelProperties;

/**
 * <code>IGSTextFieldPanel</code> is a <code>IGSDynamicPanel</code> that 
 * creates dynamically a panel containing <code>JTextField</code>s.
 * @author The MFS Development Team
 */
public class IGSTextFieldPanel extends IGSDynamicPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3681708842324890710L;
	
	/** Border line of the panel */
	boolean hasBorder;
	
	/** Title of the panel, which will be applied in the border */
	private String title;
	
	/** Field Names for the panel created in a <code>JLabel</code> */
	private String[] fieldNames;
	
	/** <code>JTextField</code>s created dynamically for each field name */
	private JTextField[] tfTextFields;	
	
	/** <code>JLabel</codes> created dynamically for each field name */
	private JLabel[] lblFields;
	
	/** <code>IGSTextFieldPanelProperties</code> contains panel properties */
	private IGSTextFieldPanelProperties  dfp;	
	
	/** Field types applied for each <code>JTextField</code> */
	private Class<?>[] fieldTypes;
	
	/**
	 * Creates a <code>IGSTextFieldPanel</code> with no title, a border,
	 * <code>JTextField</code>s and default <code>EFDyanmicFieldProperties</code>
	 * @param title of the panel
	 * @param fieldNames used to create <code>JTextField</code>s for the panel.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSTextFieldPanel(String title, String[] fieldNames) 
		throws IllegalAccessException, InstantiationException
	{
		this.title = title;
		this.fieldNames = fieldNames;
		this.hasBorder = true;
		
		initialize();
		createLayout();	
	}
	
	/**
	 * Creates a <code>IGSTextFieldPanel</code> with no title, a border,
	 * text fields specified by the fieldTypes parameter
	 * @param fieldNames used to create text fields in this panel
	 * @param fieldTypes defines the type of the text fields in the panel
	 * @param dfp defines the properties for this panel
	 * @param border the border line
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSTextFieldPanel(String title, String[] fieldNames, Class<?>[] fieldTypes, 
							IGSTextFieldPanelProperties dfp) 
		throws IllegalAccessException, InstantiationException
	{
		this.title = title;
		this.fieldNames = fieldNames;
		this.fieldTypes = fieldTypes;
		this.dfp = dfp;
		this.hasBorder = true;
		
		initialize();
		createLayout();		
	}
	
	/**
	 * Creates a <code>IGSTextFieldPanel</code> with no title, no border,
	 * <code>JTextField</code>s and default <code>EFDyanmicFieldProperties</code>
	 * @param fieldNames used to create <code>JTextField</code>s for the panel.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSTextFieldPanel(String[] fieldNames) 
		throws IllegalAccessException, InstantiationException
	{
		this.fieldNames = fieldNames;
		
		initialize();
		createLayout();	
	}		
	
	/**
	 * Creates a <code>IGSTextFieldPanel</code> with no title and no border,
	 * text fields specified by the fieldTypes parameter
	 * and a specific <code>EFDyanmicFieldProperties</code>
	 * @param fieldNames used to create text fields in this panel
	 * @param fieldTypes defines the type of the text fields in the panel
	 * @param dfp defines the properties for this panel
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IGSTextFieldPanel(String[] fieldNames, Class<?>[] fieldTypes, IGSTextFieldPanelProperties  dfp) 
		throws IllegalAccessException, InstantiationException
	{
		this.fieldNames = fieldNames;
		this.fieldTypes = fieldTypes;
		this.dfp = dfp;
		
		initialize();
		createLayout();		
	}	
	
	@Override
	/**
	 * Add the <code>KeyListener</code> to each <code>JTextField</code>
	 */
	public void addKeyListenerToComponents(KeyListener keyListener) 
	{
		super.addKeyListener(tfTextFields, keyListener);		
	}

	/**
	 * Set to blanks the value of each <code>JTextField</code>
	 */
	public void clearTextFieldValues()
	{
		for(JTextField textField : tfTextFields)
		{
			textField.setText("");
		}
	}
	
	/** Creates the layout for this panel */
	protected void createLayout()
		throws IllegalAccessException, InstantiationException
	{		
		/* Sum IGSTextFieldPanelProperties .middleMargin to the fieldAlignt to get more space
		 *  between the JLabel and the JTextField.
		 */
		int fieldAlign = dfp.getLeftMargin() + getFieldAlignment(fieldNames) + dfp.getMiddleMargin();
		
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

		// if the fields do not have a fixed size, enlarge the field to fill the panel
		if(!dfp.hasFixedSize())
		{
			gbc.weightx = 1.0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
		}
		
		gbc.weighty = 1.0;				
		
		for(int fieldNumber = 0; fieldNumber < fieldNames.length; fieldNumber++)
		{					
			gbc.gridy++;
			
			// Create the label and add it to the panel
			gbc.insets = new Insets(dfp.getTopMargin(), dfp.getLeftMargin(),
									dfp.getBottomMargin(), dfp.getRightMargin());
			
			lblFields[fieldNumber] = new JLabel(fieldNames[fieldNumber]);
			add(lblFields[fieldNumber], gbc);
						
			// Create the field and add it to the panel
			gbc.insets = new Insets(dfp.getTopMargin(), fieldAlign, 
									dfp.getBottomMargin(), dfp.getRightMargin());
			
			// The fieldTypes must be a JTextField or a subclass of JTextField else throws exception
			tfTextFields[fieldNumber] = (JTextField) fieldTypes[fieldNumber].newInstance();
			tfTextFields[fieldNumber].setName(fieldNames[fieldNumber]);
			tfTextFields[fieldNumber].setText(dfp.getInitialValue());
			tfTextFields[fieldNumber].setColumns(dfp.getColumns());
			add(tfTextFields[fieldNumber], gbc);			
			
			// Set the label for the text field
			lblFields[fieldNumber].setLabelFor(tfTextFields[fieldNumber]);
		}	
	}
	
	/**
	 * Determines the longest fieldName in the fieldNames array and returns
	 * an integer representing the length.
	 * @param fieldNames used to create text fields in this panel
	 * @return int representing the length of the longest fieldName
	 */
	protected int getFieldAlignment(String[] fieldNames)
	{
		/* In this block we are going to determine which fieldName is the largest, that will
		 * help us to align the label and fields much better. Then multiply the fieldAlign
		 * by 7 as the JLabel does to calculate the preferred size.
		 */
		int fieldAlign = 0;
		
		for(String fieldName : fieldNames)
		{
			int fieldLength = fieldName.length();
			
			if(fieldAlign < fieldLength)
			{
				fieldAlign = fieldLength;
			}
		}		
		
		fieldAlign = (fieldAlign * 7);
		
		return fieldAlign;
	}
	
	/**
	 * Get the array of <code>JLabel</code>s of this panel
	 * @return the array of <code>JLabel</code>s
	 */
	public JLabel[] getLabels()
	{
		return lblFields;
	}

	/**
	 * Gets a <code>JTextField</code> specified by the fieldName
	 * @param fieldName of the <code>JTextField</code>
	 * @return a <code>JTextField</code>
	 */
	public JTextField getTextField(String fieldName)
	{
		JTextField textField = null;
		
		for(int fieldNumber = 0; fieldNumber < fieldNames.length; fieldNumber++)
		{
			if(fieldName.equals(fieldNames[fieldNumber]))
			{
				textField = tfTextFields[fieldNumber];
			}
		}
		
		return textField;		
	}
	
	/**
	 * Get the array of <code>JTextField</code>s of this panel
	 * @return the array of <code>JTextField</code>s
	 */
	public JTextField[] getTextFields()
	{
		return tfTextFields;
	}
	
	/**
	 * Get an array of the text field values of each <code>JTextField</code>
	 * @return a <code>String</code> array of text field values.
	 */
	public String[] getTextFieldValues()
	{
		String[] fieldValues = new String[tfTextFields.length];
		
		for(int fieldNumber = 0; fieldNumber < tfTextFields.length; fieldNumber++)
		{
			fieldValues[fieldNumber] = tfTextFields[fieldNumber].getText();			
		}
		
		return fieldValues;
	}

	/**
	 * Initializes this panel and validates the info passed in the constructor.
	 */
	private void initialize()
	{
		if(null == dfp)
		{
			dfp = new IGSTextFieldPanelProperties ();
		}
		
		// Initialize fieldTypes with JTextFields by default
		if(null == fieldTypes)
		{
			fieldTypes = new Class<?>[fieldNames.length];
			
			for(int fieldNumber = 0; fieldNumber < fieldTypes.length; fieldNumber++)
			{
				fieldTypes[fieldNumber] = JTextField.class;
			}
		}
		// Length of fieldNames and fieldTypes must be the same!
		else if(fieldNames.length != fieldTypes.length)
		{
			throw new IndexOutOfBoundsException("fieldNames length and fieldTypes" +
												" length do not match");
		}
		
		// Initialize the tfTextFields
		lblFields = new JLabel[fieldNames.length];
		tfTextFields = new JTextField[fieldNames.length];				
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void removeKeyListenerToComponents(KeyListener keyListener) 
	{
		super.removeKeyListener(tfTextFields, keyListener);	
	}
}
