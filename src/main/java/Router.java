import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    @FunctionalInterface
    public interface Action<A, B> {
        public void apply(A a, B b) throws IOException;
    }

    private HashMap<HTTPMethod, HashMap<String, Action<HTTPRequest, HTTPResponse>>> executors = new HashMap<>();

    public Router() {
    }


    public void on(HTTPMethod method, String path, Action<HTTPRequest, HTTPResponse> action) {
        if (!executors.containsKey(method)) {
            executors.put(method, new HashMap<>());
        }

        executors.get(method).put(path, action);
    }

    public Action<HTTPRequest, HTTPResponse> getAction(HTTPMethod method, String path) {
        if (executors.containsKey(method)) {
            return executors.get(method).get(path);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("/test/(.*)");

        Matcher m = pattern.matcher("/test/oui");

        System.out.println(m.find());
        System.out.println(m.group(1));
    }
}


