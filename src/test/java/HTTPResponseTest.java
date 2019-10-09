import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPResponseTest {

    @Test
    void emptyResponse() {
        HTTPResponse response = new HTTPResponse();

        assertEquals(response.getRawHTTP(), "HTTP/1.0 404 Not Found\r\nX-Powered-By: react-if");
    }

    @Test
    void sendHTML() {
        HTTPResponse response = new HTTPResponse();

        response.sendHTML("okzezef");

        assertEquals(response.getRawHTTP(), "HTTP/1.0 200 OK\r\n"+
                "X-Powered-By: react-if\r\n"+
                "Content-Type: text/html\r\n"+
                "\r\n"+
                "okzezef");
    }

    @Test
    void send404() {
        HTTPResponse response = new HTTPResponse();

        response.sendWithStatus(HTTPStatusCode.NOT_FOUND);

        assertEquals(response.getRawHTTP(), "HTTP/1.0 404 Not Found\r\nX-Powered-By: react-if");
    }
}