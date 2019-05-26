package com.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.database.SQLModifier;
import com.states.dashboard.calendar.Calendar;
import com.utility.OptionLabel;

public class ServicesTab extends JPanel {
	private final static int COLUMNS = 3;
	private final static Dimension TABLE_DIM = new Dimension(400, 300);
	private final static Dimension SCROLL_SIZE = new Dimension(50, 400);
	protected final static Font FONT = new Font("Tahoma", Font.BOLD, 18);
	protected final static Font FONT_HEAD_LINE = new Font("Tahoma", Font.BOLD, 22);
	
	
	private JTable table;
	private JLabel newService;
	private GridBagConstraints gbc;
	
	public ServicesTab() {
		super(new BorderLayout());
		this.gbc = new GridBagConstraints();
		
		Insets defInsets = new Insets(0, 0, 0, 0);
		
		String [] columns = {"Name" ,"Time", "Price"};
		table = new JTable(0, COLUMNS);
		table.setPreferredSize(TABLE_DIM);
		
		for (int i = 0; i < columns.length; i++)
			table.getColumnModel().getColumn(i).setHeaderValue(columns[i]);
		
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		table.setForeground(Color.BLACK);
		Font table_font = new Font("",1,22);
		table.setFont(table_font);
		table.setRowHeight(30);
		table.setEnabled(false);

		JPanel northPane = new JPanel(new GridBagLayout());
		northPane.setPreferredSize(new Dimension(Calendar.DIM.width, (int) (Calendar.DIM.height /1.4)));
		northPane.setBackground(Color.gray.darker());
		
		JLabel headline = new JLabel("Service");
		headline.setPreferredSize(new Dimension(200,100));
		headline.setFont(FONT_HEAD_LINE);
		headline.setForeground(Color.WHITE);
		add(northPane, BorderLayout.NORTH);
		
		gbc.insets = new Insets(-10, -150, 0, 0);
		insertToPane(northPane , headline, 0, 0);
		gbc.insets = defInsets;
		
		JPanel tableContainer = new JPanel(new BorderLayout());
		tableContainer.setPreferredSize(TABLE_DIM);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(SCROLL_SIZE);
		tableContainer.add(scrollPane, BorderLayout.PAGE_START);
		add(northPane, BorderLayout.NORTH);
		
		gbc.insets = new Insets(40, -100, 0, 0);
		insertToPane(northPane , tableContainer , 1, 1);
		
		gbc.insets = defInsets;
		
		JPanel southPane = new JPanel(new GridBagLayout());
		southPane.setPreferredSize(new Dimension(Calendar.DIM.width , Calendar.DIM.height - northPane.getPreferredSize().height));
		southPane.setBackground(Color.gray.darker());
		newService = new OptionLabel("new service");
		newService.setPreferredSize(new Dimension(140,20));
		newService.setFont(FONT);
		newService.setForeground(Color.WHITE);
		add(southPane, BorderLayout.SOUTH);
		gbc.insets = new Insets(-89, 100, 0, 0);
		insertToPane(southPane , newService , 0 , 0);

		newService.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				table.setEnabled(true);
				model.setNumRows(model.getRowCount() + 1);
			}
		});
		
		JLabel delete = new OptionLabel("delete service");
		delete.setPreferredSize(new Dimension(140,20));
		gbc.insets = new Insets(-89, 100, 0, 110);
		insertToPane(southPane , delete, 1, 0);
		delete.setFont(FONT);
		delete.setForeground(Color.WHITE);
		delete.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (table.isEditing()) return;
			   int[] rows = table.getSelectedRows();
			   for(int i = 0; i < rows.length; i++)
				   model.removeRow(rows[i] - i);
			   
			   table.clearSelection();
			  // SQLModifier.write("insert into users (Name,Time,Price) ");
			}
		});
		
		
		JLabel save = new OptionLabel("Save");
		save.setPreferredSize(new Dimension(140,20));
		gbc.insets = new Insets(0, 0, 0, -100);
		insertToPane(southPane ,save, 2, 1);
		save.setFont(FONT);
		save.setForeground(Color.WHITE);
		save.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				String query;
				
				//TODO
				/*
				 * check that the row is full with data
				 */
				
				try {
					/*
					 * UPDATE services
					 * set x = y,
					 * x = y,
					 * x = y;
					 */
					
					query =  "UP INTO services(service_id, name, time, price) "
							+ "VALUES ( "
							+ table.getValueAt(0,0) + ", " + table.getValueAt(1,0) + ", "
							+ table.getValueAt(2,0) + ")";
					
					SQLModifier.write(query);
				}
				catch (Exception e1) {
					query =  "UP INTO services(service_id, name, time, price) "
							+ "VALUES ( "
							+ table.getValueAt(0,0) + ", " + table.getValueAt(1,0) + ", "
							+ table.getValueAt(2,0) + ")";
					
					try { SQLModifier.write(query); }
					catch(Exception e2) {
						System.err.println("Couldn't update or insert the new data from some reason.");
						e2.printStackTrace();
					}
				}
			}
	});
	}
		
	
	private void insertToPane(JPanel panel, Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		panel.add(c, gbc);
	}
	
}