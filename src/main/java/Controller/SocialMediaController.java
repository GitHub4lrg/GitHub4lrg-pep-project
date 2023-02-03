package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //endpoint that return all accounts
        app.post("/register", this::exampleHandler);    //post to create new acct for (user registration)
        app.post("/login", this::exampleHandler);       //to verify user login
        app.post("/messages", this::exampleHandler);    //new post for creation of new msg
        app.get("/messages", this::exampleHandler);     //to submit get request to retrieve all msg
        app.get("/messages/{message_id}", this::exampleHandler);       //to submit get request to retrieve a msg by its ID
        app.delete("/messages/{message_id}", this::exampleHandler);    //to submit delete request to delete a msg by its ID
        app.patch("/messages/{message_id}", this::exampleHandler);     //to submit patch request to update a msg text by its msg ID
        app.get("/accounts/{account_id}/messages", this::exampleHandler);    //get request retrieve all msg written by particular user

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}