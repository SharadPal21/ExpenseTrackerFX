package com.expensetracker;

import com.expensetracker.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExpenseTrackerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database on startup
        DatabaseUtil.initializeDatabase();

        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));

        Scene scene = new Scene(loader.load());

        // Add CSS styling
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setTitle("Expense Tracker - Personal Finance Manager");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
