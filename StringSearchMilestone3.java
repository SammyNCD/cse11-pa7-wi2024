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
    boolean matches(String s);
}

class Starts implements Query {
    String start;

    Starts(String start) {
        this.start = start.substring(start.indexOf("'") + 1, start.lastIndexOf("'"));
    }

    public boolean matches(String s) {
        return s.indexOf(this.start) == 0;
    }
}

class ContainsQuery implements Query{
    String str;

    ContainsQuery(String str) {
        this.str = str.substring(str.indexOf("'") + 1, str.lastIndexOf("'"));
    }
    public boolean matches(String s) {
        return s.contains(this.str);
    }
}

class StringSearch{

    static Query readQuery(String q) {
        String[] queryParts = q.split("=");
        if (queryParts.length < 2) {
            System.out.println("Invalid query, please try again");
        }

        String query = queryParts[0].trim();
        String value = queryParts[1].trim();

        switch (query) {
            case "Starts":
                return new Starts(value);
            case "Contains":
                return new ContainsQuery(value);
            default:
                System.out.println("Unsupported query type: " + query);
                return null;
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            String[] lines = FileHelper.getLines(args[0]);
            for (String line: lines) {
                System.out.println(line);
            }
        } else if (args.length == 2) {
            String[] lines = FileHelper.getLines(args[0]);
            String query = args[1];
            Query q1 = StringSearch.readQuery(query);
            for (String line : lines) {
                if (q1.matches(line)) {
                    System.out.println(line);
                }
            }
        }
    }
}