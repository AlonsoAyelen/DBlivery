package ar.edu.unlp.info.bd2.model;

import java.util.List;
import java.util.Calendar;
import java.util.Date;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
import javax.persistence.*;

import java.util.ArrayList; // import the ArrayList class
@Entity
@Table(name="products")
public class Product {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="name")
	private String name;
	@ManyToOne()
	private Supplier supplier;
	@Column(name="price")
	private Float price;
	@Column(name="weight")
	private Float weight;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "Price_id")
	private List<Price> prices = new ArrayList<Price>();
	
	public Product (String name, Float price, Float weight, Supplier supplier) {
		this.name=name;
		this.price=price;
		this.weight=weight;
		this.supplier=supplier;
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();
		Price p = new Price(todayDate, price);
		System.out.print(this.prices);
		this.prices.add(p);
		System.out.print(this.prices);
	}
	
	public Product (String name, Float price, Float weight, Supplier supplier,Date date) {
		this.name=name;
		this.price=price;
		this.weight=weight;
		this.supplier=supplier;
//		Calendar today = Calendar.getInstance();
//		Date todayDate = today.getTime();
		Price p = new Price(date, price);
		this.prices.add(p);
	}
	
	public Product() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
		return prices.get(prices.size()-1);
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
	
 
}
