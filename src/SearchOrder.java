import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class SearchOrder {
    private JPanel searchOrderPanel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField bookField;
    private JTextField quantityField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    
    public SearchOrder(OrderQueue orderQueue, DefaultTableModel mainTableModel, CardLayout cardLayout, JPanel mainPanel) {
        searchOrderPanel = new JPanel(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel("Search Order", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));

        // Create form panel using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.decode("#dfdfdf"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create text fields
        idField = new JTextField(15);
        nameField = new JTextField(15);
        addressField = new JTextField(15);
        bookField = new JTextField(15);
        quantityField = new JTextField(15);
        
        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Book:"), gbc);
        gbc.gridx = 1;
        formPanel.add(bookField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);
        
     // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton searchOrderButton = new JButton("Search Order");
        JButton backButton = new JButton("BACK");
        backButton.setForeground(Color.RED);
        
        buttonsPanel.add(searchOrderButton);
        buttonsPanel.add(backButton);

        // Add components to the search order panel
        searchOrderPanel.add(titleLabel, BorderLayout.NORTH);
        searchOrderPanel.add(formPanel, BorderLayout.WEST);
        searchOrderPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Create table for displaying search results
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Address");
        tableModel.addColumn("Book and Quantities");
        
        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(resultTable);
        searchOrderPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Back button action
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        // Search order button action
        searchOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrders(orderQueue);
            }
        });
    }
    
    public JPanel getPanel() {
        return searchOrderPanel;
    }
    
    private void searchOrders(OrderQueue orderQueue) {
        String idText = idField.getText().trim();
        String nameText = nameField.getText().trim();
        String addressText = addressField.getText().trim();
        String bookText = bookField.getText().trim();
        String quantityText = quantityField.getText().trim();
        
        List<Order> orders = orderQueue.getOrders();
        
        List<Order> matchingOrders = SearchAlgo.linearSearch(orders, idText, nameText, 
        										addressText, bookText, quantityText);

        // Clear the table
        tableModel.setRowCount(0);

        // Populate table with matching orders
        for (Order order : matchingOrders) {
            tableModel.addRow(new Object[]{
                order.getId(),
                order.getName(),
                order.getAddress(),
                order.getBooksAndQuantities()
            });
        }
    }
}
