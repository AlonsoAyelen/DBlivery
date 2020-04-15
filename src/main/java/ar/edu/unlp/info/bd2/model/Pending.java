package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Pending extends OrderStatus {

	public Pending() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
	}
	
	public void cancel(Order o) {
		o.setStatus(new Cancelled());
	}
	
	public void send(Order o) {
		o.setStatus(new Sent());
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
}
