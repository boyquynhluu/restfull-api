package serverapi.handler;

import com.sun.net.httpserver.*;

import serverapi.model.User;
import serverapi.repository.UserRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UserHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Incoming request: " + exchange.getRequestURI());

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (method.equals("GET") && path.equals("/users")) {
            getAllUsers(exchange);
        }

        else if (method.equals("GET") && path.matches("/users/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            getUser(exchange, id);
        }

        else if (method.equals("POST") && path.equals("/users")) {
            createUser(exchange);
        }

        else if (method.equals("PUT") && path.matches("/users/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            updateUser(exchange, id);
        }

        else if (method.equals("DELETE") && path.matches("/users/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            deleteUser(exchange, id);
        }

        else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private void getAllUsers(HttpExchange exchange) throws IOException {

        List<User> users = UserRepository.findAll();

        String json = users.stream().map(u -> "{\"id\":" + u.getId() + ",\"name\":\"" + u.getName() + "\"}").reduce("[",
                (a, b) -> a.equals("[") ? a + b : a + "," + b) + "]";

        sendResponse(exchange, 200, json);
    }

    private void getUser(HttpExchange exchange, int id) throws IOException {

        User user = UserRepository.findById(id);

        if (user == null) {
            sendResponse(exchange, 404, "User not found");
            return;
        }

        String json = "{\"id\":" + user.getId() + ",\"name\":\"" + user.getName() + "\"}";

        sendResponse(exchange, 200, json);
    }

    private void createUser(HttpExchange exchange) throws IOException {

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        String name = body.replace("name=", "");

        User user = UserRepository.save(name);

        sendResponse(exchange, 201, "Created user id=" + user.getId());
    }

    private void updateUser(HttpExchange exchange, int id) throws IOException {

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        String name = body.replace("name=", "");

        User user = UserRepository.update(id, name);

        if (user == null) {
            sendResponse(exchange, 404, "User not found");
            return;
        }

        sendResponse(exchange, 200, "Updated");
    }

    private void deleteUser(HttpExchange exchange, int id) throws IOException {

        UserRepository.delete(id);

        sendResponse(exchange, 200, "Deleted");
    }

    private void sendResponse(HttpExchange exchange, int status, String response) throws IOException {

        exchange.sendResponseHeaders(status, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }
}
