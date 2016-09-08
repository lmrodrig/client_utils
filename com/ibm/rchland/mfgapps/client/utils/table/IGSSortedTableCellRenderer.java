/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 * 2006-09-25   ~1 35394PL  R Prechel        -Accessibility changes for icon & tooltip
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.ibm.rchland.mfgapps.client.utils.icon.IGSVerticalArrowIcon;

/**
 * <code>IGSSortedTableCellRenderer</code> is a <code>TableCellRenderer</code>
 * for rendering the cells of a <code>JTableHeader</code> with an
 * <code>IGSVerticalArrowIcon</code> indicating the sort column and direction.
 * This class is an implementation of the <i>Decorator Pattern</i> since it
 * decorates the table cell renderer component returned by a different
 * <code>TableCellRenderer</code>.
 * @author Ryan Prechel
 * @version 1.0
 */
public class IGSSortedTableCellRenderer
	implements TableCellRenderer
{
	/** An instance of an up <code>IGSVerticalArrowIcon</code>. */
	private static IGSVerticalArrowIcon ascendingArrow = null;

	/** An instance of a down <code>IGSVerticalArrowIcon</code>. */
	private static IGSVerticalArrowIcon descendingArrow = null;

	/** The accessible name and icon description of the ascending arrow. */
	private static String ascendingArrowText; //~1A

	/** The accessible name and icon description of the descending arrow. */
	private static String descendingArrowText; //~1A

	/**
	 * The <code>TableCellRenderer</code> that provides the
	 * <code>JLabel</code> used to render the cell that this class decorates
	 * with an <code>IGSVerticalArrowIcon</code>.
	 */
	private TableCellRenderer tableCellRenderer;

	/**
	 * Constructs a new <code>IGSSortedTableCellRenderer</code>.
	 * @param tableCellRenderer the <code>TableCellRenderer</code> that
	 *        provides the <code>JLabel</code> used to render the cell that
	 *        this class decorates with an <code>IGSVerticalArrowIcon</code>
	 */
	public IGSSortedTableCellRenderer(TableCellRenderer tableCellRenderer)
	{
		this.tableCellRenderer = tableCellRenderer;
	}

	/**
	 * Returns the <code>Component</code> used for drawing a cell.
	 * @param table the <code>JTable</code> that is asking the renderer to
	 *        draw a cell
	 * @param value the value of the cell to be rendered
	 * @param isSelected <code>true</code> if the cell is to be rendered as
	 *        selected
	 * @param hasFocus <code>true</code> if the cell is to be rendered as
	 *        having the focus
	 * @param row the row index of the cell being drawn (-1 for the header)
	 * @param column the column index of the cell being drawn
	 * @return the <code>Component</code> used for drawing a cell
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
													boolean hasFocus, int row, int column)
	{
		Component c = this.tableCellRenderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		if (c instanceof JLabel)
		{
			JLabel label = (JLabel) c;
			//~1A Added StringBuffer for tooltip
			StringBuffer toolTip = new StringBuffer();
			toolTip.append(label.getText());
			if (table.getModel() instanceof IGSSortableTableModel)
			{
				IGSSortableTableModel tableModel = (IGSSortableTableModel) table.getModel();
				label.setHorizontalTextPosition(SwingConstants.LEFT);

				if (table.getColumnModel().getColumn(column).getModelIndex() == tableModel.getSortColumnIndex()
						&& tableModel.getSortingDirection() != IGSSortableTableModel.NOT_SORTED)
				{
					boolean ascending = (tableModel.getSortingDirection() == IGSSortableTableModel.ASCENDING);
					IGSVerticalArrowIcon icon = getArrow(ascending, label.getFont().getSize());
					label.setIcon(icon);
					//~1A Set tooltip information
					String desc = icon.getAccessibleDescription();
					if (desc != null)
					{
						toolTip.append(": "); //$NON-NLS-1$
						toolTip.append(desc);
					}
				}
				else
				{
					label.setIcon(null);
				}
			}
			label.setToolTipText(toolTip.toString());
		}
		return c;
	}

	/**
	 * Returns the <code>TableCellRenderer</code> decorated by this
	 * <code>IGSSortedTableCellRenderer</code>.
	 * @return the decorated <code>TableCellRenderer</code>
	 */
	public TableCellRenderer getDecoratedTableCellRender()
	{
		return this.tableCellRenderer;
	}

	/**
	 * Sets the accessible name and icon description of the arrows.
	 * @param ascending the accessible name and icon description of the ascending arrow
	 * @param descending the accessible name and icon description of the descending arrow
	 */
	public static void setArrowText(String ascending, String descending)
	{ //~1A New method
		ascendingArrowText = ascending;
		descendingArrowText = descending;

		if (ascendingArrow != null)
		{
			ascendingArrow.setAccessibleName(ascending);
			ascendingArrow.setAccessibleIconDescription(ascending);
		}

		if (descendingArrow != null)
		{
			descendingArrow.setAccessibleName(descending);
			descendingArrow.setAccessibleIconDescription(descending);
		}
	}

	/**
	 * Returns an <code>IGSVerticalArrowIcon</code> of the specified
	 * <code>size</code> for the direction indicated by <code>ascending</code>.
	 * @param ascending <code>true</code> for an up
	 *        <code>IGSVerticalArrowIcon</code>; <code>false</code> for a
	 *        down <code>IGSVerticalArrowIcon</code>
	 * @param size the size of the <code>IGSVerticalArrowIcon</code>
	 * @return the <code>IGSVerticalArrowIcon</code>
	 */
	private static IGSVerticalArrowIcon getArrow(boolean ascending, int size)
	{
		IGSVerticalArrowIcon result = null;
		if (ascending)
		{
			if (IGSSortedTableCellRenderer.ascendingArrow == null
					|| IGSSortedTableCellRenderer.ascendingArrow.getIconHeight() != size)
			{
				IGSSortedTableCellRenderer.ascendingArrow = new IGSVerticalArrowIcon(size, ascending);
				if (IGSSortedTableCellRenderer.ascendingArrowText != null)
				{ //~1A set the accessible name and icon description
					IGSSortedTableCellRenderer.ascendingArrow.setAccessibleName(IGSSortedTableCellRenderer.ascendingArrowText);
					IGSSortedTableCellRenderer.ascendingArrow.setAccessibleIconDescription(IGSSortedTableCellRenderer.ascendingArrowText);
				}
			}
			result = IGSSortedTableCellRenderer.ascendingArrow;
		}
		else
		{
			if (IGSSortedTableCellRenderer.descendingArrow == null
					|| IGSSortedTableCellRenderer.descendingArrow.getIconHeight() != size)
			{
				IGSSortedTableCellRenderer.descendingArrow = new IGSVerticalArrowIcon(size, ascending);
				if (IGSSortedTableCellRenderer.descendingArrowText != null)
				{ //~1A set the accessible name and icon description
					IGSSortedTableCellRenderer.descendingArrow.setAccessibleName(IGSSortedTableCellRenderer.descendingArrowText);
					IGSSortedTableCellRenderer.descendingArrow.setAccessibleIconDescription(IGSSortedTableCellRenderer.descendingArrowText);
				}
			}
			result = IGSSortedTableCellRenderer.descendingArrow;
		}
		return result;
	}
}
