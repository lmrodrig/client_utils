/* © Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-21      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 * 2006-09-25   ~1 35394PL  R Prechel        -Use JButton to render IGSCellInformation
 *                                           -Removed cellInformationBackground
 *                                           -Removed constructor with Color parameter
 *                                           -Set tooltip, font, and foreground of JLabel
 * 2007-05-03   ~2 37078MG  M Barth          -Add ability to change color of specified rows
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.ibm.rchland.mfgapps.client.utils.IGSUIManager;

import java.util.Vector;

/**
 * <code>IGSTableCellRenderer</code> is a <code>TableCellRenderer</code> for
 * rendering cells containing a <code>JComponent</code> or an
 * <code>IGSCellInformation</code>. This class is an implementation of the
 * <i>Decorator Pattern</i>, since it decorates the table cell renderer
 * component returned by a different <code>TableCellRenderer</code> when
 * rendering a cell that does not contain a <code>JComponent</code> or an
 * <code>IGSCellInformation</code>.
 * @author The External Fulfillment Client Development Team
 */
public class IGSTableCellRenderer
	implements TableCellRenderer
{
	/**
	 * The <code>TableCellRenderer</code> that provides the
	 * <code>Component</code> used to render a cell that this class decorates
	 * if the cell does not contain a <code>JComponent</code> or an
	 * <code>IGSCellInformation</code>.
	 */
	private TableCellRenderer tableCellRenderer;

	/**
	 * The <code>JButton</code> used to render an
	 * <code>IGSCellInformation</code>.
	 */
	private JButton button = new JButton(); //~1A

	/** The row of the armed <code>JButton</code>. */
	private int armedRow = -1; //~1A

	/** The column of the armed <code>JButton</code>. */
	private int armedColumn = -1; //~1A

	//~2
	/** The matching row. */
	@SuppressWarnings("rawtypes")
	private Vector matchRow = new Vector();

	//~2
	/** The matching background color. */
	private String backColor;

	//~2
	/** The matching foreground color. */
	private String foreColor;

	//~2
	/** The matching column name. */
	private String matchColName;

	/**
	 * Constructs a new <code>IGSTableCellRenderer</code>.
	 * @param tableCellRenderer the default <code>TableCellRenderer</code> for
	 *        the <code>JTable</code> that provides the <code>Component</code>
	 *        used to render a cell that this class decorates if the cell does
	 *        not contain a <code>JComponent</code> or an
	 *        <code>IGSCellInformation</code>
	 */
	public IGSTableCellRenderer(TableCellRenderer tableCellRenderer)
	{
		//~1C Removed constructor chain
		this.tableCellRenderer = tableCellRenderer;
	}

	/**
	 * Returns the <code>Component</code> used for drawing a cell. If
	 * <code>value</code> is an instance of <code>JComponent</code>,
	 * <code>value</code> is returned. If <code>value</code> is an instance
	 * of <code>IGSCellInformation</code>, a <code>JButton</code> is
	 * returned. Otherwise, the <code>Component</code> returned by
	 * <code>tableCellRenderer</code> is decorated and returned.
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
	@SuppressWarnings("unchecked")
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
													boolean hasFocus, int row, int column)
	{
		//~2
		if (table.getModel() instanceof IGSSortableTableModel)
		{
			IGSSortableTableModel model = (IGSSortableTableModel)table.getModel();
			for (int i=0; i<model.getColumnCount(); i++)
			{
				if (model.getColumnName(i).equals(this.matchColName))
				{
					if (row >= this.matchRow.size()) this.matchRow.add(row, model.getValueAt(row,i).toString());
					else this.matchRow.set(row, model.getValueAt(row,i).toString());
				}
			}
		}
		if (value instanceof JComponent)
		{
			return (JComponent) value;
		}

		//~1A Start change to use a JButton for IGSCellInformation
		if (value instanceof IGSCellInformation)
		{
			if (isSelected)
			{
				this.button.setForeground(table.getSelectionForeground());
			}
			else
			{
				this.button.setForeground(table.getForeground());
			}

			this.button.getModel().setArmed(row == this.armedRow && column == this.armedColumn);
			this.button.setText(value.toString());
			this.button.setFont(this.button.getFont().deriveFont(
					isSelected ? Font.BOLD | Font.ITALIC : Font.PLAIN));
			this.button.setToolTipText(value.toString());
			return this.button;
		}
		//~1A End change to use a JButton for IGSCellInformation

		//~1C Moved Component c and removed else
		Component c = this.tableCellRenderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		if (c instanceof JLabel)
		{
			JLabel label = (JLabel) c;
			//~2
			if (value instanceof IGSMatchIndicator)
			{
				if (isSelected)
				{
					label.setBackground(table.getSelectionBackground());
					label.setForeground(table.getSelectionForeground());
				}
				else
				{
					if(!value.toString().equals(""))//$NON-NLS-1$
					{
						IGSUIManager uiManager = IGSUIManager.getInstance();
						uiManager.setComponentColors(label, this.backColor, this.foreColor);
					}
					else
					{
						label.setBackground(table.getBackground());
						label.setForeground(table.getForeground());
					}
				}
				label.setText(value.toString());
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setFont(label.getFont().deriveFont(
						isSelected ? Font.BOLD | Font.ITALIC : Font.PLAIN));
				label.setToolTipText(((IGSMatchIndicator)value).getToolTip());
			}
			else
			{
				if (isSelected)
				{
					label.setBackground(table.getSelectionBackground());
					label.setForeground(table.getSelectionForeground()); //~1A
				}
				else
				{
					//~2 Use specified colors for matching rows to be colored
					if(this.matchRow != null && this.matchRow.size()>row && !this.matchRow.get(row).equals(""))//$NON-NLS-1$
					{
						IGSUIManager uiManager = IGSUIManager.getInstance();
						uiManager.setComponentColors(label, this.backColor, this.foreColor);
					}
					else
					{
						label.setBackground(table.getBackground());
						label.setForeground(table.getForeground()); //~1A
					}
				}
				label.setHorizontalAlignment(SwingConstants.LEFT);
				label.setFont(label.getFont().deriveFont(
						isSelected ? Font.BOLD | Font.ITALIC : Font.PLAIN)); //~1A
				label.setToolTipText(label.getText()); //~1A
			}
		}
		return c;
	}

	/**
	 * Returns the <code>TableCellRenderer</code> decorated by this
	 * <code>IGSTableCellRenderer</code>.
	 * @return the decorated <code>TableCellRenderer</code>
	 */
	public TableCellRenderer getDecoratedTableCellRender()
	{
		return this.tableCellRenderer;
	}

	/**
	 * Sets the row of the armed <code>JButton</code>.
	 * @param row the row of the armed <code>JButton</code>; a negative value
	 *        if no <code>JButton</code> is armed
	 */
	public void setRow(int row)
	{ //~1A new method
		this.armedRow = row;
	}

	/**
	 * Sets the column of the armed <code>JButton</code>.
	 * @param column the column of the armed <code>JButton</code>; a negative
	 *        value if no <code>JButton</code> is armed
	 */
	public void setColumn(int column)
	{ //~1A new method
		this.armedColumn = column;
	}

	//~2
	/**
	 * Sets the <code>Vector</code> of rows to be colored.
	 * @param back the background color to be used.
	 * @param fore the foreground color to be used.
	 */
	public void setColorRows(String back, String fore)
	{
		this.backColor = back;
		this.foreColor = fore;
	}

	//~2
	/**
	 * Sets the <code>String</code> with the Match column name.
	 * @param colName the Match column name to be used.
	 */
	public void setMatchColName(String colName)
	{
		this.matchColName = colName;
	}
}