/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-20      31214TB  R Prechel        -Initial version
 * 2006-08-31      35394PL  R Prechel        -Move to client utils project
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;


/**
 * The <code>IGSCellInformationProcessor</code> interface 
 * should be implemented by all classes that define the 
 * {@link #processCellInformation(IGSCellInformation)} method.
 * 
 * @author Ryan Prechel
 * @version 1.0
 */
public interface IGSCellInformationProcessor {
	/**
	 * Callback method that performs the actions necessary to process 
	 * the selection of the specified <code>information</code>.
	 * @param information the <code>IGSCellInformation</code> to process
	 */
	public void processCellInformation(IGSCellInformation information);
}
