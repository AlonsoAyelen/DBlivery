package ar.edu.unlp.info.bd2.model;

import java.util.List;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import ar.edu.unlp.info.bd2.mongo.*;

public class Product {
	@BsonId
	private ObjectId objectId;
	private String name;
	private Supplier supplier;
	private Float price;
	private Float weight;
	private List<Price> prices = new ArrayList<Price>();
	
	public Product (String name, Float price, Float weight, Supplier supplier) {
		this.name=name;
		this.price=price;
		this.weight=weight;
		this.supplier=supplier;
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		Price p = new Price(todayDate, price);
		this.prices.add(p);
	}
	
	public Product (String name, Float price, Float weight, Supplier supplier,Date date) {
		this.name=name;
		this.price=price;
		this.weight=weight;
		this.supplier=supplier;
		Price p = new Price(date, price);
		this.prices.add(p);
	}
	
	public Product() {
		
	}

	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Price> getPrices() {
		return prices;
	}
	public void addPrice(Price p) {
		this.prices.add(p);
	}
	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}
	
	public Price getLastPrice() {
		Price act= prices.get(0);
		for (Price p :this.getPrices()) {
			if(p.getStartDate().after(act.getStartDate())) act=p;
		}
		return act;
		//return prices.get(prices.size()-1);
	}
	
	public Product updatePrice(Product p,Float price, Date startDate) {
		p.setPrice(price);
		Price pOld = p.getLastPrice();
		pOld.setFinishDate(startDate);
		Price pri = new Price(startDate,null,price);
		p.addPrice(pri);
		return p;
	}

	public Float getPriceInDate(Date date) {
//		System.out.println(this.getName());
//		System.out.println(date);
		for (Price p : this.getPrices()) {
//			System.out.println("start" + p.getPrice());
//			System.out.println(p.getStartDate().toString());
//			System.out.println(p.getStartDate().before(date));
//			System.out.println(p.getFinishDate() != null);
//			if (p.getFinishDate() != null) System.out.println(p.getFinishDate().after(date));
//			System.out.println("end");
			
			if(p.getStartDate().before(date) && (p.getFinishDate() != null) && p.getFinishDate().after(date)) {
				return p.getPrice();
	        }
	    }
		return this.getLastPrice().getPrice();
	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
 
}
