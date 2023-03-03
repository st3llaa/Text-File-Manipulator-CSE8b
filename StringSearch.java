import java.nio.file.*;
import javax.swing.TransferHandler;
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
class ContainsQuery implements Query{
    String s;
    ContainsQuery(String s){
        this.s = s.substring(s.indexOf("=")+1, s.length());
    }
    public boolean matches(String check){
        if(check.contains(this.s)){
            return true;
        }
        else{
            return false;
        }
    }
}
class LengthQuery implements Query{ //based on the number of characters : NOT WORDS
    int len;
    LengthQuery(String q){
        this.len = Integer.parseInt(q.substring(q.indexOf("=")+1, q.length()));
    }
    public boolean matches(String check){
        if(check.length() == this.len)
            return true;
        else{
            return false;
        }
    }
}
class GreaterQuery implements Query{
    int num;
    GreaterQuery(String s){
        this.num = Integer.parseInt(s.substring(s.indexOf("=")+1), s.length());
    }
    public boolean matches(String check){
        if(check.length() > num)
            return true;
        else{
            return false;
        }
    }
}
class LessQuery implements Query{
    int num;
    LessQuery(String s){
        this.num = Integer.parseInt(s.substring(s.indexOf("=")+1, s.length()));
    }
    public boolean matches(String check){
        if(check.length() < num)
            return true;
        else{
            return false;
        }
    }
}
class StartsQuery implements Query{
    String word;
    StartsQuery(String s){
        this.word = s.substring(s.indexOf("=")+1, s.length());
    }
    public boolean matches(String check){
        String begginingWord = check.substring(0, check.indexOf(" "));
        if(begginingWord.equals(this.word))
            return true;
        else{
            return false;
        }
    }
}
class EndsQuery implements Query{
    String end;
    EndsQuery(String s){
        this.end = " " + s.substring(s.indexOf("=")+1, s.length());
    }
    public boolean matches(String check){
        boolean answer = false;
        if(check.contains(this.end)){
            //check that the conditional is actually at the end of the string
            String endOfCheck = check.substring(check.indexOf(this.end), check.length());
           // System.out.println(endOfCheck);
            if(endOfCheck.equals(end)){
                answer = true;
            }
        }
        return answer;
    }
}
class NotQuery implements Query{
    Query not;
    NotQuery(Query q){
        this.not = q;
    }
    public boolean matches(String check){
        return (!this.not.matches(check));
    }
}

interface Transform{
    String transform(String s);
}

class StringSearch{
    //takes query string in the form "<keyword>=<type>"
    static Query readQuery(String q){ 
        String keyword = q.substring(0, q.indexOf("="));
        Query results;
        if(q.contains("length")){
            results = new LengthQuery(q);
        }
        else if(q.contains("greater")){
            results = new GreaterQuery(q);
        }
        else if(q.contains("less")){
            results = new LessQuery(q);
        }
        else if(q.contains("contains")){
            results = new ContainsQuery(q);
        }
        else if(q.contains("starts")){
            results = new StartsQuery(q);
        }
        else if(q.contains("ends")){
            results =  new EndsQuery(q);
        }
        else{ //not query
            String query = q.substring(q.indexOf("("), q.length()-1); //appends to be just the new query statement
            System.out.println("query passed into query reader = " + query);
            results = readQuery(query);
        }
        return results;
    }
    public static void main(String[] args){ //one arguement in terminal
        String[] lines = FileHelper.getLines(args[0]);
        if(args.length == 1){ //prints all lines of given file
            for(int i =0; i<lines.length; i++){
                System.out.println(lines[i]);
            }
        }
        else if(args.length == 2){ //two arguments in terminal
            Query current = readQuery(args[1]);
            for(int i = 0; i < lines.length; i++){ //loops through every line
                if(current.matches(lines[i])){
                    System.out.println(lines[i]);
                }
            }
        }
    }
}
