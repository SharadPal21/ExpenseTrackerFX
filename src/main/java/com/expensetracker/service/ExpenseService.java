package com.expensetracker.service;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.model.Expense;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ExpenseService {

    private final ExpenseDAO expenseDAO;

    public ExpenseService() {
        this.expenseDAO = new ExpenseDAO();
    }

    /**
     * Add new expense with validation
     */
    public boolean addExpense(Expense expense) {
        // Basic validation
        if (expense.getDate() == null) {
            System.err.println("❌ Error: Date is required");
            return false;
        }
        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty()) {
            System.err.println("❌ Error: Category is required");
            return false;
        }
        if (expense.getAmount() <= 0) {
            System.err.println("❌ Error: Amount must be greater than 0");
            return false;
        }

        expenseDAO.addExpense(expense);
        return true;
    }

    /**
     * Get all expenses
     */
    public ObservableList<Expense> getAllExpenses() {
        return expenseDAO.getAllExpenses();
    }

    /**
     * Delete expense
     */
    public void deleteExpense(int id) {
        expenseDAO.deleteExpense(id);
    }

    /**
     * Update expense
     */
    public boolean updateExpense(Expense expense) {
        if (expense.getId() <= 0) {
            System.err.println("❌ Error: Invalid expense ID");
            return false;
        }
        expenseDAO.updateExpense(expense);
        return true;
    }

    /**
     * Calculate total amount of all expenses
     */
    public double calculateTotalExpense() {
        return getAllExpenses().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Get expenses grouped by category for PieChart
     */
    public Map<String, Double> getExpensesByCategory() {
        Map<String, Double> categoryMap = new HashMap<>();

        for (Expense expense : getAllExpenses()) {
            categoryMap.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }

        return categoryMap;
    }

    /**
     * Get total expense for current month
     */
    public double getCurrentMonthTotal() {
        LocalDate now = LocalDate.now();
        return getAllExpenses().stream()
                .filter(e -> e.getDate().getMonth() == now.getMonth() &&
                        e.getDate().getYear() == now.getYear())
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}