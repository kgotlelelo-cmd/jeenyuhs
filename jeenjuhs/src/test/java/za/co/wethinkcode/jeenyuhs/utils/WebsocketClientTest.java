package za.co.wethinkcode.jeenyuhs.utils;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WebsocketClientTest {

    String url;
    WebSocketClient client;
    MyWebSocket socket;
    URI destUri;
    ClientUpgradeRequest request;

    private static final Logger logger = LoggerFactory.getLogger(WebsocketClientTest.class);


    public WebsocketClientTest(String url,String post) throws URISyntaxException {
        client = new WebSocketClient();
        socket = new MyWebSocket(post);
        destUri = new URI(url);
        request = new ClientUpgradeRequest();
    }

    public void start() throws Exception {
        this.client.start();
    }

    public void close() throws Exception {
        this.client.stop();
    }

    public void connect() throws IOException, InterruptedException {
        client.connect(this.socket,this.destUri,this.request);
        socket.awaitClose(5, TimeUnit.SECONDS);
    }

    public String message(){
        return this.socket.getMessage();
    }

    @WebSocket
    public class MyWebSocket {

        String post;
        String message;

        private final CountDownLatch closeLatch = new CountDownLatch(1);

        public MyWebSocket(String post) {
            this.post = post;
        }

        @OnWebSocketConnect
        public void onConnect(Session session) throws IOException {
            logger.info("Sending message: Hello server");
            session.getRemote().sendString(this.post);
        }

        @OnWebSocketMessage
        public void onMessage(String message) {
            logger.info("Message from Server: " + message);
            this.message = message;
        }

        public String getMessage(){
            return this.message;
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            logger.info("WebSocket Closed. Code:" + statusCode);
        }

        public boolean awaitClose(int duration, TimeUnit unit)
                throws InterruptedException {
            return this.closeLatch.await(duration, unit);
        }
    }
}
