import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;
import java.io.*;

/**
 * Write a description of class TestDriveWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TestDriveWorld extends MazeWorld implements Notifyable
{
    private NotifyingClickableButton toOptions;
    private NotifyingClickableButton moreHelp;
    private Tank[] playerTanks;

    /**
     * Constructor for objects of class TestDriveWorld.
     * 
     */
    public TestDriveWorld()
    {
        super(10, 11);
        loadMazeFromFile("Box.mzw");
        List<Notifyable> This=new myList<Notifyable>(this);
        toOptions=new NotifyingClickableButton(This);
        addObject(toOptions, 250, 640);
        UIElement text=new UIElement("Go back\nto options", 12, Color.WHITE);
        addObject(text, 250, 640);

        moreHelp=new NotifyingClickableButton(This);
        addObject(moreHelp, 500, 640);
        text=new UIElement("More help", 12, Color.WHITE);
        addObject(text, 500, 640);

        for(int i=1; i<=4; i++) {
            Tank t=new ImmobileTank(i-1);
            addObject(t, 150*i, 675);
            String controls=t.getControls();
            if(controls.equals("mouse")) {
                addObject(new Mouse(), 150*i, 725);
            }
            else {
                String w=""+controls.charAt(0);
                String a=""+controls.charAt(1);
                String s=""+controls.charAt(2);
                String d=""+controls.charAt(3);
                String q=""+controls.charAt(4);
                if(controls.length()>5) {
                    w="up";
                    a="left";
                    s="down";
                    d="right";
                    q="enter";
                }
                addObject(new Key(w), 150*i-2, 700);
                addObject(new Key(a), 150*i-37, 735);
                addObject(new Key(s), 150*i-2, 770);
                addObject(new Key(d), 150*i+33, 735);
                addObject(new Key(q), 150*i-2, 735);
            }
        }
        playerTanks=new Tank[4];
        for(int i=0; i<4;i++) {
            playerTanks[i]=new Tank(i);
            spawnTank(playerTanks[i]);
        }
    }

    public void notify(Notifier b) {
        if(b==toOptions) {
            if(OptionsScreen.current==null) {
                new OptionsScreen().setMain();
            }
            else {
                OptionsScreen.current.setMain();
            }
            return;
        }
        if(b==moreHelp) {
            printExtraHelp();
        }
    }

    @Override
    public void spawnTank(Tank t) {
        addObject(t, (int)(700*Math.random())+25, (int)(550*Math.random())+25);
        while(t.getIntersectingActors(Tank.class).size()>0) {
            t.setLocation((int)(700*Math.random())+25, (int)(550*Math.random())+25);
        }
    }

    @Override
    public void act() {
        for(int i=0; i<4; i++) {
            if(!getObjects(Tank.class).contains(playerTanks[i])) {
                playerTanks[i]=new Tank(i);
                spawnTank(playerTanks[i]);
            }
        }
    }

    public void printExtraHelp() {
        Greenfoot.stop();
        try {
            for(String s:FileIO.loadFile("help.txt").split("\n\n")) {
                System.out.println(s);
                newWait(1000);
                System.out.println();
            }
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("Error reading help file");
        }
    }
}
