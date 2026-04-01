package com.expensetracker.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class ExpenseController implements Initializable {

    private final ExpenseService expenseService = new ExpenseService();

    // FXML Components (we will link them in dashboard.fxml)
    @FXML private DatePicker datePicker;
    @FXML private TextField categoryField;
    @FXML private TextField amountField;
    @FXML private TextArea descriptionArea;
    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, Integer> idColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, Double> amountColumn;
    @FXML private TableColumn<Expense, String> descriptionColumn;
    @FXML private PieChart expensePieChart;
    @FXML private Label totalLabel;
    @FXML private Label monthlyTotalLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        refreshData();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Make table editable
        expenseTable.setEditable(true);
    }

    @FXML
    private void handleAddExpense() {
        try {
            if (datePicker.getValue() == null || categoryField.getText().trim().isEmpty() || amountField.getText().trim().isEmpty()) {
                showAlert("Input Error", "Please fill all required fields (Date, Category, Amount)", Alert.AlertType.WARNING);
                return;
            }

            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                showAlert("Input Error", "Amount must be greater than 0", Alert.AlertType.WARNING);
                return;
            }

            Expense newExpense = new Expense(
                    0, // id will be auto-generated
                    datePicker.getValue(),
                    categoryField.getText().trim(),
                    amount,
                    descriptionArea.getText().trim()
            );

            boolean success = expenseService.addExpense(newExpense);

            if (success) {
                clearForm();
                refreshData();
                showAlert("Success", "Expense added successfully!", Alert.AlertType.INFORMATION);
            }

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid amount", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add expense: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteExpense() {
        Expense selected = expenseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select an expense to delete", Alert.AlertType.WARNING);
            return;
        }

        expenseService.deleteExpense(selected.getId());
        refreshData();
        showAlert("Success", "Expense deleted successfully!", Alert.AlertType.INFORMATION);
    }

    private void refreshData() {
        ObservableList<Expense> expenses = expenseService.getAllExpenses();
        expenseTable.setItems(expenses);

        // Update total
        double total = expenseService.calculateTotalExpense();
        totalLabel.setText("Total Expenses: ₹ " + String.format("%.2f", total));

        // Update monthly total
        double monthly = expenseService.getCurrentMonthTotal();
        monthlyTotalLabel.setText("This Month: ₹ " + String.format("%.2f", monthly));

        // Update Pie Chart
        updatePieChart();
    }

    private void updatePieChart() {
        expensePieChart.getData().clear();
        Map<String, Double> categoryData = expenseService.getExpensesByCategory();

        for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            expensePieChart.getData().add(slice);
        }
    }

    private void clearForm() {
        datePicker.setValue(LocalDate.now());
        categoryField.clear();
        amountField.clear();
        descriptionArea.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // This method will be called from FXML for Refresh button (optional)
    @FXML
    private void handleRefresh() {
        refreshData();
    }
}