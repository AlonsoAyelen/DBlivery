package ar.edu.unlp.info.bd2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="order_status")
public class OrderStatus {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public OrderStatus() {
		
	}
	
	public String getStatus() {
		return null;
	}
	
	public void cancel(Order o) {
		
	}
	
	public void deliver(Order o) {
			
	}
	
	public void send(Order o) {
		
	}
	
	public Boolean canCancel(Order o) {
		return false;
	}
	
	public Boolean canFinish(Order o) {
		return false;
	}
	
	public Boolean canDeliver(Order o) {
		return false;
	}
	
}