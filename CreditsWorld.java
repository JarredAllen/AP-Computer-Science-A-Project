import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.FileNotFoundException;
import java.awt.Color;

/**
 * Write a description of class CreditsWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CreditsWorld extends myWorld implements Notifyable
{
    private GreenfootSound champions;
    private GreenfootSound noDeaths;
    private GreenfootSound fail;
    private UIElement credits;

    public CreditsWorld() {
        this(null);
    }

    /**
     * Constructor for objects of class CreditsWorld.
     * 
     */
    public CreditsWorld(MazeWorld game)
    {
        super(600, 400, 1, false);
        UIElement leftBG=new UIElement(new GreenfootImage("leftBG.jpg"));
        addObject(leftBG, 150, 200);

        champions=new GreenfootSound("We Are The Champions - Queen.mp3");
        noDeaths=new GreenfootSound("Still Alive - Portal.mp3");
        fail=new GreenfootSound("Keep Your Head Up - Andy Grammer.mp3");
        String creditsText;
        try {
            creditsText=FileIO.loadFile("Credits.txt");
        }
        catch(FileNotFoundException fnfe) {
            creditsText="";
        }
        credits=new UIElement(creditsText, 35);
        addObject(credits, 450, FileIO.stringLines(creditsText)*20+310);
        NotifyingClickableButton ncb=new NotifyingClickableButton(new myList<Notifyable>(this));
        addObject(ncb, 240, 40);
        addObject(new UIElement("New\nGame", 20, Color.WHITE), 240, 40);
        if(game!=null) {
            UIElement title=new UIElement("Results", 40);
            UIElement head=new UIElement("Player:        Points");
            addObject(title, 100, 50);
            addObject(head, 110, 90);
            UIElement[]tanks = new UIElement[game.getTanks().length];
            UIElement[]points = new UIElement[game.getTanks().length];
            for(int i=0;i<tanks.length; i++) {
                tanks[i]=new UIElement(new GreenfootImage("Tank"+i+".png"));
                addObject(tanks[i], 80, 150+75*i);
                points[i]=new UIElement(""+game.getTanks()[i].getPoints());
                addObject(points[i], 200, 150+75*i);
            }
            if(game.getTopTank().getDeaths()==0&&game.getTopTank().getKills()>0) {
                noDeaths.play();
            }
            else {
                if(game.getTopTank().getPoints()<=0) {
                    fail.play();
                }
                else {
                    champions.play();
                }
            }
        }
        else {
            champions.play();
        }
    }

    public void act() {
        credits.setRotation(270);
        credits.move(1);
        credits.setRotation(0);
    }
    
    public void notify(Notifier n) {
        fail.stop();
        champions.stop();
        noDeaths.stop();
        if(OptionsScreen.current==null) {
            new OptionsScreen().setMain();
        }
        else {
            OptionsScreen.current.setMain();
        }
    }
}
