package Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    

    AccountService accountService;
    MessageService messageService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
     }


    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewUserHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.post("/login", this::postLoginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postNewUserHandler(Context context) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        
        if(addedAccount != null){
            context.json(mapper.writeValueAsString(addedAccount));
            context.status(200);
        }
        else
            context.status(400);
    }

    private void postLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyUser(account);

        if(verifiedAccount != null){
            context.json(mapper.writeValueAsString(verifiedAccount));
            context.status(200);
        }
        else
            context.status(401);
    }

    private void postNewMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.insertMessage(message);
        if(addedMessage != null){
            context.json(mapper.writeValueAsString(addedMessage));
            context.status(200);
        }
        else
            context.status(400);

    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException{
        context.status(200);
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
        

    }

    private void getMessageByIdHandler(Context context) throws JsonProcessingException{
        
        
        String message_id_param = context.pathParam("message_id");
        int message_id = Integer.parseInt(message_id_param);
        Message retrieved = messageService.getMessageById(message_id);
        if(retrieved != null)
            context.json(retrieved);
        else
            context.json("");
        context.status(200);

    }

    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException{
        String message_id_param = context.pathParam("message_id");
        int message_id = Integer.parseInt(message_id_param);
        Message retrieved = messageService.deleteMessageById(message_id);
        if(retrieved != null)
            context.json(retrieved);
        else
            context.json("");
        context.status(200);
        
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException{

        String message_id_param = context.pathParam("message_id");
        int message_id = Integer.parseInt(message_id_param);
        // I got this solution from ChatGPT since I was unfamiliar with how to get the message_text value from the context body 
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(context.body());
        String new_message = jsonNode.get("message_text").asText();
        // --------------------------------------------------------------------------------------
        Message updated = messageService.updateMessage(message_id, new_message);
        if(updated != null){
            context.json(updated);
            context.status(200);
        }
        else
            context.status(400);
    }

    private void getMessagesByAccountIdHandler(Context context) throws JsonProcessingException{
        String account_id_param = context.pathParam("account_id");
        int account_id = Integer.parseInt(account_id_param);
        List<Message> messages = messageService.getMessagedFromAccountId(account_id);
        context.status(200);
        context.json(messages);
    }


}