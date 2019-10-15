package reactif;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdExecutor {
    static String execute(String cmd) throws Exception {
        java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");

        return s.hasNext() ? s.next() : "";
    }
}
