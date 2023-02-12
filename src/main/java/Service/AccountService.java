package Service;

import Model.Account;
import DAO.AccountDAO;


/**The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
* persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
* SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
* actions undertaken by the API to a logging file.
*
* It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
* application that follows best practices will often have unnecessary code, but this makes the code more
* readable and maintainable in the long run!
*/
public class AccountService{
    private AccountDAO accountDAO;
    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /**
     * Constructor for a AccountService when a AccountDAO is provided.
     * This is used for when a mock AccountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AccountService independently of AccountDAO
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    /**
     * 
     * @param account
     * @return
     */    
    public Account addAccount(Account account){
        if(account.getUsername() == "" || account.getPassword().length() < 4){
            return null;
        }
        return accountDAO.insertAccount(account);
    }
    /**
     * 
     * @param account
     * @return
     */
    public Account getAllAccounts(Account account){
        return accountDAO.getAllAccounts(account);
    }
    
}