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
    
        // Delegate to DAO
        return messageDAO.createMessage(message);
    }

    public List<Message> getMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public boolean deleteMessageById(int id) {
    
        return messageDAO.deleteMessageById(id);
    }

    public List<Message> getMessagesByUserId(int id) {

        return messageDAO.getMessagesByUserId(id);
    }
    public Message updateMessage(int id, Message message) {
        
        return messageDAO.updateMessage(id, message);
    }

  
    // public Message getMessageById(int id) {
    //     Message message = messageDAO.getMessageById(id);
    //     if (message == null) {
    //         throw new IllegalArgumentException("Message with ID " + id + " not found.");
    //     }
    //     return message;
    // }
     //public boolean deleteMessageById(int id) {
        //if (!messageDAO.deleteMessageById(id)) {
        //    throw new IllegalArgumentException("Failed to delete message with ID " + id);
       // }
        //return true;
    }
