import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class Key here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Key extends myActor
{
    String letter;
    GreenfootImage depressed;
    GreenfootImage released;
    UIElement label;

    public Key(char c) {
        this(""+c);
    }

    public Key(String str) {
        super();
        letter=str;
        switch(letter.length()) {
            case 0:
            case 1:
            label=new UIElement(letter);
            break;
            
            case 2:
            label=new UIElement(letter, 25);
            break;
            
            case 3:
            label=new UIElement(letter, 20);
            break;
            
            case 4:
            label=new UIElement(letter, 17);
            break;
            
            case 5:
            label=new UIElement(letter, 15);
            break;
            
            default:
            label=new UIElement(letter, 75/letter.length());
        }
        released=new GreenfootImage("Key.png");
        depressed=new GreenfootImage("KeyDown.png");
        setImage(released);
    }

    @Override
    public void addedToWorld(World w) {
        w.addObject(label, getX(), getY());
    }

    @Override
    public void removedFromWorld() {
        getWorld().removeObject(label);
    }

    /**
     * Act - do whatever the Key wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    @Override
    public void act() 
    {
        if(Greenfoot.isKeyDown(letter)) {
            setImage(depressed);
        }
        else {
            setImage(released);
        }
        label.setLocation(getX(), getY());
    }    
}
