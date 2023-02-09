package Service;

import Model.Message;
import DAO.MessageDAO;

//import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;

/**The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
* persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
* SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
* actions undertaken by the API to a logging file.
*
* It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
* application that follows best practices will often have unnecessary code, but this makes the code more
* readable and maintainable in the long run!
*/
public class MessageService {
    public MessageDAO messageDAO;
    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * Constructor for a MessageService when a MessageDAO is provided.
     * This is used for when a mock MessageDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of MessageService independently of MessageDAO
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    /**
     * use the messageDAO to get all messages
     * @return all messages
     */
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    /**
     * Use the messageDAO to persist a message to the db.
     * A message_id will be provided in message. Method should check if the message_id already exists before it attempts to
     * persist it.
     * @param message a message object
     * @return message if successfully persisted, null if not successfully persisted (eg. if the message pk was already in use.)
     */
    public Message addMessage(Message message){
        Message newMessage = this.messageDAO.getMessageByMessageId(message.getMessage_id());
        if(newMessage != null)
        return null;
        messageDAO.insertMessage(message);
        return message;
        
    }

    public Message getMessageByMessageId(int message_id) {
        return messageDAO.getMessageByMessageId(message_id);
    }
    
    public Message deleteMessageByMessageId(int message_id) {
        return messageDAO.deleteMessageByMessageId(message_id);
    }

    public Message updateMessageByMessageId(int message_id) {
        return messageDAO.updateMessageByMessageId(message_id);
    }

    public Message getMessageByAccountId(int account_id) {
        return messageDAO.getMessageByAccountId(account_id);
    }
    
}
