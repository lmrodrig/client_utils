/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 *                                           -Fixed compile warnings
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;


/**
 * <code>IGSCellInformation</code> is an <code>Object</code> designed
 * to be displayed in a table and to maintain an array of information
 * that can be accessed when the table cell is selected.
 * <p>
 * An <code>IGSCellInformation</code> maintains an array of 
 * <code>IGSCellInformationProcessor</code>s that can be notified
 * to query the <code>IGSCellInformation</code> when the table cell
 * is selected by calling the {@link #notifyProcessors()} method.
 * The <code>IGSCellInformation</code>'s management of its array of
 * <code>IGSCellInformationProcessor</code>s is modeled after a
 * typical implementation of {@link javax.swing.event.EventListenerList}.
 * 
 * @author Ryan Prechel
 * @version 1.0
 */
public class IGSCellInformation {
	/** An empty array to be shared by all instances of 
	 * <code>IGSCellInformation</code> that do not have any processors. */ 
	private static final IGSCellInformationProcessor[] EMPTY = new IGSCellInformationProcessor[0];
	
	/** An empty array to be shared by all instances of
	 * <code>IGSCellInformation</code> that do not have data. */
	private static final Object[] NO_DATA = new Object[0];
	
	/** The name of the <code>IGSCellInformation</code>. */
	private String name;
	
	/** The data stored by the <code>IGSCellInformation</code>. */
	private Object[] data;
	
	/** The array of <code>IGSCellInformationProcessor</code>s 
	 *  maintained by the <code>IGSCellInformation</code>. */
	private IGSCellInformationProcessor[] processorArray = EMPTY;
	
	
	/**
	 * Constructs a new <code>IGSCellInformation</code>.
	 * @param processor a <code>IGSCellInformationProcessor</code> 
	 *  for the <code>IGSCellInformation</code>
	 * @param name the name of the <code>IGSCellInformation</code>
	 */
	public IGSCellInformation(IGSCellInformationProcessor processor, String name) {
		addProcessor(processor);
		this.name = name;
		this.data = NO_DATA;
	}
	
	
	/**
	 * Constructs a new <code>IGSCellInformation</code>.
	 * @param processor a <code>IGSCellInformationProcessor</code> for
	 *  the <code>IGSCellInformation</code>
	 * @param name the name of the <code>IGSCellInformation</code>
	 * @param data the data stored by the <code>IGSCellInformation</code>
	 */
	public IGSCellInformation(IGSCellInformationProcessor processor, String name, Object[] data) {
		addProcessor(processor);
		this.name = name;
		this.data = (data == null ? NO_DATA : data);
	}
	
	
	/**
	 * Returns the <code>name</code> of the <code>IGSCellInformation</code>.
	 * @return the <code>name</code> of the <code>IGSCellInformation</code>
	 */
	public String toString() {
		return this.name;
	}
	
	
	/**
	 * Returns the <code>Object</code> at the specified 
	 * <code>index</code> in the <code>data</code> array.
	 * @param index the index of the <code>Object</code> to return
	 * @return the <code>Object</code> at the specified <code>index</code>
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public Object getValue(int index) {
		return this.data[index];
	}
	
	
	/**
	 * Returns the <code>String</code> representation of the <code>Object</code> 
	 * at the specified <code>index</code> in the <code>data</code> array.
	 * @param index the index of the <code>Object</code>
	 * @return the <code>String</code> representation of the <code>Object</code> 
	 *  at the specified <code>index</code> as returned by {@link Object#toString()}
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public String getString(int index) {
		return this.data[index].toString();
	}
	
	
	/**
	 * Adds a <code>IGSCellInformationProcessor</code> to the 
	 * <code>IGSCellInformation</code>'s array of processors.
	 * @param processor the <code>IGSCellInformationProcessor</code> to add
	 */
	public synchronized void addProcessor(IGSCellInformationProcessor processor) {
		//Null processors are not allowed
		if(processor == null) {
			return;
		}
		//Iinitialize the array if this is the first processor added
		if(this.processorArray == EMPTY) {
			this.processorArray = new IGSCellInformationProcessor[]{processor};
		}
		else {
			int length = this.processorArray.length;
			IGSCellInformationProcessor[] temp = new IGSCellInformationProcessor[length + 1];
			System.arraycopy(this.processorArray, 0, temp, 0, length);
			temp[length] = processor;
			this.processorArray = temp;
		}
	}
	
	
	/**
	 * Removes a <code>IGSCellInformationProcessor</code> from the 
	 * <code>IGSCellInformation</code>'s array of processors.
	 * @param processor the <code>IGSCellInformationProcessor</code> to remove
	 */
	public synchronized void removeProcessor(IGSCellInformationProcessor processor) {
		//Null processors are not allowed
		if(processor == null) {
			return;
		}
		
		int index = -1;
		//Search for processor
		for(int i = this.processorArray.length-1; i >= 0; i--) {
			if(this.processorArray[i].equals(processor)) {
				index = i;
				break;
			}
		}
		
		//If processor is found, remove it
		if(index != -1) {
			IGSCellInformationProcessor[] temp = new IGSCellInformationProcessor[this.processorArray.length - 1];
			if(index != 0) {
				System.arraycopy(this.processorArray, 0, temp, 0, index);
			}
			if(index < temp.length) {
				System.arraycopy(this.processorArray, index+1, temp, index, temp.length-index);
			}
			this.processorArray = temp;
		}
	}
	
	
	/**
	 * Calls the {@link IGSCellInformationProcessor#processCellInformation(IGSCellInformation)}
	 * method on each <code>IGSCellInformationProcessor</code> in the array of processors.
	 */
	public void notifyProcessors() {
		for(int i = this.processorArray.length-1; i >= 0; i--) {
			this.processorArray[i].processCellInformation(this);
		}
	}
}
