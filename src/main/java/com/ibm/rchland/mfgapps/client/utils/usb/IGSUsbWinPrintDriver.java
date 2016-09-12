/* @ Copyright IBM Corporation 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2011-07-07       36802JM Santiago SC      -Initial version, Java 5.0
 * ---------- ----  ------------ ---------------- ----------------------------------
 * 2011-07-28       RTC538440    Edgar V.     ~01 -Handle last token        
 **********************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.usb;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.ibm.rchland.mfgapps.client.utils.io.IGSExternalCommand;

/**
 * The <code>IGSUsbWinPrintDriver</code> is an interface between Windows OS and
 * Java to retrieve USB printer information and map USB printers for printing
 * through Java.
 * @author The MFS Print Server Development Team
 */
public class IGSUsbWinPrintDriver 
{
	/** The print service "Name" attribute */
	public static final String PS_NAME = "Name";
	
	/** The print service "PortName" attribute */
	public static final String PS_PORT_NAME = "PortName";

	/** The print service "Shared" attribute */
	public static final String PS_SHARED = "Shared";

	/** The print service "ShareName" attribute */
	public static final String PS_SHARE_NAME = "ShareName";
	
	/** The <code>IGSExternalCommand</code> to execute commands directly to the OS. */
	private IGSExternalCommand exCmd;
	
	/** The <code>Map</code> of print services and its ResourceBundle */
	private Map<String, Map<String, String>> printServices;
	
	/** The <code>ResourceBundle</code> containing the Windows OS commands to be used */
	private ResourceBundle winCommands;
	
	/**
	 * Creates a new <code>IGSUsbWinPrintDriver<code> with the given 
	 * <code>IGSExternalCommnad</code> as the interface to execute external processes.
	 * @param exCmd the interface to execute external processes.
	 * @param winCommands the <code>ResourceBundle</code> that contain the Windows OS commands.
	 */
	public IGSUsbWinPrintDriver(IGSExternalCommand exCmd, ResourceBundle winCommands)
	{
		this.exCmd = exCmd;
		this.winCommands = winCommands;
		
		initialize();
	}
	
	/**
	 * Formats the given command with the given params and then executes the command
	 * using the <code>IGSExternalCommand</code> interface.
	 * @param command the command to be formatted
	 * @param params the parameters used to format the command before executed.
	 * @return the exit value of the process. By convention, 0 indicates normal termination.
	 */
	private int executeCommand(final String command, final Object... params)
	{
		String formatteCmd = String.format(command, params);
		return exCmd.execute(formatteCmd);
	}
	
	/**
	 * Gets the attribute value of the given print service
	 * @param printService the print service 
	 * @param attribute the attribute to be retrieved.
	 * @return a string representing the value of the print service attribute.
	 */
	public String getAttribute(String printService, String attribute)
	{
		return printServices.get(printService).get(attribute);
	}
	
	/**
	 * Gets a print service map of attributes.
	 * @param printService the name of the print service
	 * @return a map containing the print service attributes.
	 */
	public Map<String, String> getAttributes(String printService)
	{
		return printServices.get(printService);
	}
	
	/**
	 * Gets the error string generated by the execution of a command using the
	 * <code>IGSExternalCommand</code>.
	 * @return an error string
	 */
	public String getErrorString()
	{
		return exCmd.getErrorLog();
	}
	
	/**
	 * Gets the output string generated by the execution of a command using the
	 * <code>IGSExternalCommand</code>.
	 * @return an output string
	 */
	public String getOutputString()
	{
		return exCmd.getOutputLog();
	}
	
	/**
	 * Gets a <code>Set</code> of the print services installed in the local computer.
	 * @return a <code>Set</code> containing the print services names.
	 */
	public Set<String> getPrintServices()
	{
		return printServices.keySet();
	}
	
	/**
	 * Initializes this print driver.
	 */
	private void initialize()
	{
		printServices = new HashMap<String, Map<String, String>>();
	}	
	
	/**
	 * Loads the print services shared information. It will attempt to retrieve the
	 * "Shared" and "ShareName" attributes by executing the "net share" command.
	 * In case the "net share" fails the attributes will have null values. Note that a
	 * call to loadWmicPrintServices or lookupPrintServices is previously required 
	 * before using this method.
	 * @return exit value of the process. By convention, 0 indicates normal termination.
	 */
	public int loadPrintServicesSharedInfo()
	{
		String command = winCommands.getString("testNetShare");		
		int rc = executeCommand(command, (Object[])null);
		
		if(0 == rc)
		{			
			// Update the share attributes
			String printerInfo = getOutputString();
			
			for(String printService : getPrintServices())
			{
				Map<String, String> attributes = getAttributes(printService);
				
				if(printerInfo.contains(printService))
				{					
					attributes.put(PS_SHARED, "true");
				}
				else
				{
					attributes.put(PS_SHARED, "false");
				}
			}
		}
		
		return rc;
	}
	
