package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        // Validate message text length
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        // Delegate to DAO
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        Message message = messageDAO.getMessageById(id);
        if (message == null) {
            throw new IllegalArgumentException("Message with ID " + id + " not found.");
        }
        return message;
    }

    public boolean deleteMessageById(int id) {
        if (!messageDAO.deleteMessageById(id)) {
            throw new IllegalArgumentException("Failed to delete message with ID " + id);
        }
        return true;
    }
}
