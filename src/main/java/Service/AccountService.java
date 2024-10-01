package Service;

import Model.Account;
import DAO.AccountDAO;

/**
 * Service layer class
 * @author C. Quinto
 */
public class AccountService {
    AccountDAO accountDAO;

    /**
     * No-args constructor
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Contains methods used for processing and adding Account records.
     * @param newAccount the Account object containing the data for new record fields.
     * @return an Account object. On failure, returns null. 
     */
    public Account addAccount(Account newAccount) {
        if (usernameVerify(newAccount.getUsername()) && passwordVerify(newAccount.getPassword())) {
            return accountDAO.insertAccount(newAccount.getUsername(), newAccount.getPassword());
        }
        else return null;
    }

    /**
     * Contains methods used for processing and retrieving Account records using a username.
     * @param target the Account object containing the data to match to the desired query result.
     * @return an Account object. 
     */
    public Account getAccountByUsername(Account target) {
        return accountDAO.getAccountByUsername(target.getUsername());
    }

    /**
     * Contains methods used for processing and retrieving Account records using a username and password.
     * @param target the Account object containing the data to match to the desired query result.
     * @return an Account object. 
     */
    public Account getAccountByUsernameAndPassword(Account target) {
        return accountDAO.getAccountByUsernameAndPassword(target.getUsername(), target.getPassword());
    }

    /**
     * Verifies the user-provided password meets all prerequisites.
     * @param password String of user-provided password.
     * @return Boolean.
     */
    private boolean passwordVerify(String password) {
        // Minimum character count
        int minCharCount = 4;

        if (password.length() >= minCharCount) return true;
        else return false;
    }

    /**
     * Verifies the user-provided username meets all prerequisites.
     * @param password String of user-provided username.
     * @return Boolean.
     */
    private boolean usernameVerify(String username) {
        if (!username.isEmpty()) {
            Account account = accountDAO.getAccountByUsername(username);

            if (account == null)
            return true;
        }
        return false;
    }
}
