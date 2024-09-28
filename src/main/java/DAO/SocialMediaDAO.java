package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods used for all database-related functions, such as querying existing records and inserting new records.
 * @author C. Quinto
 */

public class SocialMediaDAO {
    /**
     * Retrieve existing accounts that match a provided username.
     * @param username String used to complete the SQL username query.
     * @return A List of Account objects.
     */
    public List<Account> getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account WHERE username=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Account objAccount = new Account(
                    results.getInt("account_id"),
                    results.getString("username"),
                    results.getString("password")
                );
                accounts.add(objAccount);

            }
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
            accounts.add(new Account("",""));
        }

        return accounts;
    }
    
    /**
     * Insert a new entry into the account table.
     * @param username String value for the data username field.
     * @param password String value for the data password field.
     */
    public Account insertNewAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);" ;
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

            
            Account objProspect = new Account(username, password);

            return objProspect;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }
}
