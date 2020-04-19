package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cancelled")
public class Cancelled extends OrderStatus {

	public Cancelled() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
	}

	public Cancelled(Order o) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
		this.order=o;
	}

	public String getStatus() {
		return "Cancelled";
	}
}
