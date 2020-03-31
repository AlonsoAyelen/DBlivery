package ar.edu.unlp.info.bd2.model;

public class Pending extends OrderStatus {

	public Pending() {
		
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
}
