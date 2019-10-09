import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPRequestTest {

    @Test
    void emptyBody() throws InvalidRequestException {
        String rawRequest = "POST / HTTP/1.1\r\n" +
                "Host: localhost:3000\r\n" +
                "User-Agent: insomnia/7.0.0\r\n" +
                "Accept: */*";


        HTTPRequest request = new HTTPRequest(rawRequest);

        assertEquals(request.getMethod(), HTTPMethod.POST);
        assertEquals(request.getRawPath(), "/");
        assertEquals(request.getPath(), "/");
        assertNull(request.getBody());

        assertEquals(request.getHeader("accept"), "*/*");
        assertEquals(request.getHeader("Accept"), "*/*");

    }

    @Test
    void parseCorrectRequest() throws InvalidRequestException {
        String rawRequest = "POST / HTTP/1.1\r\n" +
                "Host: localhost:3000\r\n" +
                "User-Agent: insomnia/7.0.0\r\n" +
                "Accept: */*\r\n" +
                "Content-Length: 22\r\n" +
                "\r\n" +
                "test\r\n" +
                "sdfsdf\r\n" +
                "sdfsdfsdfs";


        HTTPRequest request = new HTTPRequest(rawRequest);

        assertEquals(request.getMethod(), HTTPMethod.POST);
        assertEquals(request.getRawPath(), "/");
        assertEquals(request.getPath(), "/");
        assertEquals(request.getBody(), "test\r\nsdfsdf\r\nsdfsdfsdfs");

        assertEquals(request.getHeader("accept"), "*/*");
        assertEquals(request.getHeader("Accept"), "*/*");
    }

    @Test
    void emptyRequest() {
        assertThrows(InvalidRequestException.class, () -> {
            new HTTPRequest("");
        });
    }

    @Test
    void incorrectLineEndings() {
        assertThrows(InvalidRequestException.class, () -> {
            String rawRequest = "POST / HTTP/1.1\n" +
                    "Host: localhost:3000\n" +
                    "User-Agent: insomnia/7.0.0\n" +
                    "Accept: */*\n" +
                    "Content-Length: 22\n" +
                    "\n" +
                    "test\n" +
                    "sdfsdf\n" +
                    "sdfsdfsdfs";

            new HTTPRequest(rawRequest);
        });
    }

    @Test
    void getQueryParam() throws InvalidRequestException {
        String rawRequest = "GET /ping?test=1 HTTP/1.1\r\n" +
                "Host: localhost:3000\r\n" +
                "User-Agent: insomnia/7.0.0\r\n" +
                "Accept: */*";

        HTTPRequest request = new HTTPRequest(rawRequest);

        assertEquals(request.getPath(), "/ping");
        assertEquals(request.getRawPath(), "/ping?test=1");
        assertEquals(request.getQueryParam("test"), "1");
    }

    @Test
    void getSoloQueryParam() throws InvalidRequestException {
        String rawRequest = "GET /ping?test HTTP/1.1\r\n" +
                "Host: localhost:3000\r\n" +
                "User-Agent: insomnia/7.0.0\r\n" +
                "Accept: */*";

        HTTPRequest request = new HTTPRequest(rawRequest);

        assertEquals(request.getPath(), "/ping");
        assertEquals(request.getRawPath(), "/ping?test");
        assertEquals(request.getQueryParam("test"), "");
    }
}