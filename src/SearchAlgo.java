import java.util.List;
import java.util.stream.Collectors;

public class SearchAlgo {
	public static List<Order> linearSearch(List<Order> orders, 
	String idText, String nameText, String addressText, 
	String booksText, String quantityText) {
		return orders.stream().filter(order -> 
        ((!idText.isEmpty() && String.valueOf(order.getId()).equals(idText))) ||
        ((!nameText.isEmpty() && order.getName().contains(nameText))) ||
        ((!addressText.isEmpty() && order.getAddress().contains(addressText))) ||
        ((!booksText.isEmpty() && order.getBooksAsString().contains(booksText))) ||
        ((!quantityText.isEmpty() && order.getQuantityAsString().contains(quantityText)))
    ).collect(Collectors.toList());
   }
}

