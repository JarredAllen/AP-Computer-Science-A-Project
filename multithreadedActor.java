import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Actor that automatically multithreads itself.
 * The subclass is responsible for implementing the run method
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class multithreadedActor extends myActor implements Runnable
{
    /**
     * If <code>true</code>, it mutlithreads its run method.
     * If <code>false</code>, it will not multithread its run method
     */
    protected boolean isMultithreading;

    public multithreadedActor() {
        super();
        isMultithreading=false;
    }

    /**
     * Creates a new thread and handles this tank acting in the new thread. </br>
     * The run method should be implemented and given the code that would normally be in act.
     */
    public void act() 
    {
        if(isMultithreading) {
            new Thread(this).run();
        }
        else {
            run();
        }
    }    
}
