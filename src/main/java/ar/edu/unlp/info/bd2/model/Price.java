package ar.edu.unlp.info.bd2.model;

import java.util.Date;

public class Price {
	private long id;
	private Date startDate;
	private Date finishDate;
	private Float price;
	
	public Price() {
		
	}
	

	public Price(Date start, Date finish, Float price){
		this.startDate=start;
		this.finishDate=finish;
		this.price=price;
		
	}

	public Price(Date start, Float price){
		this.startDate=start;
		this.price=price;
		
	}
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
}
