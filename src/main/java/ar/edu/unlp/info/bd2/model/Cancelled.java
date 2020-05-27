package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

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
//		this.order=o;
	}

	public Cancelled(Order order2, Date date2) {
		this.date=date2;
//		this.order=order2;
	}

	public String getStatus() {
		return "Cancelled";
	}
}
