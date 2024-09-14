import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.io.Serializable;

public class OrderQueue implements Serializable{
	private LinkedList<Order> order_queue = new LinkedList<>();
	
	public void addOrder(Order order) {
		order_queue.add(order);
	}
	
	public List<Order> getOrders(){
		return new LinkedList<>(order_queue);
	}
	
	public void removeOrderByID(int id) {
		Optional<Order> orderRemoval = order_queue.stream()
												  .filter(order -> order.getId() == id)
												  .findFirst();
		orderRemoval.ifPresent(order_queue::remove);
	}
}
