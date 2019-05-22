package com.states.dashboard.calendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.concurrent.Callable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.main.Window;
import com.states.dashboard.calendar.View.Period;
import com.utility.InteractiveIcon;
import com.utility.OptionLabel;
import com.utility.math.Percentage;

public class Calendar extends JPanel
{
	private static final long serialVersionUID = 6286301053089922829L;
	public final static Dimension DIM = new Dimension((int) (Window.DIM.width / 1.3), Window.DIM.height);
	public final static Dimension MENU_DIM = new Dimension(DIM.width, 80);
	private final static Font USELECTED_VIEW = new Font("Arial", Font.BOLD, 14);
	private final static Color VIEW_COLOR = new Color(230, 230, 230);
	
	private JPanel menu, viewSelector;
	private View currentView;
	private CurrentDate currentDate;
	private OptionLabel daily, weekly;
	
	public Calendar() {
		super(new BorderLayout());
		
		this.menu = new JPanel(new BorderLayout());
		menu.setPreferredSize(MENU_DIM);
		menu.setBackground(Window.COLOR.brighter());
		setPreferredSize(DIM);
		
		//view selector panel
		this.viewSelector = new JPanel(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		viewSelector.setPreferredSize(new Dimension(DIM.width, MENU_DIM.height / 2));
		viewSelector.setBackground(Window.COLOR.brighter().brighter());
		
		//west panel
		JPanel westPane = new JPanel(new BorderLayout());
		westPane.setPreferredSize(Percentage.createDimension(MENU_DIM, 50, 100));
		westPane.setOpaque(false);
		
		Date saturday = Date.now();
		while(saturday.getDay() != Days.SATURDAY) saturday.incrementDay();
		
		this.currentDate = new CurrentDate(saturday);
		currentDate.setForeground(VIEW_COLOR);
		
		JPanel dateAssistPane = new JPanel(new BorderLayout());
		dateAssistPane.setPreferredSize(Percentage.createDimension(westPane.getPreferredSize(), 65, 100));
		dateAssistPane.setOpaque(false);
		dateAssistPane.add(currentDate, BorderLayout.LINE_START);
		
		westPane.add(dateAssistPane, BorderLayout.EAST);
		
		JPanel westIconsAssistPane = new JPanel(new GridBagLayout());
		westIconsAssistPane.setPreferredSize(Percentage.createDimension(westPane.getPreferredSize(), 35, 100));
		westIconsAssistPane.setBackground(Color.ORANGE);
		westIconsAssistPane.setOpaque(false);
		
		gbc.insets = new Insets(10, -30, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		InteractiveIcon back = new InteractiveIcon("date_back.png");
		back.setSelectedIcon("date_back_ptr.png");
		back.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				currentDate.backward();
				currentView.apply(menu, currentDate.getAddedWeeks());
				return null;
			}
		});
		westIconsAssistPane.add(back, gbc);
		
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 1;
		InteractiveIcon refresh = new InteractiveIcon("refresh.png");
		refresh.setSelectedIcon("refresh_ptr.png");
		refresh.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				currentDate.setDate(Date.now(), true);
				currentView.apply(menu, currentDate.getAddedWeeks());
				return null;
			}
		});
		westIconsAssistPane.add(refresh, gbc);
		
		westPane.add(westIconsAssistPane, BorderLayout.WEST);
		viewSelector.add(westPane, BorderLayout.WEST);
		
		//center panel
		JPanel centerPane = new JPanel(new GridBagLayout());
		centerPane.setPreferredSize(Percentage.createDimension(MENU_DIM, 50, 100));
		centerPane.setOpaque(false);
		
		gbc.gridx = 0;
		this.daily = new OptionLabel("Daily");
		daily.setFont(USELECTED_VIEW);
		daily.setForeground(VIEW_COLOR);
		daily.setFunction(new Callable<Void>() {
			public Void call() {
				setView(Period.DAILY);
				weekly.release();
				return null;
			}
		});
		centerPane.add(daily, gbc);
		
		gbc.gridx = 1;
		this.weekly = new OptionLabel("Weekly");
		weekly.setFont(USELECTED_VIEW);
		weekly.setForeground(VIEW_COLOR);
		weekly.setFunction(new Callable<Void>() {
			public Void call() {
				setView(Period.WEEKLY);
				daily.release();
				return null;
			}
		});
		centerPane.add(weekly, gbc);
		
		gbc.insets = new Insets(10, 10, 10, -200);
		gbc.gridx = 2;
		InteractiveIcon forward = new InteractiveIcon("date_forward.png");
		forward.setSelectedIcon("date_forward_ptr.png");
		forward.setFunction(new Callable<Void>() {
			public Void call() throws Exception {
				currentDate.forward();
				currentView.apply(menu, currentDate.getAddedWeeks());
				return null;
			}
		});
		centerPane.add(forward, gbc);
		
		viewSelector.add(centerPane, BorderLayout.CENTER);
		menu.add(viewSelector, BorderLayout.NORTH);
		add(menu, BorderLayout.NORTH);
		setView(Period.WEEKLY);
	}
	
	public void setView(Period period) {
		if (currentView == period.getView()) return;
		if (currentView != null) remove(currentView);
		
		currentView = period.apply(menu, currentDate.getAddedWeeks());
		add(currentView, BorderLayout.CENTER);
		revalidate();
        repaint();
	}
}

class CurrentDate extends JLabel
{
	private static final long serialVersionUID = -6950255606994746339L;
	
	private Date date;
	private int addedWeeks;
	
	public CurrentDate(Date date) {
		this.date = new Date(date);
		this.addedWeeks = 0;
		setDate(date, false);
	}
	
	public void forward() {
		date.incrementDay(7);
		addedWeeks++;
		setDate(date, false);
	}
	
	public void backward() {
		date.decrementDay(7);
		addedWeeks--;
		setDate(date, false);
	}
	
	public void setDate(Date d, boolean randomAccess) {
		date = new Date(d);
		date.displayHour(false);
		
		Date prefix = new Date(date);
		prefix.decrementDay(6);
		
		setText(prefix.toString() + " - " + date.toString());
		
		//count added weeks if date was set randomly at runtime
		if (randomAccess) {
			addedWeeks = 0;
			Date now = Date.now();
			Date pivot = new Date(d);
			
			//make hours and minutes irrelevant
			pivot.setHour(now.getHour());
			pivot.setMinutes(now.getMinutes());
			
			if (pivot.isAfter(now)) {
				while (pivot.isAfter(now)) {
					pivot.decrementDay(7);
					addedWeeks++;
				}
			}
			else if (pivot.isBefore(now)) {
				while (pivot.isBefore(now)) {
					pivot.incrementDay(7);
					addedWeeks--;
				}
			}
		}
	}
	
	public int getAddedWeeks() { return addedWeeks; }
}