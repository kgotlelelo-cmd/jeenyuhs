package za.co.wethinkcode.jeenyuhs.acceptanceTest.httpTests;

import io.javalin.Javalin;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.jeenyuhs.App;
import kong.unirest.JsonNode;
import kong.unirest.HttpResponse;
import za.co.wethinkcode.jeenyuhs.database.Service;


import static org.junit.jupiter.api.Assertions.*;

public class acceptanceTests {


    public static Javalin app;
    @BeforeAll
    public static void startServer() throws InterruptedException {
        Service.dropDatabase("jeenjuhs.db");
        app = App.initialize();
    }

    @AfterAll
    public static void tearDown() throws InterruptedException {
        app.stop();
    }

    @Test
    @DisplayName("POST /login /logout")
    void LoginLogout() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:7070/login")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\t\"email\": \"xndlangi@student.wethinkcode.co.za\"\n" +
                        "}")
                .asJson();
        assertEquals(200, response.getStatus());

        response = Unirest.get("http://localhost:7070/logout")
                .header("Content-Type", "application/json")
                .asJson();
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("create post tests")
    void storyOne(){

        //login
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:7070/login")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\t\"email\": \"kmasenam@student.wethinkcode.co.za\"\n" +
                        "}")
                .asJson();
        assertEquals(200, response.getStatus());


        response = Unirest.post("http://localhost:7070/post")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\t\"message\": \"Hello first time here\",\n" +
                        "\t\"team\": \"timeline\"\n" +
                        "}")
                .asJson();
        assertEquals(response.getStatus(),201);


        response = Unirest.get("http://localhost:7070/post/kmasenam@student.wethinkcode.co.za")
                .header("Content-Type", "application/json").asJson();
        assertEquals(response.getStatus(),200);
        JSONArray array = response.getBody().getArray();
        JSONObject jsonObject = array.getJSONObject(0);
        assertEquals(jsonObject.get("message"),"Hello first time here");
        assertEquals(jsonObject.get("team"),"timeline");
        assertEquals(jsonObject.get("likes"),0);
        assertEquals(jsonObject.get("person"),"kmasenam@student.wethinkcode.co.za");

        response = Unirest.get("http://localhost:7070/post/team/timeline")
                .header("Content-Type", "application/json").asJson();
        assertEquals(response.getStatus(),200);
        array = response.getBody().getArray();
        jsonObject = array.getJSONObject(0);
        assertEquals(jsonObject.get("message"),"Hello first time here");
        assertEquals(jsonObject.get("team"),"timeline");
        assertEquals(jsonObject.get("likes"),0);
        assertEquals(jsonObject.get("person"),"kmasenam@student.wethinkcode.co.za");
    }

    @Test
    @DisplayName("team tests")
    void storyTwo(){

        HttpResponse<JsonNode> response = Unirest.post("http://localhost:7070/login")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\t\"email\": \"jmangany@student.wethinkcode.co.za\"\n" +
                        "}")
                .asJson();
        assertEquals(200, response.getStatus());

        response = Unirest.post("http://localhost:7070/team")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\t\"team\": \"validator\"\n" +
                        "}")
                .asJson();
        assertEquals(response.getStatus(),201);

        response = Unirest.get("http://localhost:7070/team/validator")
                .header("Content-Type", "application/json").asJson();
        assertEquals(response.getStatus(),200);
        assertEquals(response.getBody().getObject().get("name"),"validator");

        response = Unirest.post("http://localhost:7070/team/join")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\t\"team\": \"validator\",\n" +
                        "\t\"email\": \"jmangany@student.wethinkcode.co.za\"\n" +
                        "}")
                .asJson();
        assertEquals(response.getStatus(),200);

        response = Unirest.get("http://localhost:7070/team/members/validator")
                .header("Content-Type", "application/json").asJson();
        assertEquals(response.getStatus(),200);
        JSONObject object = response.getBody().getArray().getJSONObject(0);
        assertEquals(object.get("email"),"jmangany@student.wethinkcode.co.za");

    }

    @Test
    @DisplayName("Online players")
    void online(){
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:7070/login")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\t\"email\": \"zuveno@kellychibale-researchgroup-uct.com\"\n" +
                        "}")
                .asJson();
        assertEquals(200, response.getStatus());

        response = Unirest.get("http://localhost:7070/dm/online-players")
                .header("Content-Type", "application/json").asJson();
        assertEquals(response.getStatus(),200);
        JSONArray array = response.getBody().getArray();
        assertTrue(array.isEmpty());
    }
}
