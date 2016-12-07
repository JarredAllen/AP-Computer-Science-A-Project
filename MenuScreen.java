import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;
import java.io.*;

/**
 * Write a description of class MenuScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MenuScreen extends myWorld implements Notifyable
{
    private byte[] bytes;
    public static final File errStream=new File("errorlog.txt");
    
    /**
     * Constructor for objects of class MenuScreen.
     * 
     */
    public MenuScreen() {
        super(750,825,1);
        try {
            if(!errStream.exists()) {
                errStream.createNewFile();
            }
        }
        catch(IOException ioe) {}
        try {
            System.setErr(new PrintStream(errStream));
        }
        catch(FileNotFoundException fnfe) {}
        setMain();
        UIElement title=new UIElement("Tank Chaos", 100);
        addObject(title, 375, 200);
        UIElement name=new UIElement("by Jarred Allen, Matthew Cho, Sean Kim, and Samuel Guerrero",30);
        addObject(name, 375, 250);
        UIElement toOptionsLabel=new UIElement("Press here \n to start", 13, Color.WHITE);

        List<Notifyable> menu=new myList<Notifyable>(this);
        NotifyingClickableButton toOptions=new NotifyingClickableButton(menu);
        addObject(toOptions, 375, 500);
        addObject(toOptionsLabel, 377, 500);
        Greenfoot.start();
    }

    public void notify(Notifier b) {
        if(OptionsScreen.current==null) {
            new OptionsScreen().setMain();
        }
        else {
            OptionsScreen.current.setMain();
        }
    }

    private void startGame(int seconds, int hp, int cp, int[]cpDif) {
        MazeWorld mzw=new MazeWorld(getWidth()/75, getHeight()/75);
        mzw.setMain();
        mzw.startGame(seconds, hp, cp, cpDif);
    }
}
