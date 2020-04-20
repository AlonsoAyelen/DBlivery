package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="delivered")
public class Delivered extends OrderStatus {

	public Delivered() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
	}

	public Delivered(Order o) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
		this.order= o;
	}
	
	public Delivered(Order order2, Date date) {
		this.date=date;
		this.order= order2;
	}

	public String getStatus() {
		return "Delivered";
	}
	
}
