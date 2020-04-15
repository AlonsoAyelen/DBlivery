package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Sent extends OrderStatus {

	public Sent() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
	}
		
	public String getStatus() {
		return "Sent";
	}
	
	public Boolean canFinish(Order o) {
		return true;
	}
	
	public void finish(Order o) {
		o.setStatus(new Delivered());
	}
}
