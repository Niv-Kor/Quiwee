package com.queue_management;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.data.MysqlLoader;
import com.data.tables.ClientsTable;
import com.main.User;

public class ClientsRetriever implements ActionListener
{
	private JTable table;
	private JTextField searchField;
	
	public ClientsRetriever(JTable table, JTextField searchField) {
		this.table = table;
		this.searchField = searchField;
		fixClientList("");
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		fixClientList(searchField.getText());
	}
	
	private void fixClientList(String keyword) {
		Object[][] clientsRows;
		String query = "SELECT * "
					 + "FROM clients c "
					 + "WHERE c.user_id = '" + User.getKey() + "' "
			 		 + "AND (c.full_name LIKE '%" + keyword + "%' "
			 		 + "OR c.phone_number LIKE '%" + keyword + "%')";
		
		//retrieve data from data base
		try { clientsRows = MysqlLoader.getRows(ClientsTable.class, query); }
		catch (SQLException ex) { clientsRows = new Object[0][0]; }
		
		//remodel the table
		try {
			DefaultTableModel model = new DefaultTableModel(clientsRows.length, 2);
			table.setModel(model);
		}
		catch (IndexOutOfBoundsException ex) {}
		
		//set all values into the table
		for (int i = 0; i < clientsRows.length; i++) {
			//name
			table.setValueAt(clientsRows[i][2], i, 0);
			
			//phone
			table.setValueAt(clientsRows[i][1], i, 1);
		}
		
		//set table's column names
		table.getColumnModel().getColumn(0).setHeaderValue("Name");
		table.getColumnModel().getColumn(1).setHeaderValue("Phone No.");
	}
}