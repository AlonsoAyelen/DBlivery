package ar.edu.unlp.info.bd2.model;

public class Sent extends OrderStatus {

	public Sent() {
		
	}
	
	public void deliver(Order o) {
		o.setStatus(new Delivered());
	}
	
	public String getStatus() {
		return "Sent";
	}
}
