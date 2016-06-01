import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class myWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myWorld extends World
{
    public myWorld() {
        this(750, 625, 1, true);
    }
    
    public myWorld(int x, int y, int p, boolean f)
    {
        super(x,y,p, f);
    }

    public myWorld(int x, int y, int p) {
        this(x,y,p,true);
    }

    public void setMain() {
        Greenfoot.setWorld(this);
    }

    @Override
    public void removeObject(Actor a) {
        try{
            myActor ma=(myActor)a;
            ma.removedFromWorld();
        }
        catch(ClassCastException e){}
        super.removeObject(a);
    }

    public void newWait(long millis) {
        if(myActor.debug) {
            System.out.println("Called");
        }
        long end=System.currentTimeMillis()+millis;
        while(System.currentTimeMillis()<end){
            if(myActor.debug) {
                System.out.println("Waiting");
            }
        }
    }
}
