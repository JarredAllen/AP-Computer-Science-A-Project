import java.io.*;


public class hasExtension implements FileFilter {

    private String extension;

    /**
     * @param ext does not include the <code>"."</code>.
     */
    public hasExtension(String ext) {
        extension=ext;
    }

    public boolean accept(File file) {
        if(!file.exists()) {
            return false;
        }
        try {
            String name=file.getPath();
            name=name.substring(name.lastIndexOf("\\")+1);
            return name.substring(name.length()-extension.length()-1).equals("."+extension)&&Character.isLetter(name.charAt(0));
        }
        catch(StringIndexOutOfBoundsException sioobe) {
            return false;
        }
    }
}