package ar.edu.unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unlp.info.bd2.model.*;

public class DBliveryRepository {
    @Autowired
    private SessionFactory sessionFactory;

    public Object save (Object obj) {//throws Exception{
        this.sessionFactory.getCurrentSession().save(obj);
        return obj;
    }
    
	public List<Product> findProductByName(String name) {
		String hql = "from Product where name like concat('%',:name,'%')";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name", name);
        List<Product> products = query.getResultList();
        return products;
	}

	public Optional<Product> findProductById(Long id) {
		String hql = "from Product where id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        Product product = (Product) query.getSingleResult();
        Optional<Product> op = Optional.ofNullable(product);
        return op;
	}

	public Optional<User> findUserByUsername(String username) {
		String hql = "from User where username = :username";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("username", username);
        User user = (User) query.getSingleResult();
        Optional<User> ou = Optional.ofNullable(user);
        return ou;
	}

	public Optional<User> findUserByEmail(String email) {
		String hql = "from User where email = :email";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", email);
        User user = (User) query.getSingleResult();
        Optional<User> ou = Optional.ofNullable(user);
        return ou;
	}

	public Optional<User> findUserById(Long id) {
		String hql = "from User where id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        User user = (User) query.getSingleResult();
        Optional<User> ou = Optional.ofNullable(user);
        return ou;
	}

	public Optional<Order> findOrderById(Long id) {
		String hql = "from Order where id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        Order order = (Order) query.getSingleResult();
        Optional<Order> op = Optional.ofNullable(order);
        return op;
	}

	public List<Order> findAllOrdersMadeByUser(String username) {
		String hql = "select order from Order as order where order.client.username = :username";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("username", username);
        List<Order> orders = query.getResultList();
        return orders;
	}

	public List<Product> findTop10MoreExpensiveProduct() {
		String hql = "from Product products order by products.price desc";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setFirstResult(0);
        query.setMaxResults(9);
        List<Product> products = query.getResultList();
        return products;
	}

	public List<User> findTop6UsersMoreOrders() {
		String hql = "select client from Order orders group by orders.client order by count(*) desc";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setFirstResult(0);
        query.setMaxResults(6);
        List<User> users = query.getResultList();
//        for (User u : users) {
//            System.out.println(u.getEmail()+" - "+u.getUsername()); 
//        }
        return users;
	}

	public List<Supplier> findTopNSuppliersInSentOrders(int n) {
		//String hql = "select client from Order orders group by orders.client order by count(*) desc";

		//aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
		String hql = "from Sent sent inner join sent.order o";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setFirstResult(0);
        query.setMaxResults(n);
        List<Supplier> suppliers = query.getResultList();
        return suppliers;
	}

	public List<Order> findOrderWithMoreQuantityOfProducts(Date day) {
//		String hql="select sum(p.cant) from Order o join o.products p where o.dateOfOrder = :day group by o.id order by sum(p.cant) desc";
//		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
//		query.setParameter("day", day);
//		query.setFirstResult(0);
//		query.setMaxResults(1);
//		List<Long> maxL = query.getResultList();
//		Long max=(long)0;
//		if(maxL.size()>0)max=maxL.get(0);
////		System.out.println(max);
//		hql = "select o from Order o join o.products p where o.dateOfOrder = :day group by o.id having (sum(p.cant)=:max )";
//		query = this.sessionFactory.getCurrentSession().createQuery(hql);
//		query.setParameter("day", day);
//		query.setParameter("max", max);
//		List<Order> orders = query.getResultList();
//	    for (Order o : orders) {
//	      System.out.println(o.getAddress()+" - "+o.getId()); 
//	    }

		String hql = "select o from Order o join o.products p where o.dateOfOrder = :day group by o.id having (sum(p.cant) >= ALL (select sum(p.cant) from Order o join o.products p where o.dateOfOrder = :day group by o.id))";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("day", day);
        List<Order> orders = query.getResultList();
        return orders;
	}

	public List<Product> findProductsNotSold() {
		String hql = "from Product p where p.id not in (select product from Order o join o.products row join row.product product)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        List<Product> products = query.getResultList();
        return products;
	}

