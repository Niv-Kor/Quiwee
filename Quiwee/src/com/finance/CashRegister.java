package com.finance;
import com.controllers.QueuesController;
import com.data.objects.Queue;
import com.data.objects.Service;

public class CashRegister
{
	private double price;
	private boolean hourly;
	private QueuesController queueCont;
	
	public CashRegister() {
		this.price = -1;
		this.hourly = false;
		this.queueCont = new QueuesController();
	}
	
	public boolean checkout(Queue q) {
		return checkout(q, q.getDuration());
	}
	
	public boolean checkout(Queue q, int duration) {
		if (price < 0) return false;
		
		double finalPayment = calcPayment(duration);
		queueCont.conclude(q, finalPayment);
		return true;
	}
	
	public void priceByService(Service s) {
		if (s != null) {
			setAsPricePerHour(s.isHourly());
			setPrice(s.getPrice());
		}
	}
	
	public void setPrice(double price) { this.price = price; }
	
	public void setAsPricePerHour(boolean flag) { hourly = flag; }
	
	private double calcPayment(int duration) {
		return hourly ? price * (duration / 60) : price;
	}
}