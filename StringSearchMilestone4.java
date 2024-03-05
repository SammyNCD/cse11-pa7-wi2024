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

class Length implements Query {
    String num;
    Length(String num){
        this.num = num;
    }

    public boolean matches(String s) {
        return s.length() == Integer.parseInt(this.num);
    }
}

class Greater implements Query {
    String num;
    Greater(String num){
        this.num = num;
    }

    public boolean matches(String s) {
        return s.length() > Integer.parseInt(this.num);
    }
}

class Less implements Query {
    String num;
    Less(String num){
        this.num = num;
    }

    public boolean matches(String s) {
        return s.length() < Integer.parseInt(this.num);
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

class Starts implements Query {
    String start;

    Starts(String start) {
        this.start = start.substring(start.indexOf("'") + 1, start.lastIndexOf("'"));
    }

    public boolean matches(String s) {
        return s.indexOf(this.start) == 0;
    }
}

class Ends implements Query {
    String end;
    Ends(String end) {
        this.end = end.substring(end.indexOf("'") + 1, end.lastIndexOf("'"));
    }

    public boolean matches(String s) {
        return s.endsWith(end);
    }
}

class Not implements Query {
    String str;
    Not(String str) {
        this.str = str;
    }

    public boolean matches(String s) {
        Query q = StringSearch.readQuery(str);
        return q.matches(s) == false;
    }
}

interface Transform {
    String transform(String s);
}

class Upper implements Transform {

    public String transform(String s) {
        return s.toUpperCase();
    }
}

class Lower implements Transform {
    public String transform(String s) {
        return s.toLowerCase();
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

class Last implements Transform {
    String num;
    Last(String num) {
        this.num = num;
    }

    public String transform(String s) {
        int val = Integer.parseInt(this.num);
        if (s.length() < val) {
            return s;
        } else {
            return s.substring(s.length() - val, s.length());
        }
    }
}

class Replace implements Transform {
    String str;
    String str1;
    Replace(String str, String str1) {
        this.str = str.substring(str.indexOf("'") + 1, str.lastIndexOf("'"));
        this.str1 = str1.substring(str1.indexOf("'") + 1, str1.lastIndexOf("'"));
    }

    public String transform(String s) {
        return s.replace(str, str1);
    }
}

class StringSearch{

    static Query readQuery(String q) {

        String query = "";
        String value = "";

        if (q.contains("=") && q.contains("(")) {
            query += q.substring(0, q.indexOf("("));
            value += q.substring(q.indexOf("(") + 1, q.indexOf(")"));
        } else if (q.contains("=") == true) {
            String[] queryParts = q.split("=");
            query += queryParts[0].trim(); // for queries w/ no = can do if elif else where these are created in those statements
            value += queryParts[1].trim();
        } else {
            System.out.println("Invalid query, please try again");
        }

        switch (query) {
            case "starts":
                return new Starts(value);
            case "contains":
                return new ContainsQuery(value);
            case "length":
                return new Length(value);
            case "greater":
                return new Greater(value);
            case "less":
                return new Less(value);
            case "ends":
                return new Ends(value);
            case "not":
                return new Not(value);
            default:
                System.out.println("Unsupported query type: " + query);
                return null;
        }
    }

    static Transform readTransform(String t) {

        String transform = "";
        String value = "";

        if (t.contains("=") != true) {
            transform += t;
        } else if (t.contains("=") == true) {
            String[] transformParts = t.split("=");
            transform += transformParts[0].trim(); // for queries w/ no = can do if elif else where these are created in those statements
            value += transformParts[1].trim();
        } else {
            System.out.println("Invalid query, please try again");
        }

        switch (transform) {
            case "upper":
                return new Upper();
            case "lower":
                return new Lower();
            case "first":
                return new First(value);
            case "last":                // "First" and "Last" need to be fixed, last/first characters
                return new Last(value); // not last/first words
            case "replace":
                String[] vals = value.split(";");
                return new Replace(vals[0], vals[1]);
            default:
                System.out.println("Unsupported transform type: " + transform);
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