/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-03-15      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.client.utils.layout.IGSCenterLayout;
import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;

/**
 * <code>IGSTestLayout</code> contains a main method to test the
 * functionality of the <code>IGSCenterLayout</code> and the
 * <code>IGSGridLayout</code> classes.
 * @author The MFS Client Development Team
 */
public class IGSTestLayout
{
	/**
	 * Main method to test functionality.
	 * @param args the command-line arguments for the application
	 */
	public static void main(String[] args)
	{
		final int rows = 3;
		final int cols = 5;
		
		JDialog dialog = new JDialog();
		dialog.setModal(true);
		ActionListener exit = new ExitActionListener();
		ActionListener setInvisible = new SetInvisibleActionListener(dialog);
		
		for(int count = 0; count <= cols; count++)
		{
			JPanel panel = new JPanel(new IGSGridLayout(rows - 1, cols, 2, 2));
			panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

			for (int r = 0; r < rows; r++)
			{
				if (r < rows - 1)
				{
					for (int c = 0; c < cols; c++)
					{
						JButton button = new JButton(r + "  " + c); //$NON-NLS-1$
						button.setName(button.getText());
						button.addActionListener(count == cols ? exit : setInvisible);
						panel.add(button);
					}
				}
				else
				{
					for (int c = 0; c < count; c++)
					{
						JButton button = new JButton(r + "  " + c); //$NON-NLS-1$
						button.setName(button.getText());
						button.addActionListener(count == cols ? exit : setInvisible);
						panel.add(button);
					}
				}
			}

			if(count == cols)
			{
				dialog.addWindowListener(new WindowAdapter()
				{
					/** 
					 * Invoked when a window is closed.
					 * @param e the <code>WindowEvent</code>
					 */
					public void windowClosing(WindowEvent e)
					{
						System.exit(0);
					}
				});
			}
			
			JPanel panelTwo = new JPanel(new IGSCenterLayout());
			panelTwo.setBackground(Color.RED);
			panelTwo.add(panel);
			dialog.setContentPane(panelTwo);
			dialog.pack();
			System.out.println(panelTwo.getPreferredSize());
			dialog.setVisible(true);
		}
	}
	
	/**
	 * <code>ExitActionListener</code> is an <code>ActionListener</code>
	 * that calls {@link System#exit(int)}.
	 * @author The MFS Client Development Team
	 */
	public static class ExitActionListener
		implements ActionListener
	{
		/** Constructs a new <code>ExitActionListener</code>. */
		public ExitActionListener()
		{
			//Nothing to do
		}
		/**
		 * Invoked when an action occurs; calls {@link System#exit(int)}.
		 * @param e the <code>ActionEvent</code>
		 */
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	/**
	 * <code>SetInvisibleActionListener</code> is an <code>ActionListener</code>
	 * that calls {@link Component#setVisible(boolean)}.
	 * @author The MFS Client Development Team
	 */
	public static class SetInvisibleActionListener
		implements ActionListener
	{
		/** The <code>Component</code> to set invisible. */
		private Component component;
		
		/**
		 * Constructs a new <code>SetInvisibleActionListener</code>.
		 * @param component the <code>Component</code> to set invisible
		 */
		public SetInvisibleActionListener(Component component)
		{
			this.component = component;
		}
		/**
		 * Invoked when an action occurs; calls {@link Component#setVisible(boolean)}.
		 * @param e the <code>ActionEvent</code>
		 */
		public void actionPerformed(ActionEvent e)
		{
			this.component.setVisible(false);
		}
	}
}
