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
class ContainsQuery{
    String s;
    ContainsQuery(String s){
        this.s = s;
    }
    boolean matches(String check){
        if(check.contains(this.s)){
            return true;
        }
        else{
            return false;
        }
    }
}
class StringSearch{
    public static void main(String[] args){
        String[] lines = FileHelper.getLines(args[0]);
        if(args.length == 1){ //prints all lines of given file
            for(int i =0; i<lines.length; i++){
                System.out.println(lines[i]);
            }
        }
        else if(args.length == 2){
            ContainsQuery current = new ContainsQuery(args[1]);
            for(int i = 0; i < lines.length; i++){ //loops through every line
                if(current.matches(lines[i]))
                    System.out.println(lines[i]);
            }
        }
    }
}