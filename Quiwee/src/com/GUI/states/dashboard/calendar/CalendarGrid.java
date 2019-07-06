package com.GUI.states.dashboard.calendar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import com.GUI.constants.ColorConstants;
import com.GUI.constants.FontConstants;
import com.GUI.windows.QueueDialog;
import com.controllers.QueuesController;
import com.data.objects.Queue;

public class CalendarGrid extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 3709458352878645312L;
	private static final Border BORDER = new EtchedBorder(EtchedBorder.LOWERED);
	
	private LocalDateTime date;
	private Color originColor;
	private int row, col;
	private boolean pressed, occupied, firstRow;
	private String occupyingName;
	private QueuesController controller;
	private Queue queue;
	
	public CalendarGrid(LocalDateTime date, int row, int col) {
		super(new BorderLayout());
		
		this.controller = new QueuesController();
		this.date = date;
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
		if (isOccupied()) dialog.inputQueueInfo(getQueue());
		
		press(true);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (!pressed) {
			TimePeriod.highlightHour(row);
			TimePeriod.highlightDay(col);
			colorize(ColorConstants.TEXT_COLOR_SELECTED);
		}
	}
	
	public void mouseExited(MouseEvent e) {
		if (!pressed) {
			TimePeriod.highlightHour(-1);
			TimePeriod.highlightDay(-1);
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
			LocalDateTime tempDate = date.plusMinutes(30);
			return CalendarTimeView.requestGrid(tempDate, row - 1, col).getMainQueueGrid();
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
		else removeQueue(queue.getDuration());
	}
	
	private void removeQueue(int duration) {
		if (!occupied || duration <= 0) return;
		
		super.setBorder(BORDER);
		setOpaque(true);
		press(false);
		occupied = false;
		firstRow = false;
		
		LocalDateTime queueDate = queue.getStartTime().plusMinutes(queue.getDuration() - (duration - 30));
		controller.delete(queue);
		queue = null;
		
		CalendarTimeView.requestGrid(queueDate, row + 1, col).removeQueue(duration - 30);
	}
	
	public void addQueue(Queue q, int currentDuration) {
		//after last grid - end condition for the recursion
		if (occupied || currentDuration <= 0) return;
		
		Insets insets = new Insets(0, 1, 0, 1);

		//decide which kind of border should the grid possess.
		//first row of queue - write down client name
		if (currentDuration == q.getDuration()) {
			String clientName = q.getClient().getName();
			occupyingName = clientName.substring(0, clientName.indexOf(" "));
			firstRow = true;
			insets.top = 1;
		}
		
		//any row during the queue
		if (currentDuration == 30) insets.bottom = 1;
		
		LocalDateTime queueStart = q.getStartTime().plusMinutes(q.getDuration() - (currentDuration - 30));
		setBorder(new MatteBorder(insets, Color.BLACK));
		setOpaque(false);
		occupied = true;
		queue = q;
		
		CalendarTimeView.requestGrid(queueStart, row + 1, col).addQueue(q, currentDuration - 30);
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
	public void setDate(LocalDateTime d) { date = d; }
	public LocalDateTime getDate() { return date; }
	public boolean isPressed() { return pressed; }
	public Color getOriginColor() { return (originColor != null) ? originColor : getBackground(); }
}