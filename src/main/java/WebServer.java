import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

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

                        System.out.println("[INFO] New request --------- >>");
                        System.out.println(rawRequest);
                        System.out.println("[INFO] --------------------- <<");

                        HTTPResponse response = new HTTPResponse();
                        HTTPRequest request;

                        try{
                            request = new HTTPRequest(rawRequest.toString());
                            new ActionHandler(request, response);
                        }catch (InvalidRequestException e){
                            e.printStackTrace();
                            response.sendWithStatus(HTTPStatusCode.BAD_REQUEST);
                        }

                        response.emitHttp(outputStream);
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
