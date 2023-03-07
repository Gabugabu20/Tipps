package model;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextFileReader {
    public String[] loadTextFile(String name) {
        try {
            return Files.readAllLines(Paths.get("resources", name + ".txt")).toArray(new String[] {});
        } catch (IOException iox) {
            throw new RuntimeException(iox);
        }
    }

    public static int getNumberOfLevels() {
        try {
            return (int) Files.list(Paths.get("resources", "maps"))
                    .filter(p -> p.toFile().getName().endsWith(".txt"))
                    .count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
