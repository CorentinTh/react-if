import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPRequestTest {

    @Test
    void requestParsing() throws InvalidRequestException {
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
        assertEquals(request.getPath(), "/");

    }
}