package Service;

import Model.Account;
import DAO.SocialMediaDAO;

import java.util.List;

/**
 * Logic-handling class
 * @author C. Quinto
 */
public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    /**
     * No-args constructor
     */
    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    public Account addAccount(Account newAccount) {
        if (usernameVerify(newAccount.getUsername()) && passwordVerify(newAccount.getPassword())) {

        }
        return null;
    }

    private boolean passwordVerify(String password) {
        // Minimum character count
        int minCharCount = 4;

        if (password.length() >= minCharCount) return true;
        else return false;
    }

    private boolean usernameVerify(String username) {
        if (!username.isEmpty()) {
            List<Account> accounts = socialMediaDAO.getAccountByUsername(username);

            if (!accounts.isEmpty() && accounts.get(0).getUsername() != username)
            return true;
        }

        return false;
    }
}
