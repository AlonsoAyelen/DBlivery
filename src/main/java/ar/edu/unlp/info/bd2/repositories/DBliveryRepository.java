package ar.edu.unlp.info.bd2.repositories;

import java.util.List;

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
//        return !users.isEmpty() ? users.get(query.getFirstResult()) : null;
	}
}
