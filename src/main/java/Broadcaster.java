import org.eclipse.jetty.util.ConcurrentArrayQueue;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Broadcaster implements Runnable{

    private volatile ConcurrentArrayQueue<String> broadcastQueue = DrawSpace.getBroadcastQueue();
    private volatile ConcurrentHashMap<Session, UUID> sessions = DrawSpace.getSessions();

    @Override
    public void run() {
        while (true){
            if (!sessions.isEmpty()&&!broadcastQueue.isEmpty()) {
                String p = broadcastQueue.poll();
                for (Session s : sessions.keySet()) {
                    try {
                        s.getRemote().sendString(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
