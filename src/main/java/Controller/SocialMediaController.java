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
        app.get("/accounts/{account_id}/messages", this::getMessagePostedByHandler);    //get request retrieve all msg written by particular user

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
        System.out.println(addedAccount);
        if(addedAccount == null){
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
        Account verifyAccount = accountService.getAllAccounts(account);
        if(verifyAccount != null){
            ctx.json(mapper.writeValueAsString(verifyAccount));
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
        if(newMessage == null){
            ctx.status(400);
        }else{
            ctx.json(newMessage);
        }
    }
    /**
     * Handler to retrieve all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException
     */
    public void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
   /**
    * 
    * @param ctx handler to retrive message by message_id
    * @throws JsonProcessingException
    */
    public void getMessageByMessageIdHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByMessageId(message_id);
        if(message != null){
            ctx.json(message);
        }
        ctx.status(200);
    }
    /**
     * handler to delete message
     * @param ctx the javalin context object manages information about http request and response.
     * @throws JsonProcessingException
     */
    private void deleteMessageByMessageIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByMessageId(message_id);
        if(deletedMessage != null){
            ctx.json(deletedMessage);
        }
        ctx.status(200);
    } 
    /**
     * Handler to update a message by message_id
     * @param ctx
     * @throws JsonProcessingException
     */
    public void updateMessageByMessageIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageByMessageId(message_id, message);
        if(updatedMessage == null){
            ctx.status(400);
        }
        ctx.json(mapper.writeValueAsString(updatedMessage));
    }
    /**
     * handler to retrive messages written by particular user
     * @param ctx
     * @throws JsonProcessingException
     */
    public void getMessagePostedByHandler(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> message = messageService.getMessagePostedBy(account_id);
        if(message != null){
            ctx.json(message);
        }
        ctx.status(200);
    }
}