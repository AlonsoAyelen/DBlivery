package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

public class Sent extends OrderStatus {

	public Sent() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.setObjectId(new ObjectId());
		this.date=todayDate;
	}

	public Sent(Order o) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.setObjectId(new ObjectId());
		this.date=todayDate;
//		this.order=o;
	}
		
	public Sent(Order order2, Date date2) {
		this.date=date2;
//		this.order=order2;
		this.setObjectId(new ObjectId());
	}

	public String getStatus() {
		return "Sent";
	}
	
	public Boolean canFinish(Order o) {
		return true;
	}
	
	public void finish(Order o) {
		OrderStatus d = new Delivered(o);
		o.addStatus(d);
		o.setStatusActual(d);
	}

	public void finish(Order order2, Date date2) {
		OrderStatus d = new Delivered(order2,date2);
		order2.addStatus(d);
		order2.setStatusActual(d);
		
	}
}
