import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Shard here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shard extends myActor
{
    boolean moving;
    String prefix;
    int frame;
    Tank parent;

    public final double frameChangeChance=1;

    public Shard() {
        this(null);
    }

    public Shard(Tank t) {
        super(new myList<Actor>(t));
        moving=true;
        int id=Math.abs(t.getID()%4);
        try {
            prefix="Tank"+id+"p5f";
        }
        catch(NullPointerException npe) {
            prefix="Tank0p5f";
        }
        setImage(prefix+"0.png");
        distance=5;
        parent=t;
    }

    @Override
    public void addedToWorld(World w) {
        setRotation((int)(Math.random()*360));
    }

    @Override
    public boolean intersectsTanks() {
        return true;
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
    }
}
