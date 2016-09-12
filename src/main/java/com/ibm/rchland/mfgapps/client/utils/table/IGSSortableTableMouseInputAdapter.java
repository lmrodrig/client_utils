/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-25      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;

import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.event.MouseInputAdapter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * <code>IGSSortableTableMouseInputAdapter</code> is a subclass of
 * <code>MouseInputAdapter</code> used to handle <code>MouseEvent</code>s
 * on the <code>JTableHeader</code> of a <code>JTable</code> that uses an
 * <code>IGSSortableTableModel</code> as its <code>TableModel</code>.
 * @author The External Fulfillment Client Development Team
 */
public class IGSSortableTableMouseInputAdapter
	extends MouseInputAdapter
	implements Serializable
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** <code>true</code> iff a mouse click should change the sort order. */
	private boolean sort = false;

	/** Constructs a new <code>IGSSortableTableMouseInputAdapter</code>. */
	public IGSSortableTableMouseInputAdapter()
	{
		//Nothing to do
	}

	/**
	 * Invoked when a mouse button has been pressed on a <code>Component</code>.
	 * Sets sort to <code>true</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mousePressed(MouseEvent me)
	{
		if (me.getButton() == MouseEvent.BUTTON1)
		{
			this.sort = true;
		}
	}

	/**
	 * Invoked when a mouse button has been pressed on a <code>Component</code>
	 * and then dragged. Sets sort to <code>false</code> to prevent the sort
	 * order from changing when the user drags a cell of the table header.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseDragged(MouseEvent me)
	{
		if (me.getButton() == MouseEvent.BUTTON1)
		{
			this.sort = false;
		}
	}

	/**
	 * Invoked when a mouse button has been clicked on a <code>Component</code>.
	 * Changes the sort order if {@link #sort} is still <code>true</code> from
	 * the call to {@link #mousePressed(MouseEvent)} and the source of the
	 * <code>MouseEvent</code> is the <code>JTableHeader</code> of a
	 * <code>JTable</code> that uses an <code>IGSSortableTableModel</code>
	 * as its <code>TableModel</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseClicked(MouseEvent me)
	{
		if (me.getButton() == MouseEvent.BUTTON1)
		{
			Object source = me.getSource();
			if (this.sort == true && source instanceof JTableHeader)
			{
				JTableHeader header = (JTableHeader) source;
				TableModel model = header.getTable().getModel();
				if (model instanceof IGSSortableTableModel)
				{
					IGSSortableTableModel sortModel = (IGSSortableTableModel) model;
					TableColumnModel columnModel = header.getColumnModel();
					int index = columnModel.getColumnIndexAtX(me.getX());
					index = columnModel.getColumn(index).getModelIndex();
					sortModel.setSortingDirection(index);
				}
			}
		}
	}
}
