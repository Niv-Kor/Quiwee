package com.GUI.states.dashboard.calendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.concurrent.Callable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.GUI.constants.ColorConstants;
import com.GUI.constants.FontConstants;
import com.GUI.states.dashboard.Tab;
import com.GUI.states.dashboard.calendar.View.Period;
import javaNK.util.GUI.swing.components.InteractiveIcon;
import javaNK.util.GUI.swing.components.InteractiveLabel;
import javaNK.util.math.DimensionalHandler;

public class CalendarTab extends JPanel
{
	private static class CurrentDate extends JLabel
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
		
		public void now() {
			Date nearestSaturday = Date.now();
			while (nearestSaturday.getDay() != Days.SATURDAY) nearestSaturday.incrementDay();
			setDate(nearestSaturday, true);
		}
		
		public void setDate(Date d, boolean randomAccess) {
			date = new Date(d);
			//move the date to the nearest Saturday
			while (date.getDay() != Days.SATURDAY) date.incrementDay();
			
			date.displayHour(false);
			
			Date prefix = new Date(date);
			prefix.decrementDay(6);
			
			setText(prefix.toString() + " - " + date.toString());
			
			//count added weeks if date was set randomly at runtime
			if (randomAccess) {
				addedWeeks = 0;
				Date now = Date.now();
				Date pivot = new Date(d);
				int counter = 0;
				
				//make hours and minutes irrelevant
				pivot.setHour(now.getHour());
				pivot.setMinutes(now.getMinutes());
				
				while (pivot.isAfter(now)) {
					pivot.decrementDay();
					
					if (++counter == 7) {
						addedWeeks++;
						counter = 0;
					}
				}
				
				while (pivot.isBefore(now)) {
					pivot.incrementDay();
					
					if (++counter == 7) {
						addedWeeks--;
						counter = 0;
					}
				}
			}
		}
		
		public int getAddedWeeks() { return addedWeeks; }
	}
	
	private static final long serialVersionUID = 6286301053089922829L;
	public static final Dimension MENU_DIM = new Dimension(Tab.DIM.width, 80);
	
	private JPanel menu, viewSelector;
	private View currentView;
	private CurrentDate currentDate;
	private InteractiveLabel daily, weekly;
	
	public CalendarTab() {
		super(new BorderLayout());
		setPreferredSize(Tab.DIM);
		setBackground(Color.YELLOW);
		
		this.menu = new JPanel(new BorderLayout());
		menu.setPreferredSize(MENU_DIM);
		menu.setBackground(ColorConstants.COLOR_3);
		
		//view selector panel
		this.viewSelector = new JPanel(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		viewSelector.setPreferredSize(new Dimension(Tab.DIM.width, MENU_DIM.height / 2));
		viewSelector.setBackground(ColorConstants.COLOR_3);
		
		//west panel
		JPanel westPane = new JPanel(new BorderLayout());
		westPane.setPreferredSize(DimensionalHandler.adjust(MENU_DIM, 60, 100));
		westPane.setOpaque(false);
		
		Date saturday = Date.now();
		while (saturday.getDay() != Days.SATURDAY) saturday.incrementDay();
		
		this.currentDate = new CurrentDate(saturday);
		currentDate.setFont(FontConstants.SMALL_LABEL_FONT);
		currentDate.setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
		
		JPanel dateAssistPane = new JPanel(new BorderLayout());
		dateAssistPane.setPreferredSize(DimensionalHandler.adjust(westPane.getPreferredSize(), 75, 100));
		dateAssistPane.setOpaque(false);
		dateAssistPane.add(currentDate, BorderLayout.LINE_START);
		westPane.add(dateAssistPane, BorderLayout.EAST);
		
		JPanel westIconsAssistPane = new JPanel(new GridBagLayout());
		westIconsAssistPane.setPreferredSize(DimensionalHandler.adjust(westPane.getPreferredSize(), 25, 100));
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
				currentDate.now();
				currentView.apply(menu, currentDate.getAddedWeeks());
				return null;
			}
		});
		westIconsAssistPane.add(refresh, gbc);
		
		westPane.add(westIconsAssistPane, BorderLayout.WEST);
		viewSelector.add(westPane, BorderLayout.WEST);
		
		//center panel
		JPanel centerPane = new JPanel(new GridBagLayout());
		centerPane.setPreferredSize(DimensionalHandler.adjust(MENU_DIM, 40, 100));
		centerPane.setOpaque(false);
		
		gbc.insets.left = -30;
		gbc.gridx = 0;
		this.daily = new InteractiveLabel("Daily");
		daily.setFont(FontConstants.SMALL_LABEL_FONT);
		daily.setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
		daily.setFunction(new Callable<Void>() {
			public Void call() {
				setView(Period.DAILY);
				weekly.release();
				return null;
			}
		});
		centerPane.add(daily, gbc);
		
		gbc.insets.left = 10;
		gbc.gridx = 1;
		this.weekly = new InteractiveLabel("Weekly");
		weekly.setFont(FontConstants.SMALL_LABEL_FONT);
		weekly.setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
		weekly.setFunction(new Callable<Void>() {
			public Void call() {
				setView(Period.WEEKLY);
				daily.release();
				return null;
			}
		});
		centerPane.add(weekly, gbc);
		
		gbc.insets = new Insets(10, 10, 10, -150);
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