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

public class AccountDAO {
    /**
     * Retrieve existing accounts that match a provided username.
     * @param username String used to complete the SQL username query.
     * @return A List of Account objects.
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account WHERE username=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet results = statement.executeQuery();
            accounts = accountQueryResultsBuilder(results);

            if (accounts.isEmpty()) return null;
            else return accounts.get(0);
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieve existing accounts that match a provided username and password.
     * @param username String used to complete the SQL username query.
     * @param password String used to complete the SQL password query.
     * @return A List of Account objects.
     */
    public Account getAccountByUsernameAndPassword(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account WHERE username=? AND password=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet results = statement.executeQuery();
            accounts = accountQueryResultsBuilder(results);

            if (accounts.isEmpty()) return null;
            else return accounts.get(0);
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }

        return null;
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
            PreparedStatement statement = connection.prepareStatement(sql);
            
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            
            return new Account(username, password);
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }

        return null;
    }

    private List<Account> accountQueryResultsBuilder(ResultSet results) throws SQLException{
        List<Account> targets = new ArrayList<>();

        while (results.next()) {
            Account objAccount = new Account(
                results.getInt("account_id"),
                results.getString("username"),
                results.getString("password")
            );
            targets.add(objAccount);
        }
        return targets;
    }
}
