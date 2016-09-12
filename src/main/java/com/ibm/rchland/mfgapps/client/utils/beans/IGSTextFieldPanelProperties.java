/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-13       42558JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.beans;

/**
 * <code>IGSTextFieldPanelProperties</code> contains properties to edit alignment,
 * initial values, fixed size for <code>IGSTextFieldPanel</code>.
 * @author The MFS Development Team
 */
public class IGSTextFieldPanelProperties 
{
	/** If false the the <code>JTextField</code> will expand to fill the panel */
	private boolean fixedSize = true;
	
	/** Number of initial columns for a <code>JTextField</code> */
	private int columns = 10;
	
	/** Top margin between the panel and the first <code>JTextField</code> */
	private int topMargin = 0;
	
	/** Left margin between the panel and the first <code>JLabel</code> of 
	 * the first <code>JTextField</code> */
	private int leftMargin = 10;
	
	/** Bottom margin between the panel and the first <code>JTextField</code> */
	private int bottomMargin = 0;
	
	/** Right margin between the panel and the first <code>JTextField</code> */
	private int rightMargin = 10;
	
	/** Margin between the <code>JLabel</code> and <code>JTextField</code> */
	private int middleMargin = 10;
	
	/** Initial value of each <code>JTextField</code> */
	private String initialValue = "";	

	/**
	 * @return the bottomMargin
	 */
	public int getBottomMargin() {
		return bottomMargin;
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @return the initialValue
	 */
	public String getInitialValue() {
		return initialValue;
	}

	/**
	 * @return the leftMargin
	 */
	public int getLeftMargin() {
		return leftMargin;
	}

	/**
	 * @return the middleMargin
	 */
	public int getMiddleMargin() {
		return middleMargin;
	}

	/**
	 * @return the rightMargin
	 */
	public int getRightMargin() {
		return rightMargin;
	}

	/**
	 * @return the topMargin
	 */
	public int getTopMargin() {
		return topMargin;
	}

	/**
	 * @return the fixedSize
	 */
	public boolean hasFixedSize() {
		return fixedSize;
	}

	/**
	 * @param bottomMargin the bottomMargin to set
	 */
	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * @param fixedSize the fixedSize to set
	 */
	public void setFixedSize(boolean fixedSize) {
		this.fixedSize = fixedSize;
	}

	/**
	 * @param initialValue the initialValue to set
	 */
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	/**
	 * @param leftMargin the leftMargin to set
	 */
	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	/**
	 * @param middleMargin the middleMargin to set
	 */
	public void setMiddleMargin(int middleMargin) {
		this.middleMargin = middleMargin;
	}

	/**
	 * @param rightMargin the rightMargin to set
	 */
	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	/**
	 * @param topMargin the topMargin to set
	 */
	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}
}
