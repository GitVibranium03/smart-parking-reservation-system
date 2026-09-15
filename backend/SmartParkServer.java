package backend;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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

            exchange.sendResponseHeaders(
                    200,
                    response.length()
            );

            try (OutputStream output =
                         exchange.getResponseBody()) {

                output.write(
                        response.getBytes()
                );
            }
        });

        // Login API
        server.createContext("/login", exchange -> {

            String response =
                    "Login API is working";

            exchange.sendResponseHeaders(
                    200,
                    response.length()
            );

            try (OutputStream output =
                         exchange.getResponseBody()) {

                output.write(
                        response.getBytes()
                );
            }
        });

        server.start();

        System.out.println(
                "SmartPark API started on port 8081"
        );
    }
}