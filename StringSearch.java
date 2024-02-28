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
        this.num = num.substring(num.indexOf("'") + 1, num.lastIndexOf("'"));
    }

    public boolean matches(String s) {
        return s.length() == Integer.parseInt(this.num);
    }
}

class Greater implements Query {
    String num;
    Greater(String num){
        this.num = num.substring(num.indexOf("'") + 1, num.lastIndexOf("'"));
    }

    public boolean matches(String s) {
        return s.length() > Integer.parseInt(this.num);
    }
}

class Less implements Query {
    String num;
    Less(String num){
        this.num = num.substring(num.indexOf("'") + 1, num.lastIndexOf("'"));
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
        int checkIndex = s.indexOf(s.length()-1);
        String[] words = s.split(" ");
        return words[checkIndex].equals(end);
    }
}

class Not implements Query {
    String str;
    Not(String str) {
        this.str = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
    }

    public boolean matches(String s) {
        return false;
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
        this.num = num.substring(num.indexOf("'") + 1, num.lastIndexOf("'"));
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
        this.num = num.substring(num.indexOf("'") + 1, num.lastIndexOf("'"));
    }

    public String transform(String s) {
        int val = Integer.parseInt(this.num);
        if (s.length() < val) {
            return s;
        } else {
            return s.substring(val);
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
            case "Replace":
                String[] vals = transformParts[1].split(";");
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