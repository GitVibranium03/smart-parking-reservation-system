package backend;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SmartParkServer {

    public static void main(String[] args) throws IOException {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8081),
                        0
                );

        // Home API
        server.createContext("/", exchange -> {

            String response =
                    "SmartPark API is running";

            sendResponse(exchange, 200, response);
        });

        // Login API
        server.createContext("/login", exchange -> {

            // Only POST requests are allowed
            if (!exchange.getRequestMethod()
                    .equalsIgnoreCase("POST")) {

                sendResponse(
                        exchange,
                        405,
                        "Only POST method is allowed"
                );

                return;
            }

            // Read request body
            String requestBody =
                    new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8
                    );

            System.out.println(
                    "Login request: " + requestBody
            );

            // Extract email and password
            String[] loginData =
                    requestBody.split("&");

            String email =
                    loginData[0].split("=")[1];

            String password =
                    loginData[1].split("=")[1];

            // Call UserDAO
            UserDAO userDAO =
                    new UserDAO();

            boolean loginSuccessful =
                    userDAO.loginUser(
                            email,
                            password
                    );

            if (loginSuccessful) {

                sendResponse(
                        exchange,
                        200,
                        "Login successful"
                );

            } else {

                sendResponse(
                        exchange,
                        401,
                        "Invalid email or password"
                );
            }
        });

        server.start();

        System.out.println(
                "SmartPark API started on port 8081"
        );
    }

    // Common response method
    private static void sendResponse(
            com.sun.net.httpserver.HttpExchange exchange,
            int statusCode,
            String response) throws IOException {

        byte[] responseBytes =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(responseBytes);
        }
    }
}