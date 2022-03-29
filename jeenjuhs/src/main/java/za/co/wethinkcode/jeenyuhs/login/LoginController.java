package za.co.wethinkcode.jeenyuhs.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.plugin.openapi.annotations.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import za.co.wethinkcode.jeenyuhs.database.Repository;
import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.utils.ErrorResponse;
import za.co.wethinkcode.jeenyuhs.utils.LoginRequest;

public class LoginController {
    public static final String LOGIN_PATH = "/login";
    public static final String LOGOUT_PATH = "/logout";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @OpenApi(
            summary = "Login",
            operationId = "Login",
            path = "/login",
            method = HttpMethod.POST,
            tags = {"login"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = LoginRequest.class)}),
            responses = {
                    @OpenApiResponse(status = "200"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void handleLogin(Context context){
        String email;
        try {
            JsonNode jsonNode = objectMapper.readTree(context.body());
            email = jsonNode.get("email").asText();
        } catch (JsonProcessingException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.result("bad requestbody");
            return;
        }

        if (Repository.INSTANCE.userExist(email)){
            Client client = Repository.INSTANCE.getQuery().client(email);
            context.sessionAttribute("user",client);
            context.status(200);
        }else {
            Client newClient = new Client(email);
            Repository.INSTANCE.getQuery().CreateClient(newClient);
            context.sessionAttribute("user",Repository.INSTANCE.getQuery().client(email));
            context.status(200);
        }

    }

    @OpenApi(
            summary = "logout",
            operationId = "logout",
            path = "/logout",
            method = HttpMethod.GET,
            tags = {"logout"},
            responses = {
                    @OpenApiResponse(status = "200"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void handleLogout(Context context){
        context.sessionAttribute("user",null);
        context.status(200);
    }
}
