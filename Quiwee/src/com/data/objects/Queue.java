package com.data.objects;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import com.data.MysqlLoader;
import com.data.tables.QueuesTable;
import com.main.User;
import javaNK.util.data.MysqlRow;

public class Queue extends MysqlRow
{
	private int duration;
	
	public Queue(Client client, Service service, LocalDateTime start, LocalDateTime end) {
		super("queues");
		
		setField(QueuesTable.CLIENT_PHONE.getColumn(), client.getPhoneNumber());
		
		String serviceName = null;
		double price = 0;
		if (service != null) {
			serviceName = service.getName();
			price = service.getPrice();
		}
		
		setField(QueuesTable.SERVICE_NAME.getColumn(), serviceName);
		setField(QueuesTable.START_TIME.getColumn(), Timestamp.valueOf(start));
		setEndTime(end);
		setPrice(price);
		
		calculateDuration();
	}
	
	public Queue(Client client, LocalDateTime start) throws SQLException {
		super("queues");
		setField(QueuesTable.CLIENT_PHONE.getColumn(), client.getPhoneNumber());
		setField(QueuesTable.START_TIME.getColumn(), Timestamp.valueOf(start));
		
		if (isInDatabase()) {
			Object[][] rows = MysqlLoader.getRows(QueuesTable.class, selectAllQuery());
			
			setServiceName((String) rows[0][2]);
			setPrice((double) rows[0][3]);
			setEndTime(((Timestamp) rows[0][5]).toLocalDateTime());
		}
		else throw new SQLException();
	}
	
	public void close(double price, boolean changed) {
		setPrice(price);
		//TODO
	}
	
	private void calculateDuration() {
		Timestamp startStamp = Timestamp.valueOf(getStartTime());
		Timestamp endStamp = Timestamp.valueOf(getEndTime());
		duration = (int) TimeUnit.MILLISECONDS.toMinutes(endStamp.getTime() - startStamp.getTime());
	}
	
	@Override
	protected void addFields() {
		addKeyField(QueuesTable.USER_ID.getColumn(), User.getKey());
		addKeyField(QueuesTable.CLIENT_PHONE.getColumn(), null);
		addKeyField(QueuesTable.START_TIME.getColumn(), null);
		addLiquidField(QueuesTable.SERVICE_NAME.getColumn(), null);
		addLiquidField(QueuesTable.PRICE.getColumn(), null);
		addLiquidField(QueuesTable.END_TIME.getColumn(), null);
	}
	
	public void setServiceName(String s) { setField(QueuesTable.SERVICE_NAME.getColumn(), s); }

	public void setEndTime(LocalDateTime t) {
		setField(QueuesTable.END_TIME.getColumn(), Timestamp.valueOf(t));
		calculateDuration();
	}
	
	public void setPrice(double p) { setField(QueuesTable.PRICE.getColumn(), p); }
	
	public Client getClient() {
		try { return new Client(getClientPhoneNumber()); }
		catch (SQLException e) { return null; }
	}
	
	public Service getService() {
		try { return new Service(getServiceName()); }
		catch (SQLException e) { return null; }
	}
	
	public String getClientPhoneNumber() {
		return (String) getField(QueuesTable.CLIENT_PHONE.getColumn());
	}
	
	public String getServiceName() {
		return (String) getField(QueuesTable.SERVICE_NAME.getColumn());
}
	
	public LocalDateTime getStartTime() {
		return ((Timestamp) getField(QueuesTable.START_TIME.getColumn())).toLocalDateTime();
	}
	
	public LocalDateTime getEndTime() {
		return ((Timestamp) getField(QueuesTable.END_TIME.getColumn())).toLocalDateTime();
	}
	
	public double getPrice() { return (double) getField(QueuesTable.PRICE.getColumn()); }
	
	public int getDuration() { return duration; }
}