import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor)
                Executors.newFixedThreadPool(15);

        HttpServer server = HttpServer.create(
                new InetSocketAddress("localhost", 8081), 0
        );
        server.setExecutor(threadPool);

        server.createContext("/staff", new StaffHttpHandler());
        server.createContext("/login", new LoginHttpHandler());

        server.start();
        System.out.println("Server started...");
    }
}
