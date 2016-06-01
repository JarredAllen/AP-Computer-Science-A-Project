import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Trap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Trap extends myActor
{
    private int transparency;
    private Tank parent;
    boolean fading;
    int explodeFrames;
    int id;

    public Trap(Tank t) {
        super();
        transparency=255;
        parent=t;
        fading=true;
        explodeFrames=-1;
        try {
            id=t.getID();
        }
        catch(NullPointerException npe) {
            id=0;
        }
    }

    @Override
    public void addedToWorld(World w) {
        setImage("beeper0.png");
    }

    /**
     * Act - do whatever the Trap wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(fading) {
            if(transparency!=0) {
                transparency-=5;
                getImage().setTransparency(transparency);
            }
            else {
                fading=false;
            }
        }
        List<Tank> near=getObjectsInRange(40, Tank.class);
        near.remove(parent);
        if(explodeFrames>=0) {
            explodeFrames++;
        }
        if(!near.isEmpty()&&explodeFrames==-1) {
            fading=false;
            explodeFrames++;
            getImage().setTransparency(255);
        }
        if(explodeFrames==12) {
            explode();
        }
    }

    public void explode() {
        for(int i=0; i<30;i++) {
            getWorld().addObject(new DeadlyShard(this), getX(), getY());
        }
        getWorld().removeObject(this);
    }

    public int getID() {
        return id;
    }
    
    public Tank getCreator() {
        return parent;
    }
}
