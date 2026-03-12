package serverapi.httpserver;

import com.sun.net.httpserver.HttpServer;

import serverapi.handler.UserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/users", new UserHandler());
        server.start();
        System.out.println("Server running http://localhost:8000");
    }
}
