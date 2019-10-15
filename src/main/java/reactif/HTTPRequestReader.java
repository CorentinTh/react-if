package reactif;

import reactif.exeptions.InvalidRequestException;

import java.io.BufferedReader;
import java.io.IOException;

public class HTTPRequestReader {
    private final BufferedReader reader;

    public HTTPRequestReader(BufferedReader reader) {
        this.reader = reader;
    }


    public HTTPRequest getRequest() throws InvalidRequestException {

        try {
            String line;
            boolean hasBody = false;
            int contentLength = 0;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                stringBuilder.append(line).append("\r\n");

                if (line.toLowerCase().matches("content-length: [0-9]+")) {
                    contentLength = Integer.parseInt(line.split(": ")[1]);
                    hasBody = true;
                }
            }

            if(hasBody){
                stringBuilder.append("\r\n");

                for (int i = 0; i < contentLength; ++i){
                    stringBuilder.append((char) reader.read());
                }
            }

            String rawRequest = stringBuilder.toString();

            System.out.println(rawRequest);

            return new HTTPRequest(rawRequest);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
