package Controller;

 import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;



/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method,
     * as the test suite must receive a Javalin object from this method.
     * 
     * return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Define routes and handlers
        app.get("example-endpoint", this::exampleHandler);

        // ADDED ENDPOINTS
        app.post("/register", this::createUser);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getMessages);
        app.get("/messages/<message_id>", this::getMessageById);
        app.delete("/messages/<message_id>", this::deleteMessageById);
        app.patch("/messages/<message_id>", this::updateMessage);
        app.get("/accounts/<account_id>/messages", this::getMessagesByUserId);
      
       
    
        return app;
    }
    
    


    // Handler to create a new user
    // As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register.
    // The body will contain a representation of a JSON Account, but will not contain an account_id.
    // - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. 
    //If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. 
    //The new account should be persisted to the database.
    // - If the registration is not successful, the response status should be 400. (Client error)
    private void createUser(Context context) {
        String postContent = context.body(); // Expecting raw text as the post content
        
        // Validate post content
        if (postContent == null || postContent.trim().isEmpty()) {
            context.status(400).result("Post content cannot be empty 1");
            return;
        }
        
        ObjectMapper mapper = new ObjectMapper();
       
        try{
        // serialize context body (JSON string) into Account object (POJO)
        
            Account account = mapper.readValue(postContent, Account.class);
            // additional validation

             // TODO: refactor into functions
             if(account.username == null || account.username.length() == 0){
                // context.status(400).result("Username cannot be blank");
                context.status(400);
                return;
            }
            if(account.password == null || account.password.length() < 4){
                // context.status(400).result("Password cannot be less than 4 characters");
                context.status(400);
                return;
            }
            if(accountExists(account.username)){
                // context.status(400).result("Account exists");
                context.status(400);
                return;
            
            
            // TODO: error handle if the account is null
        
        }


    Account createdAccount = accountService.createAccount(account);

        String accountAsString = mapper.writeValueAsString(createdAccount);
        context.status(200).json(accountAsString);

    } catch (Exception e) {
            
            System.err.println(e);
            context.status(500);
        
    }

    }

    private boolean accountExists(String username){
        // TODO: Query Account database to check if username exists
        return accountService.getAccountByUsername(username) != null;
    }

    private void login(Context context) {
        String postContent = context.body(); // Expecting raw text as the post content
        
        // Validate post content
        if (postContent == null || postContent.trim().isEmpty()) {
            context.status(400).result("Post content cannot be empty 1");

            return;
        }
        
        ObjectMapper mapper = new ObjectMapper();
        // serialize context body (JSON string) into Account object (POJO)
        try {
            Account account = mapper.readValue(postContent, Account.class);
                      
            // additional validation
            if(account.username == null || account.username.length() == 0){
                // TODO: update to message to say Post content is invalid
                context.status(400).result("Username cannot be blank");
                return;
            }
            if(account.password == null || account.password.length() < 4){
                context.status(400).result("Password cannot be less than 4 characters");
                return;
            }
            Account returnedAccount = accountService.login(account);
            if (returnedAccount == null) {
                // context.status(401).result("Unauthorized");
                context.status(401);
                return;
            }
            String accountAsString = mapper.writeValueAsString(returnedAccount);
            context.status(200).json(accountAsString);

        }catch (Exception e) {
            // context.status(500).result("Error logging in");
            context.status(401);
        }
    }
    private void createMessage(Context context) {
        String postContent = context.body(); // Expecting raw text as the post content
        
        // Validate post content
        if (postContent == null || postContent.trim().isEmpty()) {
            context.status(400).result("Post content cannot be empty 1");
            return;
        }
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            // serialize context body (JSON string) into Message object (POJO)
            Message message = mapper.readValue(postContent, Message.class);
            
            // additional validation
            // TODO: refactor into functions
            if(message.message_text == null || message.message_text.isBlank() || message.message_text.length() > 255){
                // context.status(400).result("Username cannot be blank");
                context.status(400);
                return;
            }
            if(accountService.getAccountById(message.posted_by) == null){
                // context.status(400).result("Account does not exist");
                context.status(400);
                return;
            }
            Message createdMessage = messageService.createMessage(message);
            String messageAsString = mapper.writeValueAsString(createdMessage);


       // Return the post in JSON format with the new ID
            context.status(200).json(messageAsString);
        } catch (JsonProcessingException e) {
            System.err.println("Error reading content: " + e);
            context.status(500);
        }
    }
    private void getMessages(Context context) {

        List<Message> messages = messageService.getMessages();

        ObjectMapper mapper = new ObjectMapper();

        try {
            String messagesAsString = mapper.writeValueAsString(messages);
            context.status(200).json(messagesAsString);
        } catch (Exception e) {
            context.status(400);
        }
}

private void getMessageById(Context context) {
    try {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message != null) {
            ObjectMapper mapper = new ObjectMapper();
            String messageAsString = mapper.writeValueAsString(message);
            context.json(messageAsString);
        }
        context.status(200);
    } catch (Exception e) {
        context.status(400);
    }
}
private void deleteMessageById(Context context) {
    ObjectMapper mapper = new ObjectMapper();
    try {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        // get message by id to return in response if successful
        Message message = messageService.getMessageById(messageId);
        // delete message
        boolean success = messageService.deleteMessageById(messageId);
        if (success) {
            context.json(mapper.writeValueAsString(message));
        }
    } catch (Exception e) {
        System.err.println("Error deleting message: " + e.getMessage());
    }
    context.status(200);
}
private void updateMessage(Context context){
    String postContent = context.body(); // Expecting raw text as the post content
    String messageId = context.pathParam("message_id");
    // Validate post content
    if (postContent == null || postContent.trim().isEmpty()) {
        context.status(400).result("Post content cannot be empty 1");
        return;
    }
    
    ObjectMapper mapper = new ObjectMapper();
    try {
        // serialize context body (JSON string) into Message object (POJO)
        Message message = mapper.readValue(postContent, Message.class);
        
        // additional validation
        if (message.message_text == null || message.message_text.isBlank() || message.message_text.length() > 255) {
            context.status(400);
            return;
        }
        int id = Integer.parseInt(messageId);
        // Check if the message exists
        if(messageService.getMessageById(id) == null) {
            context.status(400);
            return;
        }
        
        Message updatedMessage = messageService.updateMessage(id, message);
        // No message was updated
        if (updatedMessage == null) {
            context.status(400);
            return;
        }
        String messageAsString = mapper.writeValueAsString(updatedMessage);
        // Return the post in JSON format with the new ID
        context.status(200).json(messageAsString);
    } catch (JsonProcessingException e) {
        System.err.println("Error reading content: " + e);
        context.status(400);
    }
}
private void getMessagesByUserId(Context context){
    try {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        ObjectMapper mapper = new ObjectMapper();
        String messagesAsString = mapper.writeValueAsString(messages);
        context.status(200).json(messagesAsString);
    } catch (Exception e) {
        context.status(400);
    }
}





    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text"); // Example response for debugging or testing
    }
}