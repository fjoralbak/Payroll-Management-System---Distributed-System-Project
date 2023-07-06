import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import Utils.Authenticate;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Staff;
import repositories.StaffRepo;

public class StaffHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // leximi i tipit te kerkeses / GET|POST|PUT|DELETE
        String method = exchange.getRequestMethod();

        if(method.equals("GET")) {
            System.out.println("StaffHttpHandler get");
            this.handleGetRequest(exchange);
        } else if(method.equals("POST")) {
            System.out.println("StaffHttpHandler post");
            this.handlePostRequest(exchange);
        } else if(method.equals("PUT")) {
            System.out.println("StaffHttpHandler put");
            this.handlePutRequest(exchange);
        } else if(method.equals("DELETE")) {
            System.out.println("StaffHttpHandler delete");
            this.handleDeleteRequest(exchange);
        } else {
            throw new IOException("Method not supported!");
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String URI = exchange.getRequestURI().toString();
        String query = URI.split("\\?")[1];
        String[] parameters = query.split("&");
        String ssn = parameters[0].split("=")[1];

        List<Staff> staff = null;
        Staff s = null;

        String userJsonString = "";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        if(parameters.length > 1) {
            if(parameters[1].split("=")[1].equals("getAll")) {
                // getAll()
                // staff/ssn&getAll
                try {
                    s = StaffRepo.find(Integer.parseInt(ssn));
                    if(s.isAdmin()) {
                        staff = StaffRepo.getAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                userJsonString = gson.toJson(staff);
            }
        }
        else {
            // staff/ssn
            //get(ssn)
            try {
                s = StaffRepo.find(Integer.parseInt(ssn));
            } catch (Exception e) {
                e.printStackTrace();
            }

            userJsonString = gson.toJson(s);
        }

        OutputStream out = exchange.getResponseBody();

        //Statusi: 200, 201,
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, userJsonString.length());
        out.write(userJsonString.getBytes());
        out.flush();
        out.close();
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStreamReader in = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader buffer = new BufferedReader(in);
        StringBuilder stB = new StringBuilder();
        String request;
        while((request = buffer.readLine()) != null) {
            stB.append(request);
        }
        request = stB.toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Staff s = gson.fromJson(request, Staff.class);
        String response;

        try {
            Authenticate.register(s);
            response = "{message: Staff created!}";
        } catch (Exception e) {
            response = e.getMessage();
        }

        OutputStream out = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, response.length());
        out.write(response.getBytes());
        out.flush();
        out.close();
    }

    private void handlePutRequest(HttpExchange exchange) throws IOException {
        InputStreamReader in = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader buffer = new BufferedReader(in);
        StringBuilder stB = new StringBuilder();
        String request;
        while((request = buffer.readLine()) != null) {
            stB.append(request);
        }
        request = stB.toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Staff s = gson.fromJson(request, Staff.class);
        String response;

        try {
            StaffRepo.update(s);
            response = "{message: Staff updated!}";
        } catch (Exception e) {
            response = e.getMessage();
        }

        OutputStream out = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(204, response.length());
        out.write(response.getBytes());
        out.flush();
        out.close();
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String URI = exchange.getRequestURI().toString();
        String query = URI.split("\\?")[1];
        String ssn = query.split("&")[0].split("=")[1];

        boolean res = false;

        try {
            res = StaffRepo.delete(Integer.parseInt(ssn));
        } catch(Exception e) {
            e.printStackTrace();
        }

        OutputStream out = exchange.getResponseBody();

        //Statusi: 200, 201
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String response = "{message: Staff deleted!}";
        exchange.sendResponseHeaders(200, response.length());
        out.write(Boolean.toString(res).getBytes());
        out.flush();
        out.close();
    }
}