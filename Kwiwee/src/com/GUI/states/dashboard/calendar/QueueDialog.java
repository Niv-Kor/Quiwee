package com.GUI.states.dashboard.calendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.content.client.Client;
import com.main.User;

import javaNK.util.data.MysqlModifier;

public class QueueDialog extends JFrame
{
	private static final long serialVersionUID = -4947642279270106942L;
	private final static String MESSAGE = "New queue appointment for ";
	private final static String SEARCH_MESSAGE = "search name or phone number";
	private final static Dimension DIM = new Dimension(500, 300);
	private final static Color BACKGROUND = new Color(149, 205, 252);
	private final static String[][] CLIENT_COLUMNS = {{"full_name", "phone_number"},
			  										  {"Name", "Phone Number"}};
	
	private Date startTime, endTime;
	private CalendarGrid datedPanel;
	private JPanel panel, clientPane, headlinePane, detailsPane;
	private JLabel clientName;
	private JTable clientTable;
	private Client selectedClient;
	private JComboBox<String> endDropDown;
	private List<ArrayList<String>> tableColumns;
	private GridBagConstraints gbc;
	
	public QueueDialog(CalendarGrid datedPanel, Date date) {
		super(MESSAGE + date);
		
		this.datedPanel = datedPanel;
		this.tableColumns = new ArrayList<ArrayList<String>>();
		this.gbc = new GridBagConstraints();
		this.startTime = new Date(date);
		this.endTime = new Date(date);
		endTime.setHour(endTime.getHour() + 1);
		
		setSize(DIM);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent arg0) {
				datedPanel.press(false);
			}
			
