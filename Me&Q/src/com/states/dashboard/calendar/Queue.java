package com.states.dashboard.calendar;
import java.awt.Color;
import javax.swing.JPanel;
import com.content.Saveable;
import com.content.client.Client;
import com.content.service.Service;
import com.database.SQLModifier;
import com.main.User;

public class Queue extends JPanel implements Saveable {
	private static final long serialVersionUID = 114077834645060699L;
	private static final Color COLOR = new Color(112, 230, 142);
	
	private Client client;
	private Service service;
	private double price;
	private int duration;
	private Date startTime, endTime;
	private boolean saved;
	
	public Queue(Client c, Service s, Date start, Date end) {
		setBackground(COLOR);
		
		this.client = c;
		this.service = s;
		this.price = (s != null) ? s.getPrice() : 0;
		this.startTime = new Date(start);
		this.endTime = new Date(end);
		
		startTime.displayHour(true);
		endTime.displayHour(true);
		System.out.println(startTime + " until " + endTime);
		
		this.duration = calculateDuration(start, end);
		
		//save();
	}
	
	public Queue(Queue other) {
		this.client = other.client;
		this.service = other.service;
		this.price = other.price;
		this.startTime = new Date(other.startTime);
		this.endTime = new Date(other.endTime);
		this.duration = other.duration;
	}
	
	public void save() {
		String query;
		int serviceID = (service != null) ? service.getID() : 0;

		if (!saved) {
			query = "INSERT INTO queues(user_key, client_id, service_id, time) "
			      + "VALUES ( "
			      + User.getKey() + ", " + client.getID() + ", "
			      + serviceID + ", " + startTime + ");";
		}
		else {
			query = "UPDATE queues "
			      + "SET client_id = " + client.getID() + ", "
			      + "service_id = " + serviceID + ", "
			      + "time = " + startTime + ";";
		}
		
		SQLModifier.write(query);
		saved = true;
	}
	
	public void close(double price, boolean changed) {
		if (changed) this.price = price;
		//TODO
	}
	
	public void setClient(Client c) {
		boolean changed = client != c;
		client = c;
		//if (changed) save();
	}

	public void setService(Service s) {
		boolean changed = service != s;
		service = s;
		//if (changed) save();
	}

	public void setStartTime(Date d) {
		//increment endTime so it won't be bypassed
		if (d.isAfter(endTime)) endTime = new Date(d);
		
		boolean changed = !startTime.equals(d);
		startTime = d;
		duration = calculateDuration(startTime, endTime);
		//if (changed) save();
	}
	
	public void setEndTime(Date d) {
		boolean changed = !endTime.equals(d);
		endTime = d;
		duration = calculateDuration(startTime, endTime);
		//if (changed) save();
	}
	
	public void setPrice(double p) {
		boolean changed = price != p;
		price = p;
		//if (changed) save();
	}
	
	private int calculateDuration(Date start, Date end) {
		int startMinutes = start.getMinutes() + start.getHour() * 60;
		int endMinutes = end.getMinutes() + end.getHour() * 60;
		return endMinutes - startMinutes;
	}

	public Client getClient() { return client; }
	public Service getService() { return service; }
	public Date getStartTime() { return startTime; }
	public Date getEndTime() { return endTime; }
	public double getPrice() { return price; }
	public int getDuration() { return duration; }
}