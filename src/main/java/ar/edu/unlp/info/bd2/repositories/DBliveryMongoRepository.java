package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.*;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

public class DBliveryMongoRepository {

    @Autowired private MongoClient client;


    public void saveAssociation(PersistentObject source, PersistentObject destination, String associationName) {
        Association association = new Association(source.getObjectId(), destination.getObjectId());
        this.getDb()
                .getCollection(associationName, Association.class)
                .insertOne(association);
    }

    public MongoDatabase getDb() {
        return this.client.getDatabase("bd2_grupo2");
    }

    public <T extends PersistentObject> List<T> getAssociatedObjects(
            PersistentObject source, Class<T> objectClass, String association, String destCollection) {
        AggregateIterable<T> iterable =
                this.getDb()
                        .getCollection(association, objectClass)
                        .aggregate(
                                Arrays.asList(
                                        match(eq("source", source.getObjectId())),
                                        lookup(destCollection, "destination", "_id", "_matches"),
                                        unwind("$_matches"),
                                        replaceRoot("$_matches")));
        Stream<T> stream =
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }
    
    public <T extends PersistentObject> List<T> getObjectsAssociatedWith(
            ObjectId objectId, Class<T> objectClass, String association, String destCollection) {
        AggregateIterable<T> iterable =
                this.getDb()
                        .getCollection(association, objectClass)
                        .aggregate(
                                Arrays.asList(
                                        match(eq("destination", objectId)),
                                        lookup(destCollection, "source", "_id", "_matches"),
                                        unwind("$_matches"),
                                        replaceRoot("$_matches")));
        Stream<T> stream =
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), 0), false);
        return stream.collect(Collectors.toList());
    }

    
    
    /* COMIENZO DE METODOS CREATE */

    //Intento de hacer que todos los create usen el mismo metodo
    //REVISAR PROBLEMAS DE USAR PersistentObject.class
    public void save(PersistentObject obj, String collectionName) {
        this.getDb()
                .getCollection(collectionName, PersistentObject.class)
                .insertOne(obj);
    }

	public User createUser(User u) {
		MongoCollection<User> userCollection = this.getDb().getCollection("users",User.class);
	    userCollection.insertOne(u);
	    return u;
	}

	public Supplier createSupplier(Supplier s) {
		MongoCollection<Supplier> supplierCollection = this.getDb().getCollection("suppliers",Supplier.class);
		supplierCollection.insertOne(s);
	    return s;
	}

	public Product createProduct(Supplier s) {
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers",Supplier.class);
		suppliersCollection.replaceOne(eq("_id", s.getObjectId()),s);
		return null;
	}

	public Order createOrder(Order o) {
		MongoCollection<Order> orderCollection = this.getDb().getCollection("orders",Order.class);
		orderCollection.insertOne(o);
	    return o;
	}
	
	/* FIN DE METODOS CREATE */



	/* COMIENZO DE METODOS FIND */
	
	public List<Product> findProductsByName(String name) {
//		Bson filter = regex("name", name);
//		MongoCollection<Product> productCollection = this.getDb().getCollection("products",Product.class);
//		List<Product> products = new ArrayList<>();
//		FindIterable<Product> itr =productCollection.find(filter);
//		for (Product p : itr) {
//			products.add(p);
//		}
//		return products;
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers", Supplier.class); 		
		BasicDBObject filter = new BasicDBObject(new BasicDBObject("products",new BasicDBObject("$elemMatch", new BasicDBObject("name", new BasicDBObject("$regex",name))))); 		
		FindIterable<Supplier> itr = suppliersCollection.find(filter);
		List<Product> products = new ArrayList<Product>();
//		System.out.println("-----");
		for(Supplier s : itr) {
//			System.out.println("S - "+s.getName());
			for(Product p : s.getProducts()) {
//				System.out.println("p - "+p.getName());
				if(p.getName().matches(".*"+name+".*")) {
//					System.out.println("Va p - "+p.getName());
					products.add(p);
				}
			}
		}
		return products;
	}

	public Optional<User> findUserById(ObjectId id) {
		MongoCollection<User> usersCollection = this.getDb().getCollection("users", User.class);
		User u = usersCollection.find(eq("objectId", id)).first();
		Optional<User> ou = Optional.ofNullable(u);
		return ou;
	}
	
	public Optional<Supplier> findSupplierById(ObjectId id) {
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers", Supplier.class);
		Supplier sup = suppliersCollection.find(eq("objectId", id)).first();
		Optional<Supplier> su = Optional.ofNullable(sup);
		return su;
	}

	public Optional<User> findUserByUsername(String username) {
		MongoCollection<User> usersCollection = this.getDb().getCollection("users", User.class);
		User u = usersCollection.find(eq("username", username)).first();
		Optional<User> ou = Optional.ofNullable(u);
		return ou;
	}

	public Optional<User> findUserByEmail(String email) {
		MongoCollection<User> usersCollection = this.getDb().getCollection("users", User.class);
		User u = usersCollection.find(eq("email", email)).first();
		Optional<User> ou = Optional.ofNullable(u);
		return ou;
	}

