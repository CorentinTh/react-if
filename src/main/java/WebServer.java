import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class WebServer {
    private Router router = new Router();

    private void setRoutes() {
        router.on(HTTPMethod.GET, "/ping", (httpRequest, httpResponse) -> {
            httpResponse.sendHTML("<h1>Yep</h1> <p>User: " + httpRequest.getQueryParam("userID") +"</p>");
        });

        router.on(HTTPMethod.GET, "/", (httpRequest, httpResponse) -> {
            httpResponse.sendHTML(new Scanner(getClass().getResourceAsStream("html/index.html")).useDelimiter("\\A").next());
        });

        router.on(HTTPMethod.POST, "/receptionFormulaire", (httpRequest, httpResponse) -> {
            System.out.println(httpRequest.getBody());
            httpResponse.sendWithStatus(HTTPStatusCode.OK);
        });

        router.on(HTTPMethod.GET, "/about", (httpRequest, httpResponse) -> {
            httpResponse.sendHTML("Fait par Tania et Corentin");
        });

        router.on(HTTPMethod.GET, "/users/:id", (httpRequest, httpResponse) -> {
            String id = httpRequest.getPathParam("id");
            httpResponse.sendHTML(id);
        });

        router.on(HTTPMethod.GET, "/users/([0-9]*)", (httpRequest, httpResponse) -> {
//            StringBuilder html ..


//            httpResponse.sendHTML(/**/);
        });


    }

    public WebServer(int port) {
        try {

            ServerSocket server = new ServerSocket(port);
            System.out.println("[INFO] Server started on port " + port);

            this.setRoutes();

            while (true) {
                Socket socket = server.accept();
                System.out.println("[INFO] New connection");

                // Reception and response thread
                new Thread(() -> {
                    try {
                        // in/out streams
                        BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintStream outputStream = new PrintStream(socket.getOutputStream());

                        // Fetching raw request
                        StringBuilder rawRequest = new StringBuilder();
                        while (inputStream.ready()) {
                            rawRequest.append((char) inputStream.read());
                        }

                        HTTPRequest request = new HTTPRequest(rawRequest.toString());
                        HTTPResponse response = new HTTPResponse();

                        Pair<Router.Action<HTTPRequest, HTTPResponse>, Matcher> actionMatcherPair;

                        if ((actionMatcherPair = router.getAction(request.getMethod(), request.getPath())) != null) {
                            request.setPathMatcher(actionMatcherPair.getValue());
                            actionMatcherPair.getKey().apply(request, response);
                        }

                        outputStream.println(response.getRawHTTP());
                        outputStream.flush();
                        inputStream.close();
                        outputStream.close();
                        socket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 3000;

        new WebServer(port);
    }
}
