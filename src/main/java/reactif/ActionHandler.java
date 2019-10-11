package reactif;

import java.net.URLConnection;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class ActionHandler {
    public ActionHandler(HTTPRequest request, HTTPResponse response) {

        switch (request.getMethod()) {
            case GET:
                this.onGet(request, response);
                break;
            case POST:
                this.onPost(request, response);
                break;
            case PUT:
                this.onPut(request, response);
                break;
            case HEAD:
                this.onHead(request, response);
                break;
            case DELETE:
                this.onDelete(request, response);
                break;
            default:
                response.sendWithStatus(HTTPStatusCode.NOT_IMPLEMENTED);
                break;
        }

    }

    private void onDelete(HTTPRequest request, HTTPResponse response) {
        if (!SystemeIO.deleteFile(request.getPath())) {
            response.sendWithStatus(HTTPStatusCode.NOT_FOUND);
        } else {
            response.sendWithStatus(HTTPStatusCode.OK);
        }
    }

    private void onHead(HTTPRequest request, HTTPResponse response) {
        String filePath = request.getPath();
        if (SystemeIO.fileExists(request.getPath())) {
            response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(filePath));
            response.sendWithStatus(HTTPStatusCode.OK);
        } else {
            response.sendWithStatus(HTTPStatusCode.NOT_FOUND);
        }
    }

    private void onPut(HTTPRequest request, HTTPResponse response) {
        try {
            SystemeIO.writeFile(request.getPath(), request.getBody());
            response.sendWithStatus(HTTPStatusCode.OK);
        } catch (Exception e) {
            response.sendWithStatus(HTTPStatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void onPost(HTTPRequest request, HTTPResponse response) {
        try {
            SystemeIO.appendToFile(request.getPath(), request.getBody());
            response.sendWithStatus(HTTPStatusCode.OK);
        } catch (FileSystemException e) {

        } catch (Exception e) {
            e.printStackTrace();
            response.sendWithStatus(HTTPStatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void onGet(HTTPRequest request, HTTPResponse response) {
        String path = request.getPath();

        // Check if file exists
        if (SystemeIO.fileExists(path)) {
            byte[] content;

            // Check file is readable
            if ((content = SystemeIO.readFile(path)) != null) {

                // Check is file is cgi
                if (Pattern.compile(".*\\.cgi(\\.[a-zA-Z0-9]{2,5})?$").matcher(path).matches() && SystemeIO.fileExists(path)) {
                    String rawArgs = request.getQueryParam("args");
                    System.out.println(rawArgs);
                    rawArgs = rawArgs == null ? "" : rawArgs;
                    String args = String.join(" ", rawArgs.split(","));

                    try {
                        String result = CmdExecutor.execute(Path.of(SystemeIO.getBaseDir().toString(), path).toString() + " " + args);
                        response.send(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.sendWithStatus(HTTPStatusCode.BAD_REQUEST);
                    }
                } else {
                    response.setHeader("Content-Type", getMime(path));
                    response.send(content);
                }
            } else {
                response.sendWithStatus(HTTPStatusCode.INTERNAL_SERVER_ERROR);
            }
        } else if (SystemeIO.isDirectory(path)) {
            byte[] content;

            path = Path.of(path, "index.html").toString();

            // Check if index.html is present in the directory
            if ((content = SystemeIO.readFile(path)) != null) {
                response.setHeader("Content-Type", getMime(path));
                response.send(content);
            }else {

            }


        } else {
            response.sendWithStatus(HTTPStatusCode.NOT_FOUND);
        }
    }

    static String getMime(String path){
        String mime;
        try {
            mime = Files.probeContentType(Path.of(SystemeIO.getBaseDir().toString(), path));
        } catch (Exception e) {
            mime = URLConnection.guessContentTypeFromName(path);
        }

        return mime;
    }

}
