import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HTTPResponse {
    private HTTPStatusCode statusCode = HTTPStatusCode.NOT_FOUND;
    private HashMap<String, String> headers = new HashMap<>();
    private String body = null;

    public HTTPResponse() {
        setHeader("X-Powered-By", "react-if");
    }

    public void sendHTML(String html) {
        send(html);
    }

    public void send(String text) {
        body = text;
        statusCode = HTTPStatusCode.OK;
        setHeader("Content-Length", String.valueOf(body.length()));
    }

    public void sendWithStatus(HTTPStatusCode code){
        statusCode = code;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getRawHTTP() {
        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.0 ").append(statusCode.getCode()).append(" ") .append(statusCode.getMessage()).append("\r\n");

        String headersStr = headers.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining("\r\n"));
        builder.append(headersStr);

        if (body != null){
            builder.append("\r\n\r\n").append(body);
        }

        return builder.toString();
    }
}
