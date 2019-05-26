package com.states.dashboard.calendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class CalendarGrid extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 3709458352878645312L;
	private static final Color SELECT_COLOR = new Color(88, 178, 255);
	
	private Date date;
	private Color originColor;
	private int row, col;
	private boolean pressed;
	private boolean occupied;
	private Queue queue;
	
	public CalendarGrid(Date date, int row, int col) {
		super(new BorderLayout());
		
		this.date = new Date(date);
		this.row = row;
		this.col = col;
		addMouseListener(this);
	}
	
	public void setBackground(Color color) {
		colorize(color);
		originColor = color;
	}
	
	public void colorize(Color color) {
		super.setBackground(color);
		revalidate();
        repaint();
	}
	
	public void mouseClicked(MouseEvent arg0) {
		new QueueDialog(this, date);
		press(true);
	}
	
	public void mouseEntered(MouseEvent arg0) {
		if (!pressed) {
			View.Period.highlightHour(row);
			View.Period.highlightDay(col);
			colorize(SELECT_COLOR);
		}
	}
	
	public void mouseExited(MouseEvent arg0) {
		if (!pressed) {
			View.Period.highlightHour(-1);
			View.Period.highlightDay(-1);
			colorize(originColor);
		}
	}

	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
	public void press(boolean flag) {
		if (flag) colorize(new Color(233, 230, 112));
		else colorize(originColor);
		
		pressed = flag;
	}
	
	public void addQueue(Queue q) {
		if (q.getDuration() <= 0) return;
		
		setBackground(Color.GREEN);
		occupied = true;
		queue = q;
		
		Queue draggedQueue = new Queue(q);
		Date queueDate = new Date(draggedQueue.getStartTime());
		queueDate.incrementMinute(30);
		draggedQueue.setStartTime(queueDate);
		View.requestPanel(queueDate, row + 1, col).addQueue(draggedQueue);
	}
	
	public Queue getQueue() { return queue; }
	public boolean isOccupied() { return occupied; }
	public void setDate(Date d) { date = d; }
	public Date getDate() { return date; }
	public boolean isPressed() { return pressed; }
	public Color getOriginColor() { return (originColor != null) ? originColor : getBackground(); }
}