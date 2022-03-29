package za.co.wethinkcode.jeenyuhs.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.plugin.openapi.annotations.OpenApi;
import za.co.wethinkcode.jeenyuhs.database.Repository;
import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.database.models.Post;
import io.javalin.plugin.openapi.annotations.*;
import za.co.wethinkcode.jeenyuhs.utils.ErrorResponse;
import za.co.wethinkcode.jeenyuhs.utils.PostRequest;

import java.util.List;

public class PostController {
    public static final String POST_PATH = "/post";
    public static final String EDIT_POST_PATH = "/post/update";
    public static final String POST_BY_EMAIL_PATH = "/post/{email}";
    public static final String POST_BY_TEAM_PATH = "/post/team/{name}";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @OpenApi(
            summary = "Create post",
            operationId = "createPost",
            path = "/post",
            method = HttpMethod.POST,
            tags = {"Post"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = PostRequest.class)}),
            responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void createPost(Context context){
        String message;
        String team;

        Client client = context.sessionAttribute("user");

        try {
            JsonNode jsonNode = objectMapper.readTree(context.body());
            message = jsonNode.get("message").asText();
            team = jsonNode.get("team").asText();
        } catch (JsonProcessingException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.result("bad requestbody");
            return;
        }

        Post post = new Post(
                message,
                0,
                team,
                client.email

        );

        Repository.INSTANCE.getQuery().createPost(post);
        context.status(201);
    }

    @OpenApi(
            summary = "Get posts by Email",
            operationId = "getPostsByEmail",
            path = "/post/{email}",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "email", type = String.class, description = "The user Email")},
            tags = {"Post"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Post[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void postByEmail(Context context){
        String email = context.pathParam("email");
        List<Post> postList = Repository.INSTANCE.getQuery().postByPerson(email);
        context.json(postList);
        context.status(200);
    }

    @OpenApi(
            summary = "Get posts by Team",
            operationId = "getPostsByTeam",
            path = "/post/team/{name}",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "name", type = String.class, description = "The Team Name")},
            tags = {"Post"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Post[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void postByTeam(Context context){
        String teamName = context.pathParam("name");
        List<Post> postList = Repository.INSTANCE.getQuery().postByTeam(teamName);
        context.json(postList);
        context.status(200);
    }

//    @OpenApi(
//            summary = "like a post",
//            operationId = "likePost",
//            path = "/post/update",
//            method = HttpMethod.PATCH,
//            pathParams = {@OpenApiParam(name = "name", type = String.class, description = "The Team Name")},
//            tags = {"Post"},
//            responses = {
//                    @OpenApiResponse(status = "200", content = {}),
//                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
//                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
//            }
//    )
//    public static void updateLikes(Context context){
//        int id;
//
//        try {
//            JsonNode jsonNode = objectMapper.readTree(context.body());
//            id = jsonNode.get("id").asInt();
//        } catch (JsonProcessingException e) {
//            context.status(HttpCode.BAD_REQUEST);
//            context.result("bad requestbody");
//            return;
//        }
//
//        Post post = Repository.INSTANCE.getQuery().postById(id);
//        post.likes += 1;
//        Repository.INSTANCE.getQuery().updateLikes(post);
//        context.status(200);
//    }
}
