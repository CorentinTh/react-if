import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequest {
    private HashMap<String, String> headers = new HashMap<>();
    private String body;
    private HTTPMethod method;
    private String path;

    public HTTPRequest(String rawRequest) throws InvalidRequestException {
        parse(rawRequest);
        System.out.println(this);
    }

    private void parse(String rawRequest) throws InvalidRequestException {
        //  Regex to extract info from req. :      method      path  protocol    headers          body
        Pattern regex = Pattern.compile("^([A-Za-z]+)\\s(.+?)\\s(.+?)\\r\\n(.*?)\\r\\n\\r\\n(.*)$", Pattern.DOTALL);
        Matcher matcher = regex.matcher(rawRequest);

        if (matcher.find()) {
            this.method = HTTPMethod.valueOf(matcher.group(1).toUpperCase().trim());
            this.path = matcher.group(2);
            this.body = matcher.group(5);

            String[] rawHeaders = matcher.group(4).split("\\r?\\n");

            for (String rawHeader : rawHeaders) {
                String[] rowSplit = rawHeader.split(": ");
                this.headers.put(rowSplit[0].toLowerCase(), rowSplit[1].toLowerCase());

            }
        } else {
            throw new InvalidRequestException();
        }

    }

    public String getBody() {
        return body;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder("");

        st.append("Method: ")
                .append(method.toString())
                .append("\n")
                .append("Path: ")
                .append(path)
                .append("\n")
                .append("Body: ")
                .append(body)
                .append("\n");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            st.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return st.toString();
    }

}
