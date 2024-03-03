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

interface Transform {
    String transform(String s);
}

class Starts implements Query {
    String start;

    Starts(String start) {
        this.start = start.substring(start.indexOf("'") + 1, start.lastIndexOf("'"));
    }

    public boolean matches(String s) {
        return s.startsWith(start);
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

class First implements Transform {
    String num;
    First(String num) {
        this.num = num;
    }

    public String transform(String s) {
        int val = Integer.parseInt(this.num);
        if (s.length() < val) {
            return s;
        } else {
            return s.substring(0, val);
        }
    }
}

class StringSearch{

    static Query readQuery(String q) {
        String[] queryParts = q.split("=");
        if (queryParts.length < 2) {
            System.out.println("Invalid query, please try again");
        }

        String query = queryParts[0].trim(); // for queries w/ no = can do if elif else where these are created in those statements
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

    static Transform readTransform(String t) {
        String[] transformParts = t.split("=");
        if (transformParts.length < 2) {
            System.out.println("Invalid query, please try again");
        }

        String transform = transformParts[0].trim(); // for queries w/ no = can do if elif else where these are created in those statements
        String value = transformParts[1].trim();

        switch (transform) {
            case "First":
                return new First(value);
            default:
                System.out.println("Unsupported query type: " + transform);
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
        } else {
            String[] lines = FileHelper.getLines(args[0]);
            String query = args[1];
            String transform = args[2];
            Query q1 = StringSearch.readQuery(query);
            Transform t1 = StringSearch.readTransform(transform);
            for (String line : lines) {
                if (q1.matches(line)) {
                    String output = t1.transform(line);
                    System.out.println(output);
                }
            }


        }
    }
}