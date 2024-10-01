package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        return null;
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
     * Insert new entry into message table.
     * @param
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
     * @param
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
