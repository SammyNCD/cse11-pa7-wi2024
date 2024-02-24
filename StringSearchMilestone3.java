import java.nio.file.*;
import java.io.IOException;
class FileHelper {
    static String[] getLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path)).toArray(String[]::new);
        }
        catch(IOException e) {
            System.err.println("Error reading file " + path + ": " + e);
            return new String[]{"Error reading file " + path + ": " + e};
        }
    }
}
interface Query{
    boolean Matches(String s);
}
class StringSearch{
    public static void main(String[] args) {
        if (args.length == 1) {
            String[] lines = FileHelper.getLines(args[0]);
            for (String line: lines) {
                System.out.println(line);
            }
        }
    }
}
