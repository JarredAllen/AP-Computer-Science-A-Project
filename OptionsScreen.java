import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;

/**
 * Write a description of class OptionsScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class OptionsScreen extends myWorld implements Notifyable
{
    public static OptionsScreen current;

    private NotifyingClickableButton toBattle;

    private NotifyingClickableButton toTestDrive;

    private NumberInput hpInput;
    private NumberInput cpInput;
    private List<NumberInput> cpInputDifs;
    private NumberInput timeInput;

    private List<Notifyable>me;

    private UIElement label;

    private boolean difTwo;

    private int[]cpDif;

    public OptionsScreen()
    {    
        super(750, 825, 1);
        me=new myList<Notifyable>(this);
        toBattle=new NotifyingClickableButton(me);
        toTestDrive=new NotifyingClickableButton(me);
        UIElement battleText=new UIElement("To Battle!", 12, Color.WHITE);
        UIElement testDriveText=new UIElement("Test Controls", 12, Color.WHITE);

        UIElement title=new UIElement("Options", 100);
        label=new UIElement("CPU\nDifficulty", 15);

        hpInput=new NumberInput(me, 0, 4);
        cpInput=new NumberInput(me, 0, 4);
        cpInputDifs=new ArrayList<NumberInput>();
        timeInput=new NumberInput(me, 0, 300, 5);

        addObject(hpInput, 200, 300);
        addObject(new UIElement("Human Players", 15), 200, 250);
        addObject(cpInput, 350, 300);
        addObject(new UIElement("AI Players", 15), 350, 250);
        addObject(timeInput, 500, 300);
        addObject(new UIElement("Seconds of\nGameplay", 15), 500, 250);
        timeInput.setNumber(60);
        addObject(title, 375, 80);
        addObject(toBattle, 250, 500);
        addObject(toTestDrive, 500, 500);
        addObject(battleText, 250, 500);
        addObject(testDriveText, 500, 500);

        cpDif=new int[0];

        current=this;
    }

    public void notify(Notifier b) {
        if(b==toBattle) {
            MazeWorld mzw=new MazeWorld();
            mzw.setMain();
            if(cpDif.length!=cpInput.getNumber()) {
                int[]next=new int[cpInput.getNumber()];
                for(int i=0; i<next.length&&i<cpDif.length; i++) {
                    next[i]=cpDif[i];
                }
                for(int i=cpDif.length;i<next.length;i++) {
                    next[i]=0;
                }
                cpDif=next;
            }
            mzw.startGame(timeInput.getNumber(), hpInput.getNumber(), cpInput.getNumber(), cpDif);
            return;
        }
        if(b==toTestDrive) {
            new TestDriveWorld().setMain();
            return;
        }
        if(b==hpInput) {
            cpInput.updateMax(4-hpInput.getNumber());
            return;
        }
        if(b==cpInput) {
            hpInput.updateMax(4-cpInput.getNumber());
            int[]next=new int[cpInput.getNumber()];
            if(next.length>cpDif.length) {
                if(cpInputDifs.isEmpty()) {
                    addObject(label, 75, 250);
                }
                NumberInput n=new NumberInput(me, -2, 2);
                cpInputDifs.add(n);
                addObject(n, cpInputDifs.size()*75+75, 250);
            }
            else {
                while(cpInputDifs.size()>next.length) {
                    removeObject(cpInputDifs.remove(cpInputDifs.size()-1));
                }
                if(cpInputDifs.isEmpty()) {
                    removeObject(label);
                }
            }
            for(int i=0; i<next.length&&i<cpDif.length; i++) {
                next[i]=cpDif[i];
            }
            for(int i=cpDif.length;i<next.length;i++) {
                next[i]=0;
            }
            cpDif=next;
            return;
        }
        if(cpInputDifs.contains(b)) {
            cpDif[cpInputDifs.indexOf(b)]=((NumberInput)b).getNumber();
        }
    }
}
