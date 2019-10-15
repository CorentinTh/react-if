package reactif;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HTTPResponse {
    private HTTPStatusCode statusCode = HTTPStatusCode.NOT_FOUND;
    private HashMap<String, String> headers = new HashMap<>();
    private byte[] body = null;

    public HTTPResponse() {
        setHeader("X-Powered-By", "react-if");
        setHeader("Connection", "close");
    }

    void send(String text) {
        body = text.getBytes();
        statusCode = HTTPStatusCode.OK;
        setHeader("Content-Length", String.valueOf(body.length));
    }

    void send(byte[] text) {
        body = text;
        statusCode = HTTPStatusCode.OK;
        setHeader("Content-Length", String.valueOf(body.length));
    }

    void sendWithStatus(HTTPStatusCode code) {
        statusCode = code;
    }

    void setHeader(String key, String value) {
        headers.put(key, value);
    }

    void emitHttp(OutputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.0 ").append(statusCode.getCode()).append(" ").append(statusCode.getMessage()).append("\r\n");

        String headersStr = headers.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining("\r\n"));
        builder.append(headersStr);

        stream.write(builder.toString().getBytes());

        if (body != null) {
            stream.write("\r\n\r\n".getBytes());
            stream.write(body);
        }
    }
}
