import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class ClientHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("Connected!");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        DrawSpace.sessions.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        DrawSpace.broadcastQueue.add(message);
        if (!DrawSpace.sessions.contains(user)) DrawSpace.sessions.add(user);
    }

}