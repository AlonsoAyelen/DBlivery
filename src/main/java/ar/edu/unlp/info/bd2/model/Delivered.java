package ar.edu.unlp.info.bd2.model;

import javax.persistence.Entity;

@Entity
public class Delivered extends OrderStatus {

	public Delivered() {
		
	}
	
	public String getStatus() {
		return "Delivered";
	}
}
