package com.expensetracker.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Expense {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty category = new SimpleStringProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final StringProperty description = new SimpleStringProperty("");

    // Default constructor
    public Expense() {
    }

    // Full constructor
    public Expense(int id, LocalDate date, String category, double amount, String description) {
        setId(id);
        setDate(date);
        setCategory(category);
        setAmount(amount);
        setDescription(description);
    }

    // Property methods - Important for TableView binding
    public IntegerProperty idProperty() { return id; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty categoryProperty() { return category; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty descriptionProperty() { return description; }

    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate date) { this.date.set(date); }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }

    public double getAmount() { return amount.get(); }
    public void setAmount(double amount) { this.amount.set(amount); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
}