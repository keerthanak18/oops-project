import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class BillCalculatorApp {
    public static void main(String[] args) {
        BillGUI billGUI = new BillGUI();
        billGUI.showGUI();
    }
}

// Utility class for managing individual services like electricity and water
class Utility {
    private String name;
    private double unitCost;
    private double usage;

    // Constructor to initialize name, unit cost, and usage
    public Utility(String name, double unitCost, double usage) {
        this.name = name;
        this.unitCost = unitCost;
        this.usage = usage;
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getUsage() {
        return usage;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }

    // Method to calculate cost
    public double calculateCost() {
        return unitCost * usage;
    }

    // Overloaded method to apply a discount
    public double calculateCost(double discount) {
        return calculateCost() * (1 - discount);
    }
}

// Interface for Bill Calculation (For future extensibility)
interface BillCalculator {
    double getTotalBill(List<Utility> utilities);
}

// Implementation class for BillCalculator (Calculates the total bill)
class SimpleBillCalculator implements BillCalculator {
    @Override
    public double getTotalBill(List<Utility> utilities) {
        double total = 0;
        for (Utility utility : utilities) {
            total += utility.calculateCost();
        }
        return total;
    }
}

// GUI class for handling user input and displaying the results
class BillGUI {
    private List<Utility> utilities = new ArrayList<>();
    private JTextArea outputArea;

    public void showGUI() {
        // JFrame setup for the main window
        JFrame frame = new JFrame("Bill Calculator (Electricity and Water)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

        // Panel to organize input fields and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Input fields for both electricity and water usage
        JTextField nameField = new JTextField(15);
        JTextField unitCostField = new JTextField(15);
        JTextField usageField = new JTextField(15);
        JTextField discountField = new JTextField(15); // Discount field

        // Add buttons for adding utilities and calculating the total bill
        JButton addButton = new JButton("Add Utility");
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                double unitCost = Double.parseDouble(unitCostField.getText());
                double usage = Double.parseDouble(usageField.getText());
                double discount = Double.parseDouble(discountField.getText());

                // Add the new utility (electricity or water) to the list
                utilities.add(new Utility(name, unitCost, usage));
                nameField.setText(""); // Reset fields
                unitCostField.setText("");
                usageField.setText("");
                discountField.setText(""); // Reset discount field
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
            }
        });

        JButton calculateButton = new JButton("Calculate Total");
        calculateButton.addActionListener(e -> {
            BillCalculator calculator = new SimpleBillCalculator();
            double total = calculator.getTotalBill(utilities);

            // Display each utility's cost and total amount
            StringBuilder billDetails = new StringBuilder();
            for (Utility utility : utilities) {
                billDetails.append(utility.getName() + ": د.إ " + String.format("%.2f", utility.calculateCost()) + "\n");
            }
            billDetails.append("\nTotal Bill: د.إ " + String.format("%.2f", total));
            outputArea.setText(billDetails.toString());
        });

        // Adding labels and input fields to the panel
        panel.add(new JLabel("Utility Name (Electricity/Water):"));
        panel.add(nameField);
        panel.add(new JLabel("Unit Cost (in AED):"));
        panel.add(unitCostField);
        panel.add(new JLabel("Usage:"));
        panel.add(usageField);
        panel.add(new JLabel("Discount (as decimal, e.g., 0.1 for 10%):"));
        panel.add(discountField);

        panel.add(addButton);
        panel.add(calculateButton);

        // Output area to display the total bill
        outputArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Adding components to the JFrame
        frame.getContentPane().add(panel, "North");
        frame.getContentPane().add(scrollPane, "South");

        // Make the frame visible
        frame.setVisible(true);
    }
}
