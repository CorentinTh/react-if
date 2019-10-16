package reactif;

import reactif.exeptions.InvalidRequestException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
                    // context is COPIED in lambda functions: meaning that "socket" is a copy of the previous one
                    // so, no overwriting

                    try {
                        // in/out streams
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintStream outputStream = new PrintStream(socket.getOutputStream());

                        HTTPRequestReader requestReader = new HTTPRequestReader(bufferedReader);

                        HTTPRequest request;
                        HTTPResponse response = new HTTPResponse();

                        try {
                            request = requestReader.getRequest();
                            new ActionHandler(request, response);
                        } catch (InvalidRequestException e) {
                            e.printStackTrace();
                            response.sendWithStatus(HTTPStatusCode.BAD_REQUEST);
                        }

                        response.emitHttp(outputStream);

                        outputStream.flush();
                        bufferedReader.close();
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
