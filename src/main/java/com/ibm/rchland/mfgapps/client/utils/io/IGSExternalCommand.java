/* @ Copyright IBM Corporation 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2011-07-07       36802JM Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.io;

/**
 * The <code>IGSExternalCommand</code> is an interface that provides methods to
 * execute processes dependent of the operative system.
 * @author The MFS Print Server Development Team
 */
public interface IGSExternalCommand 
{
	/**
	 * Executes the given command as a new <code>Process</code>.
	 * @param command the external command string
	 * @return the exit value of the process. By convention, 0 indicates normal termination.
	 */
	public int execute(String command);
	
	/**
	 * Get the error buffer data
	 * @return the error buffer data
	 */
	public String getErrorLog();
	
	/**
	 * Get the output buffer data
	 * @return the output buffer data
	 */
	public String getOutputLog();
}
