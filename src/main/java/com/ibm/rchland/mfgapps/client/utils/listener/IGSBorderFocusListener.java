/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-24      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.listener;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import com.ibm.rchland.mfgapps.client.utils.border.IGSStippleBorder;

/**
 * <code>IGSBorderFocusListener</code> is a <code>FocusListener</code> that
 * highlights a <code>JComponent</code> with a stipple <code>Border</code>
 * when the <code>JComponent</code> has the keyboard focus.
 * @author The External Fulfillment Client Development Team
 */
public class IGSBorderFocusListener
	implements FocusListener, Serializable
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** The default empty <code>Border</code>. */
	private Border emptyBorder;

	/** The focus stipple <code>Border</code>. */
	private Border stippleBorder;

	/**
	 * Constructs a new <code>IGSBorderFocusListener</code>.
	 * @param thickness the thickness of the <code>Border</code>s
	 */
	public IGSBorderFocusListener(int thickness)
	{
		this.emptyBorder = BorderFactory.createEmptyBorder(thickness, thickness, thickness, thickness);
		this.stippleBorder = new IGSStippleBorder(thickness);
	}

	/**
	 * Adds a default empty <code>Border</code> to the specified
	 * <code>JComponent</code> and adds this as a <code>FocusListener</code>
	 * to the specified <code>JComponent</code> so that the default empty
	 * <code>Border</code> will be switched with the focus stipple
	 * <code>Border</code> when the <code>JComponent</code> gains the focus.
	 * @param component a <code>JComponent</code>
	 */
	public void addBorderTo(JComponent component)
	{
		Border b = component.getBorder();
		if (b == null)
		{
			component.setBorder(this.emptyBorder);
		}
		else
		{
			component.setBorder(BorderFactory.createCompoundBorder(this.emptyBorder, b));
		}
		component.addFocusListener(this);
	}

	/**
	 * Invoked when a <code>Component</code> gains the keyboard focus. If the
	 * originator of the <code>FocusEvent</code> had a <code>Border</code>
	 * added via the {@link #addBorderTo(JComponent)} method, its
	 * <code>Border</code> is set to the focus stipple <code>Border</code>.
	 * @param e the <code>FocusEvent</code>
	 */
	public void focusGained(FocusEvent e)
	{
		Component c = e.getComponent();
		if (c instanceof JComponent)
		{
			JComponent jc = (JComponent) c;
			Border b = jc.getBorder();
			if (b == this.emptyBorder)
			{
				jc.setBorder(this.stippleBorder);
			}
			else if (b instanceof CompoundBorder)
			{
				CompoundBorder cb = (CompoundBorder) b;
				if (cb.getOutsideBorder() == this.emptyBorder)
				{
					jc.setBorder(BorderFactory.createCompoundBorder(this.stippleBorder, cb.getInsideBorder()));
				}
			}
		}
	}

	/**
	 * Invoked when a <code>Component</code> loses the keyboard focus. If the
	 * originator of the <code>FocusEvent</code> had a <code>Border</code>
	 * added via the {@link #addBorderTo(JComponent)} method, its
	 * <code>Border</code> is set to the default empty <code>Border</code>.
	 * @param e the <code>FocusEvent</code>
	 */
	public void focusLost(FocusEvent e)
	{
		Component c = e.getComponent();
		if (c instanceof JComponent)
		{
			JComponent jc = (JComponent) c;
			Border b = jc.getBorder();
			if (b == this.stippleBorder)
			{
				jc.setBorder(this.emptyBorder);
			}
			else if (b instanceof CompoundBorder)
			{
				CompoundBorder cb = (CompoundBorder) b;
				if (cb.getOutsideBorder() == this.stippleBorder)
				{
					jc.setBorder(BorderFactory.createCompoundBorder(this.emptyBorder, cb.getInsideBorder()));
				}
			}
		}
	}
}
