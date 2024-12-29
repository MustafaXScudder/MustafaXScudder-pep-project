package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
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

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method,
     * as the test suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Define routes and handlers
        app.get("example-endpoint", this::exampleHandler);

        // ADDED ENDPOINTS
        app.post("/register", this::createUser);

        return app;
    }


    // Handler to create a new user
    // As a user, I should be able to create a new AccPount on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an account_id.
    // - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
    // - If the registration is not successful, the response status should be 400. (Client error)
    private void createUser(Context context) {
        String postContent = context.body(); // Expecting raw text as the post content
        
        // Validate post content
        if (postContent == null || postContent.trim().isEmpty()) {
            context.status(400).result("Post content cannot be empty");
            return;
        }
        
        ObjectMapper mapper = new ObjectMapper();
        // serialize context body (JSON string) into Account object (POJO)
        try {
            Account account = mapper.readValue(postContent, Account.class);
            // additional validation
            if(account.username.length() == 0 || account.password.length() < 4 || accountExists(account.username)){
                // TODO: update to message to say Post content is invalid
                context.status(400).result("Post content cannot be empty");
                return;
            }

            Account createdAccount = accountService.createAccount(account);
            
            // TODO: return 400 if the createdAccount is null
            // ...

            String accountJSON = mapper.writeValueAsString(createdAccount);

            // Return the post in JSON format with the new ID
            context.status(200).json(accountJSON);
        } catch (Exception e) {
            // TODO: handle exception
            context.status(400).result("Error reading content");
        }

    }

    private boolean accountExists(String username){
        // TODO: Query Account database to check if username exists

        return true;
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