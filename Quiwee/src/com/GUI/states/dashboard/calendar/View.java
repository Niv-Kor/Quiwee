package com.GUI.states.dashboard.calendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import com.GUI.constants.ColorConstants;
import com.GUI.states.dashboard.Tab;

public class View extends JPanel
{
	public static enum Period {
		DAILY(24, 1),
		WEEKLY(24, 7);
		
		private View view;
		
		private Period(int rows, int cols) {
			this.view = new View(rows, cols);
		}
		
		public View apply(JPanel menu, int addedWeeks) {
			//remove the previous index
			menu.remove(view.getDayIndex());
			menu.remove(getOther().view.getDayIndex());
			//apply the new index
			view.apply(menu, addedWeeks);
			
			return view;
		}
		
		private Period getOther() {
			switch(name()) {
				case "DAILY": return WEEKLY;
				case "WEEKLY": return DAILY;
			}
			
			return null; //formal return statement
		}
		
		public static List<View> connectDatedPanel(CalendarGrid dp) {
			List<View> views = new ArrayList<View>();
			
			for (Period p : values()) views.add(p.view);
			return views;
		}
		
		public static void highlightHour(int index) { for (Period p : values()) p.view.highlightHour(index); }
		public static void highlightDay(int index) { for (Period p : values()) p.view.highlightDay(index); }
		
		public View getView() { return view; }
	}
	
	private static final long serialVersionUID = 4429666829928454250L;
	private final static Border BORDER = new EtchedBorder(EtchedBorder.LOWERED);
	private final static int INDEX_THICKNESS = 40;
	private final static Font DESELECTED_DAY = new Font("Aller Display", Font.PLAIN, 15);
	private final static Font SELECTED_DAY = new Font("Aller Display", Font.BOLD, 15);
	private final static Color QUEUE_COLOR = ColorConstants.COLOR_2;
	
	private static HashMap<Integer, CalendarGrid> grids = new HashMap<Integer, CalendarGrid>();
	private GridBagConstraints gbc;
	private JPanel calendar, dayIndex, hourIndex, tablePane;
	private JLabel[] dayLabels;
	private HourPanel[] hourLabels;
	private CalendarGrid[][] table;
	private Dimension cellDim;
	private int todayIndex, cols;
	
	public View(int rows, int cols) {
		super(new BorderLayout());
		setBackground(Color.DARK_GRAY);
		
		this.cols = cols;
		this.gbc = new GridBagConstraints();
		this.calendar = new JPanel(new BorderLayout());
		this.calendar.setPreferredSize(new Dimension(Tab.DIM.width, Tab.DIM.height * 2 + 180));
		calendar.setBackground(QUEUE_COLOR);
		
		JScrollPane calendarScroll = new JScrollPane(calendar);
		calendarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		calendarScroll.getVerticalScrollBar().setUnitIncrement(16);
		
		//declare dimensions
		int scrollBarWidth = calendarScroll.getVerticalScrollBar().getPreferredSize().width;
		int tableLength = Tab.DIM.height * 2;
		Dimension tableDim = new Dimension(Tab.DIM.width - INDEX_THICKNESS - scrollBarWidth - 20, tableLength);
		Dimension indexDim = new Dimension(INDEX_THICKNESS, tableLength);
		this.cellDim = new Dimension(tableDim.width / cols, tableDim.height / (rows * 2));
		
		calendarScroll.setPreferredSize(calendar.getPreferredSize());
		
		JPanel supportEastPane = new JPanel();
		supportEastPane.setPreferredSize(new Dimension(scrollBarWidth + 20, tableLength));
		calendar.add(supportEastPane, BorderLayout.EAST);
		
		this.tablePane = new JPanel(new GridBagLayout());
		tablePane.setPreferredSize(tableDim);
		tablePane.setOpaque(false);
		calendar.add(tablePane, BorderLayout.CENTER);
		
		//days index
		todayIndex = LocalDateTime.now().getDayOfWeek().getValue();
		if (todayIndex == 7) todayIndex = 0; //sunday
		Days[] week;
		
		if (cols == 1) week = Days.getWeek(todayIndex + 1); //daily view
		else week = Days.getWeek(Days.SUNDAY); //weekly view
		
		dayIndex = new JPanel(new GridBagLayout());
		dayIndex.setPreferredSize(new Dimension(Tab.DIM.width, INDEX_THICKNESS));
		dayIndex.setBackground(ColorConstants.COLOR_3);

		String tempStr;
		this.dayLabels = new JLabel[cols];
		Insets ins = new Insets(5, 50, 5, 46);
		for (int i = 0, x = 0; i < cols; i++, x++) {
			//if first, push a little more to the right
			if (i == 0) gbc.insets = new Insets(5, 75, 5, 45);
			else gbc.insets = ins;
			
			//decide if showing full name or only acronym
			if (cols == 1) tempStr = "" + week[i].name();
			else tempStr = "" + week[i].name().charAt(0);
			dayLabels[i] = new JLabel(tempStr);
			
			//colorize today
			if (i == todayIndex) {
				dayLabels[i].setForeground(Color.CYAN);
				dayLabels[i].setFont(SELECTED_DAY);
			}
			else {
				dayLabels[i].setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
				dayLabels[i].setFont(DESELECTED_DAY);
			}
			
			//placement
			gbc.gridx = x;
			dayIndex.add(dayLabels[i], gbc);
		}
		
		//hours index
		hourIndex = new JPanel(new GridBagLayout());
		hourIndex.setPreferredSize(indexDim);
		hourIndex.setOpaque(false);
		
		Dimension hourDim = new Dimension(hourIndex.getPreferredSize().width, cellDim.height);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.ipadx = hourDim.width;
		gbc.ipady = hourDim.height - 10;
		gbc.gridx = 0;
		int minutes;
		
		this.hourLabels = new HourPanel[rows * 2];
		for (int i = 0, hour, j = 0, y = 0; i < hourLabels.length; i++, j++, y++) {
			hour = j / 2;
			
			if (i == 0) minutes = 0;
			else minutes = (i % 2 == 1) ? 30 : 0;
			
			hourLabels[i] = new HourPanel(hour, minutes);
			hourLabels[i].setBackground(ColorConstants.COLOR_2.darker());
			hourLabels[i].setPreferredSize(hourDim);
			hourLabels[i].setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
			hourLabels[i].setBorder(BORDER);
			
			gbc.gridy = y;
			hourIndex.add(hourLabels[i], gbc);
		}
		calendar.add(hourIndex, BorderLayout.WEST);
		
		//calendar table
		gbc.ipadx = cellDim.width;
		gbc.ipady = cellDim.height;

		this.table = new CalendarGrid[rows * 2][cols];
		for (int i = 0, y = 0; i < table.length; i++, y++) {
			for (int j = 0, x = 0; j < table[i].length; j++, x++) {
				gbc.gridx = x;
				gbc.gridy = y;
				table[i][j] = requestGrid(fixDate(i, j, todayIndex, 0), i, j);
				table[i][j].setPreferredSize(cellDim);
				
				//if today, brighten the color
				Color color = new Color(221, 221, 221);
				if (j == todayIndex && getCols() > 1) color = color.brighter();
				
				table[i][j].setBackground(color);
				tablePane.add(table[i][j], gbc);
			}
		}
		add(calendarScroll, BorderLayout.CENTER);
	}
	
