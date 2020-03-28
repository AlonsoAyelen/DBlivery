package ar.edu.unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DBliveryRepository {
    @Autowired
    private SessionFactory sessionFactory;

    public Object save (Object obj) {//throws Exception{
        this.sessionFactory.getCurrentSession().save(obj);
        return obj;
    }
    
}
