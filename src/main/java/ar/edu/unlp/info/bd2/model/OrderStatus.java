package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="order_status")

public class OrderStatus {
	@Id
    @GeneratedValue
	protected Integer id;
	@Column(name="date")
	protected Date date;
	@ManyToOne
    protected Order order;
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OrderStatus() {
		
	}
	
	public String getStatus() {
		return null;
	}
	
	public void cancel(Order o) {
		
	}
	
//	public void cancel(Order o,Date date) {
//		
//	}
	
	public void deliver(Order o) {
			
	}
	
	public void send(Order o) {
		
	}

//	public void send(Order o,Date date) {
//		
//	}

	public void finish(Order o) {
		
	}
	
//	public void finish(Order o,Date date) {
//		
//	}
	
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