	/** 
	 * Loads the print services attributes "Name", "PortName", "Shared" and "ShareName"
	 * using the Windows OS wmic command. 
	 * @return the exit value of the process. By convention, 0 indicates normal termination.
	 */
	public int loadWmicPrintServices()
	{
		printServices.clear();
		
		String command = winCommands.getString("getPrintServices");
		int rc = executeCommand(command);
		
		// If no error, parse the command output
		if(0 == rc)
		{
			String printerInfo = getOutputString();
			printerInfo = printerInfo.replaceAll("\r\n", "");
			
			while(!printerInfo.trim().equals(""))
			{
				HashMap<String,String> attributes = new HashMap<String,String>();
				
				// Get and set the printer name
				printerInfo = printerInfo.substring(PS_NAME.length() + 1);
				String printerName = printerInfo.substring(0, printerInfo.indexOf(PS_PORT_NAME));
				attributes.put(PS_NAME, printerName);
				
				// Get and set the port name
				printerInfo = printerInfo.substring(printerName.length() + PS_PORT_NAME.length() + 1);
				String portName = printerInfo.substring(0, printerInfo.indexOf(PS_SHARED));
				attributes.put(PS_PORT_NAME, portName);
				
				// Get and set the share status
				printerInfo = printerInfo.substring(portName.length() + PS_SHARED.length() + 1);
				String shared = printerInfo.substring(0, printerInfo.indexOf(PS_SHARE_NAME));
				attributes.put(PS_SHARED, shared);
				
				// Get and set the share name
				String shareName = null;
				printerInfo = printerInfo.substring(shared.length() + PS_SHARE_NAME.length() + 1);								
				
				if(printerInfo.contains(PS_NAME))
				{
					shareName = printerInfo.substring(0, printerInfo.indexOf(PS_NAME));
					printerInfo = printerInfo.substring(shareName.length());
				}
				else
				{
					shareName = printerInfo.trim();
					printerInfo = "";//~01A
				}
				attributes.put(PS_SHARE_NAME, shareName);

				printServices.put(printerName, attributes);
			}
		}
		
		return rc;
	}
	
	/** 
	 * Loads the print services from the Java API. The attributes map will have the
	 * "PortName", "Shared" and "ShareName" with null values.
	 */
	public void lookupPrintServices()
	{
		printServices.clear();
		
		// Load the print services from java
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		
		for(PrintService service : services)
		{
			HashMap<String, String> attributes = new HashMap<String, String>();
			
			attributes.put(PS_NAME, service.getName());
			attributes.put(PS_PORT_NAME, null);
			attributes.put(PS_SHARED, null);
			attributes.put(PS_SHARE_NAME, null);
			
			printServices.put(service.getName(), attributes);
		}
	}
	
	/**
	 * Maps a print service in the given ipAddress and port. It uses the given shareName to identify
	 * the print service in the network. The mapping is done via the "net share" command.
	 * @param port the port used to map the print service.
	 * @param ipAddress the ip address where the print service is installed.
	 * @param shareName the print service share name.
	 * @return exit value of the process. By convention, 0 indicates normal termination.
	 */
	public int mapPrintService(String port, String ipAddress, String shareName)
	{
		String command = winCommands.getString("mapPrintService");
		return executeCommand(command, port, ipAddress, shareName);
	}
	
	/**
	 * Sends to print the file from the given fileFullPath to the mapped port by using
	 * the "copy" command. Note that a print service should be mapped to the port specified
	 * before printing.
	 * @param fileFullPath the full path of the file to be printed.
	 * @param port the mapped port.
	 * @return the exit value of the process. By convention, 0 indicates normal termination.
	 */
	public int print(String fileFullPath, String port)
	{
		String command = winCommands.getString("sendToPrint");
		return executeCommand(command, fileFullPath, port);
	}
	
	/**
	 * Shares a print service in the network using the "rundll32 printui.dll,PrintUIEntry"
	 * command.
	 * @param printService the print service name
	 * @param shareName the print service share name in the to be used in the network.
	 * @return exit value of the process. By convention, 0 indicates normal termination.
	 */
	public int sharePrintService(String printService, String shareName)
	{
		String command = winCommands.getString("sharePrintService");
		return executeCommand(command, printService, shareName);
	}

	/**
	 * Unmaps the print service in the given port using the "net share" command.
	 * @param port the port where the print service is currently mapped.
	 * @return exit value of the process. By convention, 0 indicates normal termination.
	 */
	public int unmapPrintService(String port)
	{
		String command = winCommands.getString("unmapPrintService");
		return executeCommand(command, port);
	}
}