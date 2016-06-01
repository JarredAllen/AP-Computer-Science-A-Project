import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class LaserGUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LaserGUI extends Bullet
{
    private int frames;
    private Tank shooter;
    
    public LaserGUI(Tank t) {
        super(new myList<Actor>(t));
        shooter=t;
        frames=0;
    }
    
    public void act() {
        List<Tank>next=getIntersectingObjects(Tank.class);
        next.remove(shooter);
        if(!next.isEmpty()) {
            for(Tank t:next) {
                t.destroyed(this, false);
            }
        }
        if(frames>22) {
            getWorld().removeObject(this);
        }
        frames++;
    }
    
    @Override
    public void addedToWorld(World w) {}
    
    @Override
    public Tank getTank() {
        return shooter;
    }
}
