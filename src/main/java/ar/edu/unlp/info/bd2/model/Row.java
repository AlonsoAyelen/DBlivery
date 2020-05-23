package ar.edu.unlp.info.bd2.model;

public class Row {
	private Integer id;
	private Long cant;
	private Product product;
//PORQUE NUNCA SE USABA?!
	private Order order;

	public Row(Product product, Long quantity, Order order) {
		this.cant=quantity;
		this.product=product;
		this.order=order;
	}
	
	public Row() {
		
	}
	
	public Long getCant() {
		return cant;
	}

	public void setCant(Long cant) {
		this.cant = cant;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
