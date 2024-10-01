package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Model.Account;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService service = new AccountService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegistrationHandler);
        app.get("/login", this::getLoginHandler);
        return app;
    }

    /**
     * Handle POST calls to the registration end point.
     * @param context context object managing HTTP request and response.
     */
    private void postRegistrationHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account userAccount = mapper.readValue(context.body(), Account.class);
        Account newAccount = service.addAccount(userAccount);

        if (newAccount != null) {
            context.status(200).json(mapper.writeValueAsString(newAccount));
        } else context.status(400);
    }

    /**
     * Handle GET calls to the login end point.
     * @param context context object managing HTTP request and response.
     */
    private void getLoginHandler(Context context) {

    }

}