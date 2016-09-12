/* @ Copyright IBM Corporation 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-05       45878MS Santiago SC      -Initial version, Java 5.0
 * 											  Copy from elf_common_gui
 * 2011-11-07   ~1  588440  Santiago SC      -Fixed small bug in addRow()
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.table;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * The <code>IGSDocumentTableModel</code> is an <code>AbstractTableModel</code>
 * that accepts adding and removing rows dynamically.
 * @author The External Fulfillment Client Development Team
 */
public class IGSDocumentTableModel extends AbstractTableModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6303188571262393952L;
	
	/** Column names */
	private String[] cNames;
	
	/** Column types */
	private Class<?>[] cTypes;
	
	/** Table data */
    private Vector<Object[]> data  = new Vector<Object[]>();   
    
    /**
     * Creates a <code>IGSDocumentTableModel</code> with the given column names.
     * @param cNames the column names.
     */
    public IGSDocumentTableModel(String[] cNames)
    {
    	this.cNames = cNames;
    	this.cTypes = getDefaultTypes(cNames);
    }   
    
    /**
     * Creates a <code>IGSDocumentTableModel</code> with the given column names
     * and column types.
     * @param cNames the column names.
     * @param cTypes the column types.
     */
    public IGSDocumentTableModel(String[] cNames, Class<?>[] cTypes)
    {
    	this.cNames = cNames;
    	this.cTypes = cTypes;
    }
    
    /**
     * Add the row to the table model data
     * @param row to add.
     */
    public void addRow(Object[] row)
    {
    	data.add(row);       	
    	int rowIndex = data.size() - 1; //~1A
    	fireTableRowsInserted(rowIndex, rowIndex); //~1C
    }    	
    
    /**
     * Add the rows to the table model data
     * @param rows to add
     */
    public void addRows(Vector<Object[]> rows)
    {
    	for(Object[] row : rows)
    	{
    		addRow(row);
    	}
    }    
    
    /**
     * Clears the table model data.
     */
    public void clearData()
    {
    	this.data.clear();
    	
    	fireTableDataChanged();
    }
    
    @Override
    /**
     * {@inheritDoc}
     */
    public Class<?> getColumnClass(int column) 
    {
    	return cTypes[column];
    }

    /**
     * Count the columns.
     * @return the number of columns
     */
    public int getColumnCount() 
    {
    	return cNames.length;
    }	
 
    @Override
    /**
     * {@inheritDoc}
     */
    public String getColumnName(int column) 
    {
    	return cNames[column];
    }
    
	/**
     * Get the table model data.
     * @return a <code>Vector</code> of array objects
     */
    public Vector<Object[]> getData()
    {
    	return data;    	
    }	
	
    /**
     * Create a default object array for each column name.
     * @param cNames the column names.
     * @return an object class array.
     */
    private Class<?>[] getDefaultTypes(String[] cNames)
    {
    	Class<?>[] cTypes = new Class<?>[cNames.length];
    	Arrays.fill(cTypes, Object.class);
    	return cTypes;
    }
	
    /**
     * Count the rows.
     * @return the number of rows
     */
    public int getRowCount() 
	{
		return data.size();
	}

    /**
     * Gets the value in the cell specified by the rowIndex and columnIndex
     * @return the Object
     */
    public Object getValueAt(int rowIndex, int columnIndex) 
	{		
		return data.get(rowIndex)[columnIndex];
	}  
    
    /**
     * Get the value in the cell specified by the rowIndex and columnName
     * @param rowIndex row of cell
     * @param columnName name of the column
     * @return aValue value to assign to cell
     */
	public Object getValueAt(int rowIndex, String columnName)
	{
		Object value = null;
		
    	for(int columnIndex = 0; columnIndex < cNames.length; columnIndex++)
    	{
    		if(columnName.equals(cNames[columnIndex]))
    		{
    			value = getValueAt(rowIndex, columnIndex);
    			break;
    		}
    	}
    	
    	return value;
	}
    
    @Override
    /**
     * {@inheritDoc}
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) 
    {   
        return (getColumnClass(columnIndex) == Boolean.class);   
    }
    
    /**
     * Remove the row in the given row index
     * @param rowIndex the row index
     */
    public void removeRow(int rowIndex)
    {
    	data.remove(rowIndex);
    	
    	this.fireTableRowsDeleted(rowIndex, rowIndex);
    }        
    
    /**
     * Sets the table model data.
     * @param data the model data
     */
    public void setData(Vector<Object[]> data)
    {
    	this.data = data;    	
    	
    	fireTableDataChanged();
    }
    
    /**
	 * Adds an entire data row/record without
	 * @param row the array containing values for the row
	 * @param rowIndex the index of the row
	 */
	public void setRow(Object[] row, int rowIndex)
	{
		data.set(rowIndex, row);
		
		fireTableRowsUpdated(rowIndex, rowIndex);
	}
    
    @Override
    /**
     * {@inheritDoc}
     */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		data.get(rowIndex)[columnIndex] = aValue;

		fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	/**
     * Set the value in the cell specified by the rowIndex and columnName
     * @param aValue value to assign to cell
     * @param rowIndex row of cell
     * @param columnName name of the column
     */
	public void setValueAt(Object aValue, int rowIndex, String columnName)
	{
    	for(int columnIndex = 0; columnIndex < cNames.length; columnIndex++)
    	{
    		if(columnName.equals(cNames[columnIndex]))
    		{
    			setValueAt(aValue, rowIndex, columnIndex);
    			break;
    		}
    	}
	}
}
