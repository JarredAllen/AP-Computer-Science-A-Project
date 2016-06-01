import java.util.*;
import java.io.*;
import greenfoot.*;

/**
 * Write a description of class FileIO here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FileIO {

    public static void writeActorsToFile(List<Actor> actors, String name) throws FileNotFoundException {
        if(!name.substring(Math.max(0, name.length()-6)).contains(".")) {
            name+=".mzw";
        }
        PrintWriter writer=new PrintWriter(name);
        writer.println(actors.toString());
        writer.close();
    }
    
    public static void appendToFile(String name, String line) throws FileNotFoundException {
        if(!name.substring(Math.max(0, name.length()-6)).contains(".")) {
            name+=".mzw";
        }
        if(!line.substring(line.length()-1).equals("\n")) {
            line+="\n";
        }
        String file=loadFile(name);
        PrintWriter writer=new PrintWriter(name);
        writer.println(file+line);
        writer.close();
    }

    public static String loadFile(String name) throws FileNotFoundException {
        if(!name.substring(Math.max(0, name.length()-6)).contains(".")) {
            name+=".mzw";
        }
        File file=new File(name);
        Scanner in=new Scanner(file);
        String text="";
        while(in.hasNextLine()) {
            text+=in.nextLine();
            if(in.hasNextLine()) {
                text+="\n";
            }
        }
        return text;
    }

    public static String readControls(boolean human) throws FileNotFoundException {
        if(!human) {
            return loadFile("controls.txt");
        }
        Scanner controls=new Scanner(new File("controls.txt"));
        String out="";
        for(int i=1; i<=4; i++) {
            Scanner line=new Scanner (controls.nextLine());
            out+="Player "+i+": "+line.next()+" to shoot and "+line.nextLine()+" to move.\n";
        }
        return out;
    }
    public static String readControls() throws FileNotFoundException {
        return readControls(true);
    }
    
    public static int countLines(String name) throws FileNotFoundException {
        return loadFile(name).split("\n").length;
    }
    
    public static int stringLines(String str) {
        return str.split("\n").length;
    }
    
    public static long sumLines(String[] names) throws FileNotFoundException {
        long sum=0;
        for(String name:names) {
            sum+=countLines(name);
        }
        return sum;
    }
    
    public static long sumLines(File[] files) throws FileNotFoundException {
        long sum=0;
        for(File file:files) {
            sum+=countLines(file.getPath());
        }
        return sum;
    }
    
    public static String absolutePathOfProject() throws FileNotFoundException {
        return new File("").getAbsolutePath();
    }
    
    public static long linesOfCode() throws FileNotFoundException {
        return sumLines(getCode());
    }
    
    public static File[] getCode() throws FileNotFoundException {
        return new File(absolutePathOfProject()).listFiles(new isJavaCode());
    }
    
    public static int classes() throws FileNotFoundException {
        return getCode().length;
    }
}
