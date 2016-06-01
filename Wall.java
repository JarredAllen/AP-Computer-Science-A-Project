import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends AlwaysTurningActor
{    
    public Wall() {
        distance=0;
    }

    public void act() 
    {
        if(debug) {
            if(Greenfoot.isKeyDown(" ")) {
                System.out.println(getIntersectingObjects(Wall.class));
            }
        }
    }
    
    public void handleEdges() {
        if(getRotation()==0) {
            List<Wall> walls=getWorld().getObjectsAt(getX(), getY()+38, Wall.class);
            walls.remove(this);
            if(walls.isEmpty()) {
                Wall2 w=new Wall2();
                getWorld().addObject(w, getX(), getY()+38);
                w.setRotation(90);
            }
            walls=getWorld().getObjectsAt(getX(), getY()-37, Wall.class);
            walls.remove(this);
            if(walls.isEmpty()) {
                Wall2 w=new Wall2();
                getWorld().addObject(w, getX(), getY()-37);
                w.setRotation(270);
            }
        }
        else {
            List<Wall> walls=getWorld().getObjectsAt(getX()+38, getY(), Wall.class);
            walls.remove(this);
            if(walls.isEmpty()) {
                Wall2 w=new Wall2();
                getWorld().addObject(w, getX()+38, getY());
            }
            walls=getWorld().getObjectsAt(getX()-37, getY(), Wall.class);
            walls.remove(this);
            if(walls.isEmpty()) {
                Wall2 w=new Wall2();
                getWorld().addObject(w, getX()-37, getY());
                w.setRotation(180);
            }
        }
    }
}
