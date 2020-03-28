package ar.edu.unlp.info.bd2.model;

public class OrderStatus {

	public String getStatus() {
		return null;
	}
	
	public void cancel(Order o) {
		
	}
	
	public void deliver(Order o) {
			
	}
	
	public void send(Order o) {
		
	}
	
	public Boolean canCancel() {
		return false;
	}
	
	public Boolean canFinish() {
		return false;
	}
	
	public Boolean canDeliver() {
		return false;
	}
	
}