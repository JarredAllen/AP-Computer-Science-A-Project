import greenfoot.*;

/**
 * Write a description of class ClickableButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ClickableButton extends myActor implements Button
{
    private boolean pressed;
    
    private String press;
    private String release;

    public void press() {
        pressed=true;
        new GreenfootSound(press).play();
    }

    public void release() {
        pressed=false;
        //new GreenfootSound(release).play();
    }

    public boolean isPressed() {
        return pressed;
    }

    public void act() {
        MouseInfo mi=Greenfoot.getMouseInfo();
        if(mi!=null) {
            if(getWorld().getObjectsAt(mi.getX(), mi.getY(), ClickableButton.class).contains(this)) {
                if(mi.getButton()==1) {
                    if(!pressed) {
                        press();
                    }
                    else {
                        release();
                    }
                }
            }
            else {
                if(pressed) {
                    release();
                }
            }
        }
    }

    /**
     * Constructor for objects of class ClickableButton
     */
    public ClickableButton()
    {
        super();
        pressed=false;
        press="Button Press.mp3";
        release="Button Release.mp3";
    }
}
