package ar.edu.unlp.info.bd2.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Price;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Row;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;
import ar.edu.unlp.info.bd2.mongo.Association;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import ar.edu.unlp.info.bd2.repositories.DBliveryMongoRepository;
import ar.edu.unlp.info.bd2.services.DBliveryService;

public class DBliveryServiceImpl implements DBliveryService, DBliveryStatiticsService {
	
	private DBliveryMongoRepository repository;
	
    public DBliveryServiceImpl(DBliveryMongoRepository repository) {
        this.repository = repository;
    }

	@Override
	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
		Product p = new Product(name, price,weight,supplier);
		Supplier supp = repository.findSupplierById(supplier.getObjectId()).get();
		supp.addProduct(p);
		repository.createProduct(supp);
		return p;
	}

	@Override
	public Product createProduct(String name, Float price, Float weight, Supplier supplier, Date date) {
		Product p = new Product(name, price,weight,supplier,date);
		Supplier supp = repository.findSupplierById(supplier.getObjectId()).get();
		supp.addProduct(p);
		repository.createProduct(supp);
		return p;
	}

	@Override
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier s = new Supplier(name, cuil,address,coordX,coordY);
		repository.createSupplier(s);
		return s;
	}

	@Override
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User u = new User(email,password,username,name,dateOfBirth);
		repository.createUser(u);
		return u;
	}

	private Product findProduct(Object id, Supplier s) {
		for( Product p: s.getProducts() ) {
			if( p.getObjectId().equals(id) ) {
				return p;
			}
		}
		return null;
	}
	
	@Override
	public Product updateProductPrice(ObjectId id, Float price, Date startDate) throws DBliveryException {
		Optional<Supplier> sup = repository.findSupplierOfProduct(id);
		if (sup.isPresent()) {
			Supplier s = sup.get();
			Product p = this.findProduct(id, s);
			p.updatePrice(p,price,startDate);
			repository.refreshSupplier(sup.get());
			repository.updateProductInRows(p, startDate);
			return p;
		}else {
			throw new  DBliveryException("Product not found");
		}
	}

	@Override
	public Optional<User> getUserById(ObjectId id) {
		return repository.findUserById(id);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		return repository.findUserByEmail(email);
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		return repository.findUserByUsername(username);
	}

	@Override
	public Optional<Order> getOrderById(ObjectId id) {
		Optional<Order> oo = repository.findOrderById(id);
		return oo;
	}

	@Override
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order o = new Order(dateOfOrder, address,coordX,coordY,client);
		repository.createOrder(o);
		return o;
	}

	@Override
	public Order addProduct(ObjectId order, Long quantity, Product product) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if(oo.isPresent()) {
			Optional<Supplier> sup = repository.findSupplierOfProduct(product.getObjectId());
			Product p=null;
			if (sup.isPresent()) {
				Supplier s = sup.get();
				p = this.findProduct(product.getObjectId(), s);
			} else {
				throw new DBliveryException("Error with the product");
			}
			Order o =oo.get();
			Row r = new Row(p, quantity, o);
			repository.save(r,"rows");
			repository.saveAssociation(o,r,"order_rows");
			return o;
		} else {
			throw new DBliveryException("Error finding the order");
		}
	}

	@Override
	public Order deliverOrder(ObjectId order, User deliveryUser) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if (oo.isPresent() && oo.get().canDeliver()){
			Order o = oo.get();
			o.send(deliveryUser);
			repository.refreshOrder(o);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be delivered");
		}
	}

	@Override
	public Order deliverOrder(ObjectId order, User deliveryUser, Date date) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if (oo.isPresent() && oo.get().canDeliver()){
			Order o = oo.get();
			o.send(deliveryUser,date);
			repository.refreshOrder(o);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be delivered");
		}
	}
	
	@Override
	public boolean canDeliver(ObjectId order) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if(oo.isPresent()) {
			return oo.get().canDeliver();
		} else {
			throw new DBliveryException("Order not found\n");
		}
	}
	
	@Override
	public Order cancelOrder(ObjectId order) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if(oo.isPresent() && oo.get().canCancel()) {
			Order o = oo.get();
			o.cancel();
			repository.refreshOrder(o);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be cancelled");
		}
	}


	
	@Override
	public Order cancelOrder(ObjectId order, Date date) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if(oo.isPresent() && oo.get().canCancel()) {
			Order o = oo.get();
			o.cancel(date);
			repository.refreshOrder(o);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be cancelled");
		}
	}

	@Override
	public Order finishOrder(ObjectId order) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if(oo.isPresent() && oo.get().canFinish()) {
			Order o = oo.get();
			o.finish();
			repository.refreshOrder(o);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be cancelled");
		}
	}

	@Override
	public Order finishOrder(ObjectId order, Date date) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if(oo.isPresent() && oo.get().canFinish()) {
			Order o = oo.get();
			o.finish(date);
			repository.refreshOrder(o);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be cancelled");
		}
	}

	@Override
	public boolean canCancel(ObjectId order) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if(oo.isPresent()) {
			return oo.get().canCancel();
		}
		else{
			throw new DBliveryException("Order not found\n");
		}
	}

	@Override
	public boolean canFinish(ObjectId id) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(id);
		if(oo.isPresent()) {
			return oo.get().canFinish();
		}
		else{
			throw new DBliveryException("Order not found\n");
		}
	}

	@Override
	public OrderStatus getActualStatus(ObjectId order) {
		Optional<Order> oo = repository.findOrderById(order);
		return oo.get().getActualStatus();
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return repository.findProductsByName(name);
	}

	@Override
	public List <Order>  getPendingOrders(){
		List<Order> orders=repository.findPendingOrders();
		return orders;
	}

	@Override
	public List<Order> getAllOrdersMadeByUser(String username) throws DBliveryException {
		List<Order> orders=repository.findOrdersMadeByUser(username);
		return orders;
	}

	@Override
	public List<Supplier> getTopNSuppliersInSentOrders(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> getSentOrders() {
		List<Order> orders=repository.findSentOrders();
		return orders;
	}

	@Override
	public List<Order> getDeliveredOrdersInPeriod(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> getDeliveredOrdersForUser(String username) {
		List<Order> o=repository.findDeliveredOrdersForUser(username);
		return o;
	}

	@Override
	public Product getBestSellingProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductsOnePrice() {
		List<Product> p=repository.findProductsOnePrice();
		return p;
	}

	@Override
	public List<Product> getSoldProductsOn(Date day) {
		List<Product> p=repository.findSoldProductsOn(day);
		return p;
	}

	@Override
	public Product getMaxWeigth() {
		return repository.findProductWithMaxWeigth();
	}

	@Override
	public List<Order> getOrderNearPlazaMoreno() {
		List<Order> oo = repository.findOrderNearPlazaMoreno();
		return oo;
	}

}
