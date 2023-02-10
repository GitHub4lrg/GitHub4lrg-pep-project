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
        app.post("/register", this::postnewAccountHandler);    //post to create new acct for (user registration)
        app.post("/login", this::postLoginAccountHandler);       //to verify user login
        app.post("/messages", this::postMessageHandler);    //new post for creation of new msg
        app.get("/messages", this::getAllMessagesHandler);     //to submit get request to retrieve all msg
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);       //to submit get request to retrieve a msg by its ID
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);    //to submit delete request to delete a msg by its ID
        app.patch("/messages/{message_id}", this::updateMessageByMessageIdHandler);     //to submit patch request to update a msg text by its msg ID
        app.get("/accounts/{account_id}/messages", this::getMessageByPostedByHandler);    //get request retrieve all msg written by particular user

        return app;
    }

    /**
     * Handler to post a new User registration. (create a new Account)
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If AccountService returns a null account (meaning posting an Account was unsuccessful), the API will return a 400
     * message (client error).
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     *            The context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postnewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount == null || addedAccount.getUsername() == "" || addedAccount.getPassword().length() < 4){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedAccount));
        }   
    }

    /**
     * Handler to proccess a User login. (verify a User login on the endpoint)
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If AccountService returns a null account (meaning posting an Account was unsuccessful), the API will return a 400
     * message (client error).
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     *            The context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postLoginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account verifyAccount = accountService.getAllUsers(account);
        if(verifyAccount == null){
            ctx.json(mapper.writeValueAsString(verifyAccount));
            ctx.json(account);
        }else{
            ctx.status(401);
        }   
    }

    /**
     * Handler to post a new Message. (create a new Message)
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If MessageService returns a null message (meaning message_text is blank, over 255 characters, or not existing user), 
     * the API will return a 400 message (client error).
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     *            The context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.addMessage(message);
        if(newMessage != null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(newMessage));
        }
    }

    /**
     * Handler to retrieve all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    public void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    public void getMessageByMessageIdHandler(Context ctx){
        Message message = messageService.getMessageByMessageId(0);
        ctx.json(message);
    }

    public void deleteMessageByMessageIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByMessageId(message_id);
        //System.out.println(deletedMessage);
        if(deletedMessage != null){
            ctx.json(deletedMessage);
        }
        ctx.status(200);
        ctx.json(deletedMessage);
    }

    public void updateMessageByMessageIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageByMessageId(message_id, message);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
        ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    public void getMessageByPostedByHandler(Context ctx){
        Message messages = messageService.getMessageByPostedBy(0);
        ctx.json(messages);
    }
}