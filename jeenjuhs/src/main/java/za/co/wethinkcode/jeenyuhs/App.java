package za.co.wethinkcode.jeenyuhs;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.wethinkcode.jeenyuhs.database.session.DefaultAccessManager;
import za.co.wethinkcode.jeenyuhs.database.session.Sessions;
import za.co.wethinkcode.jeenyuhs.directMessage.DirectMessageController;
import za.co.wethinkcode.jeenyuhs.groups.GroupsController;
import za.co.wethinkcode.jeenyuhs.login.LoginController;
import za.co.wethinkcode.jeenyuhs.posts.PostController;
import za.co.wethinkcode.jeenyuhs.team.TeamController;
import io.swagger.v3.oas.models.info.Info;
import io.javalin.plugin.openapi.OpenApiOptions;
import za.co.wethinkcode.jeenyuhs.utils.ErrorResponse;

import static io.javalin.apibuilder.ApiBuilder.*;

public class App {

    private static final int DEFAULT_PORT = 7070;
    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) {
        Javalin app = getInstance();
        app.start(DEFAULT_PORT);

        logger.info("\n");
        logger.info("Check out ReDoc docs at http://localhost:7070/redoc");
        logger.info("Check out Swagger UI docs at http://localhost:7070/swagger-ui");
        logger.info("\n");
    }

    //for testing
    public static Javalin initialize(){
        Javalin app = getInstance();
        app.start(DEFAULT_PORT);
        return app;
    }

    public static Javalin getInstance() {
        Javalin server = createAndConfigureServer();
        setupRoutes(server);
        return server;
    }

    private static void setupRoutes(Javalin server) {
        server.routes(() -> {
            loginAndLogoutRoutes();
            teamRoutes();
            postRoutes();
            GroupRoutes();
            DirectMessageRoutes();
        });
    }

    private static void loginAndLogoutRoutes() {
        //login
        path(LoginController.LOGIN_PATH,()->post(LoginController::handleLogin));

        //logout
        path(LoginController.LOGOUT_PATH,()->get(LoginController::handleLogout));
    }

    public static void postRoutes(){
        path(PostController.POST_PATH,()->post(PostController::createPost));

        path(PostController.POST_BY_EMAIL_PATH,()->get(PostController::postByEmail));

        path(PostController.POST_BY_TEAM_PATH,()->get(PostController::postByTeam));

//        path(PostController.EDIT_POST_PATH,()->post(PostController::updateLikes));
    }

    public static void GroupRoutes(){
        path(GroupsController.HOME_PATH,()->ws(GroupsController::home));
    }

    public static void DirectMessageRoutes(){
        path(DirectMessageController.DIRECT_MESSAGE_PATH,()->ws(DirectMessageController::directMessage));
        path(DirectMessageController.ONLINE_PLAYERS_PATH,()->get(DirectMessageController::onlinePlayers));
    }

    private static void teamRoutes(){
        path(TeamController.TEAM_PATH,()->post(TeamController::createTeam));

        path(TeamController.TEAM_BY_NAME_PATH,()->get(TeamController::teamByName));

        path(TeamController.TEAM_JOIN_PATH,()->post(TeamController::joinTeam));

        path(TeamController.TEAM_MEMBERS_PATH,()->get(TeamController::teamMembers));
    }

    private static OpenApiPlugin getConfiguredOpenApiPlugin(){
        Info info = new Info().version("1.0").description("JeenYuhs API");
        OpenApiOptions options = new OpenApiOptions(info)
                .activateAnnotationScanningFor("za.co.wethinkcode.jeenyuhs")
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger-ui"))
                .reDoc(new ReDocOptions("/redoc"))
                .defaultDocumentation(doc -> {
                    doc.json("500", ErrorResponse.class);
                    doc.json("503", ErrorResponse.class);
                });
        return new OpenApiPlugin(options);
    }

    @NotNull
    private static Javalin createAndConfigureServer() {
        return Javalin.create(config -> {
            config.sessionHandler(Sessions::nopersistSessionHandler);
            config.accessManager(new DefaultAccessManager());
            config.enableCorsForAllOrigins();
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.requestLogger((ctx,ms)->{
                logger.info("\n");
                logger.info("Path: "+ctx.path());
//                logger.info("Http Headers: "+ ctx.headerMap());
//                logger.info("Http Cookies: "+ ctx.cookieMap());
                logger.info("Http status: "+ ctx.status());
                logger.info("Executed in: "+ms+" ms");
                logger.info("\n");
            });
        });
    }
}
