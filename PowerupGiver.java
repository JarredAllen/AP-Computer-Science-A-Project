import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PowerupGiver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerupGiver extends myActor
{
    private final int code;
    public static final int numberOfPowerups=4;
    
    public PowerupGiver() {
        super();
        code=(int)(Math.random()*numberOfPowerups);
    }
    
    @Override
    public void addedToWorld(World w) {
        GreenfootImage image=getImage();
        switch(code) {
            case 2:
            image=new GreenfootImage("Powerup_Laser.png");
            break;
            
            case 3:
            image=new GreenfootImage("Powerup_Trap.png");
            break;
            
            default:
            image=new GreenfootImage("Stat_Boost.png");
            break;
        }
        setImage(image);
    }
    
    public int getCode() {
        return code;
    }
}
