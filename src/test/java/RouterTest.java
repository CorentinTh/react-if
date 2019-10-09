import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    @Test
    void simpleRoute() {

        Router.Action<HTTPRequest, HTTPResponse> callback = (httpRequest, httpResponse) -> {};
        Router router = new Router();

        router.on(HTTPMethod.GET, "/foo", callback);

        Pair<Router.Action<HTTPRequest, HTTPResponse>, Matcher> actionMatcherPair = router.getAction(HTTPMethod.GET, "/foo");

        assertEquals(actionMatcherPair.getKey(), callback);
    }

    @Test
    void routeWithParam() {

        Router.Action<HTTPRequest, HTTPResponse> callback = (httpRequest, httpResponse) -> {};
        Router router = new Router();

        router.on(HTTPMethod.GET, "/foo/:bar", callback);

        assertNull(router.getAction(HTTPMethod.GET, "/foo"));
        assertNull(router.getAction(HTTPMethod.GET, "/foo/bar/foo"));

        Pair<Router.Action<HTTPRequest, HTTPResponse>, Matcher> actionMatcherPair = router.getAction(HTTPMethod.GET, "/foo/value");

        assertEquals(actionMatcherPair.getKey(), callback);
        assertEquals(actionMatcherPair.getValue().group("bar"), "value");
    }
}