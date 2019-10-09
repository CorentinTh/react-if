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

                        HTTPRequest request = new HTTPRequest(rawRequest.toString());
                        HTTPResponse response = new HTTPResponse();

                        switch (request.getMethod()) {
                            case GET:
                                String path = request.getPath();
                                byte[] content;
                                if ((content = SystemeIO.readFile(path)) == null) {
                                    response.sendWithStatus(HTTPStatusCode.NOT_FOUND);
                                } else {
                                    String[] pathSplit = path.split("\\.");
                                    response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(path));
                                    response.send(content);
//                                    response.setHeader("Content-Length", String.valueOf(content.length));
                                }
                                break;
                            case POST:
                                try {
                                    SystemeIO.appendToFile(request.getPath(), request.getBody());
                                    response.sendWithStatus(HTTPStatusCode.OK);
                                } catch (Exception e) {
                                    response.sendWithStatus(HTTPStatusCode.INTERNAL_SERVER_ERROR);
                                }
                                break;
                            case PUT:
                                try {
                                    SystemeIO.writeFile(request.getPath(), request.getBody());
                                    response.sendWithStatus(HTTPStatusCode.OK);
                                } catch (Exception e) {
                                    response.sendWithStatus(HTTPStatusCode.INTERNAL_SERVER_ERROR);
                                }
                                break;
                            case HEAD:
                                   String filePath = request.getPath();
                                if (SystemeIO.fileExists(request.getPath())){
                                    String[] pathSplit = filePath.split("\\.");
                                    response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(filePath));
                                    response.sendWithStatus(HTTPStatusCode.OK);
                                }else{
                                    response.sendWithStatus(HTTPStatusCode.NOT_FOUND);
                                }
                                break;
                            case DELETE:
                                if(!SystemeIO.deleteFile(request.getPath())){
                                    response.sendWithStatus(HTTPStatusCode.NOT_FOUND);
                                }else{
                                    response.sendWithStatus(HTTPStatusCode.OK);
                                }
                                break;
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
