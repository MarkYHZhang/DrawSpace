import org.eclipse.jetty.util.ConcurrentArrayQueue;
import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.webSocket;

public class DrawSpace {

    static volatile ConcurrentArrayQueue<String> broadcastQueue = new ConcurrentArrayQueue<>();
    static volatile ConcurrentArrayQueue<Session> sessions = new ConcurrentArrayQueue<>();

    public static void main(String[] args) {

        Thread thread = new Thread(new Broadcaster());
        thread.start();

        port(8000);

        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });


//        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
        webSocket("/communication", ClientHandler.class);
        init();

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
