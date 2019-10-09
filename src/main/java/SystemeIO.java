import java.io.FileReader;
import java.nio.file.Path;
import java.util.Scanner;

public class SystemeIO {
    static Path baseDir = Path.of(System.getProperty("user.dir"), "public");

    public static String readFile(String file) {
        try {
            Path path = Path.of(baseDir.toString(), file);
            System.out.println("[SYSTEM IO] Reading: " + path);
            return new Scanner(path.toFile()).useDelimiter("\\A").next();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
