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
		
	public String getStatus() {
		return "Sent";
	}
	
	public Boolean canFinish(Order o) {
		return true;
	}
	
	public void finish(Order o) {
		o.setStatus(new Delivered(o));
	}
	
//	public void finish(Order o,Date date) {
//		o.setStatus(new Delivered(o,date));
//	}
}
