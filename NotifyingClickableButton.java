import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class NotifyingClickableButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NotifyingClickableButton extends ClickableButton implements Notifier
{
    private List<Notifyable> toNotify;
    public NotifyingClickableButton(List<Notifyable> dependencies) {
        super();
        toNotify=dependencies;
    }
    
    public void press() {
        super.press();
        notifyDependents();
    }
    
    public void notifyDependents() {
        for(Notifyable n:toNotify) {
            n.notify(this);
        }
    }
}
