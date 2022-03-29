package za.co.wethinkcode.jeenyuhs.directMessage;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;
import za.co.wethinkcode.jeenyuhs.database.Repository;
import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.database.models.Post;
import za.co.wethinkcode.jeenyuhs.utils.ErrorResponse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DirectMessageController {

    public static final String DIRECT_MESSAGE_PATH = "/dm/direct-message/{email}";
    public static final String ONLINE_PLAYERS_PATH = "/dm/online-players";
    private static Map<String, WsContext> onlinePlayers = new ConcurrentHashMap<>();

    public static void directMessage(WsConfig wsConfig){

        wsConfig.onConnect(ctx->{
            String email = ctx.pathParam("email");
            onlinePlayers.put(email,ctx);
        });
        wsConfig.onClose(ctx->{
            String email = ctx.pathParam("email");
            onlinePlayers.remove(email);
        });
        wsConfig.onMessage(ctx->{
            Post post = ctx.messageAsClass(Post.class);

            WsContext context = onlinePlayers.get(post.team);
            if (Optional.ofNullable(context).isPresent()){
                Repository.INSTANCE.getQuery().createPost(post);
                ctx.send(post);//send to the owner
                context.send(post); //send to the recipient
                return;
            }
            ctx.send(new ErrorResponse(
                    "User is offline",
                    HttpCode.BAD_REQUEST.getStatus(),
                    "BadRequest",
                    new HashMap<>()
            ));

        });
    }

    public static void onlinePlayers(Context context){
        List<Client> clientList = new ArrayList<>();
        clientList = onlinePlayers.keySet().stream().map(Client::new).collect(Collectors.toList());
        context.json(clientList);
        context.status(200);
    }
}
