package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Price;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Row;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import ar.edu.unlp.info.bd2.repositories.DBliveryRepository;
import ar.edu.unlp.info.bd2.services.DBliveryService;

public class DBliveryServiceImpl implements DBliveryService {
	
	private DBliveryRepository repository;
	
    public DBliveryServiceImpl(DBliveryRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
	@Override
	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
		Product p = new Product(name, price,weight,supplier);
		repository.save(p);
		return p;
	}

    @Transactional
	@Override
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier s = new Supplier(name, cuil,address,coordX,coordY);
		repository.save(s);
		return s;
	}

    @Transactional
	@Override
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User u = new User(email,password,username,name,dateOfBirth);
		repository.save(u);
		return u;
	}

    @Transactional
	@Override
	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
		Optional<Product> pp = repository.findProductById(id);
		if (pp.isPresent()){
			Product p = pp.get();
			p.updatePrice(p,price,startDate);
			return p;
		}
		return null;
	}

	@Override
	public Optional<User> getUserById(Long id) {
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
	public Optional<Product> getProductById(Long id) {
		return repository.findProductById(id);
//		Optional<Product> p = repository.findProductById(id);
//		return p;
	}

	@Override
	public Optional<Order> getOrderById(Long id) {
		//
		return repository.findOrderById(id);
	}

	@Transactional
	@Override
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order o = new Order(dateOfOrder, address,coordX,coordY,client);
		repository.save(o);
		//System.out.println(o.getAddress()+o.getCoordX()+o.getDateOfOrder()+o.getId());
		return o;
	}

	@Transactional
	@Override
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
		// TODO Auto-generated method stub
		Optional<Order> oo = repository.findOrderById(order);
		if (oo.isPresent()){
			Order o = oo.get();
			Row r = new Row(product,quantity);
			o.addProduct(r); //addRow
			return o;
		}
		return null;
	}

	@Transactional
	@Override
	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if (oo.isPresent() && oo.get().canDeliver()){
			Order o = oo.get();
			o.send(deliveryUser);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be delivered");
		}
	}

	@Transactional
	@Override
	public Order cancelOrder(Long order) throws DBliveryException {		
		Optional<Order> oo = repository.findOrderById(order);
		if (oo.isPresent() && oo.get().canCancel()){
			Order o = oo.get();
			o.cancel();
			return o;
		}
		else {
			throw new DBliveryException("The order can't be cancelled");
		}
	}

	@Transactional
	@Override
	public Order finishOrder(Long order) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		if (oo.isPresent() && oo.get().canFinish()){
			Order o = oo.get();
			o.finish();
			return o;
		}
		else {
			throw new DBliveryException("The order can't be finished");
		}
	}

	@Override
	public boolean canCancel(Long order) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		return (oo.isPresent() && oo.get().canCancel());
	}

	@Override
	public boolean canFinish(Long id) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(id);
		return (oo.isPresent() && oo.get().canFinish());
	}

	@Override
	public boolean canDeliver(Long order) throws DBliveryException {
		Optional<Order> oo = repository.findOrderById(order);
		return (oo.isPresent() && oo.get().canDeliver());
	}

	@Override
	public OrderStatus getActualStatus(Long order) {
		Optional<Order> oo = repository.findOrderById(order);
		return (oo.isPresent()?oo.get().getActualStatus():null);
		
	}

	@Override
	public List<Product> getProductByName(String name) {
		List<Product> p=repository.findProductByName(name);
		return p;
	}

}
