import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequest {
    private HashMap<String, String> headers = new HashMap<>();
    private HashMap<String, String> queryParams = new HashMap<>();
    private String body;
    private HTTPMethod method;
    private String rawPath;
    private String path;

    public HTTPRequest(String rawRequest) throws InvalidRequestException {
        parse(rawRequest);

    }

    private void parse(String rawRequest) throws InvalidRequestException {
        //  Regex to extract info from req. :      method      path  protocol    headers          body
        Pattern regex = Pattern.compile("^([A-Za-z]+)\\s(.+?)\\s(.+?)\\r\\n(.*?)(?:\\r\\n\\r\\n(.*))?$", Pattern.DOTALL);
        Matcher matcher = regex.matcher(rawRequest);

        if (matcher.find()) {
            this.method = HTTPMethod.valueOf(matcher.group(1).toUpperCase().trim());
            this.rawPath = matcher.group(2);

            String[] rawPathSplit = this.rawPath.split("\\?");
            this.path = rawPathSplit[0];
            if (rawPathSplit.length > 1) {
                String[] rawQueryParamsSplit = rawPathSplit[1].split("&");

                for (String rawQueryParams: rawQueryParamsSplit){
                    String[] keyValues = rawQueryParams.split("=");

                    if (keyValues.length>1){
                        this.queryParams.put(keyValues[0], keyValues[1]);
                    }else{
                        this.queryParams.put(keyValues[0], "");
                    }
                }
            }

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

    public String getRawPath() {
        return rawPath;
    }

    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }

    public String getQueryParam(String key){
        return queryParams.get(key);
    }
}
