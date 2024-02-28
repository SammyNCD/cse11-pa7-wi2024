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
class ContainsQuery{
    String str;

    ContainsQuery(String str) {
        this.str = str.substring(str.indexOf("'") + 1, str.lastIndexOf("'"));
    }
    boolean matches(String s) {
        return s.contains(this.str);
    }
    
}
class StringSearch{
    public static void main(String[] args) {
        if (args.length == 1) {
            String[] lines = FileHelper.getLines(args[0]);
            for (String line: lines) {
                System.out.println(line);
            }
        } else if (args.length == 2) {
            String[] lines = FileHelper.getLines(args[0]);
            ContainsQuery query = new ContainsQuery(args[1]);
            for (String line : lines) {
                if (query.matches(line)) {
                    System.out.println(line);
                }
            }
        }
    }
}