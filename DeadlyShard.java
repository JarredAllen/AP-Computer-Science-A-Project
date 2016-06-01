import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class DeadlyShard here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DeadlyShard extends Bullet
{
    boolean moving;
    String prefix;
    int frame;
    int id;

    public final double frameChangeChance=.4;

    public DeadlyShard(Trap t) {
        super(t.getCreator());
        distance=12;
        moving=true;
        id=t.getID();
        try {
            id=t.getID();
            prefix="Tank"+id+"p5f";
        }
        catch(NullPointerException npe) {
            prefix="Tank0p5f";
        }
        setImage(prefix+"0.png");
        distance=5;
    }

    @Override
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        setRotation((int)(Math.random()*360));
    }

    /**
     * Act - do whatever the Shard wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    @Override
    public void act() 
    {
        if(moving){
            turn(-45);
            if(blocked()) {
                moving=false;
            }
            else {
                move();
            }
            turn(45);
        }
        if(Math.random()<frameChangeChance) {
            frame++;
            if(frame>11) {
                getWorld().removeObject(this);
            }
            else {
                setImage(prefix+frame+".png");
            }
        }
        if(getWorld()!=null) {
            List<Tank>neighboring=getIntersectingObjects(Tank.class);
            for(Tank a:neighboring) {
                if(debug) {
                    System.out.println("Destroying "+a.toString()+".");
                }
                if(a.killed()==-1) {
                    a.destroyed(this);
                }
            }
        }
    }
}
