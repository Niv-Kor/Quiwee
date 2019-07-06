package com.GUI.states.dashboard.calendar;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public enum TimePeriod {
	DAILY(24, 1),
	WEEKLY(24, 7);
	
	private CalendarTimeView timeView;
	
	private TimePeriod(int rows, int cols) {
		this.timeView = new CalendarTimeView(rows, cols);
	}
	
	public CalendarTimeView apply(JPanel menu, int addedWeeks) {
		//remove the previous index
		menu.remove(timeView.getDayIndex());
		menu.remove(getOther().timeView.getDayIndex());
		
		//apply the new index
		timeView.apply(menu, addedWeeks);
		
		return timeView;
	}
	
	public CalendarTimeView getTimeView() { return timeView; }
	
	private TimePeriod getOther() {
		switch(valueOf(name())) {
			case DAILY: return WEEKLY;
			case WEEKLY: return DAILY;
		}
		
		return null; //formal return statement
	}
	
	public static List<CalendarTimeView> connectDatedPanel(CalendarGrid dp) {
		List<CalendarTimeView> views = new ArrayList<CalendarTimeView>();
		
		for (TimePeriod p : values()) views.add(p.timeView);
		return views;
	}
	
	public static void highlightHour(int index) { for (TimePeriod p : values()) p.timeView.highlightHour(index); }
	public static void highlightDay(int index) { for (TimePeriod p : values()) p.timeView.highlightDay(index); }
}