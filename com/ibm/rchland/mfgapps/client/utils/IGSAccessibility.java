/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      35394PL  R Prechel        -Initial version
 * 2006-09-28   ~1 35394PL  R Prechel        -Changed implementation of replace
 *                                           -Changed upperCase to capitalize
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils;

import java.awt.Component;
import java.awt.Container;

import javax.accessibility.AccessibleBundle;
import javax.accessibility.AccessibleContext;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * <code>IGSAccessibility</code> contains utility methods for setting the
 * properties of an <code>AccessibleContext</code>.
 * @author The External Fulfillment Client Development Team
 */
public class IGSAccessibility
{
	/** The first replacement target <code>String</code>. */
	public static final String TARGET_0 = "{0}"; //$NON-NLS-1$

	/**
	 * Sets the accessible description of the specified <code>JMenuItem</code>
	 * based on its accessible name, its accessible role, and the accessible
	 * name of the specified <code>JMenu</code>.
	 * @param item the <code>JMenuItem</code>
	 * @param menu the <code>JMenu</code> which contains the specified
	 *        <code>JMenuItem</code>
	 */
	public static void setDescription(JMenuItem item, JMenu menu)
	{
		AccessibleContext context = item.getAccessibleContext();
		//~1 Changed upperCase to capitalize
		String role = capitalize(context.getAccessibleRole(), false);
		StringBuffer description = new StringBuffer();
		if (menu != null)
		{
			description.append(menu.getAccessibleContext().getAccessibleName());
			description.append(": "); //$NON-NLS-1$
		}
		description.append(context.getAccessibleName());
		description.append(' ');
		description.append(role);
		context.setAccessibleDescription(description.toString());
	}

	/**
	 * Sets the accessible description of the specified <code>JComponent</code>
	 * based on its accessible name and its accessible role.
	 * @param component the <code>JComponent</code>
	 */
	public static void setDescription(JComponent component)
	{
		AccessibleContext context = component.getAccessibleContext();
		//~1 Changed upperCase to capitalize
		String role = capitalize(context.getAccessibleRole(), false);
		StringBuffer description = new StringBuffer();
		description.append(context.getAccessibleName());
		description.append(' ');
		description.append(role);
		context.setAccessibleDescription(description.toString());
	}

	/**
	 * Sets the accessible name and the accessible description of the specified
	 * <code>JScrollPane</code> and its children <code>Component</code>s
	 * based on the specified <code>name</code>.
	 * @param scrollPane the <code>JScrollPane</code>
	 * @param name the accessible name. The substring &quot;{0}&quot; will be
	 *        replaced with a space for the <code>JScrollPane</code> and role
	 *        information for the <code>JScrollPane</code>'s children
	 *        <code>Component</code>s.
	 */
	public static void setNameAndDescription(JScrollPane scrollPane, String name)
	{
		AccessibleContext context = scrollPane.getAccessibleContext();
		String description = replace(name, TARGET_0, " "); //$NON-NLS-1$
		context.setAccessibleName(description);
		context.setAccessibleDescription(description);

		context = scrollPane.getViewport().getAccessibleContext();
		//~1 Changed upperCase to capitalize
		description = replace(name, TARGET_0, capitalize(context.getAccessibleRole(), true));
		context.setAccessibleName(description);
		context.setAccessibleDescription(description);

		context = scrollPane.getVerticalScrollBar().getAccessibleContext();
		//~1 Changed upperCase to capitalize
		description = replace(name, TARGET_0, capitalize(context.getAccessibleRole(), true));
		context.setAccessibleName(description);
		context.setAccessibleDescription(description);

		context = scrollPane.getHorizontalScrollBar().getAccessibleContext();
		//~1 Changed upperCase to capitalize
		description = replace(name, TARGET_0, capitalize(context.getAccessibleRole(), true));
		context.setAccessibleName(description);
		context.setAccessibleDescription(description);
	}

	/**
	 * Replaces all occurrences of <code>target</code> in <code>string</code>
	 * with <code>replacement</code>.
	 * <p>
	 * Examples:<br>
	 * replace(&quot;ABC&quot;, &quot;&quot;, &quot;D&quot;) &rarr; &quot;DADBDCD&quot;<br>
	 * replace(&quot;ABBC&quot;, &quot;B&quot;, &quot;BB&quot;) &rarr; &quot;ABBBBC&quot;
	 * @param string the <code>String</code> in which all occurrences of
	 *        <code>target</code> are replaced with <code>replacement</code>
	 * @param target the <code>String</code> to replace
	 * @param replacement the <code>String</code> to insert
	 * @return the <code>String</code> resulting from the replacement. If
	 *         <code>target</code> was not in <code>string</code>, the
	 *         original value of <code>string</code> is returned.
	 */
	public static String replace(String string, String target, String replacement)
	{
		//~1 Changed implementation of replace.
		//   Previous version was not designed to handle the case where
		//   target was a substring of replacement; this version is

		//Store the lengths of the three Strings, the are used often
		final int sLength = string.length();
		final int tLength = target.length();
		final int rLength = replacement.length();

		//target is the empty String
		//Example: replace("ABC", "", "D") -> "DADBDCD"
		if (tLength == 0)
		{
			//((sLength + 1) copies of replacement) + the original string
			StringBuffer result = new StringBuffer(((sLength + 1) * rLength) + sLength);
			result.append(replacement);
			for (int i = 0; i < sLength; i++)
			{
				result.append(string.charAt(i));
				result.append(replacement);
			}
			return result.toString();
		}

		//target is not the nonempty String
		//Example: replace("ABBC", "B", "BB") -> "ABBBBC"
		StringBuffer result = new StringBuffer();
		final char first = target.charAt(0);
		int substringIndex = 0;
		int matchIndex = string.indexOf(first, 0);

		WHILE:
		while (matchIndex != -1 && matchIndex + tLength <= sLength)
		{
			//Make sure each character matches
			for (int i = 1; i < tLength; i++)
			{
				if (string.charAt(matchIndex + i) != target.charAt(i))
				{
					//Not a match, start while over with next character
					matchIndex = string.indexOf(first, matchIndex + 1);
					continue WHILE;
				}
			}

			//Match.  Insert replacement and skip over target
			result.append(string.substring(substringIndex, matchIndex));
			result.append(replacement);
			substringIndex = matchIndex + tLength;
			matchIndex = string.indexOf(first, substringIndex);
		}

		//target not in string, return original string
		if (result.length() == 0 && substringIndex == 0)
		{
			return string;
		}
		result.append(string.substring(substringIndex));
		return result.toString();
	}

