import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;

public class PizzaGUIFrame extends JFrame {
    private JTextArea orderDisplay;
    private JComboBox<String> sizeComboBox;
    private ArrayList<JCheckBox> toppingsBoxes;
    private ButtonGroup crustGroup;
    private final DecimalFormat df = new DecimalFormat("#.00");
    public PizzaGUIFrame() {
        createComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Pizza Order Form");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void createComponents() {
        setLayout(new BorderLayout());
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        crustGroup = new ButtonGroup();
        String[] crustTypes = {"Thin", "Regular", "Deep-dish"};
        for (String crust : crustTypes) {
            JRadioButton button = new JRadioButton(crust);
            crustGroup.add(button);
            crustPanel.add(button);
        }
        add(crustPanel, BorderLayout.NORTH);
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeComboBox = new JComboBox<>(sizes);
        sizePanel.add(sizeComboBox);
        add(sizePanel, BorderLayout.WEST);
        JPanel toppingPanel = new JPanel();
        toppingPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        String[] toppings = {"Pepperoni", "Mushrooms", "Onions", "Sausage", "Bacon", "Extra Cheese"};
        toppingsBoxes = new ArrayList<>();
        for (String topping : toppings) {
            JCheckBox checkBox = new JCheckBox(topping);
            toppingsBoxes.add(checkBox);
            toppingPanel.add(checkBox);
        }
        add(toppingPanel, BorderLayout.CENTER);
        orderDisplay = new JTextArea(20, 40);
        orderDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderDisplay);
        add(scrollPane, BorderLayout.EAST);
        JPanel buttonPanel = new JPanel();
        JButton orderButton = new JButton("Order");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);
        orderButton.addActionListener(this::processOrder);
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> confirmQuit());
    }
    private void processOrder(ActionEvent e) {
        StringBuilder receipt = new StringBuilder();
        double totalCost = 0.0;
        receipt.append("=========================================\n");
        JRadioButton selectedCrust = getSelectedRadioButton(crustGroup);
        if (selectedCrust != null) {
            receipt.append("Type of Crust: ").append(selectedCrust.getText()).append("\n");
        }
        String selectedSize = (String) sizeComboBox.getSelectedItem();
        if (selectedSize != null) {
            double[] sizeCosts = {8.00, 12.00, 16.00, 20.00};
            String[] sizes = {"Small", "Medium", "Large", "Super"};
            for (int i = 0; i < sizes.length; i++) {
                if (selectedSize.equals(sizes[i])) {
                    totalCost += sizeCosts[i];
                    receipt.append("Size (").append(selectedSize).append("): $").append(df.format(sizeCosts[i])).append("\n");
                    break;
                }
            }
        }
        receipt.append("Ingredients:\n");
        for (JCheckBox checkBox : toppingsBoxes) {
            if (checkBox.isSelected()) {
                totalCost += 1.00;
                receipt.append(" - ").append(checkBox.getText()).append(" $1.00\n");
            }
        }
        double tax = totalCost * 0.07;
        double finalTotal = totalCost + tax;
        receipt.append("----------------------------------------\n");
        receipt.append("Sub-total: $").append(df.format(totalCost)).append("\n");
        receipt.append("Tax (7%): $").append(df.format(tax)).append("\n");
        receipt.append("----------------------------------------\n");
        receipt.append("Total: $").append(df.format(finalTotal)).append("\n");
        receipt.append("=========================================\n");

        orderDisplay.setText(receipt.toString());
    }
    private JRadioButton getSelectedRadioButton(ButtonGroup group) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return (JRadioButton) button;
            }
        }
        return null;
    }
    private void clearForm() {
        crustGroup.clearSelection();
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox checkBox : toppingsBoxes) {
            checkBox.setSelected(false);
        }
        orderDisplay.setText("");
    }
    private void confirmQuit() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
