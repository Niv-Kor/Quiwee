package com.content.service;

import com.content.Saveable;

public class Service implements Saveable {
	private final static int DEFAULT_TIME = 120;
	private final static int DEFAULT_CAPACITY = 1;
	
	private String name, description;
	private double price;
	private boolean hourly;
	private int duration, capacity;
	
	public Service(String name, String desc, double price, boolean hourly, int minutes, int capacity) {
		this.name = new String(name);
		this.description = (desc != null) ? new String(desc) : "";
		this.price = price;
		this.hourly = hourly;
		this.duration = (minutes > 0) ? minutes : DEFAULT_TIME;
		this.capacity = (capacity > 0) ? capacity : DEFAULT_CAPACITY;
	}
	
	public void save() {
		// TODO Auto-generated method stub
		
	}
	
	public void setName(String n) { name = new String(n); }
	public void setDescription(String d) { description = new String(d); }
	public void setPrice(double p) { price = p; }
	public void setHourlyPrice(boolean flag) { hourly = flag; }
	public void setDuration(int d) { duration = d; }
	public void setCapacity(int c) { capacity = c; }
	public String getName() { return name; }
	public String getDesctiption() { return description; }
	public double getPrice() { return price; }
	public boolean isHourly() { return hourly; }
	public int getDuration() { return duration; }
	public int getCapacity() { return capacity; }
	public int getID() { return 0; } //TODO BOM
}