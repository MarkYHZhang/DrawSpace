import org.eclipse.jetty.util.ConcurrentArrayQueue;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class ClientHandler {

    private volatile ConcurrentArrayQueue<String> broadcastQueue = DrawSpace.getBroadcastQueue();
    private volatile ConcurrentHashMap<Session, UUID> sessions = DrawSpace.getSessions();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("Connected!" + user.getRemoteAddress());
        if (!sessions.containsKey(user)){
            UUID id = UUID.randomUUID();
            sessions.put(user,id);
            try {
                user.getRemote().sendString(id.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String id) {
        sessions.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
            broadcastQueue.add(message);
    }

}