package backend;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SmartParkServer {

    public static void main(String[] args) throws IOException {

        HttpServer server
                = HttpServer.create(
                        new InetSocketAddress(8081),
                        0
                );

        // Home API
        server.createContext("/", exchange -> {

            String response
                    = "SmartPark API is running";

            sendResponse(
                    exchange,
                    200,
                    response
            );
        });

        // Login API
        server.createContext("/login", exchange -> {

            if (!exchange.getRequestMethod()
                    .equalsIgnoreCase("POST")) {

                sendResponse(
                        exchange,
                        405,
                        "Only POST method is allowed"
                );

                return;
            }

            String requestBody
                    = new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8
                    );

            System.out.println(
                    "Login request: " + requestBody
            );

            String[] loginData
                    = requestBody.split("&");

            String email
                    = URLDecoder.decode(
                            loginData[0].split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            String password
                    = URLDecoder.decode(
                            loginData[1].split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            UserDAO userDAO
                    = new UserDAO();

            boolean loginSuccessful
                    = userDAO.loginUser(
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

        // Registration API
        server.createContext("/register", exchange -> {

            if (!exchange.getRequestMethod()
                    .equalsIgnoreCase("POST")) {

                sendResponse(
                        exchange,
                        405,
                        "Only POST method is allowed"
                );

                return;
            }

            String requestBody
                    = new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8
                    );

            System.out.println(
                    "Registration request: "
                    + requestBody
            );

            String[] registrationData
                    = requestBody.split("&");

            String name
                    = URLDecoder.decode(
                            registrationData[0]
                                    .split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            String email
                    = URLDecoder.decode(
                            registrationData[1]
                                    .split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            String phone
                    = URLDecoder.decode(
                            registrationData[2]
                                    .split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            String password
                    = URLDecoder.decode(
                            registrationData[3]
                                    .split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            String vehicleNumber
                    = URLDecoder.decode(
                            registrationData[4]
                                    .split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            String vehicleType
                    = URLDecoder.decode(
                            registrationData[5]
                                    .split("=", 2)[1],
                            StandardCharsets.UTF_8
                    );

            // Create user
            UserDAO userDAO
                    = new UserDAO();

            int userId
                    = userDAO.insertUserAndGetId(
                            name,
                            email,
                            password,
                            phone
                    );

            if (userId == -1) {

                sendResponse(
                        exchange,
                        400,
                        "User registration failed"
                );

                return;
            }

            // Create vehicle
            VehicleDAO vehicleDAO
                    = new VehicleDAO();

            boolean vehicleAdded
                    = vehicleDAO.addVehicle(
                            userId,
                            vehicleNumber,
                            vehicleType.toUpperCase()
                    );

            if (vehicleAdded) {

                sendResponse(
                        exchange,
                        201,
                        "Registration successful"
                );

            } else {

                sendResponse(
                        exchange,
                        400,
                        "User created, but vehicle registration failed"
                );
            }
        });

        // Start server
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

        byte[] responseBytes
                = response.getBytes(
                        StandardCharsets.UTF_8
                );

        // CORS
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        try (OutputStream output
                = exchange.getResponseBody()) {

            output.write(responseBytes);
        }
    }
}