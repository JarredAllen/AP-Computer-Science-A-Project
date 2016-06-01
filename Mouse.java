import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Mouse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Mouse extends myActor
{
    public Mouse() {
        super();
    }
    
    
    /**
     * Act - do whatever the Mouse wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        MouseInfo m=Greenfoot.getMouseInfo();
        if(m!=null&&m.getButton()>0) {
            setImage("MouseClicked.png");
        }
        else {
            setImage("Mouse.png");
        }
    }    
}
