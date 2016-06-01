import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Timer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Timer extends AlwaysTurningActor
{
    private long end;
    private UIElement time;
    
    public Timer(long millis) {
        end=millis+System.currentTimeMillis();
        int sec=secondsLeft()%60;
        int min=secondsLeft()/60;
        time=new UIElement(String.format("%02d", min)+":"+String.format("%2d", sec));
    }
    
    @Override
    public void addedToWorld(World w) {
        w.addObject(time, getX(), getY());
    }
    
    public void act() {
        int sec=secondsLeft()%60;
        int min=secondsLeft()/60;
        String seconds=""+sec;
        while(seconds.length()!=2) {
            seconds="0"+seconds;
        }
        time.changeText(min+":"+seconds);
        if(sec<0) {
            getWorld().removeObject(this);
        }
    }    
    
    @Override
    public void removedFromWorld() {
        getWorld().removeObject(time);
    }
    
    public int secondsLeft() {
        return (int)Math.ceil((end-System.currentTimeMillis())/1000);
    }
}
