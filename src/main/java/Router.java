import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    @FunctionalInterface
    public interface Action<A, B> {
        public void apply(A a, B b) throws IOException;
    }

    private HashMap<HTTPMethod, HashMap<Pattern, Action<HTTPRequest, HTTPResponse>>> executors = new HashMap<>();

    public Router() {
    }


    public void on(HTTPMethod method, String path, Action<HTTPRequest, HTTPResponse> action) {
        if (!executors.containsKey(method)) {
            executors.put(method, new HashMap<>());
        }

        // Transform "/user/:id" in "/user/(?<id>.*?)
        String regex = path.replaceAll(":(.*)", "(?<$1>[^/]+)"); // [^/]+ au lieu de .*?
        Pattern pattern = Pattern.compile(regex);

        executors.get(method).put(pattern, action);
    }

    public Pair<Action<HTTPRequest, HTTPResponse>, Matcher> getAction(HTTPMethod method, String path) {
        if (executors.containsKey(method)) {

            for (Map.Entry<Pattern, Action<HTTPRequest, HTTPResponse>> e : executors.get(method).entrySet()) {
                Matcher matcher = e.getKey().matcher(path);

                if (matcher.matches()) {
                    return new Pair<>(e.getValue(), matcher);
                }
            }

            return null;
        } else {
            return null;
        }
    }

//    public static void main(String[] args) {
//        String path = "/foo/:id";
//
//        String regex = path.replaceAll(":(.*)", "(?<$1>[^/]+)"); // [^/]+ au lieu de .*?
//        System.out.println(regex);
//        Matcher m = Pattern.compile(regex).matcher("/foo/bar");
//
//        System.out.println(m.matches());
//        System.out.println(m.group("id"));
//    }
}


