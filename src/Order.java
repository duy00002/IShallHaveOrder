import java.io.Serializable;
import java.util.*;

public class Order implements Serializable{
	
	private int id;
	private String name;
	private String address;
	private String[] books;
	private Integer[] quantity;
	
	
	public Order (int id, String name, String address, String[] books, Integer[] quantity) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.books = books;
		this.quantity = quantity;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public String[] getBooks() {
		return books;
	}
	public Integer[] getQuantity() {
		return quantity;
	}
	public String getBooksAsString() {
		return Arrays.toString(books);
	}
	public String getQuantityAsString() {
		return Arrays.toString(quantity);
	}
	public String getBooksAndQuantities() {
		List<String> bookL = Arrays.asList(books);
		List<Integer> quantityL = Arrays.asList(quantity);
		String bookNqua = "";
		for(int i=0; i<bookL.size(); i++) {
			String book = bookL.get(i);
			String quantity = String.valueOf(quantityL.get(i));
			String bNq = book + "-" + quantity;
			bookNqua += bNq;
			if (i < bookL.size() - 1) {
				bookNqua += "; ";
			}
		}
		return bookNqua;
	}
}
