package com.GUI.windows;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.GUI.constants.ColorConstants;
import com.GUI.constants.FontConstants;
import com.GUI.states.dashboard.calendar.CalendarGrid;
import com.controllers.QueuesController;
import com.controllers.ServicesController;
import com.data.objects.Client;
import com.data.objects.Queue;
import com.data.objects.Service;
import com.main.User;
import com.queue_management.ClientsRetriever;
import javaNK.util.GUI.swing.components.FocusField;
import javaNK.util.GUI.swing.components.InteractiveLabel;
import javaNK.util.data.MysqlModifier;
import javaNK.util.math.DimensionalHandler;

public class QueueDialog extends JFrame
{
	private static final long serialVersionUID = 5348484787032161945L;
	private static final String MESSAGE = "New queue appointment for ";
	private static final Dimension DIM = new Dimension(620, 300);
	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy HH:mm");
	private static final String SEARCH_MESSAGE = "search name or phone number";
	
	private LocalDateTime startTime, endTime;
	private CalendarGrid calendarGrid;
	private JLabel clientName, timeLab;
	private JPanel southPane;
	private JTable clientTable;
	private Client selectedClient;
	private Service selectedService;
	private JComboBox<String> servicesDropDown, endDropDown;
	private GridBagConstraints gridConstraints;
	private QueuesController controller;
	private boolean existingQueue;
	
