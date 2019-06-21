package com.GUI.states.dashboard;
import java.awt.Color;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import com.content.service.Service;

public class ServicesTab extends DatabaseTab
{
	private static final long serialVersionUID = 4305725457016866299L;

	public ServicesTab() {
		super("services");
	}

	@Override
	protected String getTitle() { return "Services"; }
	
	@Override
	protected Color getTabColor() { return new Color(201, 162, 58); }

	@Override
	protected String[] getDescription() {
		return new String[] {
			"Services are there to make the creation of similar queues easier for you.",
			"Create and modify your services right here!"
		};
	}
	
	@Override
	protected boolean deleteButtonFunction(boolean activate) {
		if (activate) {
			if (!table.isEditing()) {
				int[] rows = table.getSelectedRows();
				
				for(int i = 0; i < rows.length; i++) {
					//remove the selected rows from the data base
					String name = (String) table.getValueAt(i, 0);
					try { new Service(name).delete(); }
					catch (SQLException | NullPointerException e) {}
					
					//remove the selected rows from the table
					model.removeRow(rows[i] - i);
				}
				
				table.clearSelection();
			}
		}
		
		return true;
	}

	@Override
	protected boolean saveButtonFunction(boolean activate) {
		if (activate) {
			Queue<Integer> rowsToAdd = new LinkedList<Integer>();
			boolean add;
			
			//find the rows that need to be updated in the database
			for (int i = 0; i < table.getRowCount(); i++) {
				add = true;
				for (int j = 0; j < getTableColumns().length; j++) {
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
				String name = (String) table.getValueAt(row, 0);
				double price;
				boolean hourly;
				
				try {
					price = Double.parseDouble((String) table.getValueAt(row, 1));
					hourly = Boolean.parseBoolean((String) table.getValueAt(row, 2));
				}
				catch (ClassCastException e) { return true; }

				/*
				 * Call Service constructor that will update or insert
				 * the new information straight into the data base.
				 */
				new Service(name, price, hourly);
			}
		}
		
		return true;
	}

	@Override
	protected String[] getTableColumns() {
		return new String[] {"Name", "Price", "Hourly"};
	}
	
	@Override
	protected String[] getMysqlColumns() {
		return new String[] {"name", "price", "hourly"};
	}
}