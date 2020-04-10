package ar.edu.unlp.info.bd2.repositories;

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
//		String hql = "from Product where name like :name";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name", name);
//      query.setParameter("name", '%'+name+'%');
        List<Product> products = query.getResultList();
//        System.out.println(products.size());
        return products;
//      return !products.isEmpty() ? products.get(query.getFirstResult()) : null;
	}


	public Optional<Product> findProductById(Long id) {
		String hql = "from Product where id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        List<Product> products = query.getResultList();
        Optional<Product> op = Optional.ofNullable(products.get(query.getFirstResult()));;
        return op;
	}


	public Optional<User> findUserByUsername(String username) {
		String hql = "from User where username = :username";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("username", username);
        List<User> users = query.getResultList();
        Optional<User> ou = Optional.ofNullable(users.get(query.getFirstResult()));;
        return ou;
	}


	public Optional<User> findUserByEmail(String email) {
		String hql = "from User where email = :email";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        Optional<User> ou = Optional.ofNullable(users.get(query.getFirstResult()));;
        return ou;
	}


	public Optional<User> findUserById(Long id) {
		String hql = "from User where id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        List<User> users = query.getResultList();
        Optional<User> ou = Optional.ofNullable(users.get(query.getFirstResult()));;
        return ou;
	}
	
	public Optional<Order> findOrderById(Long id) {
		
		String hql = "from Order where id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        List<Order> products = query.getResultList();
        Optional<Order> op = Optional.ofNullable(products.get(query.getFirstResult()));;
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
        query.setMaxResults(10);
        List<Product> products = query.getResultList();
        return products;
	}
}
