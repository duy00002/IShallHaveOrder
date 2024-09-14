import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class OrderApp {
	private static OrderQueue queue = new OrderQueue();
	private static DefaultTableModel tableModel;
	
	public static void main(String[] args) {
		loadOrders();
		//frame
		JFrame frame = new JFrame("Chimps Knowledge");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(720, 640);
		
		CardLayout layout = new CardLayout();
		JPanel main = new JPanel(layout);
		
		JPanel home = createHome(layout, main);
		home.setBackground(Color.decode("#ffffff"));
		
		JPanel placeOrder = createPlaceOrder(layout, main);
		placeOrder.setBackground(Color.decode("#ffffff"));
		
        JPanel allOrders = createAllOrders(layout, main);
        allOrders.setBackground(Color.decode("#ffffff"));
        
		JPanel searchOrder = createSearchOrder(layout, main);
		searchOrder.setBackground(Color.decode("#ffffff"));
		
		main.add(home, "Home");
		main.add(placeOrder, "PlaceOrder");
        main.add(allOrders, "AllOrders");
		main.add(searchOrder, "SearchOrder");
        
		frame.getContentPane().add(main);
		
		frame.setVisible(true);
		layout.show(main, "Home");
		
		Runtime.getRuntime().addShutdownHook(new Thread(OrderApp::saveOrders));
	}
	
	private static JPanel createHome(CardLayout layout, JPanel main) {
		//home page
		JPanel home = new JPanel(new BorderLayout());
		
		//title
		JLabel title = new JLabel("Chimps Knowledge Main Shed", SwingConstants.CENTER);
		title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		
		//button panel
		JPanel btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 44, 12));
		btn.setBackground(Color.decode("#d4d4d4"));
		
		//buttons
        JButton placeOrderBtn = new JButton("Place Order");
        JButton allOrdersBtn = new JButton("All Orders");
        JButton searchOrderBtn = new JButton("Search Order");
		
        //add btns to btn panel
        btn.add(placeOrderBtn);
        btn.add(allOrdersBtn);
        btn.add(searchOrderBtn);
        
        //add to home page
        home.add(title, BorderLayout.NORTH);
        home.add(btn, BorderLayout.CENTER);
        
        placeOrderBtn.addActionListener(e -> layout.show(main, "PlaceOrder"));
        
        allOrdersBtn.addActionListener(e -> layout.show(main, "AllOrders"));
        
        searchOrderBtn.addActionListener(e -> layout.show(main, "SearchOrder"));
        
		return home;
	}
	
	private static JPanel createPlaceOrder (CardLayout layout, JPanel main) {
		//place order panel
		JPanel placeOrder = new JPanel(new BorderLayout());
		
		//title
		JLabel title = new JLabel("Chimps Knowledge Place Order", SwingConstants.CENTER);
		title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		
		//form panel with GridBagLayout
		JPanel form = new JPanel (new GridBagLayout());
		form.setBackground(Color.decode("#d4d4d4"));
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4,4,4,4);
		constraints.anchor = GridBagConstraints.CENTER;
		
		//text fields
		JTextField nameF = new JTextField(16);
		JTextField addressF = new JTextField(16);
		JTextField booksAndQuantitiesF = new JTextField(16);
		
		JLabel errorMsg = new JLabel("");
		errorMsg.setForeground(Color.RED);
		
		//add components
		constraints.gridx = 1;
		constraints.gridy = 0;
		form.add(title, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		form.add(new JLabel("Name"), constraints);
		constraints.gridx = 1;
		form.add(nameF, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		form.add(new JLabel("Address"), constraints);
		constraints.gridx = 1;
		form.add(addressF, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		form.add(new JLabel("Books & Quantities"), constraints);
		constraints.gridx = 1;
		form.add(booksAndQuantitiesF, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 4;
		form.add(errorMsg, constraints);
		
		//buttons panel
		JPanel btns= new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btns.setBackground(Color.decode("#d4d4d4"));
		
		JButton placeOrderBtn = new JButton("Place Order");
		JButton backBtn = new JButton("Back To Menu");
		backBtn.setForeground(Color.RED);
		
		btns.add(placeOrderBtn);
		btns.add(backBtn);

		
		//add components to place order
		placeOrder.add(form, BorderLayout.CENTER);
		placeOrder.add(btns, BorderLayout.SOUTH);
		
        //back action
        backBtn.addActionListener(e -> {
            errorMsg.setText("");
            layout.show(main, "Home");
        });
        
        placeOrderBtn.addActionListener(e -> {
        	boolean name_empty = false;
        	boolean address_empty = false;    
        	
        	int id = getLatestId();
        	String name = nameF.getText();
        	String address = addressF.getText();
        	String booksAndQuantities = booksAndQuantitiesF.getText();
        	
        	if (name.trim().isEmpty() || name == null) {name_empty = true;}
        	if (address.trim().isEmpty() || address == null) {address_empty = true;}
        	
        	if(isValidBooksAndQuantities(booksAndQuantities) && !name_empty && !address_empty) {
        		// Split each book and quantity
        		String[] booksAndQuantitiesArr = booksAndQuantities.split("\\s*;\\s*");
        		booksAndQuantitiesArr = SortAlgo.insertionSort(booksAndQuantitiesArr);
        		
        		//Split book and quantity
        		String[] booksArr = {};
        		Integer[] quantitiesArr = {};
        		List<String> booksL = new ArrayList<String>(Arrays.asList(booksArr));
        		List<Integer> quantitiesL = new ArrayList<Integer>(Arrays.asList(quantitiesArr));
        		for (String bookAndQuantity:booksAndQuantitiesArr) {
            		String[] bookAndQuantitiyArr = bookAndQuantity.split("\\s*-\\s*");
            		booksL.add(bookAndQuantitiyArr[0]);
            		quantitiesL.add(Integer.valueOf(bookAndQuantitiyArr[1]));
        		}
        		booksArr = booksL.toArray(booksArr);
        		quantitiesArr = quantitiesL.toArray(quantitiesArr);
        		
        		Order order = new Order(id, name, address, booksArr, quantitiesArr);
        		queue.addOrder(order);      		
        		JOptionPane.showMessageDialog(placeOrder, "Order Placed Successfully!");
        		
                // update table model
                tableModel.addRow(new Object[]{
                        order.getId(),
                        order.getName(),
                        order.getAddress(),
                        order.getBooksAndQuantities(),
                        "Delete"
                });
        		nameF.setText("");
        		addressF.setText("");
        		booksAndQuantitiesF.setText("");
        		errorMsg.setText("");
        	} else {
        		if (name_empty) {errorMsg.setText("Please insert your name");} 
        		else if(address_empty) {errorMsg.setText("Please insert your address");} 
        		else {errorMsg.setText("Please follow the format \"[Name]-[Number];[Name]-[Number]\"");}
        	}
        });
        
        return placeOrder;
	}
	
	private static JPanel createAllOrders (CardLayout layout, JPanel main) {
		//all orders panel
		JPanel allOrders = new JPanel(new BorderLayout());
		
		//title label
		JLabel title = new JLabel("Chimps Knowledge All Order", SwingConstants.CENTER);
		title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4;
			}
		};
		JTable table = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Address");
        tableModel.addColumn("Book and Quantities");
        tableModel.addColumn("Actions");
		
        // get orders to add to table model
        queue.getOrders().forEach(order ->{
        	tableModel.addRow(new Object[] {
        			order.getId(),
        			order.getName(),
        			order.getAddress(),
        			order.getBooksAndQuantities(),
        			"Delete"
        	});
        });
        
        // delete button function
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // scroll pane
        JScrollPane scroll = new JScrollPane(table);
        
        // add components 
        allOrders.add(title, BorderLayout.NORTH);
        allOrders.add(scroll, BorderLayout.CENTER);
        
        JButton backBtn = new JButton("BACK");
        backBtn.setForeground(Color.RED);
        allOrders.add(backBtn, BorderLayout.SOUTH);
        
        // back button 
        backBtn.addActionListener(e -> layout.show(main, "Home"));

        return allOrders;
	}
	
    private static JPanel createSearchOrder (CardLayout layout, JPanel main) {
        return new SearchOrder(queue, tableModel, layout, main).getPanel();
    }
	
	private static void saveOrders() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("order_save.dat"))){
			oos.writeObject(new ArrayList<>(queue.getOrders()));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void loadOrders() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("order_save.dat"))){
			List<Order> orders = (List<Order>)ois.readObject();
			orders.forEach(queue::addOrder);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static int getLatestId() {
		int assigned_id = 0;
		List<Order> q = queue.getOrders();
		List<Integer> id_arr = new ArrayList<Integer>();
		if (q.isEmpty()) {
			System.out.println("Lmao");
			return 1;
		}
		for (Order o: q) {
			id_arr.add(o.getId());
		}
		Collections.sort(id_arr);
		assigned_id = id_arr.get(id_arr.size() - 1).intValue() + 1;
		return assigned_id;
	}
	
	private static boolean isValidBooksAndQuantities(String input) {
        String regex = "^([a-zA-Z0-9 ]+-\\d+)(;[a-zA-Z0-9 ]+-\\d+)*$";
        return Pattern.matches(regex, input);
	}
	
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Delete" : value.toString());
            return this;
        }
    }
    
    static class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "Delete" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(e -> {
                if (isPushed) {
                    int modelRow = table.convertRowIndexToModel(row);
                    int orderId = (int) tableModel.getValueAt(modelRow, 0);
                    queue.removeOrderByID(orderId);
                    tableModel.removeRow(modelRow);
                }
                fireEditingStopped();
            });
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }
    }
}