	public void highlightHour(int hour) {
		for (int i = 0; i < hourLabels.length; i++) {
			if (i == hour) hourLabels[i].setForeground(ColorConstants.TEXT_COLOR_SELECTED);
			else hourLabels[i].setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
		}
	}
	
	public void highlightDay(int day) {
		for (int i = 0; i < dayLabels.length; i++) {
			if (i == day) dayLabels[i].setForeground(ColorConstants.TEXT_COLOR_SELECTED);
			else if (i != todayIndex) dayLabels[i].setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
			else dayLabels[i].setForeground(Color.CYAN);
		}
	}
	
	public void apply(JPanel menu, int addedWeeks) {
		menu.add(dayIndex, BorderLayout.SOUTH);
		
		tablePane.removeAll();
		for (int i = 0, y = 0; i < table.length; i++, y++) {
			for (int j = 0, x = 0; j < table[i].length; j++, x++) {
				gbc.gridx = x;
				gbc.gridy = y;
				table[i][j] = requestGrid(fixDate(i, j, todayIndex, addedWeeks), i, j);
				table[i][j].setPreferredSize(cellDim);
				
				if (!table[i][j].isOccupied()) {
					//if today, brighten the color
					Color color = new Color(221, 221, 221);
					
					if (j == todayIndex && addedWeeks == 0 && getCols() > 1)
						color = color.brighter();
					
					table[i][j].setBackground(color);
				}
				
				table[i][j].setBorder(BORDER);
				tablePane.add(table[i][j], gbc);
			}
		}
		
		revalidate();
		repaint();
	}
	
	private Date fixDate(int row, int col, int todayIndex, int addedWeeks) {
		if (cols == 1) todayIndex = 0;
		
		LocalDateTime today = LocalDateTime.now().plusDays(col - todayIndex + addedWeeks * 7);
		int dayOfMonth = today.getDayOfMonth();
		int month = today.getMonthValue();
		int year = today.getYear();
		int hour = row / 2;
		int minutes = (row % 2 == 0) ? 0 : 30;
		
		return new Date(today.getDayOfWeek(), dayOfMonth, month, year, hour, minutes);
	}
	
	public static CalendarGrid requestGrid(Date d, int row, int col) {
		CalendarGrid cg = grids.get(d.hashCode());
		
		if (cg == null) {
			cg = new CalendarGrid(d, row, col);
			grids.put(d.hashCode(), cg);
		}
		
		return cg;
	}
	
	public int getRows() { return table.length; }
	public int getCols() { return table[0].length; }
	public JPanel getDayIndex() { return dayIndex; }
	public CalendarGrid getDatedPanel(int i, int j) { return table[i][j]; }
}