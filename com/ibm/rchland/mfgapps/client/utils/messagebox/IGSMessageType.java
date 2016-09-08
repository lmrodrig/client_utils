/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-30      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.messagebox;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * <code>IGSMessageType</code> is the abstract base class for the type of
 * message displayed in an <code>IGSMessageBox</code>.
 * <p>
 * <code>IGSMessageBox</code> was designed using the <cite>Strategy</cite>
 * design pattern. An <code>IGSMessageBox</code> is constructed with an
 * instance of <code>IGSMessageType</code> that determines what buttons are
 * displayed on the message box and the behavior of the buttons.
 * @author The MFS Client Development Team
 */
public abstract class IGSMessageType
{
	/** The <code>ResourceBundle</code> used to obtain localized resources. */
	private static final ResourceBundle bundle = ResourceBundle.getBundle(
			IGSMessageTypeResources.class.getName(), Locale.getDefault(),
			IGSMessageType.class.getClassLoader());

	/** Constructs a new <code>IGSMessageType</code>. */
	protected IGSMessageType()
	{
		super();
	}

	/**
	 * Creates the button <code>JComponent</code> for this
	 * <code>IGSMessageType</code>.
	 * @param messageBox the <code>IGSMessageBox</code> to which the
	 *        button <code>JComponent</code> will be added
	 * @return the button <code>JComponent</code>
	 */
	public abstract JComponent createButtonComponent(IGSMessageBox messageBox);

	/**
	 * Creates a <code>JButton</code> that will be displayed in the specified
	 * <code>messageBox</code>. The button's text, <code>KeyStroke</code>
	 * key binding, and mnemonic will be determined using the
	 * <code>IGSMessageTypeResources ResourceBundle</code> and the specified
	 * <code>ResourceBundle</code> key <code>root</code>. The keystroke
	 * will be bound to the button with the specified focus <code>condition</code>.
	 * @param root a <code>ResourceBundle</code> key root
	 * @param condition one of
	 *        <ul>
	 *        <li>{@link JComponent#WHEN_ANCESTOR_OF_FOCUSED_COMPONENT}</li>
	 *        <li>{@link JComponent#WHEN_FOCUSED}</li>
	 *        <li>{@link JComponent#WHEN_IN_FOCUSED_WINDOW}</li>
	 *        </ul>
	 * @param messageBox the <code>IGSMessageBox</code> that will display the
	 *        <code>JButton</code>
	 * @return the <code>JButton</code>
	 * @see IGSMessageTypeResources for a list of supported key roots
	 */
	protected JButton createButton(String root, int condition, IGSMessageBox messageBox)
	{
		String key = root + IGSMessageTypeResources.BUTTON_TEXT;
		String text = bundle.getString(key);
		
		key = root + IGSMessageTypeResources.BUTTON_MNEMONIC;
		Integer mnemonic = (Integer) bundle.getObject(key);
		
		key = root + IGSMessageTypeResources.BUTTON_KEY_STROKE;
		KeyStroke keyStroke = KeyStroke.getKeyStroke(bundle.getString(key));

		Action action = new IGSCloseMessageBoxAction(messageBox);
		action.putValue(Action.NAME, text);

		action.putValue(Action.ACTION_COMMAND_KEY, text);
		action.putValue(Action.MNEMONIC_KEY, mnemonic);

		JButton button = new JButton(action);
		button.setFont(IGSMessageBox.FONT);
		button.getInputMap(condition).put(keyStroke, text);
		button.getActionMap().put(text, action);
		
		return button;
	}

	/**
	 * Returns the <code>String</code> associated with the specified key.
	 * @param key the key to the resource <code>String</code>
	 * @return the resource <code>String</code>
	 */
	public static String getString(String key)
	{
		return IGSMessageType.bundle.getString(key);
	}
}
