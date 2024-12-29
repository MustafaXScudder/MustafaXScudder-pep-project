package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    private Connection conn = ConnectionUtil.getConnection();

    public Account saveAccount(Account account) {
        // Define the SQL query for inserting or updating the account
        String query = "INSERT INTO Accounts (username, password) " +
                "VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the values for the query parameters
            // Replace with appropriate getter for the account ID
            pstmt.setString(1, account.getUsername()); // Replace with appropriate getter for the account name
            pstmt.setString(2, account.getPassword()); // Replace with appropriate getter for the account balance

            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // if no rows are changed
            if (affectedRows == 0){
                // throw exception
                throw new SQLException("Creating account failed, no rows affected");
            }
            
            // otherwise, get account id
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setAccount_id(generatedKeys.getInt(1));
            }
            
            return account;

        } catch (SQLException e) {
            // TODO: log exception
            // Log the exception (use a logger in production code)
            System.err.println("Error saving account: " + e.getMessage());
            e.printStackTrace(); // Avoid using in production; use a logging framework instead
            return null;
        }
    }
}
