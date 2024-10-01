package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods used for all database-related functions, such as querying existing accounts and inserting new accounts.
 * @author C. Quinto
 */
public class AccountDAO {
    /**
     * Retrieve existing accounts that match a provided id.
     * @param id integer used to complete the SQL id query.
     * @return an Account.
     */
    public Account getAccountById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account WHERE account_id=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

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
     * Retrieve existing accounts that match a provided username.
     * @param username String used to complete the SQL username query.
     * @return an Account.
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
     * @return an Account.
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
     * @return the processed Account.
     */
    public Account insertAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);" ;
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return new Account(
                    generatedKeys.getInt(1),
                    username,
                    password
                );
            } else throw new SQLException("Record insertion failed.");
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return null;
    }

    /**
     * Processes query results into Accounts and populates their fields.
     * @param results ResultSet from a query.
     * @return A list of Accounts.
     * @throws SQLException Exception handling should occur in parent method.
     */
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
