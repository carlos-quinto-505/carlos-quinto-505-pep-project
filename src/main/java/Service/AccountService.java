package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

/**
 * Logic-handling class
 * @author C. Quinto
 */
public class AccountService {
    AccountDAO socialMediaDAO;

    /**
     * No-args constructor
     */
    public AccountService(){
        socialMediaDAO = new AccountDAO();
    }

    /**
     * Contains methods used for processing and adding Account records to a database.
     * @param newAccount the Account object containing the data related to the new record
     * @return an Account object representation of the inserted record. On failure, returns null. 
     */
    public Account addAccount(Account newAccount) {
        if (usernameVerify(newAccount.getUsername()) && passwordVerify(newAccount.getPassword())) {
            return socialMediaDAO.insertNewAccount(newAccount.getUsername(), newAccount.getPassword());
        }
        else return null;
    }

    /**
     * Contains methods used for processing and retrieving Account records using a username.
     * @param target the Account object containing the data to match to the desired query result.
     * @return an Account object representation of the queried record. 
     */
    public Account getAccountByUsername(Account target) {
        return socialMediaDAO.getAccountByUsername(target.getUsername());
    }

    /**
     * Contains methods used for processing and retrieving Account records using a username and password.
     * @param target the Account object containing the data to match to the desired query result.
     * @return an Account object representation of the queried record. 
     */
    public Account getAccountByUsernameAndPassword(Account target) {
        return socialMediaDAO.getAccountByUsernameAndPassword(target.getUsername(), target.getPassword());
    }

    /**
     * Verifies the user-provided password meets all password requirements.
     * @param password String value of user-provided password.
     * @return Boolean.
     */
    private boolean passwordVerify(String password) {
        // Minimum character count
        int minCharCount = 4;

        if (password.length() >= minCharCount) return true;
        else return false;
    }

    /**
     * Verifies the user-provided username meets all username requirements.
     * @param password String value of user-provided username.
     * @return Boolean.
     */
    private boolean usernameVerify(String username) {
        if (!username.isEmpty()) {
            Account account = socialMediaDAO.getAccountByUsername(username);

            if (account == null)
            return true;
        }

        return false;
    }
}
