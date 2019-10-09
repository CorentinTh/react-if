import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class WebServer {

    public WebServer(int port) {
        try {

            ServerSocket server = new ServerSocket(port);
            System.out.println("[INFO] Server started on port " + port);


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

                        switch (request.getMethod()){
                            case GET:
                                String content;
                                if((content = File.readFile(request.getPath())) == null){
                                    response.sendWithStatus(HTTPStatusCode.NOT_FOUND);
                                }else{
                                    response.send(content);
                                }
                                break;
                            case POST:
                                break;

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
