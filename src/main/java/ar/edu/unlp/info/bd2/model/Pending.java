package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

public class Pending extends OrderStatus {

	public Pending() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.setObjectId(new ObjectId());
		this.date=todayDate;
	}

	public Pending(Order o) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.setObjectId(new ObjectId());
		this.date=todayDate;
//		this.order=o;
	}
	
	public Pending(Order order, Date date) {
		this.date=date;
		this.setObjectId(new ObjectId());
//		this.order=order;
	}
	
	public void cancel(Order o) {
		OrderStatus c = new Cancelled(o);
		o.addStatus(c);
		o.setStatusActual(c);
	}
	
//	public void cancel(Order o,Date date) {
//		o.setStatus(new Cancelled(o,date));
//	}

//	public void send(Order o,Date date) {
//		o.setStatus(new Sent(o,date));
//	}
	
	public void send(Order o) {
		OrderStatus s = new Sent(o);
		o.addStatus(s);
		o.setStatusActual(s);
		
	}
	
	public String getStatus() {
		return "Pending";
	}
	public Boolean canDeliver(Order o) {
		return (!o.getProducts().isEmpty());
	}
	public Boolean canCancel(Order o) {
		return true;
	}
	
	public void send(Order order2, Date date2) {
		OrderStatus s = new Sent(order2,date2);
		order2.addStatus(s);
		order2.setStatusActual(s);
	}
	
	public void cancel(Order order2, Date date2) {
		OrderStatus c = new Cancelled(order2,date2);
		order2.addStatus(c);
		order2.setStatusActual(c);
		
	}
}
