import java.io.IOException;
import java.util.HashMap;

public class Router {
    @FunctionalInterface
    public interface Action<A, B> {
        public void apply(A a, B b) throws IOException;
    }

    String basePath;

    private HashMap<HTTPMethod, HashMap<String, Action<HTTPRequest, HTTPResponse>>> executors = new HashMap<>();

    public Router() {
        this("");
    }

    public Router(String basePath) {
        this.basePath = basePath;
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
}


