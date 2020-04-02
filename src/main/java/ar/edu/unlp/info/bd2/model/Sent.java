package ar.edu.unlp.info.bd2.model;

import javax.persistence.Entity;

@Entity
public class Sent extends OrderStatus {

	public Sent() {
		
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
