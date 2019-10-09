import java.util.Scanner;

public class File {

    public static String readFile(String path) {
        try {
            return new Scanner(File.class.getResourceAsStream(path)).useDelimiter("\\A").next();
        } catch (Exception e) {
            return null;
        }
    }
}
