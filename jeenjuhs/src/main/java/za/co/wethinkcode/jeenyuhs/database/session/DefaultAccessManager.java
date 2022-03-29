package za.co.wethinkcode.jeenyuhs.database.session;

import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;
import za.co.wethinkcode.jeenyuhs.database.models.Client;

import java.util.Set;

public class DefaultAccessManager implements AccessManager {

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<RouteRole> set) throws Exception {
        Client currentClient = context.sessionAttribute("user");

        if (currentClient == null &&
                !context.path().equals("/login") &&
                !context.path().contains("/group") &&
                !context.path().contains("/swagger") &&
                !context.path().contains("/team") &&
                !context.path().contains("/dm") &&
                !context.path().contains("/post") &&
                !context.path().contains("/redoc")
        ) {
            UnauthorizedAccess(context);
        } else {
            handler.handle(context);
        }
    }

    public static void UnauthorizedAccess(Context context){
        context.status(403);
        context.result("You are not allowed here");
    }
}
