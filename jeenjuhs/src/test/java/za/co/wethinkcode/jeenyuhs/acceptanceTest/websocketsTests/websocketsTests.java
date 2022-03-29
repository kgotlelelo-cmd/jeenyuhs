package za.co.wethinkcode.jeenyuhs.acceptanceTest.websocketsTests;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.jeenyuhs.App;
import za.co.wethinkcode.jeenyuhs.database.Service;
import za.co.wethinkcode.jeenyuhs.database.models.Post;
import za.co.wethinkcode.jeenyuhs.utils.ErrorResponse;
import za.co.wethinkcode.jeenyuhs.utils.WebsocketClientTest;

import java.net.URISyntaxException;


public class websocketsTests {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static Javalin app;
    @BeforeAll
    public static void startServer(){
        app = App.initialize();
    }

    @AfterAll
    public static void tearDown(){
        app.stop();
    }


    @Test
    @DisplayName("Home websocket")
    void HomeTest() throws Exception {
        WebsocketClientTest clientTest = new WebsocketClientTest(
                "ws://localhost:7070/group/home/hagesak263@superyp.com",
                "{\n" +
                        "\t\"message\": \"How are you doing?\",\n" +
                        "\t\"likes\": 0,\n" +
                        "\t\"team\": \"TimeLine\",\n" +
                        "\t\"person\": \"hagesak263@superyp.com\"\n" +
                        "}"
        );

        clientTest.start();
        clientTest.connect();
        Post post = objectMapper.readValue(clientTest.message(),Post.class);
//        System.out.println(post.team);
        clientTest.close();
        assertEquals(post.team,"TimeLine");
        assertEquals(post.person,"hagesak263@superyp.com");
        assertEquals(post.message,"How are you doing?");
    }

    @Test
    @DisplayName("Direct message Error websocket")
    void directMessageErrorTest() throws Exception {
        WebsocketClientTest clientTest = new WebsocketClientTest(
                "ws://localhost:7070/dm/direct-message/vidoqe@thecarinformation.com",
                "{\n" +
                        "\t\"message\": \"How are you doing?\",\n" +
                        "\t\"likes\": 0,\n" +
                        "\t\"team\": \"TimeLine\",\n" +
                        "\t\"person\": \"vidoqe@thecarinformation.com\"\n" +
                        "}"
        );

        clientTest.start();
        clientTest.connect();
        ErrorResponse errorResponse = objectMapper.readValue(clientTest.message(), ErrorResponse.class);
//        System.out.println(clientTest.message());
        clientTest.close();
        assertEquals(errorResponse.title,"User is offline");
        assertEquals(errorResponse.status,400);
        assertEquals(errorResponse.type,"BadRequest");
    }

    @Test
    @DisplayName("Direct message Pass websocket")
    void directMessagePassTest() throws Exception {

        WebsocketClientTest clientTest1 = new WebsocketClientTest(
                "ws://localhost:7070/dm/direct-message/hagesak263@superyp.com",
                "{\n" +
                        "\t\"message\": \"How are you doing?\",\n" +
                        "\t\"likes\": 0,\n" +
                        "\t\"team\": \"vidoqe@thecarinformation.com\",\n" +
                        "\t\"person\": \"hagesak263@superyp.com\"\n" +
                        "}"
        );

        WebsocketClientTest clientTest2 = new WebsocketClientTest(
                "ws://localhost:7070/dm/direct-message/vidoqe@thecarinformation.com",
                "{\n" +
                        "\t\"message\": \"I am good and you?\",\n" +
                        "\t\"likes\": 0,\n" +
                        "\t\"team\": \"hagesak263@superyp.com\",\n" +
                        "\t\"person\": \"vidoqe@thecarinformation.com\"\n" +
                        "}"
        );
        clientTest1.start();
        clientTest2.start();
        clientTest1.connect();
        clientTest2.connect();
//        System.out.println("client one" + clientTest1.message());
//        System.out.println("client two" + clientTest2.message());
        clientTest1.close();
        clientTest2.close();
        Post post1 = objectMapper.readValue(clientTest1.message(),Post.class);
        Post post2 = objectMapper.readValue(clientTest2.message(),Post.class);

        assertEquals(post1.team,"hagesak263@superyp.com");
        assertEquals(post1.person,"vidoqe@thecarinformation.com");
        assertEquals(post1.message,"I am good and you?");

        assertEquals(post2.team,"hagesak263@superyp.com");
        assertEquals(post2.person,"vidoqe@thecarinformation.com");
        assertEquals(post2.message,"I am good and you?");

        // threads are the problem
    }
}
