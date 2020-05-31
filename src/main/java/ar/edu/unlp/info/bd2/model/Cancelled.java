package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

public class Cancelled extends OrderStatus {

	public Cancelled() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.setObjectId(new ObjectId());
		this.date=todayDate;
	}

	public Cancelled(Order o) {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
		this.setObjectId(new ObjectId());
//		this.order=o;
	}

	public Cancelled(Order order2, Date date2) {
		this.date=date2;
		this.setObjectId(new ObjectId());
//		this.order=order2;
	}

	public String getStatus() {
		return "Cancelled";
	}
	

}
