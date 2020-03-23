package ar.edu.unlp.info.bd2.model;

public class Sent extends State {

	public void deliver(Order o) {
		o.setState(new Delivered());
	}
}
