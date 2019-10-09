import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPResponseTest {

    @Test
    void emptyResponse() {
        HTTPResponse response = new HTTPResponse();

        assertEquals(response.getRawHTTP(), "HTTP/1.0 500 Internal Server Error\r\nX-Powered-By: react-if");
    }

    @Test
    void sendHTML() {
        HTTPResponse response = new HTTPResponse();

        response.sendHTML("ok");

        assertEquals(response.getRawHTTP(), "HTTP/1.0 200 OK\r\nX-Powered-By: react-if\r\nContent-Type: text/html\r\n\r\nok");
    }

    @Test
    void send404() {
        HTTPResponse response = new HTTPResponse();

        response.sendWithStatus(HTTPStatusCode.NOT_FOUND);

        assertEquals(response.getRawHTTP(), "HTTP/1.0 404 Not Found\r\nX-Powered-By: react-if");
    }
}