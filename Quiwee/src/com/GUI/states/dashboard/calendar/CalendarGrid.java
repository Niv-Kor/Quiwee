package com.GUI.states.dashboard.calendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import com.GUI.constants.ColorConstants;
import com.GUI.constants.FontConstants;

public class CalendarGrid extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 3709458352878645312L;
	private static final Border BORDER = new EtchedBorder(EtchedBorder.LOWERED);
	
	private Date date;
	private Color originColor;
	private int row, col;
	private boolean pressed, occupied, firstRow;
	private String occupyingName;
	private Queue queue;
	
	public CalendarGrid(Date date, int row, int col) {
		super(new BorderLayout());
		
		this.date = new Date(date);
		this.row = row;
		this.col = col;
		setBorder(BORDER);
		addMouseListener(this);
	}
	
	@Override
	public void setBackground(Color color) {
		colorize(color);
		originColor = color;
	}
	
	public void colorize(Color color) {
		super.setBackground(color);
		revalidate();
        repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//open a queue dialog
		QueueDialog dialog = new QueueDialog(this, date);
		
		//insert info from the original first row queue
		if (isOccupied()) dialog.inputQueueInfo(getMainQueueGrid().getQueue());
		
		press(true);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (!pressed) {
			View.Period.highlightHour(row);
			View.Period.highlightDay(col);
			colorize(ColorConstants.TEXT_COLOR_SELECTED);
		}
	}
	
	public void mouseExited(MouseEvent e) {
		if (!pressed) {
			View.Period.highlightHour(-1);
			View.Period.highlightDay(-1);
			colorize(originColor);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	private CalendarGrid getMainQueueGrid() {
		if (firstRow || queue == null) return this;
		else {
			Date tempDate = new Date(date);
			tempDate.decrementMinute(30);
			return View.requestGrid(tempDate, row - 1, col).getMainQueueGrid();
		}
	}
	
	public void press(boolean flag) {
		pressed = flag;
		if (!flag) colorize(originColor);
	}
	
	public void removeQueue() {
		if (!occupied) return;
		else if (!firstRow) {
			getMainQueueGrid().removeQueue();
			return;
		}
		else removeQueue(queue);
	}
	
	private void removeQueue(Queue q) {
		if (!occupied || q.getDuration() <= 0) return;
		
		super.setBorder(BORDER);
		setOpaque(true);
		press(false);
		occupied = false;
		firstRow = false;
		
		//q.delete();
		Queue draggedQueue = new Queue(q);
		queue = null;
		Date queueDate = new Date(draggedQueue.getStartTime());
		queueDate.incrementMinute(30);
		draggedQueue.setStartTime(queueDate);
		View.requestGrid(queueDate, row + 1, col).removeQueue(draggedQueue);
	}
	
	public void addQueue(Queue q, int overallDuration) {
		int queueDuration = q.getDuration();
		Insets insets = new Insets(0, 1, 0, 1);

		//decide which kind of border should the grid possess.
		//first row of queue - write down client name
		if (queueDuration == overallDuration) {
			String clientName = q.getClient().getName();
			occupyingName = clientName.substring(0, clientName.indexOf(" "));
			firstRow = true;
			insets.top = 1;
		}
		//after last grid - end condition for the recursion
		else if (queueDuration <= 0) return;
		//any row during the queue
		if (queueDuration == 30) insets.bottom = 1;
		
		setBorder(new MatteBorder(insets, Color.BLACK));
		setOpaque(false);
		occupied = true;
		queue = q;
		
		Queue draggedQueue = new Queue(q);
		Date queueDate = new Date(draggedQueue.getStartTime());
		queueDate.incrementMinute(30);
		draggedQueue.setStartTime(queueDate);
		View.requestGrid(queueDate, row + 1, col).addQueue(draggedQueue, overallDuration);
	}
	
	@Override
	public void setBorder(Border border) {
		if (!isOccupied()) super.setBorder(border);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (firstRow) {
			Font font = FontConstants.SMALL_LABEL_FONT;
			g.setFont(font);
			g.setColor(ColorConstants.TEXT_COLOR_BRIGHT);
			g.drawString(occupyingName, 5, font.getSize());
		}
	}
	
	public Queue getQueue() { return queue; }
	public boolean isOccupied() { return occupied; }
	public void setDate(Date d) { date = d; }
	public Date getDate() { return date; }
	public boolean isPressed() { return pressed; }
	public Color getOriginColor() { return (originColor != null) ? originColor : getBackground(); }
}