	public QueueDialog(CalendarGrid datedPanel, LocalDateTime date) {
		super(MESSAGE + date.format(FORMAT));
		setSize(DIM);
		setResizable(false);
		setLocationRelativeTo(null);
		
		this.controller = new QueuesController();
		this.gridConstraints = new GridBagConstraints();
		this.calendarGrid = datedPanel;
		this.startTime = date;
		this.endTime = date.plusMinutes(30);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setFocusable(true);
		mainPanel.requestFocus();
		mainPanel.setPreferredSize(DIM);
		mainPanel.setBackground(ColorConstants.BASE_COLOR);
		
		JPanel clientsPane = new JPanel(new BorderLayout());
		clientsPane.setPreferredSize(DimensionalHandler.adjust(DIM, 50, 100));
		clientsPane.setOpaque(false);
		createClientPane(clientsPane);
		mainPanel.add(clientsPane, BorderLayout.WEST);
		
		JPanel detailsPane = new JPanel(new BorderLayout());
		detailsPane.setPreferredSize(DimensionalHandler.adjust(DIM, 50, 100));
		detailsPane.setOpaque(false);
		createDetailsPane(detailsPane);
		mainPanel.add(detailsPane, BorderLayout.CENTER);
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) { calendarGrid.press(false); }

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowOpened(WindowEvent e) {}
		});
		
		add(mainPanel);
		pack();
		setVisible(true);
	}

	private void createClientPane(JPanel panel) {
		//dimensions and panel creation
		Dimension centerPaneDim = DimensionalHandler.adjust(panel, 100, 50);
		Dimension tableDim = DimensionalHandler.adjust(centerPaneDim, 90, 100);
		Dimension searchButtonDim = new Dimension(20, 20);
		Dimension searchFieldDim = new Dimension(tableDim.width - searchButtonDim.width, searchButtonDim.height);
		
		//north search pane
		JPanel northPane = new JPanel(new GridBagLayout());
		northPane.setPreferredSize(DimensionalHandler.adjust(panel, 100, 25));
		northPane.setOpaque(false);
		
		//table
		this.clientTable = new JTable(0, 0);
		clientTable.setPreferredSize(tableDim);
		clientTable.setRowHeight(15);
		clientTable.setBackground(Color.WHITE);
		clientTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
		        	try {
						String name = (String) table.getValueAt(row, 0);
						String phone = (String) table.getValueAt(row, 1);
						selectedClient = new Client(phone);
						clientName.setText(name);
						clientName.setForeground(ColorConstants.COLOR_2);
					}
					catch (SQLException ex) { ex.printStackTrace(); }
		        }
		    }
		});
		
		//search field and button
		FocusField searchField = new FocusField(SEARCH_MESSAGE);
		searchField.setPreferredSize(searchFieldDim);
		searchField.setHorizontalAlignment(JTextField.CENTER);
		addComponent(northPane, searchField, 0, 0);
		
		JButton searchButton = new JButton("...");
		searchButton.setPreferredSize(searchButtonDim);
		searchButton.setFont(new Font("Arial", Font.PLAIN, 7));
		searchButton.addActionListener(new ClientsRetriever(clientTable, searchField));
		addComponent(northPane, searchButton, 1, 0);
		panel.add(northPane, BorderLayout.NORTH);
		
		//center table pane
		JPanel centerPane = new JPanel(new GridBagLayout());
		centerPane.setPreferredSize(centerPaneDim);
		centerPane.setOpaque(false);
		
		//force selection of only 1 row
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		clientTable.setSelectionModel(selectionModel);
		clientTable.setDefaultEditor(Object.class, null);
		
		//include table within a scroll pane
		JScrollPane clientScrollPane = new JScrollPane(clientTable);
		clientScrollPane.setPreferredSize(tableDim);
		addComponent(centerPane, clientScrollPane, 0, 0);
		panel.add(centerPane, BorderLayout.CENTER);
		
		//south select pane
		JPanel southPane = new JPanel(new GridBagLayout());
		southPane.setPreferredSize(DimensionalHandler.adjust(panel, 100, 25));
		southPane.setOpaque(false);
		
		panel.add(southPane, BorderLayout.SOUTH);
	}
	
	private void createDetailsPane(JPanel panel) {
		//north details panel
		JPanel northPane = new JPanel(new BorderLayout());
		northPane.setPreferredSize(DimensionalHandler.adjust(panel, 100, 80));
		northPane.setOpaque(false);
		panel.add(northPane, BorderLayout.CENTER);
		
		//left mini pane
		JPanel northWestPane = new JPanel(new GridBagLayout());
		northWestPane.setPreferredSize(DimensionalHandler.adjust(northPane, 40, 100));
		northWestPane.setOpaque(false);
		northPane.add(northWestPane, BorderLayout.WEST);
		
		//right mini pane
		JPanel northEastPane = new JPanel(new GridBagLayout());
		northEastPane.setPreferredSize(DimensionalHandler.adjust(northPane, 60, 100));
		northEastPane.setOpaque(false);
		northPane.add(northEastPane, BorderLayout.EAST);
		
		//labels
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		
		JLabel clientLab = new JLabel("client:");
		clientLab.setFont(FontConstants.SMALL_LABEL_FONT);
		gridConstraints.insets.left = 42;
		addComponent(northWestPane, clientLab, 0, 0);
		
		JLabel serviceLab = new JLabel("service:");
		serviceLab.setFont(FontConstants.SMALL_LABEL_FONT);
		gridConstraints.insets.left = 31;
		addComponent(northWestPane, serviceLab, 0, 1);
		
		JLabel endTimeLab = new JLabel("end time:");
		endTimeLab.setFont(FontConstants.SMALL_LABEL_FONT);
		gridConstraints.insets.left = 19;
		addComponent(northWestPane, endTimeLab, 0, 2);
		
		JLabel queueTime = new JLabel("queue time:");
		queueTime.setFont(FontConstants.SMALL_LABEL_FONT);
		gridConstraints.insets.bottom = 40;
		gridConstraints.insets.left = 0;
		addComponent(northWestPane, queueTime, 0, 3);
		
		//details
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		Font detailsFont = FontConstants.SMALL_LABEL_FONT;
		Font dropDownFont = detailsFont.deriveFont((float) (4 * detailsFont.getSize() / 5));
		Dimension dropDownSize = DimensionalHandler.adjust(northEastPane, 80, 0);
		dropDownSize.height = 25;
		
		//name of the selected client
		this.clientName = new JLabel("-not selected-");
		clientName.setPreferredSize(dropDownSize);
		clientName.setForeground(ColorConstants.TEXT_COLOR_DARK);
		clientName.setFont(FontConstants.SMALL_LABEL_FONT);
		gridConstraints.insets.left = -25;
		gridConstraints.insets.top = 8;
		addComponent(northEastPane, clientName, 1, 0);
		
		//services drop down menu
		//retrieve all services from data base
		String servicesQuery = "SELECT name FROM services WHERE user_id = '" + User.getKey() + "'";
		List<String> servicesFromDB = new ArrayList<String>();
		try { servicesFromDB = MysqlModifier.readAllVARCHAR(servicesQuery, "name"); }
		catch (SQLException e) {}
		
		//create a new list with the options "-" and those that the data base has
		List<String> serviceChoices = new ArrayList<String>();
		serviceChoices.add("-");
		serviceChoices.addAll(servicesFromDB);
		
		//convert the list to a String[] object
		String[] serviceChoicesArr = serviceChoices.toArray(new String[serviceChoices.size()]);
		
		this.servicesDropDown = new JComboBox<String>(serviceChoicesArr);
		servicesDropDown.setPreferredSize(dropDownSize);
		servicesDropDown.setFocusable(false);
		servicesDropDown.setForeground(ColorConstants.TEXT_COLOR_DARK);
		servicesDropDown.setFont(dropDownFont);
		servicesDropDown.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				ServicesController serviceController = new ServicesController();
				selectedService = (Service) serviceController.getObj(servicesDropDown.getSelectedItem());
			}
		});
		
		gridConstraints.insets.left = -25;
		gridConstraints.insets.top = 10;
		addComponent(northEastPane, servicesDropDown, 1, 1);
		
		//ending time drop down menu
		this.endDropDown = new JComboBox<String>();
		endDropDown.setPreferredSize(dropDownSize);
		endDropDown.setFocusable(false);
		endDropDown.setForeground(ColorConstants.TEXT_COLOR_DARK);
		endDropDown.setFont(dropDownFont);
		endDropDown.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (endDropDown.getItemCount() > 0 && !timeLab.getText().equals("")) {
					String item = (String) endDropDown.getSelectedItem();
					String hour = item.substring(0, 2);
					String minutes = item.substring(3, 5);
					endTime = endTime.withHour(Integer.parseInt(hour));
					endTime = endTime.withMinute(Integer.parseInt(minutes));
					timeLab.setText(timeLab.getText().substring(0, 8) + hour + ":" + minutes);
				}
			}
		});
		gridConstraints.insets.left = -25;
		addComponent(northEastPane, endDropDown, 1, 2);
		
		//time of the queue from beginning to end
		this.timeLab = new JLabel(getTimeRange(startTime, endTime));
		timeLab.setForeground(ColorConstants.COLOR_2);
		timeLab.setFont(FontConstants.SMALL_LABEL_FONT);
		gridConstraints.insets.bottom = 38;
		addComponent(northEastPane, timeLab, 1, 3);
		
		//update the end time in drop down menu and time range
		updateEndTimeMenu();
		
		//south button panel
		this.southPane = new JPanel(new GridBagLayout());
		southPane.setPreferredSize(DimensionalHandler.adjust(panel, 100, 20));
		southPane.setOpaque(false);
		panel.add(southPane, BorderLayout.SOUTH);
		
		//queue creation button
		InteractiveLabel createQueueBtn = new InteractiveLabel("Save");
		createQueueBtn.setPreferredSize(new Dimension(90, 20));
		createQueueBtn.setFont(FontConstants.SMALL_LABEL_FONT);
		createQueueBtn.setForeground(ColorConstants.TEXT_COLOR_DARK);
		createQueueBtn.enableSelectionColor(false);
		createQueueBtn.setFunction(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				if (selectedClient == null) return null;
				
				//remove previous queue first
				if (existingQueue) calendarGrid.removeQueue();
				
				//add new one
				controller.insertNew(selectedClient, selectedService, startTime, endTime);
				
				dispose();
				return null;
			}
		});
		
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		addComponent(southPane, createQueueBtn, 1, 0);
	}
	
	private void updateEndTimeMenu() {
		endDropDown.removeAllItems();
		int hour = startTime.getHour();
		int minutes = startTime.getMinute();
		boolean hasStartTime30 = minutes == 30;
		String excess, minutesStr;
		int amount = (24 - hour) * 2 - 1;

		for (int i = 0, j = 1, currentHour; i < amount; i++, j++) {
			//user chose start time XX:30
			if (i == 0 && hasStartTime30) {
				currentHour = hour + 1; 
				minutesStr = "00";
				i++;
				j++;
			}
			else {
				currentHour = hour + (j / 2);
				minutesStr = (i % 2 == 0) ? "30" : "00";
			}
			
			excess = (currentHour <= 9) ? "0" : "";
			endDropDown.addItem(excess + currentHour + ":" + minutesStr);
		}
		
		endDropDown.addItem("00:00");
	}
	
	public void inputQueueInfo(Queue q) {
		existingQueue = true;
		selectedClient = q.getClient();
		clientName.setText(selectedClient.getName());
		startTime = q.getStartTime();
		updateEndTimeMenu();
		endDropDown.setSelectedIndex(q.getDuration() / 30 - 1);
		endTime = q.getStartTime();
		endTime = endTime.plusMinutes(q.getDuration());
		timeLab.setText(getTimeRange(startTime, endTime));
		
		//change title
		setTitle(getTitle().replaceAll("New", "Edit"));
		
		try { servicesDropDown.setSelectedItem(q.getService().getName()); }
		//item might be not longer available
		catch (NullPointerException e) { servicesDropDown.setSelectedItem("-"); }
		
		//add delete button
		InteractiveLabel deleteQueueBtn = new InteractiveLabel("Delete");
		deleteQueueBtn.setPreferredSize(new Dimension(90, 20));
		deleteQueueBtn.setFont(FontConstants.SMALL_LABEL_FONT);
		deleteQueueBtn.setForeground(ColorConstants.TEXT_COLOR_DARK);
		deleteQueueBtn.enableSelectionColor(false);
		deleteQueueBtn.setFunction(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				controller.delete(q);
				dispose();
				return null;
			}
		});
		
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		addComponent(southPane, deleteQueueBtn, 0, 0);
	}
	
	private String getTimeRange(LocalDateTime start, LocalDateTime end) {
		int hour, min;
		String hourPrefix, minPrefix;
		
		hour = start.getHour();
		min = start.getMinute();
		hourPrefix = (hour < 10) ? "0" : "";
		minPrefix = (min < 10) ? "0" : "";
		String startStr = hourPrefix + hour + ":" + minPrefix + min;
		
		hour = end.getHour();
		min = end.getMinute();
		hourPrefix = (hour < 10) ? "0" : "";
		minPrefix = (min < 10) ? "0" : "";
		String endStr = hourPrefix + hour + ":" + minPrefix + min;
		
		return startStr + " - " + endStr;
	}
	
	private void addComponent(JPanel panel, Component component, int x, int y) {
		gridConstraints.gridx = x;
		gridConstraints.gridx = x;
		panel.add(component, gridConstraints);
	}
}