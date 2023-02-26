package tdtu.edu.vn;

public class Product {
	private int id;
    private String name;
    private double price;

    public Product(int id, String name, float price) {
    	this.id = id;
        this.name = name;
        this.price = price;
    }
    
    public int getId() {
    	return id;
    }
    
    public String getName() {
        return name;
    }

    public float getPrice() {
        return (float) price;
    }
}
