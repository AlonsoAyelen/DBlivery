package ar.edu.unlp.info.bd2.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Cancelled extends OrderStatus {

	public Cancelled() {
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		this.date=todayDate;
	}

	public String getStatus() {
		return "Cancelled";
	}
}
