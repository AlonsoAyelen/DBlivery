package ar.edu.unlp.info.bd2.model;

import javax.persistence.Entity;

@Entity
public class Cancelled extends OrderStatus {

	public Cancelled() {
		
	}

	public String getStatus() {
		return "Cancelled";
	}
}
