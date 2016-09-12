/* @ Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 * 2006-09-24   ~1 35394PL  R Prechel        -Removed MouseHandler class and
 *                                            used IGSSortableTableMouseInputAdapter
 *                                           -setSortingDirection changes/addition
 *                                           -Made MyComparator a named class
 * 2008-01-16   ~2 39619JL  R Prechel        -Support specifying Comparator
 * 2008-04-22   ~3 41258JM  D Pietrasik      -make setSortingDirection public for use
 *                                            setting defaults
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * <code>IGSSortableTableModel</code> is a <code>TableModel</code> with the
 * ability to sort its data based on the values in a single column.
 * @author The External Fulfillment Client Development Team
 */
public class IGSSortableTableModel
	implements TableModel
{
	/** Indicates the rows are not to be sorted. */
	public static final int NOT_SORTED = 0;

	/** Indicates the rows are to be sorted in ascending order. */
	public static final int ASCENDING = 1;

	/** Indicates the rows are to be sorted in descending order. */
	public static final int DESCENDING = 2;

	/** The list of <code>EventListener</code>s. */
	private EventListenerList listenerList = new EventListenerList();

	/** The <code>Comparator</code> used to sort the model data. */
	@SuppressWarnings("rawtypes")
	private final Comparator comparator; //~2C

	/**
	 * The array of arrays of <code>Object</code> values stored by the
	 * <code>TableModel</code>.
	 */
	protected Object data[][];

	/** The array of column names for the <code>TableModel</code>. */
	private String[] names;

	/**
	 * Indicates the direction in which the rows of the <code>TableModel</code>
	 * should be sorted.
	 */
	private int direction = NOT_SORTED;

	/**
	 * Indicates the column of the <code>TableModel</code> used to sort the
	 * rows of the <code>TableModel</code>.
	 */
	private int sortColumnIndex = 0;

	/** The <code>JTableHeader</code> set by <code>setSortTableHeader</code>. */
	private JTableHeader tableHeader;

	/**
	 * The <code>MouseInputAdapter</code> set by
	 * <code>setSortTableHeader</code>.
	 */
	//~1C Changed to MouseInputAdapter and IGSSortableTableMouseInputAdapter
	private MouseInputAdapter mouseHandler = new IGSSortableTableMouseInputAdapter();

	//Constructor

	/**
	 * Constructs a new <code>IGSSortableTableModel</code>.
	 * @param rowData the <code>List</code> of <code>List</code>s or the
	 *        <code>List</code> of <code>Object</code> arrays that contains
	 *        the <code>Object</code> values to be stored in the
	 *        <code>TableModel</code>
	 * @param columnNames a <code>String</code> array containing the names of
	 *        each column in the <code>TableModel</code>
	 */
	@SuppressWarnings("rawtypes")
	public IGSSortableTableModel(List rowData, String[] columnNames)
	{
		//~2C Chain to new constructor
		this(rowData, columnNames, new MyComparator());
		((MyComparator) this.comparator).setModel(this);
	}
	
	//~2A New constructor
	/**
	 * Constructs a new <code>IGSSortableTableModel</code>.
	 * @param rowData the <code>List</code> of <code>List</code>s or the
	 *        <code>List</code> of <code>Object</code> arrays that contains
	 *        the <code>Object</code> values to be stored in the
	 *        <code>TableModel</code>
	 * @param columnNames a <code>String</code> array containing the names of
	 *        each column in the <code>TableModel</code>
	 * @param comparator the <code>Comparator</code> used to sort the model data
	 */
	@SuppressWarnings("rawtypes")
	public IGSSortableTableModel(List rowData, String[] columnNames, Comparator comparator)
	{
		super();
		this.comparator = comparator;
		int colCount = columnNames.length;
		this.names = new String[colCount];
		System.arraycopy(columnNames, 0, this.names, 0, colCount);
		setData(rowData);
	}

	//Methods to replace the table model data

	/**
	 * Replaces the data stored in the <code>TableModel</code> with
	 * <code>rowData</code>. The existing column names are maintained.
	 * @param rowData the <code>List</code> of <code>List</code>s or the
	 *        <code>List</code> of <code>Object</code> arrays that contains
	 *        the <code>Object</code> values to be stored in the
	 *        <code>TableModel</code>
	 */
	@SuppressWarnings("rawtypes")
	public void setData(List rowData)
	{
		int colCount = this.names.length;
		int rowCount = (rowData == null ? 0 : rowData.size()); //~2C
		this.data = new Object[rowCount][colCount];
		if (rowCount > 0)
		{
			for (int row = 0; row < rowCount; row++)
			{
				Object dataRow = rowData.get(row);
				if (dataRow instanceof List)
				{
					dataRow = ((List) dataRow).toArray();
				}
				System.arraycopy(dataRow, 0, this.data[row], 0, colCount);
			}
		}

		this.direction = NOT_SORTED;

		// Notify all listeners that all cell values in the table have changed
		fireTableModelEvent(new TableModelEvent(this));

		if (this.tableHeader != null)
		{
			this.tableHeader.repaint();
		}
	}

	/**
	 * Sets the value of the cell at <code>rowIndex</code> and
	 * <code>columnIndex</code> to <code>value</code>. Does <b>not</b>
	 * fire a <code>TableModelEvent</code> to indicate a change in the
	 * <code>TableModel</code>.
	 * @param value the new value for the cell
	 * @param rowIndex the index of the cell's row
	 * @param columnIndex the index of the cell's column
	 */
	public void changeValue(Object value, int rowIndex, int columnIndex)
	{
		if (this.data[rowIndex][columnIndex].getClass().isInstance(value))
		{
			this.data[rowIndex][columnIndex] = value;
		}
	}

	/**
	 * @param x
	 * @param value
	 * @param rowIndex
	 * @param columnIndex
	 */
	public void changeValue(Object x, String value, int rowIndex, int columnIndex)
	{
		this.data[rowIndex][columnIndex] = value;
	}
	
	/**
	 * @param oldObj
	 * @param newObj
	 * @param rowIndex
	 * @param columnIndex
	 */
	public void changeValue(Object oldObj,  Object newObj, int rowIndex, int columnIndex)
	{
		this.data[rowIndex][columnIndex] = newObj;
	}
	
	//Definition of TableModel methods

	/**
	 * Returns the number of rows in the model.
	 * @return the number of rows in the model
	 */
	public int getRowCount()
	{
		return this.data.length;
	}

	/**
	 * Returns the number of columns in the model.
	 * @return the number of columns in the model
	 */
	public int getColumnCount()
	{
		return this.names.length;
	}

	/**
	 * Returns the name of the column at <code>columnIndex</code>.
	 * @param columnIndex the index of the column
	 * @return the name of the column at <code>columnIndex</code>
	 */
	public String getColumnName(int columnIndex)
	{
		return this.names[columnIndex];
	}

	/**
	 * Returns the most specific superclass for all cell values in the column.
	 * This is used by the <code>JTable</code> to set up a default renderer
	 * and editor for the column.
	 * @param columnIndex the index of the column
	 * @return the common ancestor <code>Class</code> of all
	 *         <code>Object</code> values in the column
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex)
	{
		//~1A Added check of data length to avoid
		// ArrayIndexOutOfBoundsException
		return this.data.length == 0 ? Object.class : this.data[0][columnIndex].getClass();
	}

	/**
	 * Returns <code>true</code> if the cell at <code>rowIndex</code> and
	 * <code>columnIndex</code> is editable. Otherwise, returns
	 * <code>false</code> and the {@link #setValueAt(Object, int, int)} method
	 * will not change the value of the cell.
	 * @param rowIndex the index of the cell's row
	 * @param columnIndex the index of the cell's column
	 * @return <code>false</code> since <code>setValueAt</code> does not
	 *         change the value of cell's in this <code>TableModel</code>
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	/**
	 * Returns the value of the cell at <code>rowIndex</code> and
	 * <code>columnIndex</code>.
	 * @param rowIndex the index of the cell's row
	 * @param columnIndex the index of the cell's column
	 * @return the value of the cell
	 * @throws ArrayIndexOutOfBoundsException if an invalid row or column index
	 *         is specified
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return this.data[rowIndex][columnIndex];
	}

	/**
	 * Sets the value of the cell at <code>rowIndex</code> and
	 * <code>columnIndex</code> to <code>value</code> if
	 * {@link #isCellEditable(int, int) isCellEditable} returns
	 * <code>true</code> for the cell.
	 * @param value the new value for the cell
	 * @param rowIndex the index of the cell's row
	 * @param columnIndex the index of the cell's column
	 */
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		//Nothing to do since isCellEditable always returns false
	}

	/**
	 * Adds a <code>TableModelListener</code> to the list that is notified
	 * each time a change to the <code>TableModel</code> occurs.
	 * @param l the <code>TableModelListener</code>
	 * @see EventListenerList#add(java.lang.Class, java.util.EventListener)
	 */
	public void addTableModelListener(TableModelListener l)
	{
		this.listenerList.add(TableModelListener.class, l);
	}

	/**
	 * Removes a <code>TableModelListener</code> from the list that is
	 * notified each time a change to the <code>TableModel</code> occurs.
	 * @param l the <code>TableModelListener</code>
	 * @see EventListenerList#remove(java.lang.Class, java.util.EventListener)
	 */
	public void removeTableModelListener(TableModelListener l)
	{
		this.listenerList.remove(TableModelListener.class, l);
	}

	//Fire event method

	/**
	 * Forwards the specified <code>TableModelEvent</code> to all
	 * <code>TableModelListener</code>s that are registered as listeners for
	 * this model.
	 * @param e the <code>TableModelEvent</code> to forward
	 * @see TableModelEvent
	 * @see EventListenerList
	 */
	public void fireTableModelEvent(TableModelEvent e)
	{
		//Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		//Process the listeners last to first, notifying
		//those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == TableModelListener.class)
			{
				((TableModelListener) listeners[i + 1]).tableChanged(e);
			}
		}
	}

	//Methods to handle sorting of table data

	/**
	 * Returns an integer representing the direction in which the rows of the
	 * model should be sorted.
	 * @return the sorting direction. This value is set by
	 *         <code>setSortingDirection</code> and should be one of:
	 *         <ul>
	 *         <li>{@link #NOT_SORTED}</li>
	 *         <li>{@link #ASCENDING}</li>
	 *         <li>{@link #DESCENDING}</li>
	 *         </ul>
	 */
	public int getSortingDirection()
	{
		return this.direction;
	}

	/**
	 * If the specified <code>columnIndex</code> corresponds to the column
	 * currently used to sort the table, this method changes the direction in
	 * which the table is sorted based on the following sequence:
	 * <ul>
	 * <li>{@link #NOT_SORTED}</li>
	 * <li>{@link #ASCENDING}</li>
	 * <li>{@link #DESCENDING}</li>
	 * </ul>
	 * Otherwise, this method sets the column in the model used to sort the
	 * table to the column corresponding to <code>columnIndex</code> and sets
	 * the direction to {@link #ASCENDING}.
	 * @param columnIndex the index of a column in the model
	 */
	@SuppressWarnings("unchecked")
	public void setSortingDirection(int columnIndex)  // ~3C
	{
		if (columnIndex == this.sortColumnIndex)
		{
			this.direction = (this.direction + 1) % 3;
		}
		else
		{
			this.direction = ASCENDING;
			this.sortColumnIndex = columnIndex;
		}

		//~1A Start addition to sort model data
		if (this.direction != NOT_SORTED)
		{
			Arrays.sort(this.data, this.comparator);
		}
		fireTableModelEvent(new TableModelEvent(this));
		//~1A End addition to sort model data
	}

	/**
	 * Sets the column used for sorting and the sort direction.
	 * @param columnIndex the index of a column in the model
	 * @param direction the sort direction. Must be one of:
	 *        <ul>
	 *        <li>{@link #NOT_SORTED}</li>
	 *        <li>{@link #ASCENDING}</li>
	 *        <li>{@link #DESCENDING}</li>
	 *        </ul>
	 */
	@SuppressWarnings("unchecked")
	public void setSortingDirection(int columnIndex, int direction)
	{ //~1A New method     ~3C made public
		this.sortColumnIndex = columnIndex;
		this.direction = direction % 3;
		if (this.direction != NOT_SORTED)
		{
			Arrays.sort(this.data, this.comparator);
		}
		fireTableModelEvent(new TableModelEvent(this));
	}

	/**
	 * Returns an integer indicating the index of the column in the model used
	 * to sort the rows of the model.
	 * @return the index of the sort column in the model
	 */
	public int getSortColumnIndex()
	{
		return this.sortColumnIndex;
	}

	/**
	 * Sets the <code>JTableHeader</code> used to specify which column is used
	 * to sort the data in the <code>TableModel</code>.
	 * @param tableHeader the <code>JTableHeader</code> used to specify which
	 *        column is used to sort the model. For the sort functionality to be
	 *        activated, this parameter MUST be the table header of the table
	 *        used to display the model, as returned by
	 *        {@link javax.swing.JTable#getTableHeader()}.
	 */
	public void setSortTableHeader(JTableHeader tableHeader)
	{

		// Only set if parameter is not null and the tableHeader corresponds
		// to the table used to display this model
		if (tableHeader != null && tableHeader.getTable().getModel() == this)
		{

			//Undo last call to this method
			//~1D Removed null check on mouseHandler
			if (this.tableHeader != null)
			{
				this.tableHeader.removeMouseListener(this.mouseHandler);
				this.tableHeader.removeMouseMotionListener(this.mouseHandler);

				TableCellRenderer temp = this.tableHeader.getDefaultRenderer();
				if (temp instanceof IGSSortedTableCellRenderer)
				{
					this.tableHeader.setDefaultRenderer(((IGSSortedTableCellRenderer) temp)
							.getDecoratedTableCellRender());
				}
			}

			//~1D Removed lazy instantiation of mouseHandler

			this.tableHeader = tableHeader;
			this.tableHeader.addMouseListener(this.mouseHandler);
			this.tableHeader.addMouseMotionListener(this.mouseHandler);
			this.tableHeader.setDefaultRenderer(new IGSSortedTableCellRenderer(tableHeader
					.getDefaultRenderer()));
		}
	}

	//~1C Made MyComparator a named class instead of an anonymous class
	/**
	 * <code>MyComparator</code> is the <code>Comparator</code> used to sort
	 * the elements of an <code>IGSSortableTableModel</code>.
	 * @author The External Fulfillment Client Development Team
	 */
	@SuppressWarnings("rawtypes")
	public static class MyComparator
		implements Comparator
	{
		/** The <code>Collator</code> used to sort the table. */
		private final Collator collator = Collator.getInstance();

		/** The <code>IGSSortableTableModel</code> to sort. */
		private IGSSortableTableModel model; //~2A

		/** Constructs a new <code>MyComparator</code>. */
		public MyComparator()
		{
			super();
		}

		/**
		 * Compares the two objects for order. Returns a negative integer, zero,
		 * or a positive integer if the first object is less than, equal to, or
		 * greater than the second. The ordering of the two objects depends on
		 * the value returned by <code>getSortingDirection</code>.
		 * @param o1 the first object to be compared
		 * @param o2 the second object to be compared
		 * @return a negative integer, zero, or a positive integer if the first
		 *         object is less than, equal to, or greater than the second
		 */
		public int compare(Object o1, Object o2)
		{
			int status = this.model.getSortingDirection(); //~2C
			int index = this.model.getSortColumnIndex(); //~2C
			if (status == ASCENDING)
			{
				return this.collator.compare(((Object[]) o1)[index].toString(),
						((Object[]) o2)[index].toString());
			}
			else if (status == DESCENDING)
			{
				return this.collator.compare(((Object[]) o2)[index].toString(),
						((Object[]) o1)[index].toString());
			}
			else
			{
				return 0;
			}
		}
		
		//~2A New method
		/**
		 * Sets the <code>IGSSortableTableModel</code> to sort.
		 * @param model the <code>IGSSortableTableModel</code> to sort
		 */
		public void setModel(IGSSortableTableModel model)
		{
			this.model = model;
		}
	}
}
