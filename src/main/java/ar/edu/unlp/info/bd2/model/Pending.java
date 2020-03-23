package ar.edu.unlp.info.bd2.model;

public class Pending extends State {

	
	public void cancel(Order o) {
		o.setState(new Cancelled());
	}
	
	public void send(Order o) {
		o.setState(new Sent());
	}
	
}
