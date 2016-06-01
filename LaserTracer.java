import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LaserTracer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LaserTracer extends myActor
{
    private Tank parent;

    public LaserTracer(Tank t) {
        super();
        parent=t;
    }

    public void act() 
    {
        if(getWorld()!=null) {
            getWorld().removeObject(this);
        }
    }

    public void shootLaser() {
        while(getIntersectingObjects(Tank.class).contains(parent)) {
            move(1);
        }
        move(4);
        boolean first=true;
        while(getX()!=0&&getY()!=0&&getX()!=getWorld().getWidth()-1&&getY()!=getWorld().getHeight()-1) {
            move(7);
            LaserGUI l=new LaserGUI(parent);
            getWorld().addObject(l, getX(), getY());
            if(first) {
                first=false;
                l.setImage("Laser.png");
            }
            else {
                l.setRotation(getRotation());
            }
        }
        getWorld().removeObject(this);
    }
}
