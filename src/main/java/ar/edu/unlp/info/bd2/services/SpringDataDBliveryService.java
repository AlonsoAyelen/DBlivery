package ar.edu.unlp.info.bd2.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unlp.info.bd2.model.Delivered;
import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
//import ar.edu.unlp.info.bd2.model.Price;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Row;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import ar.edu.unlp.info.bd2.repositories.OrderRepository;
import ar.edu.unlp.info.bd2.repositories.ProductRepository;
import ar.edu.unlp.info.bd2.repositories.SupplierRepository;
import ar.edu.unlp.info.bd2.repositories.UserRepository;
import ar.edu.unlp.info.bd2.services.DBliveryService;

public class SpringDataDBliveryService implements DBliveryService,DBliveryStatisticsService {

    public SpringDataDBliveryService() {
    }

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    @Override
	@Transactional
	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
		Product p = new Product(name, price,weight,supplier);
		productRepository.save(p);
		return p;
	}

	@Override
	@Transactional
	public Product createProduct(String name, Float price, Float weight, Supplier supplier, Date date) {
		Product p = new Product(name, price,weight,supplier,date);
		productRepository.save(p);
		return p;
	}

	@Override
	@Transactional
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier s = new Supplier(name, cuil,address,coordX,coordY);
		supplierRepository.save(s);
		return s;
	}

	@Override
	@Transactional
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User u = new User(email,password,username,name,dateOfBirth);
		userRepository.save(u);
		return u;
	}

	@Override
	@Transactional
	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
		if (productRepository.existsById(id)){
			Product prod = productRepository.findById(id).get();
			prod.updatePrice(price, startDate);
			productRepository.save(prod);
			return prod;
		} else {
			throw new  DBliveryException("Product not found");
		}
	}

	@Override
	public Optional<User> getUserById(Long id) {
		return userRepository.getById(id);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.getByEmail(email);
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		return userRepository.getByUsername(username);
	}

	@Override
	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	@Transactional
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order o = new Order(dateOfOrder, address, coordX, coordY, client);
		orderRepository.save(o);
		return o;
	}

	@Override
	@Transactional
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
		if (orderRepository.existsById(order)) {
			Order o = orderRepository.findById(order).get();
			Row r = new Row(product,quantity,o);
			o.addProduct(r);
			orderRepository.save(o);
			return o;
		}else {
			throw new  DBliveryException("Order not found\n");
		}
	}

	@Override
	@Transactional
	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
		Optional<Order> oo = orderRepository.findById(order);
		if (oo.isPresent() && oo.get().canDeliver()){
			Order o = oo.get();
			o.send(deliveryUser);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be delivered");
		}
	}

	@Override
	@Transactional
	public Order deliverOrder(Long order, User deliveryUser, Date date) throws DBliveryException {
		Optional<Order> oo = orderRepository.findById(order);
		if (oo.isPresent() && oo.get().canDeliver()){
			Order o = oo.get();
			o.send(deliveryUser,date);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be delivered");
		}
	}

	@Override
	@Transactional
	public Order cancelOrder(Long order) throws DBliveryException {
		if (orderRepository.existsById(order)) {
			Order o = orderRepository.findById(order).get();
			if (o.canCancel()) {
				o.cancel();
				return o;
			} else {
				throw new DBliveryException("The order can't be cancelled");
			}
		} else {
			throw new DBliveryException("The order can't be cancelled");
		}
	}

	@Override
	@Transactional
	public Order cancelOrder(Long order, Date date) throws DBliveryException {
		Optional<Order> oo = orderRepository.findById(order);
		if (oo.isPresent() && oo.get().canCancel()){
			Order o = oo.get();
			o.cancel(date);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be cancelled");
		}
	}

	@Override
	@Transactional
	public Order finishOrder(Long order) throws DBliveryException {
		if (orderRepository.existsById(order)) {
			Order o = orderRepository.findById(order).get();
			if (o.canFinish()) {
				o.finish();
				return o;
			} else {
				throw new DBliveryException("The order can't be finished");
			}
		} else {
			throw new DBliveryException("The order can't be finished");
		}
	}

	@Override
	@Transactional
	public Order finishOrder(Long order, Date date) throws DBliveryException {
		Optional<Order> oo = orderRepository.findById(order);
		if (oo.isPresent() && oo.get().canFinish()){
			Order o = oo.get();
			o.finish(date);
			return o;
		}
		else {
			throw new DBliveryException("The order can't be finished");
		}
	}

	@Override
	public boolean canCancel(Long order) throws DBliveryException {
		if (orderRepository.existsById(order)) {
			Order o = orderRepository.findById(order).get();
			return o.canCancel();
		} else {
			throw new DBliveryException("Order not found\n");
		}
	}

	@Override
	public boolean canFinish(Long id) throws DBliveryException {
		if (orderRepository.existsById(id)) {
			Order o = orderRepository.findById(id).get();
			return o.canFinish();
		} else {
			throw new DBliveryException("Order not found\n");
		}
	}

	@Override
	public boolean canDeliver(Long order) throws DBliveryException {
		Optional<Order> oo = orderRepository.findById(order);
		if(oo.isPresent()) {
			return oo.get().canDeliver();
		} else {
			throw new DBliveryException("Order not found\n");
		}
	}

	@Override
	public OrderStatus getActualStatus(Long order) {
		if (orderRepository.existsById(order)) {
			Order o = orderRepository.findById(order).get();
			return o.getActualStatus();
		}
		return null;
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return productRepository.findAllByNameLike("%"+name+"%");
	}

	@Override
	public Product getMaxWeigth() {
		Optional<Product> op = productRepository.findTopByOrderByWeightDesc();
		return op.get();
	}

	@Override
	public List<Order> getAllOrdersMadeByUser(String username) {
		List<Order> orders = orderRepository.findAllByClient_Username(username);
		return orders;
	}

	@Override
	public List<Order> getPendingOrders() {
		List<Order> orders= orderRepository.findPendingOrders();
		return orders;
	}

	@Override
	public List<Order> getSentOrders() {
		List<Order> orders= orderRepository.findSentOrders();
		return orders;
	}

	@Override
	public List<Order> getDeliveredOrdersInPeriod(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> getDeliveredOrdersForUser(String username) {
		List<Order> orders=orderRepository.findDeliveredOrdersForUser(username);
		return orders;
	}

	@Override
	public List<Product> getProductsOnePrice() {
		List<Product> prods = productRepository.findByGroupByPrices_Product();
		return prods;
	}

	@Override
	public List<Product> getSoldProductsOn(Date day) {
		List<Product> prods = productRepository.findByDateByOrder_Product(day);
		return prods;
	}
    
//    @Transactional
//	@Override
//	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
//		Product p = new Product(name, price,weight,supplier);
//		repository.save(p);
//		return p;
//	}
//    
//    @Transactional
//	@Override
//	public Product createProduct(String name, Float price, Float weight, Supplier supplier, Date date) {
//		Product p = new Product(name, price,weight,supplier,date);
//		repository.save(p);
//		return p;
//	}
//
//    @Transactional
//	@Override
//	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
//		Supplier s = new Supplier(name, cuil,address,coordX,coordY);
//		repository.save(s);
//		return s;
//	}
//
//    @Transactional
//	@Override
//	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
////    	Optional<User> ou1=this.getUserByEmail(email);
////    	Optional<User> ou2=this.getUserByUsername(username);
////    	if(!ou1.isPresent() && !ou2.isPresent()){
////    		User u = new User(email,password,username,name,dateOfBirth);
////			repository.save(u);
////			return u;
////    	} else {
////    		System.out.println("El usuario ya esta registrado");
////    		return ou1.get();
////    	}
//    	User u = new User(email,password,username,name,dateOfBirth);
//		repository.save(u);
//		return u;
//	}
//
//    @Transactional
//	@Override
//	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
//		Optional<Product> pp = repository.findProductById(id);
//		if (pp.isPresent()){
//			Product p = pp.get();
//			p.updatePrice(p,price,startDate);
//			return p;
//		} else {
//			throw new  DBliveryException("Product not found");
//		}
//	}
//
//	@Override
//	public Optional<User> getUserById(Long id) {
//		return repository.findUserById(id);
//	}
//
//	@Override
//	public Optional<User> getUserByEmail(String email) {
//		return repository.findUserByEmail(email);
//	}
//
//	@Override
//	public Optional<User> getUserByUsername(String username) {
//		return repository.findUserByUsername(username);
//	}
//
//	@Override
//	public Optional<Product> getProductById(Long id) {
//		return repository.findProductById(id);
//	}
//
//	@Override
//	public Optional<Order> getOrderById(Long id) {
//		return repository.findOrderById(id);
//	}
//
//	@Transactional
//	@Override
//	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
//		Order o = new Order(dateOfOrder, address,coordX,coordY,client);
//		repository.save(o);
//		return o;
//	}
//
//	@Transactional
//	@Override
//	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent()){
//			Order o = oo.get();
//			Row r = new Row(product,quantity,o);
//			o.addProduct(r); //addRow
//			return o;
//		} else {
//			throw new  DBliveryException("Order not found\n");
//		}
//	}
//	@Transactional
//	@Override
//	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent() && oo.get().canDeliver()){
//			Order o = oo.get();
//			o.send(deliveryUser);
//			return o;
//		}
//		else {
//			throw new DBliveryException("The order can't be delivered");
//		}
//	}
//
//	@Transactional
//	@Override
//	public Order cancelOrder(Long order) throws DBliveryException {		
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent() && oo.get().canCancel()){
//			Order o = oo.get();
//			o.cancel();
//			return o;
//		}
//		else {
//			throw new DBliveryException("The order can't be cancelled");
//		}
//	}
//
//	@Transactional
//	@Override
//	public Order finishOrder(Long order) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent() && oo.get().canFinish()){
//			Order o = oo.get();
//			o.finish();
//			return o;
//		}
//		else {
//			throw new DBliveryException("The order can't be finished");
//		}
//	}
//
//	@Override
//	public boolean canCancel(Long order) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent()) {
//			return oo.get().canCancel();
//		} else {
//			throw new DBliveryException("Order not found\n");
//		}
//	}
//
//	@Override
//	public boolean canFinish(Long id) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(id);
//		if(oo.isPresent()) {
//			return oo.get().canFinish();
//		} else {
//			throw new DBliveryException("Order not found\n");
//		}
//	}
//
//	@Override
//	public boolean canDeliver(Long order) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if(oo.isPresent()) {
//			return oo.get().canDeliver();
//		} else {
//			throw new DBliveryException("Order not found\n");
//		}
//	}
//
//	@Override
//	public OrderStatus getActualStatus(Long order) {
//		Optional<Order> oo = repository.findOrderById(order);
//		return (oo.isPresent()?oo.get().getActualStatus():null);
//		
//	}
//
//	@Override
//	public List<Product> getProductByName(String name) {
//		List<Product> p=repository.findProductByName(name);
//		return p;
//	}
//
//	
//	
//	
//	
//	
//	
//	@Override
//	public List<Order> getAllOrdersMadeByUser(String username) {
//		List<Order> o=repository.findAllOrdersMadeByUser(username);
//		return o;
//	}
//
//	@Override
//	public List<User> getUsersSpendingMoreThan(Float amount) {
//		List<User> u=repository.findSpendingMoreThan(amount);
//		return u;
//	}
//
//	@Override
//	public List<Supplier> getTopNSuppliersInSentOrders(int n) {
//		List<Supplier> suppliers=repository.findTopNSuppliersInSentOrders(n);
//		return suppliers;
//	}
//
//	@Override
//	public List<Product> getTop10MoreExpensiveProducts() {
//		List<Product> p=repository.findTop10MoreExpensiveProduct();
//		return p;
//	}
//
//	@Override
//	public List<User> getTop6UsersMoreOrders() {
//		List<User> users=repository.findTop6UsersMoreOrders();
//		return users;
//	}
//
//	@Override
//	public List<Order> getCancelledOrdersInPeriod(Date startDate, Date endDate) {
//		List<Order> orders=repository.findCancelledOrdersInPeriod(startDate,endDate);
//		return orders;
//	}
//
//	@Override
//	public List<Order> getPendingOrders() {
//		List<Order> orders=repository.findPendingOrders();
//		return orders;
//	}
//
//	@Override
//	public List<Order> getSentOrders() {
//		List<Order> orders=repository.findSentOrders();
//		return orders;
//	}
//
//	@Override
//	public List<Order> getDeliveredOrdersInPeriod(Date startDate, Date endDate) {
//		List<Order> orders=repository.findDeliveredOrdersInPeriod(startDate,endDate);
//		return orders;
//	}
//
//	@Override
//	public List<Order> getDeliveredOrdersForUser(String username) {
//		List<Order> o=repository.findDeliveredOrdersForUser(username);
//		return o;
////		return null;
//	}
//
//	@Override
//	public List<Order> getSentMoreOneHour() {
//		List<Order> o = repository.findMoreOneHour();
//		return o;
//	}
//
//	@Override
//	public List<Order> getDeliveredOrdersSameDay() {
//		List<Order> o = repository.findSameDay();
//		return o;
//	}
//
//	@Override
//	public List<User> get5LessDeliveryUsers() {
//		List<User> u = repository.find5LessDeliveryUsers();
//		return u;
//	}
//
//	@Override
//	public Product getBestSellingProduct() {
//		Product p = repository.findBestSelling();
//		return p;
//	}
//
//	@Override
//	public List<Product> getProductsOnePrice() {
//		List<Product> p=repository.findProductsOnePrice();
//		return p;
//	}
//
//	@Override
//	public List<Product> getProductIncreaseMoreThan100() {
//		List<Product> p=repository.findProductIncreaseMoreThan100();
//		return p;
//	}
//
//	@Override
//	public Supplier getSupplierLessExpensiveProduct() {
//		Supplier supplier = repository.findSupplierLessExpensiveProduct();
//		return supplier;
//	}
//
//	@Override
//	public List<Supplier> getSuppliersDoNotSellOn(Date day) {
//		List<Supplier> suppliers = repository.findSuppliersDoNotSellOn(day);
//		return suppliers;
//	}
//
//	@Override
//	public List<Product> getSoldProductsOn(Date day) {
//		List<Product> p=repository.findSoldProductsOn(day);
//		return p;
//	}
//
//	@Override
//	public List<Order> getOrdersCompleteMorethanOneDay() {
//		List<Order> orders=repository.findOrdersCompleteMorethanOneDay();
//		return orders;
//	}
//
//	@Override
//	public List<Object[]> getProductsWithPriceAt(Date day) {
//		List<Object[]> products = repository.findProductsWithPriceAt(day);
//		return products;
//	}
//
//	@Override
//	public List<Product> getProductsNotSold() {
//		List<Product> p=repository.findProductsNotSold();
//		return p;
//	}
//
//	@Override
//	public List<Order> getOrderWithMoreQuantityOfProducts(Date day) {
//		List<Order> orders=repository.findOrderWithMoreQuantityOfProducts(day);
//		return orders;
//	}
//
//	@Transactional
//	@Override
//	public Order deliverOrder(Long order, User deliveryUser, Date date) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent() && oo.get().canDeliver()){
//			Order o = oo.get();
//			o.send(deliveryUser,date);
//			return o;
//		}
//		else {
//			throw new DBliveryException("The order can't be delivered");
//		}
//	}
//
//	@Transactional
//	@Override
//	public Order cancelOrder(Long order, Date date) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent() && oo.get().canCancel()){
//			Order o = oo.get();
//			o.cancel(date);
//			return o;
//		}
//		else {
//			throw new DBliveryException("The order can't be cancelled");
//		}
//	}
//
//	@Transactional
//	@Override
//	public Order finishOrder(Long order, Date date) throws DBliveryException {
//		Optional<Order> oo = repository.findOrderById(order);
//		if (oo.isPresent() && oo.get().canFinish()){
//			Order o = oo.get();
//			o.finish(date);
//			return o;
//		}
//		else {
//			throw new DBliveryException("The order can't be finished");
//		}
//	}

}
