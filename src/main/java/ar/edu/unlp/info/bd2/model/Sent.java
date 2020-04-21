package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sent")
public class Sent extends OrderStatus {

	public Sent() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
	}

	public Sent(Order o) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
		this.order=o;
	}
		
	public Sent(Order order2, Date date2) {
		this.date=date2;
		this.order=order2;
	}

	public String getStatus() {
		return "Sent";
	}
	
	public Boolean canFinish(Order o) {
		return true;
	}
	
	public void finish(Order o) {
		o.setStatus(new Delivered(o));
	}

	public void finish(Order order2, Date date2) {
		order2.setStatus(new Delivered(order2,date2));
		
	}
}
