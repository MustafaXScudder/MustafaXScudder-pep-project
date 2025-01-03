package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    private Connection conn = ConnectionUtil.getConnection();

   
    public Account createAccount(Account account) {
        // Define the SQL query for inserting or updating the account
        
        String query = "INSERT INTO Account (username, password) " +
                "VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Set the values for the query parameters
            // Replace with appropriate getter for the account ID
            pstmt.setString(1, account.getUsername()); // Replace with appropriate getter for the account name
            pstmt.setString(2, account.getPassword()); // Replace with appropriate getter for the account balance

            // Print out the query for debugging purposes
            System.out.println("Executing Query: " + pstmt.toString());
            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // if no rows are changed
            if (affectedRows == 0){
                // throw exception
                throw new SQLException("Creating account failed, no rows affected");
            }

            // otherwise, get account id
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            System.out.println(generatedKeys.toString());
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
    public Account getAccountByUsername(String username){
        // Define the SQL query for retrieving the account by username
        String query = "SELECT * FROM Account WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the values for the query parameters
            pstmt.setString(1, username);
            // Execute the query
            ResultSet rs = pstmt.executeQuery();
            // If the result set is empty, return null
            if (!rs.next()) {
                return null;
            }
            Account account = new Account();
            account.setAccount_id(rs.getInt("account_id"));
            account.setUsername(rs.getString("username"));            
            account.setPassword(rs.getString("password"));            
            return account;
        } catch (SQLException e) {
            System.err.println("Error retrieving account: " + e.getMessage());
            return null;
        }
    }
    public Account getAccountById(int id) {
        // Define the SQL query for retrieving the account by ID
        String query = "SELECT * FROM Account WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the values for the query parameters
            pstmt.setInt(1, id);
            // Execute the query
            ResultSet rs = pstmt.executeQuery();
            // If the result set is empty, return null
            if (!rs.next()) {
                return null;
            }
            Account account = new Account();
            account.setAccount_id(rs.getInt("account_id"));
            account.setUsername(rs.getString("username"));            
            account.setPassword(rs.getString("password"));            
            return account;
        } catch (SQLException e) {
            System.err.println("Error retrieving account: " + e.getMessage());
            return null;
        }
    }
    public Account createAccountAccount(Account account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccountAccount'");
    }
}
