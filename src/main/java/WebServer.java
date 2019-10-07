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
                    try {
                        BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        StringBuilder lines = new StringBuilder();

                        while (inputStream.ready()) {
                            lines.append((char) inputStream.read());
                        }

                        System.out.println(lines);
                        HTTPRequest request = new HTTPRequest(lines.toString());
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
