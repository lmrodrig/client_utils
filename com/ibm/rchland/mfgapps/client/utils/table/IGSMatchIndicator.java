//© Copyright IBM Corporation 2007. All rights reserved.
/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-06-19      37078MG  M Barth          -Initial
 * 2008-01-11      39619JL  R Prechel        -Fix compile warnings
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;

/**
 * <code>IGSMatchIndicator</code> is used to create a table column for matches
 * to the search criteria.
 * @author The External Fulfillment Client Development Team
 */
public class IGSMatchIndicator
{
	/** The indicator used to display a match. */
	private String matchIndicator = null;

	/** The toolTip display for this <code>IGSMatchInicator</code> cell. */
	private String toolTip = null;

	/** Constructs a new <code>IGSMatchIndicator</code>. */
	public IGSMatchIndicator()
	{
		this.matchIndicator = ""; //$NON-NLS-1$
		this.toolTip = ""; //$NON-NLS-1$
	}

	/**
	 * Constructs a new <code>IGSMatchIndicator</code>
	 * @param indicator the indicator used to display a match
	 * @param tip the toolTip to be display for the match indicator
	 */
	public IGSMatchIndicator(String indicator, String tip)
	{
		this.matchIndicator = indicator;
		this.toolTip = tip;
	}

	/**
	 * Returns the <code>matchIndicator</code> of the <code>IGSMatchIndicator</code>.
	 * @return the <code>matchIndicator</code> of the <code>IGSMatchIndicator</code>
	 */
	public String toString()
	{
		return this.matchIndicator;
	}

	/**
	 * Returns the <code>toolTip</code> of the <code>IGSMatchIndicator</code>.
	 * @return the <code>toolTip</code> of the <code>IGSMatchIndicator</code>
	 */
	public String getToolTip()
	{
		return this.toolTip;
	}
}