	/**
	 * Sets the message <code>JLabel</code> of a <code>JOptionPane</code> as
	 * the label for the input value <code>Component</code> of the
	 * <code>JOptionPane</code>.
	 * @param pane the <code>JOptionPane</code>
	 * @param message the message displayed by the specified
	 *        <code>JOptionPane</code>'s message <code>JLabel</code>
	 */
	public static final void setLabelFor(JOptionPane pane, String message)
	{
		new MyJOptionPaneHelper(pane, message).run();
	}

	/**
	 * Capitalizes the words in the display <code>String</code> of an
	 * <code>AccessibleBundle</code>.
	 * <p>
	 * Example: <br>
	 * For <code>bundle.toDisplayString()</code> &rarr; &quot;push button&quot;
	 * <br>
	 * <code>capitalize(role, true)</code> &rarr; &quot;&nbsp;Push Button&nbsp;&quot;
	 * <br>
	 * <code>capitalize(role, false)</code> &rarr; &quot;Push Button&quot;
	 * @param bundle the <code>AccessibleBundle</code>
	 * @param spaces <code>true</code> if a leading and a trailing space
	 *        should be appended to the display <code>String</code>
	 * @return the display <code>String</code> with the words capitalized and,
	 *         if <code>spaces == true</code>, a leading and a trailing space
	 * @see AccessibleBundle#toDisplayString()
	 */
	private static String capitalize(AccessibleBundle bundle, boolean spaces)
	{
		//~1 Changed upperCase to capitalize and updated documentation
		String string = bundle.toDisplayString();
		StringBuffer buffer = new StringBuffer(string.length() + 2);
		char current;
		char previous = '.'; //Any non letter

		if (spaces)
		{
			buffer.append(' ');
		}

		// A letter (current) is the start of a new word if the previous
		// character was not a letter. previous starts out as a non-letter, so
		// the first letter of the string is capitalized.
		for (int i = 0; i < string.length(); i++)
		{
			current = string.charAt(i);
			if (Character.isLetter(current) && !Character.isLetter(previous))
			{
				buffer.append(Character.toUpperCase(current));
			}
			else
			{
				buffer.append(current);
			}
			previous = current;
		}
		if (spaces)
		{
			buffer.append(' ');
		}
		return buffer.toString();
	}

	/**
	 * <code>MyJOptionPaneHelper</code> is used to set the message
	 * <code>JLabel</code> of a <code>JOptionPane</code> as the label for
	 * the input value <code>Component</code> of the <code>JOptionPane</code>.
	 * @author The External Fulfillment Client Development Team
	 */
	private static class MyJOptionPaneHelper
		implements Runnable
	{
		/** The message <code>JLabel</code>. */
		private JLabel label;

		/** The input value <code>Component</code>.*/
		private Component component;

		/** The message displayed by the message <code>JLabel</code>. */
		private String message;

		/** The <code>JOptionPane</code>. */
		private JOptionPane optionPane;

		/**
		 * Constructs a new <code>MyJOptionPaneHelper</code>.
		 * @param pane the <code>JOptionPane</code>
		 * @param message the message displayed by the specified
		 *        <code>JOptionPane</code>'s message <code>JLabel</code>
		 */
		public MyJOptionPaneHelper(JOptionPane pane, String message)
		{
			this.optionPane = pane;
			this.message = message;
		}

		/**
		 * Finds the <code>JOptionPane</code>'s message <code>JLabel</code>
		 * and input value <code>Component</code>. Sets the message
		 * <code>JLabel</code> as the label for the input value
		 * <code>Component</code>.
		 */
		public void run()
		{
			find(this.optionPane);
			if (this.component != null && this.label != null)
			{
				this.label.setLabelFor(this.component);
			}
		}

		/**
		 * Finds the message <code>JLabel</code> and input value
		 * <code>Component</code> by recursing on the children of the
		 * specified <code>Container</code>.
		 * @param container a <code>Container</code> in the
		 *        <code>JOptionPane</code> containment hierarchy
		 */
		private void find(Container container)
		{
			Component[] children = container.getComponents();
			for (int i = 0; i < children.length && (this.component == null || this.label == null); i++)
			{
				if (children[i] instanceof JLabel)
				{
					JLabel temp = (JLabel) children[i];
					if (this.message.equals(temp.getText()))
					{
						this.label = temp;
						continue;
					}
				}
				else if (children[i] instanceof JComboBox || children[i] instanceof JList
						|| children[i] instanceof JTextField)
				{
					this.component = children[i];
					continue;
				}

				if (children[i] instanceof Container)
				{
					find((Container) children[i]);
				}
			}
		}
	}
}