//	public Optional<Product> findProductById(ObjectId id) {
//		MongoCollection<Product> productsCollection = this.getDb().getCollection("products", Product.class);
//		Product p = productsCollection.find(eq("objectId", id)).first();
//		Product p = productsCollection.find(eq("_id", id)).first();
//		Optional<Product> op = Optional.ofNullable(p);
//		return op;
//	}

	public Optional<Order> findOrderById(ObjectId order) {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class);
		Order o = ordersCollection.find(eq("_id", order)).first();
		List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
		o.setProducts(lr);
		Optional<Order> oo = Optional.ofNullable(o);
		return oo;
	}

	public void refreshOrder(Order o) {
		// TODO Auto-generated method stub
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders",Order.class);
		ordersCollection.replaceOne(eq("_id", o.getObjectId()),o);
		//return null;
	}
	
	public void refreshSupplier(Supplier p) {
		MongoCollection<Supplier> ordersCollection = this.getDb().getCollection("suppliers",Supplier.class);
		ordersCollection.replaceOne(eq("_id", p.getObjectId()),p);
	}

//	public Optional<Product> findProductById(ObjectId id) {
//		MongoCollection<Supplier> ordersCollection = this.getDb().getCollection("suppliers",Supplier.class);		
//		Product p = ordersCollection.find(eq("products.id", id));
//		Optional<Product> p = Optional.ofNullable(value)
//		return 
//	}
	
	public Optional<Supplier> findSupplierOfProduct(ObjectId id) { 		
		MongoCollection<Supplier> suppliersCollection = this.getDb().getCollection("suppliers", Supplier.class); 		
		BasicDBObject filter = new BasicDBObject(new BasicDBObject("products",new BasicDBObject("$elemMatch", new BasicDBObject("_id", id)))); 		
		Supplier s = suppliersCollection.find(filter).first();
		Optional<Supplier> os = Optional.ofNullable(s);
		return os; 	
	}
	
	public List<Order> findPendingOrders() {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class); 		
		BasicDBObject filter = new BasicDBObject("statusActual.status", "Pending"); 		
		FindIterable<Order> itr = ordersCollection.find(filter);
		List<Order> orders = new ArrayList<Order>();
		for(Order o : itr) {
			orders.add(o);
		}
		return orders;
	}
	
	public List<Order> findSentOrders() {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class); 		
		BasicDBObject filter = new BasicDBObject("statusActual.status", "Sent"); 		
		System.out.print(filter);
		FindIterable<Order> itr = ordersCollection.find(filter);
		List<Order> orders = new ArrayList<Order>();
		for(Order o : itr) {
			orders.add(o);
//			System.out.println(o.getActualStatus());
//			System.out.println(o.getStatusActual());
		}
		return orders;
	}

	
	
	/* FIN DE METODOS FIND */


}
