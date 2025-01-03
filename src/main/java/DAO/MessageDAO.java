package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    private Connection conn = ConnectionUtil.getConnection();

    public Message createMessage(Message message) {

        // TODO: Update the SQL query for inserting or updating the account
        String query = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) " +
                "VALUES (?, ?, ?)";


        try (PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Set the values for the query parameters
            pstmt.setInt(1, message.getPosted_by()); 
            pstmt.setString(2, message.getMessage_text()); 
            pstmt.setLong(3, message.getTime_posted_epoch()); 

            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // if no rows are changed
            if (affectedRows == 0){
                
                // throw new SQLException("Creating message failed, no rows affected");
                return null;
            }
            
            // otherwise, get message id
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                message.setMessage_id(generatedKeys.getInt(1));
            }
            
            return message;
        } catch (SQLException e) {
            System.err.println("Error saving message: " + e.getMessage());
            return null;
        }
    }
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM Message";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            // Add each row in the ResultSet to the messages list
            // Each row represents a message
            while (rs.next()) {
                Message m = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );
                messages.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
        return messages;
    }
    public Message getMessageById(int id) {
        String query = "SELECT * FROM Message WHERE message_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );
        } catch (SQLException e) {
            System.err.println("Error retrieving message: " + e.getMessage());
            return null;
        }
    }
    public boolean deleteMessageById(int id) {
        String query = "DELETE FROM Message WHERE message_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting message: " + e.getMessage());
            return false;
        }
    }
    public List<Message> getMessagesByUserId(int id) {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM Message WHERE posted_by = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            // Add each row in the ResultSet to the messages list
            // Each row represents a message
            while (rs.next()) {
                Message m = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );
                messages.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
        return messages;
    }
    public Message updateMessage(int id, Message message) {
        String query = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, message.getMessage_text());
            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            return getMessageById(id);
        } catch (SQLException e) {
            System.err.println("Error updating message: " + e.getMessage());
            return null;
        }
    }
}