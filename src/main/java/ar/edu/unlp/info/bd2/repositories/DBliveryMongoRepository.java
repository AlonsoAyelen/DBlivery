package ar.edu.unlp.info.bd2.repositories;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.mongo.*;
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

	public Product createProduct(Product p) {
		MongoCollection<Product> productCollection = this.getDb().getCollection("products",Product.class);
		productCollection.insertOne(p);
		return p;
	}

	public Order createOrder(Order o) {
		MongoCollection<Order> orderCollection = this.getDb().getCollection("orders",Order.class);
		orderCollection.insertOne(o);
	    return o;
	}
	
	/* FIN DE METODOS CREATE */



	/* COMIENZO DE METODOS FIND */
	
	public List<Product> findProductsByName(String name) {
		Bson filter = regex("name", name);
		MongoCollection<Product> productCollection = this.getDb().getCollection("products",Product.class);
		List<Product> products = new ArrayList<>();
		FindIterable<Product> itr =productCollection.find(filter);
		for (Product p : itr) {
			products.add(p);
		}
		return products;
	}

	public Optional<User> findUserById(ObjectId id) {
		MongoCollection<User> usersCollection = this.getDb().getCollection("users", User.class);
		User u = usersCollection.find(eq("objectId", id)).first();
		Optional<User> ou = Optional.ofNullable(u);
		return ou;
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

	public Optional<Product> findProductById(ObjectId id) {
		MongoCollection<Product> productsCollection = this.getDb().getCollection("products", Product.class);
//		Product p = productsCollection.find(eq("objectId", id)).first();
		Product p = productsCollection.find(eq("_id", id)).first();
		Optional<Product> op = Optional.ofNullable(p);
		return op;
	}

	public Optional<Order> findOrderById(ObjectId order) {
		MongoCollection<Order> ordersCollection = this.getDb().getCollection("orders", Order.class);
		Order o = ordersCollection.find(eq("_id", order)).first();
		List<Row> lr= this.getAssociatedObjects(o, Row.class, "order_rows", "rows");
		o.setProducts(lr);
		Optional<Order> oo = Optional.ofNullable(o);
		return oo;
	}

	/* FIN DE METODOS FIND */


}
