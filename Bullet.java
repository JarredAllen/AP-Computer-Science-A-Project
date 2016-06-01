import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Bullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bullet extends AlwaysTurningActor
{
    private int frames;
    private Tank creator;
    private boolean armed;

    public Bullet() {
        this(null, null, 8);
    }
    
    public Bullet(Collection<Actor> actors) {
        this(actors, null, 8);
    }

    public Bullet(Tank c) {
        this(c,8);
    }
    
    public Bullet(Tank t, int d) {
        this(new ArrayList<Actor>(), t, d);
    }
    
    public Bullet(Collection<Actor> actors, Tank c, int d) {
        super(actors);
        creator=c;
        distance=d;
        armed=false;
        try {
            setImage("images/Bullet"+Math.abs(c.getID()%4)+".png");
        }catch(NullPointerException e) {}
    }

    public void addedToWorld(World w) {
        frames=0;
    }

    public void act() 
    {
        if(frames>110&&blocked()) {
            if(creator!=null) {
                creator.endBullet(this);
            }
            getWorld().removeObject(this);
            return;
        }
        if(!getIntersectingObjects(Tank.class).contains(creator)) {
            armed=true;
        }
        if(blocked()) {
            int d=bounceAngle();
            setRotation(d);
            armed=true;
        }
        move();
        frames++;
        if(armed) {
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

    public Tank getTank() {
        return creator;
    }
}
