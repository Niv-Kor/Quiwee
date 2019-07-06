package com.controllers;
import java.sql.SQLException;
import java.time.LocalDateTime;
import com.GUI.states.dashboard.calendar.CalendarGrid;
import com.GUI.states.dashboard.calendar.CalendarTimeView;
import com.data.objects.Client;
import com.data.objects.Queue;
import com.data.objects.Service;

/**
 * This class controls and manages queues in the database.
 * 
 * All arguments:
 * 		1. Client - The queue's client
 *		2. Service - The service of the queue (can be null)
 *		3. LocalDateTime - Queue start time
 *		4. LocalDateTime - Queue end time
 * 
 * Key arguments:
 * 		1. Client - The queue's client
 *		2. LocalDateTime - Queue start time
 * 
 * @author Niv Kor
 */
public class QueuesController extends Controller<Queue>
{
	@Override
	public Queue getObj(Object... obj) {
		try {
			Client client = (Client) obj[0];
			LocalDateTime start = (LocalDateTime) obj[1];
			return new Queue(client, start);
		}
		catch (SQLException | ClassCastException | ArrayIndexOutOfBoundsException ex) { return null; }
	}
	
	@Override
	public Queue createObj(Object... obj) {
		try {
			Client client = (Client) obj[0];
			Service service = (Service) obj[1];
			LocalDateTime start = (LocalDateTime) obj[2];
			LocalDateTime end = (LocalDateTime) obj[3];
			
			Queue queue = new Queue(client, service, start, end);
			CalendarGrid grid = getGrid(start);
			grid.addQueue(queue, queue.getDuration());
			
			return queue;
		}
		catch (ClassCastException | ArrayIndexOutOfBoundsException ex) { return null; }
	}
	
	/**
	 * Import a queue from the database to the calendar.
	 * 
	 * @param client - The queue's client
	 * @param start - Queue's start time
	 * @return true if the queue was imported successfully.
	 */
	public boolean importQueue(Client client, LocalDateTime start) {
		try {
			CalendarGrid grid = getGrid(start);
			Queue queue = getObj(client, start);
			grid.addQueue(queue, queue.getDuration());
			return true;
		}
		catch (NullPointerException ex) { return false; }
	}
	
	@Override
	public boolean delete(Queue queue) {
		boolean deleted = super.delete(queue);
		
		if (deleted) removeFromCalendar(queue.getStartTime());
		return deleted;
	}
	
	@Override
	public boolean delete(Object... obj) {
		boolean deleted = super.delete(obj);
		
		if (deleted) removeFromCalendar((LocalDateTime) obj[1]);
		return deleted;
	}
	
	/**
	 * Remove a queue from the calendar.
	 * 
	 * @param startTime - Queue's start time
	 */
	private void removeFromCalendar(LocalDateTime startTime) {
		CalendarGrid grid = getGrid(startTime);
		grid.removeQueue();
	}
	
	/**
	 * @param startTime - Queue's start time
	 * @return the calendar's grid of the queue.
	 */
	private CalendarGrid getGrid(LocalDateTime startTime) {
		int halfHour = (startTime.getMinute() == 30) ? 1 : 0;
		int row = startTime.getHour() * 2 + halfHour;
		int column = startTime.getDayOfWeek().getValue();
		if (column == 7) column = 0; //sunday
		
		return CalendarTimeView.requestGrid(startTime, row, column);
	}
}