			public void windowOpened(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowActivated(WindowEvent arg0) {}
		});
		
		this.clientName = new JLabel("-no client selected-");
		this.panel = new JPanel(new BorderLayout());
		panel.setBackground(BACKGROUND);
		panel.setPreferredSize(DIM);
		
		this.endDropDown = new JComboBox<String>();
		endDropDown.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (endDropDown.getItemCount() > 0) {
					String item = (String) endDropDown.getSelectedItem();
					String hour = item.substring(0, 2);
					String minutes = item.substring(3, 5);
					endTime.setHour(Integer.parseInt(hour));
					endTime.setMinutes(Integer.parseInt(minutes));
				}
			}
		});
		
		createClientPane();
		createHeadlinePane();
		createDetailsPane();
		add(panel);
		panel.requestFocus();
	}
	
	private void createClientPane() {
		Dimension clientDim = new Dimension((int) (DIM.width / 2.3), (int) (DIM.height / 2));
		
		this.clientPane = new JPanel(new GridBagLayout());
		clientPane.setPreferredSize(new Dimension(DIM.width / 2, DIM.height));
		clientPane.setBackground(Color.GREEN);
		
		//create table
		JPanel clientTablePane = new JPanel(new BorderLayout());
		clientTablePane.setPreferredSize(clientDim);
		
		this.clientTable = new JTable(0, 0);
		clientTable.setPreferredSize(clientDim);
		clientTable.setRowHeight(15);
		clientTable.setBackground(Color.WHITE);
		fixClientList("");
		
		//align table cells
		DefaultTableCellRenderer tableRender = new DefaultTableCellRenderer();
		tableRender.setHorizontalAlignment(JLabel.CENTER);
		tableRender.setVerticalAlignment(JLabel.CENTER);
		clientTable.getColumnModel().getColumn(0).setCellRenderer(tableRender);
		clientTable.getColumnModel().getColumn(1).setCellRenderer(tableRender);
		
		//force selection of only 1 row
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		clientTable.setSelectionModel(selectionModel);
		clientTable.setDefaultEditor(Object.class, null);
		
		//include table within a scroll pane
		JScrollPane clientScrollPane = new JScrollPane(clientTable);
		clientScrollPane.setPreferredSize(clientDim);
		
		//search field and buttons
		String defMessage = new String(SEARCH_MESSAGE);
		JTextField searchField = new JTextField(defMessage);
		searchField.setPreferredSize(new Dimension(clientDim.width - 20, 20));
		searchField.setForeground(Color.GRAY);
		searchField.setHorizontalAlignment(JTextField.CENTER);
		
		searchField.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	if (searchField.getText().equals(defMessage)) {
		    		searchField.setText("");
		    		searchField.setForeground(Color.BLACK);
		    	}
		    }

		    public void focusLost(FocusEvent e) {
		    	if (searchField.getText().equals("")) {
		    		searchField.setText(defMessage);
		    		searchField.setForeground(Color.GRAY);
		    	}
		    }
		});
		
		JButton searchButton = new JButton("...");
		searchButton.setPreferredSize(new Dimension(20, 20));
		searchButton.setFont(new Font("Arial", Font.PLAIN, 7));
		searchButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				String filledText = searchField.getText();
				String keyword = (!filledText.equals(SEARCH_MESSAGE)) ? filledText : "";
				fixClientList(keyword);
			}
		});
		
		JButton selectButton = new JButton("Select client");
		selectButton.setPreferredSize(new Dimension(clientDim.width / 2, 20));
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String name, phone;
					
					int row = clientTable.getSelectedRow();
					if (row != -1) {
						name = tableColumns.get(0).get(row);
						phone = tableColumns.get(1).get(row);
						
						selectedClient = new Client(name, phone);
						clientName.setText(name);
					}
				}
				catch (SQLException e) {}
			}
		});
		
		gbc.insets = new Insets(0, -20, 10, 0);
		insertClientPane(searchField, 0, 1);
		gbc.insets = new Insets(0, -20, 10, 0);
		insertClientPane(searchButton, 1, 1);
		gbc.insets = new Insets(0, 0, 0, 0);
		insertClientPane(clientTablePane, 0, 2);
		gbc.insets = new Insets(10, 0, 10, 0);
		insertClientPane(selectButton, 0, 3);
		
		clientTablePane.add(clientScrollPane, BorderLayout.PAGE_START);
		panel.add(clientPane, BorderLayout.WEST);
	}
	
	private void createHeadlinePane() {
		Font labelFont = new Font("Tahoma", Font.PLAIN, 15);
		
		this.headlinePane = new JPanel(new GridBagLayout());
		headlinePane.setPreferredSize(new Dimension(DIM.width / 4 - 20, DIM.height));
		headlinePane.setBackground(Color.ORANGE);
		
		JLabel client = new JLabel("client:");
		client.setFont(labelFont);
		
		JLabel service = new JLabel("service:");
		service.setFont(labelFont);
		
		JLabel startTime = new JLabel("start time:");
		startTime.setFont(labelFont);
		
		JLabel endTime = new JLabel("end time:");
		endTime.setFont(labelFont);
		
		gbc.insets = new Insets(0, -30, 20, 10);
		insertHeadlinePane(client, 0, 0);
		gbc.insets = new Insets(0, -17, 20, 10);
		insertHeadlinePane(service, 0, 1);
		gbc.insets = new Insets(0, 0, 20, 10);
		insertHeadlinePane(startTime, 0, 2);
		gbc.insets = new Insets(0, -5, 90, 10);
		insertHeadlinePane(endTime, 0, 3);
		
		panel.add(headlinePane, BorderLayout.CENTER);
	}
	
	private void createDetailsPane() {
		this.detailsPane = new JPanel(new GridBagLayout());
		detailsPane.setPreferredSize(new Dimension(DIM.width / 4 + 20, DIM.height));
		detailsPane.setBackground(Color.CYAN);
		
		String[] serviceChoices = {"-", "lack", "gel", "hair", "other", "else"};
		JComboBox<String> servicesDropDown = new JComboBox<String>(serviceChoices);
		
		//starting time
		String[] startHours = new String[2];
		String excess = (startTime.getHour() <= 9) ? "0" : "";
		startHours[0] = excess + startTime.getHour() + ":00";
		startHours[1] = excess + startTime.getHour() + ":30";
		
		JComboBox<String> startDropDown = new JComboBox<String>(startHours);
		startDropDown.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				startTime.setMinutes(Integer.parseInt(((String) startDropDown.getSelectedItem()).substring(3, 5)));
				setTitle(MESSAGE + startTime);
				updateEndTimeMenu((String) startDropDown.getSelectedItem());
			}
		});
		
		updateEndTimeMenu((String) startDropDown.getSelectedItem());
		
		JFrame currentFrame = this;
		JButton createQueue = new JButton("Create Q");
		createQueue.setPreferredSize(new Dimension(90, 20));
		createQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selectedClient == null) return;
				datedPanel.addQueue(new Queue(selectedClient, null, startTime, endTime));
				datedPanel.press(false);
				currentFrame.dispose();
			}
		});
		
		gbc.insets = new Insets(-2, -0, 19, 10);
		insertDetailsPane(clientName, 0, 0);
		gbc.insets = new Insets(0, -60, 14, 10);
		insertDetailsPane(servicesDropDown, 0, 1);
		insertDetailsPane(startDropDown, 0, 2);
		gbc.insets = new Insets(0, -60, 14, 10);
		insertDetailsPane(endDropDown, 0, 3);
		gbc.insets = new Insets(40, 0, 10, 10);
		insertDetailsPane(createQueue, 0, 4);
		
		panel.add(detailsPane, BorderLayout.EAST);
	}
	
	private void updateEndTimeMenu(String startTime) {
		/*
		endDropDown.removeAllItems();
		boolean hasStartTime30 = startTime.charAt(3) == '3';
		String minutes, excess;
		int amount = (24 - startTime.getHour()) * 2 - 1;

		for (int i = 0, j = 1, currentHour; i < amount; i++, j++) {
			//user chose start time XX:30
			if (i == 0 && hasStartTime30) {
				currentHour = startTime.getHour() + 1; 
				minutes = "00";
				i++;
				j++;
			}
			else {
				currentHour = startTime.getHour() + (j / 2);
				minutes = (i % 2 == 0) ? "30" : "00";
			}
			
			excess = (currentHour <= 9) ? "0" : "";
			endDropDown.addItem(excess + currentHour + ":" + minutes);
		}
		*/
	}
	
	private void fixClientList(String keyword) {
		String query = "SELECT * "
					 + "FROM clients c "
					 + "WHERE c.user_key = '" + User.getKey() + "' "
			 		 + "AND (c.full_name LIKE '%" + keyword + "%' "
			 		 + "OR c.phone_number LIKE '%" + keyword + "%');";
		
		tableColumns.clear();
		for (int i = 0; i < CLIENT_COLUMNS[0].length; i++) {
			try { tableColumns.add(MysqlModifier.readAllVARCHAR(query, CLIENT_COLUMNS[0][i])); }
			catch (SQLException e) {}
		}
		
		DefaultTableModel model = new DefaultTableModel(tableColumns.get(0).size(), CLIENT_COLUMNS[1].length);
		clientTable.setModel(model);
		
		for (int i = 0; i < clientTable.getRowCount(); i++)
			for (int j = 0; j < tableColumns.size(); j++)
				clientTable.setValueAt(tableColumns.get(j).get(i), i, j);
		
		//set column names
		for (int i = 0; i < CLIENT_COLUMNS[0].length; i++)
			clientTable.getColumnModel().getColumn(i).setHeaderValue(CLIENT_COLUMNS[1][i]);
	}
	
	private void insertClientPane(Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		clientPane.add(c, gbc);
	}
	
	private void insertHeadlinePane(Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		headlinePane.add(c, gbc);
	}
	
	private void insertDetailsPane(Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		detailsPane.add(c, gbc);
	}
}