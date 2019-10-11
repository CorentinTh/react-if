import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class SystemeIO {
    static Path baseDir = Path.of(System.getProperty("user.dir"), "public");

    /**
     * Get the webserver base directory
     * @return {String} Webserver base directory
     */
    public static Path getBaseDir() {
        return baseDir;
    }

    /**
     * Read the content of a file
     * @param filePath path of the file to read
     * @return {byte[]|null} the content of the file to read or null if something occur
     */
    static byte[] readFile(String filePath) {
        try {
            Path path = Path.of(baseDir.toString(), filePath);
            System.out.println("[SYSTEM IO] Reading: " + path);

            return Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Write content to a file
     * @param file path of the file to write to
     * @param content content to write to the file
     * @throws IOException in case of impossibility to write
     */
    static void writeFile(String file, String content) throws IOException {
        Path path = Path.of(baseDir.toString(), file);
        Files.write(path, content.getBytes());
    }

    /**
     * Append content to a file
     * @param file path of the file to append content to
     * @param content content to write to the file
     * @throws IOException in case of impossibility to write
     */
    static void appendToFile(String file, String content) throws IOException {
        Path path = Path.of(baseDir.toString(), file);
        Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * Check if a file exists
     * @param file path of the file to check
     * @return {boolean} true if it exists, false instead
     */
    static boolean fileExists(String file){
        Path path = Path.of(baseDir.toString(), file);
        return path.toFile().exists() && !path.toFile().isDirectory();
    }

    /**
     * Check if a path correspond to a directory
     * @param file path to check if it's a directory
     * @return {boolean} true if directory, false instead
     */
    static boolean isDirectory(String file){
        Path path = Path.of(baseDir.toString(), file);
        return path.toFile().exists() && path.toFile().isDirectory();
    }

    /**
     * Delete a file
     * @param file path of the file to delete
     * @return {boolean} true if the file has been deleted, false instead
     */
    static Boolean deleteFile(String file){
        Path path = Path.of(baseDir.toString(), file);
        return path.toFile().delete();
    }

}
