import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class SystemeIO {
    static Path baseDir = Path.of(System.getProperty("user.dir"), "public");

    static byte[] readFile(String file) {
        try {
            Path path = Path.of(baseDir.toString(), file);
            System.out.println("[SYSTEM IO] Reading: " + path);

            return Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void writeFile(String file, String content) throws IOException {
        Path path = Path.of(baseDir.toString(), file);
        Files.write(path, content.getBytes());
    }

    static void appendToFile(String file, String content) throws IOException {
        Path path = Path.of(baseDir.toString(), file);
        Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
    }

    static boolean fileExists(String file){
        Path path = Path.of(baseDir.toString(), file);
        System.out.println(path);
        return path.toFile().exists() && !path.toFile().isDirectory();
    }

    static Boolean deleteFile(String file) throws Exception {
        Path path = Path.of(baseDir.toString(), file);
        return path.toFile().delete();
    }
}
