package com.GUI.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.GUI.constants.ColorConstants;
import com.GUI.constants.FontConstants;
import com.main.User;
import javaNK.util.data.MysqlModifier;

public abstract class DatabaseTab extends Tab
{
	private static final long serialVersionUID = -1581118177227279546L;
	
	protected JTable table;
	protected DefaultTableModel model;
	
	public DatabaseTab(String tableName) {
		//retrieve all information from data base
		String[] columns = getTableColumns();
		String overallQuery = "SELECT * FROM " + tableName + " WHERE user_id = '" + User.getKey() + "'";
		List<List<String>> lists = new ArrayList<List<String>>();
		
		this.model = new DefaultTableModel(0, columns.length);
		model.setColumnIdentifiers(columns);
		
		Dimension tableDim = new Dimension(550, 400);
		this.table = new JTable();
		table.setPreferredSize(tableDim);
		table.setModel(model);
		table.setForeground(ColorConstants.TEXT_COLOR_DARK);
		table.setFont(FontConstants.SMALL_LABEL_FONT.deriveFont((float) 12));
		table.getTableHeader().setBackground(getTabColor().darker());
		table.getTableHeader().setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
		table.setRowHeight(30);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		for (int i = 0; i < getMysqlColumns().length; i++) {
			//align all cells of that column to the center
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			
			//retrieve a list for that column from the data base
			try { lists.add(MysqlModifier.readAllVARCHAR(overallQuery, getMysqlColumns()[i])); }
			catch (SQLException e) {}
			model.setRowCount(lists.get(i).size());
			
			for (int j = 0; j < lists.get(i).size(); j++)
				model.setValueAt(lists.get(i).get(j), j, i);
		}
		
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
	protected boolean addButtonFunction(boolean activate) {
		if (activate) model.setNumRows(model.getRowCount() + 1);
		return true;
	}

	protected abstract String[] getTableColumns();
	protected abstract String[] getMysqlColumns();
}