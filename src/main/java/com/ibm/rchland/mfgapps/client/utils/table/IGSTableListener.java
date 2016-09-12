/* Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-25      35394PL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.client.utils.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.ibm.rchland.mfgapps.client.utils.IGSAccessibility;
import com.ibm.rchland.mfgapps.client.utils.listener.IGSWindowDisposeAL;

/**
 * <code>IGSTableListener</code> is a <code>KeyListener</code> and
 * <code>MouseListener</code> for a <code>JTable</code> that:
 * <ul>
 * <li>allows a <code>JButton</code> in the table to appear armed</li>
 * <li>allows an <code>IGSCellInformation</code> to be selected</li>
 * <li>displays a JPopupMenu with table options</li>
 * </ul>
 * @author The External Fulfillment Client Development Team
 */
public class IGSTableListener
	implements KeyListener, MouseListener, Serializable
{
	/**
	 * Identifies the original class version for which this class is capable of
	 * writing streams and from which it can read.
	 */
	private static final long serialVersionUID = 1L;

	/** The <code>ResourceBundle</code>. */
	protected static final ResourceBundle bundle = ResourceBundle.getBundle(
			IGSTableListener.class.getName() + "Resources", //$NON-NLS-1$
			Locale.getDefault(), IGSTableListener.class.getClassLoader());

	/** The key suffix for a name. */
	protected static final String NAME_SUFFIX = "Name"; //$NON-NLS-1$

	/** The key suffix for a mnemonic. */
	protected static final String MNEMONIC_SUFFIX = "Mnemonic"; //$NON-NLS-1$

	/** The key suffix for a description. */
	protected static final String DESC_SUFFIX = "Description"; //$NON-NLS-1$

	/** The key root for the set sort order menu. */
	protected static final String SET_SORT_ORDER = "setSortOrder"; //$NON-NLS-1$

	/** The key root for the column width text field. */
	protected static final String COLUMN_WIDTH = "columnWidth"; //$NON-NLS-1$

	/** The key root for the ok button. */
	protected static final String OK_BUTTON = "okButton"; //$NON-NLS-1$

	/** The key root for the cancel button. */
	protected static final String CANCEL_BUTTON = "cancelButton"; //$NON-NLS-1$

	/** The action command and key root to change a column's width. */
	protected static final String CHANGE_WIDTH = "changeWidth"; //$NON-NLS-1$

	/** The action command and key root for the ascending sort menu item. */
	protected static final String ASCENDING = "ascending"; //$NON-NLS-1$

	/** The action command and key root for the descending sort menu item. */
	protected static final String DESCENDING = "descending"; //$NON-NLS-1$

	/** The action command and key root for the not sorted menu item. */
	protected static final String NOT_SORTED = "notSorted"; //$NON-NLS-1$

	/** The amount of padding in the change width dialog. */
	private static final int PADDING = 10;

	/** The currently armed <code>IGSTableCellRenderer</code>. */
	private IGSTableCellRenderer armedTCR = null;

	/** The <code>JTable</code> that uses {@link #armedTCR}. */
	private JTable armedTable = null;

	/** Constructs a new <code>IGSTableListener</code>. */
	public IGSTableListener()
	{
		//Nothing to do
	}

	/**
	 * Invoked when a key has been typed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyTyped(KeyEvent ke)
	{
		if (ke.getKeyChar() == ' ')
		{
			Object source = ke.getSource();
			if (source instanceof JTable)
			{
				JTable table = (JTable) source;
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				disarm();
				dispatch(table, row, column);
			}
		}
	}

	/**
	 * Invoked when a key has been pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		if (ke.getKeyCode() == KeyEvent.VK_F2 && ke.getModifiers() == 0)
		{
			Object source = ke.getSource();
			if (source instanceof JTable)
			{
				JTable table = (JTable) source;
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				showPopupMenu(table, row, column);
			}
		}
		else if (ke.getKeyCode() == KeyEvent.VK_SPACE)
		{
			Object source = ke.getSource();
			if (source instanceof JTable)
			{
				JTable table = (JTable) source;
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				arm(table, row, column);
			}
		}
	}

	/**
	 * Invoked when a key has been released.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyReleased(KeyEvent ke)
	{
		//Nothing to do
		//Disarm is handled by keyTyped
	}

	/**
	 * Invoked when the mouse has been clicked on a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseClicked(MouseEvent me)
	{
		Object source = me.getSource();
		if (source instanceof JTable)
		{
			JTable table = (JTable) source;
			Point point = me.getPoint();
			int row = table.rowAtPoint(point);
			int column = table.columnAtPoint(point);
			if (me.getButton() == MouseEvent.BUTTON1)
			{
				dispatch(table, row, column);
			}
			else
			{
				showPopupMenu(table, row, column);
			}
		}
	}

	/**
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mousePressed(MouseEvent me)
	{
		if (me.getButton() == MouseEvent.BUTTON1)
		{
			Object source = me.getSource();
			if (source instanceof JTable)
			{
				JTable table = (JTable) source;
				Point point = me.getPoint();
				int row = table.rowAtPoint(point);
				int column = table.columnAtPoint(point);
				arm(table, row, column);
			}
		}
	}

	/**
	 * Invoked when the mouse has been released on a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseReleased(MouseEvent me)
	{
		if (me.getButton() == MouseEvent.BUTTON1)
		{
			disarm();
		}
	}

	/**
	 * Invoked when the mouse enters a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseEntered(MouseEvent me)
	{
		//Nothing to do
	}

	/**
	 * Invoked when the mouse exits a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseExited(MouseEvent me)
	{
		//Nothing to do
	}

	/**
	 * Disarms the currently armed <code>IGSTableCellRenderer</code> and
	 * repaints the <code>JTable</code> that uses it to render cells.
	 */
	protected void disarm()
	{
		if (this.armedTCR != null)
		{
			this.armedTCR.setColumn(-1);
			this.armedTCR.setRow(-1);
			this.armedTCR = null;
			this.armedTable.repaint();
			this.armedTable = null;
		}
	}

	/**
	 * Arms the <code>IGSTableCellRenderer</code> used to render the cell at
	 * the specified position in the specified <code>table</code>.
	 * @param table a <code>JTable</code>
	 * @param row the 0-based row index
	 * @param column the 0-based column index
	 */
	protected void arm(JTable table, int row, int column)
	{
		if (table.isEnabled() == false)
		{
			return;
		}
		if (row >= 0 && column >= 0 && row < table.getRowCount() && column < table.getColumnCount())
		{
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			if (renderer instanceof IGSTableCellRenderer)
			{
				this.armedTCR = (IGSTableCellRenderer) renderer;
				this.armedTCR.setColumn(column);
				this.armedTCR.setRow(row);
				this.armedTable = table;
				this.armedTable.repaint();
			}
		}
	}

	/**
	 * Invokes the {@link IGSCellInformation#notifyProcessors()} method if the
	 * <code>Object</code> returned by {@link JTable#getValueAt(int, int)} is
	 * an instance of <code>IGSCellInformation</code>. Otherwise, if the
	 * <code>Object</code> returned by {@link JTable#getValueAt(int, int)} is
	 * a <code>Component</code>, a mouse clicked <code>MouseEvent</code> is
	 * dispatched to the <code>Component</code>.
	 * @param table a <code>JTable</code>
	 * @param row the 0-based row index
	 * @param column the 0-based column index
	 */
	protected void dispatch(JTable table, int row, int column)
	{
		if (table.isEnabled() == false)
		{
			return;
		}
		if (row >= 0 && column >= 0 && row < table.getRowCount() && column < table.getColumnCount())
		{
			Object obj = table.getValueAt(row, column);
			if (obj instanceof IGSCellInformation)
			{
				((IGSCellInformation) obj).notifyProcessors();
			}
			else if (obj instanceof Component)
			{
				Component cmp = (Component) obj;
				MouseEvent me = new MouseEvent(cmp, MouseEvent.MOUSE_CLICKED, new Date().getTime(), 0, 0, 0, 1, false);
				cmp.dispatchEvent(me);
			}
		}
	}

	/**
	 * Displays a <code>JPopupMenu</code> with table options.
	 * @param table a <code>JTable</code>
	 * @param row the 0-based row index
	 * @param column the 0-based column index
	 */
	protected void showPopupMenu(JTable table, int row, int column)
	{
		if (table.isEnabled() == false)
		{
			return;
		}
		if (row >= 0 && column >= 0 && row < table.getRowCount() && column < table.getColumnCount())
		{
			JPopupMenu popupMenu = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem(bundle.getString(CHANGE_WIDTH + NAME_SUFFIX));
			menuItem.setMnemonic(bundle.getString(CHANGE_WIDTH + MNEMONIC_SUFFIX).charAt(0));
			IGSAccessibility.setDescription(menuItem);
			menuItem.setActionCommand(CHANGE_WIDTH);
			popupMenu.add(menuItem);

			TableColumn tc = table.getColumnModel().getColumn(column);
			if (table.getModel() instanceof IGSSortableTableModel)
			{
				ActionListener listener = new MyMenuActionListener(tc, table);
				menuItem.addActionListener(listener);

				JMenu menu = new JMenu(bundle.getString(SET_SORT_ORDER + NAME_SUFFIX));
				menu.setMnemonic(bundle.getString(SET_SORT_ORDER + MNEMONIC_SUFFIX).charAt(0));
				IGSAccessibility.setDescription(menu);
				popupMenu.add(menu);

				addMenuItem(menu, ASCENDING, listener);
				addMenuItem(menu, DESCENDING, listener);
				addMenuItem(menu, NOT_SORTED, listener);
			}
			else
			{
				menuItem.addActionListener(new MyMenuActionListener(tc, null));
			}

			int xPos = 0;
			for (int i = 0; i < column; i++)
			{
				xPos += table.getColumnModel().getColumn(i).getWidth();
			}
			popupMenu.show(table, xPos, row * table.getRowHeight());
		}
	}

	/**
	 * Creates and adds a <code>JMenuItem</code> to the specified
	 * <code>JMenu</code> based on the specified <code>root</code>.
	 * @param menu the <code>JMenu</code> to which the <code>JMenuItem</code>
	 *        will be added
	 * @param root the <code>String</code> for the <code>JMenu</code>'s
	 *        action command and the key root for the
	 *        <code>ResourceBundle</code> lookup
	 * @param listener the <code>ActionListener</code> for the
	 *        <code>JMenuItem</code>
	 */
	private void addMenuItem(JMenu menu, String root, ActionListener listener)
	{
		JMenuItem item = new JMenuItem(bundle.getString(root + NAME_SUFFIX));
		item.setMnemonic(bundle.getString(root + MNEMONIC_SUFFIX).charAt(0));
		item.setActionCommand(root);
		item.addActionListener(listener);
		IGSAccessibility.setDescription(item, menu);
		menu.add(item);
	}

	/**
	 * <code>MyMenuActionListener</code> is an <code>ActionListener</code>
	 * for the <code>JMenuItem</code>s of the <code>JPopupMenu</code>.
	 * @author The External Fulfillment Client Development Team
	 */
	private static class MyMenuActionListener
		implements ActionListener
	{
		/** The selected <code>TableColumn</code>. */
		private TableColumn tc;

		/** The selected <code>JTable</code>. */
		private JTable table;

		/**
		 * Constructs a new <code>MyMenuActionListener</code>.
		 * @param tc the selected <code>TableColumn</code>
		 * @param table the selected <code>JTable</code>. MUST be
		 *        <code>null</code> if the selected <code>JTable</code> does
		 *        not use an <code>IGSSortableTableModel</code>.
		 */
		public MyMenuActionListener(TableColumn tc, JTable table)
		{
			this.tc = tc;
			this.table = table;
		}

		/**
		 * Invoked when an action occurs.
		 * @param ae the <code>ActionEvent</code>
		 */
		public void actionPerformed(ActionEvent ae)
		{
			String command = ae.getActionCommand();
			if (CHANGE_WIDTH.equals(command))
			{
				changeWidth();

			}
			else if (this.table != null)
			{
				int index = this.tc.getModelIndex();
				IGSSortableTableModel model = (IGSSortableTableModel) this.table.getModel();
				if (ASCENDING.equals(command))
				{
					model.setSortingDirection(index, IGSSortableTableModel.ASCENDING);
					this.table.getTableHeader().repaint();
				}
				else if (DESCENDING.equals(command))
				{
					model.setSortingDirection(index, IGSSortableTableModel.DESCENDING);
					this.table.getTableHeader().repaint();
				}
				else if (NOT_SORTED.equals(command))
				{
					model.setSortingDirection(index, IGSSortableTableModel.NOT_SORTED);
					this.table.getTableHeader().repaint();
				}
			}
		}

		/**
		 * Creates the change column width <code>JDialog</code>.
		 * @return the change column width <code>JDialog</code>
		 */
		private JDialog createJDialog()
		{
			String title = bundle.getString(CHANGE_WIDTH + NAME_SUFFIX);
			Container c = this.table;
			JPanel panel = null;
			while (c != null && !(c instanceof Window))
			{
				if (panel == null && (c instanceof JPanel))
				{
					panel = (JPanel) c;
				}
				c = c.getParent();
			}

			JDialog result = null;
			if (c instanceof Frame)
			{
				result = new JDialog((Frame) c, title, true);
			}
			else if (c instanceof Dialog)
			{
				result = new JDialog((Dialog) c, title, true);
			}
			else
			{
				result = new JDialog();
				result.setTitle(title);
				result.setModal(true);
			}

			if (panel != null)
			{
				result.setLocationRelativeTo(panel);
			}
			return result;
		}

		/** Changes the width of the <code>TableColumn</code>. */
		private void changeWidth()
		{
			JDialog dialog = createJDialog();

			JPanel panel = new JPanel(new GridLayout(2, 2, PADDING, PADDING));
			panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
			dialog.setContentPane(panel);

			JLabel label = new JLabel(bundle.getString(COLUMN_WIDTH + NAME_SUFFIX));
			label.setToolTipText(bundle.getString(COLUMN_WIDTH + DESC_SUFFIX));
			panel.add(label);

			JTextField textField = new JTextField(5);
			textField.setText(Integer.toString(this.tc.getPreferredWidth()));
			label.setLabelFor(textField);
			panel.add(textField);

			JButton button = new JButton(bundle.getString(OK_BUTTON + NAME_SUFFIX));
			button.setToolTipText(bundle.getString(OK_BUTTON + DESC_SUFFIX));
			button.setMnemonic(bundle.getString(OK_BUTTON + MNEMONIC_SUFFIX).charAt(0));
			button.addActionListener(new MyOkActionListener(dialog, textField, this.tc));
			panel.add(button);
			dialog.getRootPane().setDefaultButton(button);

			button = new JButton(bundle.getString(CANCEL_BUTTON + NAME_SUFFIX));
			button.setToolTipText(bundle.getString(CANCEL_BUTTON + DESC_SUFFIX));
			button.setMnemonic(bundle.getString(CANCEL_BUTTON + MNEMONIC_SUFFIX).charAt(0));
			button.addActionListener(new IGSWindowDisposeAL(dialog));
			panel.add(button);

			dialog.pack();
			dialog.setVisible(true);
		}
	}

	/**
	 * <code>MyOkActionListener</code> is an <code>ActionListener</code> for
	 * the OK <code>JButton</code>.
	 * @author The External Fulfillment Client Development Team
	 */
	private static class MyOkActionListener
		implements ActionListener
	{
		/** The <code>JDialog</code> to dispose. */
		private JDialog dialog;

		/** The <code>JTextField</code> to read. */
		private JTextField textField;

		/** The <code>TableColumn</code> to size. */
		private TableColumn tableColumn;

		/**
		 * Constructs a new <code>MyOkActionListener</code>.
		 * @param dialog the <code>JDialog</code> to dispose
		 * @param textField the <code>JTextField</code> to read
		 * @param tableColumn the <code>TableColumn</code> to size
		 */
		public MyOkActionListener(JDialog dialog, JTextField textField, TableColumn tableColumn)
		{
			this.dialog = dialog;
			this.textField = textField;
			this.tableColumn = tableColumn;
		}

		/**
		 * Invoked when an action occurs.
		 * @param ae the <code>ActionEvent</code>
		 */
		public void actionPerformed(ActionEvent ae)
		{
			try
			{
				String text = this.textField.getText();
				int width = Integer.parseInt(text);
				this.tableColumn.setPreferredWidth(width);
				this.dialog.dispose();
			}
			catch (NumberFormatException nfe)
			{
				//Do nothing
			}
		}
	}
}
