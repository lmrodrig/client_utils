/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-01      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.messagebox;

import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;

/**
 * <code>IGSMessageTypeResources</code> is the base <code>ResourceBundle</code>
 * for the <code>IGSMessageType</code> classes.
 * @author The MFS Client Development Team
 */
public class IGSMessageTypeResources
	extends ListResourceBundle
{
	/** The key suffix for a button's key stroke. */
	public static final String BUTTON_KEY_STROKE = "ButtonKeyStroke"; //$NON-NLS-1$

	/** The key suffix for a button's mnemonic character. */
	public static final String BUTTON_MNEMONIC = "ButtonMnemonic"; //$NON-NLS-1$

	/** The key suffix for a button's text. */
	public static final String BUTTON_TEXT = "ButtonText"; //$NON-NLS-1$

	/** The key root for an escape button. */
	public static final String ESCAPE = "escape"; //$NON-NLS-1$

	/** The key for an escape button's key stroke. */
	public static final String ESCAPE_BUTTON_KEY_STROKE = "escapeButtonKeyStroke"; //$NON-NLS-1$

	/** The key for an escape button's mnemonic character. */
	public static final String ESCAPE_BUTTON_MNEMONIC = "escapeButtonMnemonic"; //$NON-NLS-1$

	/** The key for an escape button's text. */
	public static final String ESCAPE_BUTTON_TEXT = "escapeButtonText"; //$NON-NLS-1$

	/** The key root for a no button. */
	public static final String NO = "no"; //$NON-NLS-1$

	/** The key for a no button's key stroke. */
	public static final String NO_BUTTON_KEY_STROKE = "noButtonKeyStroke"; //$NON-NLS-1$

	/** The key for a no button's mnemonic character. */
	public static final String NO_BUTTON_MNEMONIC = "noButtonMnemonic"; //$NON-NLS-1$

	/** The key for a no button's text. */
	public static final String NO_BUTTON_TEXT = "noButtonText"; //$NON-NLS-1$

	/** The key root for an ok button. */
	public static final String OK = "ok"; //$NON-NLS-1$

	/** The key for an ok button's key stroke. */
	public static final String OK_BUTTON_KEY_STROKE = "okButtonKeyStroke"; //$NON-NLS-1$

	/** The key for an ok button's mnemonic character. */
	public static final String OK_BUTTON_MNEMONIC = "okButtonMnemonic"; //$NON-NLS-1$

	/** The key for an ok button's text. */
	public static final String OK_BUTTON_TEXT = "okButtonText"; //$NON-NLS-1$

	/** The key root for a yes button. */
	public static final String YES = "yes"; //$NON-NLS-1$

	/** The key for a yes button's key stroke. */
	public static final String YES_BUTTON_KEY_STROKE = "yesButtonKeyStroke"; //$NON-NLS-1$

	/** The key for a yes button's mnemonic character. */
	public static final String YES_BUTTON_MNEMONIC = "yesButtonMnemonic"; //$NON-NLS-1$

	/** The key for a yes button's text. */
	public static final String YES_BUTTON_TEXT = "yesButtonText"; //$NON-NLS-1$

	/** The key for the &quot;\n\nCaused by:\n&quot; message. */
	public static final String CAUSED_BY = "causedBy"; //$NON-NLS-1$

	/** The key for the &quot;Program Exception: &quot; message. */
	public static final String PROGRAM_EXCEPTION = "programException"; //$NON-NLS-1$

	/** The key for the message displayed when no message was set. */
	public static final String NO_MESSAGE = "noMessage"; //$NON-NLS-1$

	/** Constructs a new <code>IGSMessageTypeResources</code>. */
	public IGSMessageTypeResources()
	{
		super();
	}

	/**
	 * Returns an array where each element in the array is a pair of
	 * <code>Object</code>s. The first element of the pair is the key and is
	 * of type <code>String</code>. The second element is the value
	 * associated with the key.
	 * @return the contents of this <code>ListResourceBundle</code>
	 */
	protected Object[][] getContents()
	{
		return new Object[][] { 
				{ESCAPE_BUTTON_KEY_STROKE, "ESCAPE"}, //$NON-NLS-1$
				{ESCAPE_BUTTON_MNEMONIC, new Integer(KeyEvent.VK_E)}, 
				{ESCAPE_BUTTON_TEXT, "Esc"}, //$NON-NLS-1$

				{NO_BUTTON_KEY_STROKE, "ENTER"}, //$NON-NLS-1$
				{NO_BUTTON_MNEMONIC, new Integer(KeyEvent.VK_N)}, 
				{NO_BUTTON_TEXT, "No"}, //$NON-NLS-1$

				{OK_BUTTON_KEY_STROKE, "ENTER"}, //$NON-NLS-1$
				{OK_BUTTON_MNEMONIC, new Integer(KeyEvent.VK_O)}, 
				{OK_BUTTON_TEXT, "OK"}, //$NON-NLS-1$

				{YES_BUTTON_KEY_STROKE, "ENTER"}, //$NON-NLS-1$
				{YES_BUTTON_MNEMONIC, new Integer(KeyEvent.VK_Y)}, 
				{YES_BUTTON_TEXT, "Yes"}, //$NON-NLS-1$

				{CAUSED_BY, "\n\nCaused by:\n"}, //$NON-NLS-1$
				{PROGRAM_EXCEPTION, "Program Exception: "}, //$NON-NLS-1$
				{NO_MESSAGE, "A program error occurred."} //$NON-NLS-1$
		};
	}
}
