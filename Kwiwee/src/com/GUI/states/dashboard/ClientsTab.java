package com.GUI.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.GUI.ColorConstants;
import javaNK.util.data.MysqlModifier;

public class ClientsTab extends Tab
{
	private static final long serialVersionUID = 5805704433867260314L;
	private final static int COLUMNS = 3;
	protected final static Font FONT = new Font("Tahoma", Font.BOLD, 18);
	protected final static Font FONT_HEAD_LINE = new Font("Tahoma", Font.BOLD, 22);
	
	private JTable table;
	private DefaultTableModel model;
	private String[] columns;
	
	public ClientsTab() {
		//services table
		this.columns = new String[3];
		columns[0] = "Name";
		columns[1] = "Duration";
		columns[2] = "Price";
		
		this.model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		
		Dimension tableDim = new Dimension(550, 400);
		this.table = new JTable(0, COLUMNS);
		table.setPreferredSize(tableDim);
		table.setModel(model);
		table.setForeground(ColorConstants.TEXT_COLOR_DARK);
		table.setRowHeight(30);
		table.setEnabled(false);
		
		//set headers
		for (int i = 0; i < columns.length; i++)
			table.getColumnModel().getColumn(i).setHeaderValue(columns[i]);
		
		//table's scroll pane
		JPanel tableContainer = new JPanel(new BorderLayout());
		tableContainer.setPreferredSize(tableDim);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(tableDim);
		tableContainer.add(scrollPane, BorderLayout.PAGE_START);
		gbc.insets = new Insets(20, 20, 10, 10);
		addComponent(tableContainer, 0, 0);
	}

	@Override
	protected String getTitle() { return "Clients"; }
	
	@Override
	protected Color getTabColor() { return new Color(60, 177, 137); }
	
	@Override
	protected String[] getDescription() {
		return new String[] {
			"Take a look at all of the clients that ever visited your business.",
			"You can add clients or delete them manually."
		};
	}

	@Override
	protected boolean addButtonFunction(boolean activate) {
		if (activate) {
			table.setEnabled(true);
			model.setNumRows(model.getRowCount() + 1);
		}
		
		return true;
	}

	@Override
	protected boolean deleteButtonFunction(boolean activate) {
		if (activate) {
			if (!table.isEditing()) {
				//remove the selected rows
				int[] rows = table.getSelectedRows();
				for(int i = 0; i < rows.length; i++)
					model.removeRow(rows[i] - i);
			   
				table.clearSelection();
			}
		}
		
		return true;
	}

	@Override
	protected boolean saveButtonFunction(boolean activate) {
		if (activate) {
			Queue<Integer> rowsToAdd = new LinkedList<Integer>();
			String query;
			boolean add;
			
			//find the rows that need to be updated in the database
			for (int i = 0; i < table.getRowCount(); i++) {
				add = true;
				for (int j = 0; j < columns.length; j++) {
					String value = (String) table.getValueAt(i, j);
					if (value == null || value.equals("")) {
						add = false;
						break;
					}
				}
				if (add) rowsToAdd.add(i);
			}
			
			//insert the new rows to the database or update them if they already exist
			while (!rowsToAdd.isEmpty()) {
				int row = rowsToAdd.poll();
				
				query = "SELECT name "
					  + "FROM services "
					  + "WHERE name = '" + table.getValueAt(row, 0) + "'";
				
				//check if value exists - update
				try {
					MysqlModifier.readVARCHAR(query, "name");
					query = "UPDATE services "
						  + "SET time = " + table.getValueAt(row, 1) + ", "
						  + "price = " + table.getValueAt(row, 2) + " "
						  + "WHERE name = '" + table.getValueAt(row, 0) + "'";
				}
				//create new entry
				catch (SQLException e1) {
					query =  "INSERT INTO services(name, time, price) "
						   + "VALUES ("
						   + "'" + table.getValueAt(row, 0) + "', " + table.getValueAt(row, 1) + ", "
						   + table.getValueAt(row, 2) + ")";
				}
				
				//update or insert to database
				try { MysqlModifier.write(query); }
				catch (SQLException e2) { e2.printStackTrace(); }
			}
		}
		
		return true;
	}
}