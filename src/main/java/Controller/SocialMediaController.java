package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();
    /**
     * Javalin endpoints and app start.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegistrationHandler);
        app.post("/login", this::postLoginHandler);
        app.get("/messages", this::getMessageHandler);
        app.post("/messages", this::postMessageHandler);
        return app;
    }

    /**
     * Handle POST calls to the registration end point.
     * @param context context object managing HTTP request and response.
     */
    private void postRegistrationHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account userAccount = mapper.readValue(context.body(), Account.class);
        Account newAccount = accountService.addAccount(userAccount);

        if (newAccount != null) {
            context.status(200).json(mapper.writeValueAsString(newAccount));
        } else context.status(400);
    }

    /**
     * Handle POST calls to the login end point.
     * @param context context object managing HTTP request and response.
     */
    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account userAccount = mapper.readValue(context.body(), Account.class);
        Account target = accountService.getAccountByUsernameAndPassword(userAccount);

        if(target != null) context.status(200).json(mapper.writeValueAsString(target));
        else context.status(401);
    }

    /**
     * Handle GET calls to the messages end point.
     * @param context context object managing HTTP request and response.
     */
    private void getMessageHandler(Context context) throws JsonProcessingException {
        context.status(200).json(messageService.getAllMessages());
    }

    /**
     * Handle POST calls to the messages end point.
     * @param context context object managing HTTP request and response.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.addMessage(message);

        if(newMessage != null) context.status(200).json(mapper.writeValueAsString(newMessage));
        else context.status(400);
    }
}