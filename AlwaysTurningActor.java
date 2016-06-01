import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class AlwaysTurningActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class AlwaysTurningActor extends myActor
{
    public AlwaysTurningActor() {
        super();
    }
    
    public AlwaysTurningActor(Collection<Actor> actors) {
        super(actors);
    }
    
    @Override 
    public void turn(int deg) {
        super.turn(deg, true);
    }
}
