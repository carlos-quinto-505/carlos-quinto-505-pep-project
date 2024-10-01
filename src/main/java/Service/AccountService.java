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

    public Account addAccount(Account newAccount) {
        if (usernameVerify(newAccount.getUsername()) && passwordVerify(newAccount.getPassword())) {
            return socialMediaDAO.insertNewAccount(newAccount.getUsername(), newAccount.getPassword());
        }
        else return null;
    }

    public Account getAccountByUsername(Account target) {
        return socialMediaDAO.getAccountByUsername(target.getUsername());
    }

    public Account getAccountByUsernameAndPassword(Account target) {
        return socialMediaDAO.getAccountByUsernameAndPassword(target.getUsername(), target.getPassword());
    }

    private boolean passwordVerify(String password) {
        // Minimum character count
        int minCharCount = 4;

        if (password.length() >= minCharCount) return true;
        else return false;
    }

    private boolean usernameVerify(String username) {
        if (!username.isEmpty()) {
            Account account = socialMediaDAO.getAccountByUsername(username);

            if (account == null)
            return true;
        }

        return false;
    }
}
