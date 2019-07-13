package com.data.objects;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import com.data.MysqlLoader;
import com.data.tables.QueuesTable;
import com.main.User;
import javaNK.util.data.MysqlRow;
import javaNK.util.real_time.TimeStampConverter;

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
		
		setField(QueuesTable.START_TIME.getColumn(), TimeStampConverter.toString(start));
		setServiceName(serviceName);
		setConcluded(false);
		setEndTime(end);
		setPrice(price);
		
		calculateDuration();
	}
	
	public Queue(Client client, LocalDateTime start) throws SQLException {
		super("queues");
		
		setField(QueuesTable.CLIENT_PHONE.getColumn(), client.getPhoneNumber());
		setField(QueuesTable.START_TIME.getColumn(), TimeStampConverter.toString(start));
		
		if (isInDatabase()) {
			Object[][] rows = MysqlLoader.getRows(QueuesTable.class, selectAllQuery());
			
			setServiceName((String) rows[0][2]);
			setPrice((double) rows[0][3]);
			setEndTime(TimeStampConverter.toLocalDateTime((String) rows[0][5]));
			setConcluded((boolean) rows[0][6]);
		}
		else throw new SQLException();
	}
	
	public boolean conclude(double price) {
		setPrice(price);
		setConcluded(true);
		boolean save = save();
		return save;
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
		addLiquidField(QueuesTable.IS_CONCLUDED.getColumn(), null);
	}
	
	public void setServiceName(String s) { setField(QueuesTable.SERVICE_NAME.getColumn(), s); }

	public void setEndTime(LocalDateTime t) {
		setField(QueuesTable.END_TIME.getColumn(), TimeStampConverter.toString(t));
		calculateDuration();
	}
	
	public void setPrice(double p) { setField(QueuesTable.PRICE.getColumn(), p); }
	
	public void setConcluded(boolean flag) { setField(QueuesTable.IS_CONCLUDED.getColumn(), flag); }
	
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
		return TimeStampConverter.toLocalDateTime((String) getField(QueuesTable.START_TIME.getColumn()));
	}
	
	public LocalDateTime getEndTime() {
		return TimeStampConverter.toLocalDateTime((String) getField(QueuesTable.END_TIME.getColumn()));
	}
	
	public boolean isConcluded() {
		return (boolean) getField(QueuesTable.IS_CONCLUDED.getColumn());
	}
	
	public double getPrice() { return (double) getField(QueuesTable.PRICE.getColumn()); }
	
	public int getDuration() { return duration; }
}