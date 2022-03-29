package za.co.wethinkcode.jeenyuhs.groups;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;
import za.co.wethinkcode.jeenyuhs.database.Repository;
import za.co.wethinkcode.jeenyuhs.database.models.Post;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupsController {
    public static final String HOME_PATH = "/group/home/{email}";
    private static Map<WsContext, String> HomeMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static void home(WsConfig wsConfig){

        wsConfig.onConnect(ctx->{
            String email = ctx.pathParam("email");
            HomeMap.put(ctx,email);
        });
        wsConfig.onClose(ctx->{
            HomeMap.remove(ctx);
        });
        wsConfig.onMessage(ctx->{
            Post post = ctx.messageAsClass(Post.class);
            Repository.INSTANCE.getQuery().createPost(post);
            broadcastStatus(HomeMap,post);
        });
    }

    private static void broadcastStatus(Map<WsContext, String> map, Post post){
        map.keySet().stream().filter(ctx->ctx.session.isOpen()).forEach(session->{
            session.send(post);
        });
    }

    public String objectToString(Post post){
        try {
            return objectMapper.writeValueAsString(post);
        } catch (JsonProcessingException e) {
            throw new NullPointerException("Error");
        }
    }
}