	public List<Object[]> findProductsWithPriceAt(Date day) {
		String hql = "select prod,price.price as price from Product prod join prod.prices price where (:day between price.startDate and price.finishDate) or (:day > price.startDate and price.finishDate=NULL)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("day", day);
		List<Object[]> products = query.getResultList();
        return products;
	}

	public List<Product> findSoldProductsOn(Date day) {
		String hql = "select row.product from Order o join o.products row where o.dateOfOrder=:day";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("day", day);
	    List<Product> products = query.getResultList();
//        for (Product p : products) {
//	      System.out.println(p.getName()+" - "+p.getId()); 
//	    }
        return products;
	}

	public List<Supplier> findSuppliersDoNotSellOn(Date day) {
		String hql="from Supplier s where s.id not in (select p.supplier from Order o join o.products row join row.product p where o.dateOfOrder=:day)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("day",day);
		List<Supplier> suppliers = query.getResultList();
		return suppliers;
	}

	public Supplier findSupplierLessExpensiveProduct() {
		String hql="select s from Product p join p.supplier s where p.price = (select min(prod.price) from Product prod)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		Supplier supplier = (Supplier) query.getSingleResult();
		return supplier;
	}

	public List<Product> findProductIncreaseMoreThan100() {
		String hql = "select prod from Product prod join prod.prices prc where prod.price > (2*prc.price)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		List<Product> products = query.getResultList();
//		for (Product p : products) {
//			System.out.println(p.getName()+" - "+p.getPrice()+" - "+p.getId()); 
//		}
        return products;
	}

	public List<Product> findProductsOnePrice() {
		String hql = "select prod from Product prod join prod.prices prc group by prod.id having (count(*)=1)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		List<Product> products = query.getResultList();
        return products;
	}

	public List<Order> findDeliveredOrdersForUser(String username) {
		String hql="select o from Sent s join s.order o where o.client.username = :username";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("username", username);
        List<Order> orders = query.getResultList();   
        return orders;
	}

	public List<Order> findPendingOrders() {
		String hql="select o from Pending p join p.order o where o.id not in(select c.order.id from Cancelled c) and o not in(select s.order.id from Sent s) and o not in(select d.order.id from Delivered d)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        List<Order> orders = query.getResultList();
        return orders;
	}

	public List<Order> findSentOrders() {
		String hql="select o from Sent s join s.order o where o.id not in(select c.order.id from Cancelled c) and o not in(select d.order.id from Delivered d)";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        List<Order> orders = query.getResultList();
        return orders;
	}

	public List<Order> findDeliveredOrdersInPeriod(Date startDate, Date endDate) {
		String hql="select o from Delivered d join d.order o where o.id not in(select c.order.id from Cancelled c) and d.date between :startDate and :endDate";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Order> orders = query.getResultList();
        return orders;
	}

	public List<Order> findCancelledOrdersInPeriod(Date startDate, Date endDate) {
		String hql="select o from Cancelled c join c.order o where c.date between :startDate and :endDate";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Order> orders = query.getResultList();
        return orders;
	}

	public List<Order> findOrdersCompleteMorethanOneDay() {
		String hql="select o from Delivered d join d.order o where d.date>= o.dateOfOrder+2";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		List<Order> orders = query.getResultList();
        return orders;
	}
	//select * from delivered join orders on delivered.order_id = orders.id where day(delivered.date) = day(orders.date);
	
	public Product findBestSelling() {
		String hql="SELECT p FROM Row r JOIN r.product p GROUP BY p.id ORDER BY SUM(cant) DESC ";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult(0);
        query.setMaxResults(1);
        Product prod = (Product) query.getSingleResult();
		return prod;
	}
	
	public List<Order> findMoreOneHour(){
		String hql="SELECT o FROM Sent s join s.order o WHERE s.date >= o.dateOfOrder + 1";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		List<Order> orders = query.getResultList();
		return orders;
	}
	
	public List<Order> findSameDay(){
		String hql="SELECT o FROM Delivered s join s.order o WHERE s.date = o.dateOfOrder";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		List<Order> orders = query.getResultList();
		return orders;
	}
	
}
