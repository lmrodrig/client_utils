/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-05-25       45878MS Santiago SC      -Initial version, Java 1.4
 * 2011-07-19       36802JM Santiago SC      -Restructured
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The <code>IGSCommand</code> is used to execute console commands from different OS.
 * @author The External Fulfillment Client Development Team
 */
public class IGSCommand implements IGSExternalCommand
{
	/**
	 * The <code>IGSCommandLogger</code> is in charge of reading the input stream  from a
	 * <code>IGSCommand</code> executed and put into a buffer.
	 * @author The External Fulfillment Client Development Team
	 */
	private class IGSCommandLogger extends Thread
	{
		/** The input stream to be buffered */
	    private InputStream is;
	    
	    /** The result of the input stream */
	    private StringBuffer inputData;
	    
	    /** The command executed */
	    private String cmd;
	  
	    /**
	     * Creates a new <code>IGSCommandLogger</code> for the given input stream.
	     * @param is the <code>InputStream</code>
	     * @param cmd the command string
	     */
	    IGSCommandLogger(String cmd, InputStream is)
	    {
	    	this.cmd = cmd;
	        this.is = is;
	    }

	    /**
	     * Get the data from the input stream
	     * @return the input stream data
	     */
		public String getInputData()
	    {
			StringBuffer out = new StringBuffer("Command: ");
			out.append(cmd);
			out.append("\r\n");
			out.append(inputData.toString());
	    	return out.toString();
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
		public void run() 
		{
	        try
	        {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            
	            inputData = new StringBuffer();
	        	
	            while (null != (line = br.readLine()))
	            {
	            	inputData.append(line);
	            	inputData.append("\r\n");
	            }
	            
	        	inputData.trimToSize();
	        } 
	        catch (IOException ioe)
	        {
	        	ioe.printStackTrace();  
	        }
		}
	}
	
	/** The buffer to store error */
	private IGSCommandLogger errorLogger;
	
	/** The buffer to store the output */
	private IGSCommandLogger outputLogger;
	
	/** The command structure */
	private String[] cmd;
	
	/** The formatted command */
	private String formattedCmd;
	
	/** {@inheritDoc} */
	public int execute(String command)
	{
		int exitVal = -1;
		Process process = null;
		
		// The command structure
        cmd = new String[3];
        cmd[0] = "";
        cmd[1] = "";
        cmd[2] = command;
		
		try 
		{
            String osName = System.getProperty("os.name");
            
            if(osName.startsWith("Windows"))
            {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C" ;
            }
            
            // Save the command for later use
    		StringBuffer sb = new StringBuffer();
    		
    		for(String cmdToken : cmd)
    		{
    			sb.append(cmdToken);
    			sb.append(" ");
    		}
    		
    		formattedCmd = sb.toString();
			
    		// Execute the command in a new process
			process = Runtime.getRuntime().exec(cmd);
			
			// Error and Output buffers in the process have limited capacity
			errorLogger = new IGSCommandLogger(formattedCmd, process.getErrorStream());			
			outputLogger = new IGSCommandLogger(formattedCmd, process.getInputStream());  
			
			errorLogger.start();
			outputLogger.start();

			// Wait for the process to exit
			exitVal = process.waitFor();
			
			errorLogger.join();
			outputLogger.join();
			
			process.getErrorStream().close();
			process.getOutputStream().flush();
			process.getOutputStream().close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		return exitVal;
	}
	
	/** {@inheritDoc} */
	public String getErrorLog()
	{
		return errorLogger.getInputData();
	}
	
	/** {@inheritDoc} */
	public String getOutputLog()
	{
		return outputLogger.getInputData();
	}
	
	@Override
	public String toString()
	{
		return formattedCmd;
	}
}
