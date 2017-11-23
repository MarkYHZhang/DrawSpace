import org.eclipse.jetty.util.ConcurrentArrayQueue;
import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.webSocket;

public class DrawSpace {

    private static volatile ConcurrentArrayQueue<String> broadcastQueue = new ConcurrentArrayQueue<>();
    private static volatile ConcurrentHashMap<Session, UUID> sessions = new ConcurrentHashMap<Session, UUID>();

    public static void main(String[] args) {

        port(8000);

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });


//        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
        webSocket("/communication", ClientHandler.class);
        init();

        new DrawSpace().run();

    }

    private void run(){
        Thread thread = new Thread(new Broadcaster());
        thread.start();
    }

    static ConcurrentArrayQueue<String> getBroadcastQueue() {
        return broadcastQueue;
    }

    static ConcurrentHashMap<Session, UUID> getSessions() {
        return sessions;
    }


    //    static class Packet{
//        int x,y,r;
//        String color;
//        public Packet(int x, int y, int r, String color){
//            this.x=x;
//            this.y=y;
//            this.r=r;
//            this.color=color;
//        }
//    }

}
