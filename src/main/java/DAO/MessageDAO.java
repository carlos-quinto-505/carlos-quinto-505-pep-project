package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Provides methods used for all database-related functions, such as querying existing messages and inserting new messages.
 * @author C. Quinto
 */
public class MessageDAO {
    /**
     * Retrieve all existing messages.
     * @return A List of Messages.
     */
    public List<Message> getMessagesAll() {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();

            return messageQueryResultsBuilder(results);
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Retrieve message by message_id field.
     * @return a Message.
     */
    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();
            List<Message> queryMessages = messageQueryResultsBuilder(results);
            
            if (!queryMessages.isEmpty()) return queryMessages.get(0);
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return null;
    }
    

    /**
     * Retrieve message by account_id field.
     * @return a Message.
     */
    public List<Message> getMessageByAccountId(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE posted_by=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();
            List<Message> queryMessages = messageQueryResultsBuilder(results);
            
            if (!queryMessages.isEmpty()) return queryMessages;
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Insert new entry into message table.
     * @param posterId integer used to match a user record.
     * @param contentText String value for the record text field.
     * @param timePosted Long value for the record time field.
     * @return the processed Message object.
     */
    public Message insertMessage(int posterId, String contentText, Long timePosted) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by,message_text,time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, posterId);
            statement.setString(2, contentText);
            statement.setLong(3, timePosted);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return new Message(
                    generatedKeys.getInt(1),
                    posterId,
                    contentText,
                    timePosted
                );
            } else throw new SQLException("Record insertion failed.");
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return null;
    }

    /**
     * Delete entry from message table.
     * @param id integer used to match the record id.
     * @return the processed Message.
     */
    public int deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id=?;";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Patch entry from message table.
     * @param id integer matched to the message record message_id field.
     * @param text String value for the message record message_text field.
     * @return the processed Message.
     */
    public int patchMessageById(int id, String text) {
        Connection connection = ConnectionUtil.getConnection();
        System.out.println("Values passed to DAO: " + id + " | " + text);

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id=?;";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, text);
            statement.setInt(2, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Processes query results into Messages and populates their fields.
     * @param results ResultSet from a query.
     * @return A list of Message objects.
     * @throws SQLException Exception handling should occur in parent method.
     */
    private List<Message> messageQueryResultsBuilder(ResultSet results) throws SQLException{
        List<Message> targets = new ArrayList<>();

        while (results.next()) {
            Message objMessage = new Message(
                results.getInt("message_id"),
                results.getInt("posted_by"),
                results.getString("message_text"),
                results.getLong("time_posted_epoch")
            );
            targets.add(objMessage);
        }
        return targets;
    }
}
