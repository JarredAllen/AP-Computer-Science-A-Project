import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;

/**
 * Write a description of class NumberInput here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NumberInput extends myActor implements Notifyable, Notifier
{
    private int number;

    private ClickableButton increase;
    private ClickableButton decrease;

    private UIElement display;

    private GreenfootSound click;

    private int min;
    private int max;
    private int increment;

    private List<Notifyable> toNotify;

    public NumberInput(){
        this(new ArrayList<Notifyable>(),Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    }

    public NumberInput(List<Notifyable> nots) {
        this(nots, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    }

    public NumberInput(int min, int max) {
        this(new ArrayList<Notifyable>(), min, max, 1);
    }

    public NumberInput(List<Notifyable> nots, int min, int max) {
        this(nots, min, max, 1);
    }

    public NumberInput(List<Notifyable> nots, int min, int max, int increment) {
        super();
        number=Math.max(min, Math.min(max, 0));;
        this.min=min;
        this.max=max;
        increase=new PlusButton(this);
        decrease=new MinusButton(this);
        display=new UIElement(""+number, 30, Color.BLACK);
        this.increment=increment;
        toNotify=nots;
    }

    public void addedToWorld(World w) {
        int x=getX();
        int y=getY();
        w.addObject(display, x, y);
        w.addObject(increase, x, y-27);
        w.addObject(decrease, x, y+27);
    }

    public int getNumber() {
        return number;
    }

    public void notify(Notifier b) {
        if(b==increase) {
            if(number+increment<=max) {
                number+=increment;
            }
            else {
                number=max;
            }
            updateGUI();
            notifyDependents();
            return;
        }
        if(b==decrease) {
            if(number-increment>=min) {
                number-=increment;
            }
            else {
                number=min;
            }
            updateGUI();
            notifyDependents();
            return;
        }
    }

    public void updateGUI() {
        display.changeText(""+number);
    }

    public void notifyDependents() {
        for(Notifyable n:toNotify) {
            n.notify(this);
        }
    }

    @Override
    public void removedFromWorld() {
        World w=getWorld();
        w.removeObject(increase);
        w.removeObject(decrease);
        w.removeObject(display);
    }

    public void updateMax(int newMax) {
        max=newMax;
    }

    public void updateMin(int newMin) {
        min=newMin;
    }

    public void setNumber(int value) {
        number=value;
        updateGUI();
    }

    public class PlusButton extends NotifyingClickableButton {
        public PlusButton(NumberInput owner) {
            super(new myList<Notifyable>(owner));
            setImage("images/plus.png");
        }
    }

    public class MinusButton extends NotifyingClickableButton {
        public MinusButton(NumberInput owner) {
            super(new myList<Notifyable>(owner));
            setImage("images/minus.png");
        }
    }
}
