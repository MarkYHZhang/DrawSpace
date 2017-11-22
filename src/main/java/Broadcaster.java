import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Broadcaster implements Runnable{

    @Override
    public void run() {
        while (true){
            if (!DrawSpace.sessions.isEmpty()&&!DrawSpace.broadcastQueue.isEmpty()) {
                String p = DrawSpace.broadcastQueue.poll();
                for (Session s : DrawSpace.sessions) {
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
