package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;

/**
 * Service layer class
 * @author C. Quinto
 */
public class MessageService {
    MessageDAO messageDAO = new MessageDAO();
    AccountDAO accountDAO = new AccountDAO();

    /**
     * No-args constructor
     */
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /**
     * Contains methods used for querying Message records.
     * @return a list of Messages. 
     */
    public List<Message> getAllMessages() {
        return messageDAO.getMessagesAll();
    }

    /**
     * Contains methods used for querying Message records.
     * @return a Message. 
     */
    public Message getMessageById(String targetId) {
        int id = Integer.valueOf(targetId).intValue();

        return messageDAO.getMessageById(id);
    }

    /**
     * Contains methods used for processing and adding Message records.
     * @param newMessage the Message object containing the data for new record fields.
     * @return a Message object. On failure, returns null. 
     */
    public Message addMessage(Message newMessage) {
        if (textVerify(newMessage.getMessage_text()) && posterIdVerify(newMessage.getPosted_by())) {
            return messageDAO.insertMessage(newMessage.getPosted_by(), newMessage.getMessage_text(), newMessage.getTime_posted_epoch());
        }
        else return null;
    }

    /**
     * Contains methods used for processing and deleting Message records.
     * @param id the Message containing the data for new record fields.
     * @return a Message. When no deletions occur, return null. 
     */
    public Message deleteMessageById(String targetId) {
        int id = Integer.valueOf(targetId).intValue();
        Message target = messageDAO.getMessageById(id);
        
        if (messageDAO.deleteMessageById(id) > 0) return target;
        else return null;
    }

    /**
     * Verifies the user-provided text meets prerequisite requirements.
     * @param text String of message content text.
     * @return Boolean.
     */
    private boolean textVerify(String text) {
        int maxCharCount = 255, minCharCount = 1;

        if (text.length() >= minCharCount && text.length() <= maxCharCount) return true;
        else return false;
    }

    /**
     * Verifies a message's poster id matches an account entry id.
     * @param text integer Id.
     * @return Boolean.
     */
    private boolean posterIdVerify(int id) {
        if (accountDAO.getAccountById(id) != null) return true;
        else return false;
    }
}
