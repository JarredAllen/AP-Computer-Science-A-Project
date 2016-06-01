import greenfoot.*;
import java.awt.*;

public class UIElement extends AlwaysTurningActor{
    
    private int size;
    private Color color;
    private String text;
    
    public UIElement(String text, int size, Color c) {
        this(new GreenfootImage(text, size, c, null));
        this.size=size;
        this.text=text;
        this.color=c;
    }
    
    public UIElement(String text) {
        this(text, 30);
    }
    
    public UIElement(String text, int size) {
        this(text, size, null);
    }
    
    public UIElement(GreenfootImage image) {
        super();
        setImage(image);
        size=0;
        color=null;
        text=null;
    }

    public UIElement(){
        super();
    }
    
    public void changeText(String text) {
        setImage(new GreenfootImage(text, size, color, null));
        this.text=text;
    }
    
    public void changeText(String text, int fontSize) {
        setImage(new GreenfootImage(text, fontSize, color, null));
        this.text=text;
        this.size=fontSize;
    }
}