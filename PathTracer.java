import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class PathTracer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PathTracer extends AlwaysTurningActor
{    
    private Collection<Tank> ignore;
    
    public PathTracer(Collection<Tank> ignoreList) {
        super();
        distance=8;
        ignore=ignoreList;
    }

    public PathTracer() {
        this(new ArrayList<Tank>());
    }

    public Tank followPath() {
        for(int i=0; i<108; i++) {
            int d=bounceAngle();
            setRotation(d);
            move();
            List<Tank>neighboring=getIntersectingObjects(Tank.class);
            if(!neighboring.isEmpty()) {
                if(!ignore.containsAll(neighboring)) {
                    getWorld().removeObject(this);
                    return neighboring.get(0);
                }
            }
        }
        return lineOfSight();
    }

    public Tank lineOfSight() {
        while(!blocked()) {
            move();
            List<Tank>neighboring=getIntersectingObjects(Tank.class);
            if(!neighboring.isEmpty()) {
                if(!ignore.containsAll(neighboring)) {
                    getWorld().removeObject(this);
                    return neighboring.get(0);
                }
            }
            if(getY()>getWorld().getHeight()-75) {
                getWorld().removeObject(this);
                return null;
            }
        }
        getWorld().removeObject(this);
        return null;
    }

    public Tank followLaserPath() {
        while(getX()!=0&&getY()!=0&&getX()!=getWorld().getWidth()-1&&getY()!=getWorld().getHeight()-1) {
            move();
            List<Tank>neighboring=getObjectsInRange(32, Tank.class);
            if(!neighboring.isEmpty()) {
                if(!ignore.containsAll(neighboring)) {
                    getWorld().removeObject(this);
                    return neighboring.get(0);
                }
            }
            if(getY()>getWorld().getHeight()-75) {
                getWorld().removeObject(this);
                return null;
            }
        }
        getWorld().removeObject(this);
        return null;
    }
    
    @Override
    public void act() {
        getWorld().removeObject(this);
    }
}
