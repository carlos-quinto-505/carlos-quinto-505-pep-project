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
        app.get("/messages/{message_id}", this::getMessageParamsHandler);
        app.post("/messages", this::postMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesFromAccountHandler);
        return app;
    }

    /**
     * Handle POST calls to the registration end point.
     * @param context context object managing HTTP request and response.
     */
    private void postRegistrationHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account request = mapper.readValue(context.body(), Account.class);
        Account response = accountService.addAccount(request);

        if (response != null) {
            context.status(200).json(mapper.writeValueAsString(response));
        } else context.status(400);
    }

    /**
     * Handle POST calls to the login end point.
     * @param context context object managing HTTP request and response.
     */
    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account request = mapper.readValue(context.body(), Account.class);
        Account response = accountService.getAccountByUsernameAndPassword(request);

        if(response != null) context.status(200).json(mapper.writeValueAsString(response));
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
     * Handle GET calls to the messages/PARAM end point.
     * @param context context object managing HTTP request and response.
     */
    private void getMessageParamsHandler(Context context) throws JsonProcessingException {
        Message response = messageService.getMessageById(context.pathParam("message_id"));

        if (response != null) context.status(200).json(response);
        else context.status(200);
    }

    /**
     * Handle POST calls to the messages end point.
     * @param context context object managing HTTP request and response.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message request = mapper.readValue(context.body(), Message.class);
        Message response = messageService.addMessage(request);

        if(response != null) context.status(200).json(mapper.writeValueAsString(response));
        else context.status(400);
    }

    /**
     * Handle DELETE calls to the messages end point.
     * @param context context object managing HTTP request and response.
     */
    private void deleteMessageHandler(Context context) throws JsonProcessingException {
        Message response = messageService.deleteMessageById(context.pathParam("message_id"));

        if (response != null) context.status(200).json(response);
        else context.status(200);
    }

    /**
     * Handle PATCH calls to the messages end point.
     * @param context context object managing HTTP request and response.
     */
    private void patchMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message request = new Message();

        request = mapper.readValue(context.body(), Message.class);

        Message response = messageService.patchMessageById(request, context.pathParam("message_id"));

        if (response != null) context.status(200).json(response);
        else context.status(400);
    }

    /**
     * Handle GET calls to the accounts/PARAM/messages end point.
     * @param context context object managing HTTP request and response.
     */
    private void getMessagesFromAccountHandler(Context context) throws JsonProcessingException {
        List<Message> response = messageService.getMessageByAccountId(context.pathParam("account_id"));

        context.status(200).json(response);
    }
}