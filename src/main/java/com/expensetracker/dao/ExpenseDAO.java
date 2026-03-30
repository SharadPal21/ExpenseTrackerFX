package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import com.expensetracker.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class ExpenseDAO {

    /**
     * Add a new expense to the database
     */
    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (date, category, amount, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, expense.getDate().toString());
            pstmt.setString(2, expense.getCategory());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getDescription());

            pstmt.executeUpdate();
            System.out.println("✅ Expense added successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error adding expense:");
            e.printStackTrace();
        }
    }

    /**
     * Get all expenses from database as ObservableList (for TableView)
     */
    public ObservableList<Expense> getAllExpenses() {
        ObservableList<Expense> expenses = FXCollections.observableArrayList();
        String sql = "SELECT * FROM expenses ORDER BY date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("id"),
                        LocalDate.parse(rs.getString("date")),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("description")
                );
                expenses.add(expense);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching expenses:");
            e.printStackTrace();
        }

        return expenses;
    }

    /**
     * Delete an expense by ID
     */
    public void deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Expense deleted successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error deleting expense:");
            e.printStackTrace();
        }
    }

    /**
     * Update an existing expense
     */
    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET date = ?, category = ?, amount = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, expense.getDate().toString());
            pstmt.setString(2, expense.getCategory());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getDescription());
            pstmt.setInt(5, expense.getId());

            pstmt.executeUpdate();
            System.out.println("✅ Expense updated successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error updating expense:");
            e.printStackTrace();
        }
    }
}