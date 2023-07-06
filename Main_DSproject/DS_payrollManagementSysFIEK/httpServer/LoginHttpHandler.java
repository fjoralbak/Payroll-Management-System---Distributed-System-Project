import Utils.Authenticate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.LoginInfo;
import models.Staff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class LoginHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // leximi i tipit te kerkeses / GET|POST|PUT|DELETE
        String method = exchange.getRequestMethod();

        if (method.equals("POST")) {
            System.out.println("LoginHttpHandler post");
            this.handlePostRequest(exchange);
        } else {
            throw new IOException("Method not supported!");
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStreamReader in = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader buffer = new BufferedReader(in);
        StringBuilder stB = new StringBuilder();
        String request;
        while ((request = buffer.readLine()) != null) {
            stB.append(request);
        }
        request = stB.toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        LoginInfo loginInfo = gson.fromJson(request, LoginInfo.class);

        Staff user = null;

        try {
            String emailF = loginInfo.getEmail();
            String passwordF = loginInfo.getPassword();
            user = Authenticate.login(emailF,passwordF);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = gson.toJson(user);

        OutputStream out = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        out.write(response.getBytes());
        out.flush();
        out.close();
    }
}