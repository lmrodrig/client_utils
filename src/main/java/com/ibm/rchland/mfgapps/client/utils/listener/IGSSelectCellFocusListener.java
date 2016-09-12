/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-24      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;

import javax.swing.JTable;

/**
 * <code>IGSSelectCellFocusListener</code> is a <code>FocusListener</code>
 * that selects the first cell in a <code>JTable</code> when the
 * <code>JTable</code> gains the keyboard focus and deselects all selected
 * cells when the <code>JTable</code> loses the keyboard focus.
 * @author The External Fulfillment Client Development Team
 */
public class IGSSelectCellFocusListener
	implements FocusListener, Serializable
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** Constructs a new <code>IGSSelectCellFocusListener</code>. */
	public IGSSelectCellFocusListener()
	{
		//Nothing to do
	}

	/**
	 * Invoked when a <code>Component</code> gains the keyboard focus.
	 * @param fe the <code>FocusEvent</code>
	 */
	public void focusGained(FocusEvent fe)
	{
		Object source = fe.getSource();
		if (source instanceof JTable)
		{
			JTable table = (JTable) source;
			if (table.getSelectedRow() == -1 && table.getRowCount() > 0)
			{
				table.addColumnSelectionInterval(0, 0);
				table.addRowSelectionInterval(0, 0);
			}
		}
	}

	/**
	 * Invoked when a <code>Component</code> loses the keyboard focus.
	 * @param fe the <code>FocusEvent</code>
	 */
	public void focusLost(FocusEvent fe)
	{
		Object source = fe.getSource();
		if (source instanceof JTable)
		{
			((JTable) source).clearSelection();
		}
	}
}
