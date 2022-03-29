package za.co.wethinkcode.jeenyuhs.team;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.jeenyuhs.database.Repository;
import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.database.models.Team;
import io.javalin.plugin.openapi.annotations.*;
import za.co.wethinkcode.jeenyuhs.utils.ErrorResponse;
import za.co.wethinkcode.jeenyuhs.utils.JoinRequest;
import za.co.wethinkcode.jeenyuhs.utils.TeamRequest;

public class TeamController {
    public static final String TEAM_PATH = "/team";
    public static final String TEAM_BY_NAME_PATH = "/team/{team}";
    public static final String TEAM_JOIN_PATH = "/team/join";
    public static final String TEAM_MEMBERS_PATH = "/team/members/{team}";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @OpenApi(
            summary = "Create team",
            operationId = "createTeam",
            path = "/team",
            method = HttpMethod.POST,
            tags = {"Team"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = TeamRequest.class)}),
            responses = {
                    @OpenApiResponse(status = "201"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void createTeam(Context context){
        String team;

        try {
            JsonNode jsonNode = objectMapper.readTree(context.body());
            team = jsonNode.get("team").asText();
        } catch (JsonProcessingException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.result("bad requestbody");
            return;
        }

        Repository.INSTANCE.getQuery().createTeam(new Team(team));
        context.status(201);
    }

    @OpenApi(
            summary = "Get Team by Name",
            operationId = "getTeamByName",
            path = "/team/{team}",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "team", type = String.class, description = "The Team Name")},
            tags = {"Team"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Team.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void teamByName(Context context){
        String team = context.pathParam("team");
        context.json(Repository.INSTANCE.getQuery().teamByName(team));
        context.status(200);
    }

    @OpenApi(
            summary = "Join team",
            operationId = "joinTeam",
            path = "/team/join",
            method = HttpMethod.POST,
            tags = {"Team"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = JoinRequest.class)}),
            responses = {
                    @OpenApiResponse(status = "200"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void joinTeam(Context context){
        String email;
        String team;

        try {
            JsonNode jsonNode = objectMapper.readTree(context.body());
            team = jsonNode.get("team").asText();
            email = jsonNode.get("email").asText();
        } catch (JsonProcessingException e) {
            context.status(HttpCode.BAD_REQUEST);
            context.result("bad requestbody");
            return;
        }

        Repository.INSTANCE.userJoinTeam(email,team);
        context.status(200);
    }

    @OpenApi(
            summary = "Get Team Members",
            operationId = "getTeamMembers",
            path = "/team/members/{team}",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "team", type = String.class, description = "The Team Name")},
            tags = {"Team"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Client[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void teamMembers(Context context){
        String team = context.pathParam("team");
        context.json(Repository.INSTANCE.teamMembers(team));
        context.status(200);
    }

}
