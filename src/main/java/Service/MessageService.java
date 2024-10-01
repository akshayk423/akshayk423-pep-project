package Service;
import Model.Message;
import DAO.MessageDAO;

import java.util.List;

import DAO.AccountDAO;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message insertMessage(Message message){
        if(accountDAO.getAccountById(message.getPosted_by()) == null)
            return null;
        if(message.getMessage_text().length() > 255 || message.getMessage_text().isBlank())
            return null;
        
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
       Message retrieved = getMessageById(message_id);
       if(retrieved != null){
        messageDAO.deleteMessageById(retrieved.getMessage_id());
        return retrieved;
       }
       return null;
    }

    public Message updateMessage(int message_id, String new_message) {
        if(new_message.isBlank() || new_message.length() > 255)
            return null;
        return messageDAO.updateMessage(message_id, new_message);
    }

    public List<Message> getMessagedFromAccountId(int account_id) {
        return messageDAO.getMessagesFromAccountId(account_id);
    }
}
