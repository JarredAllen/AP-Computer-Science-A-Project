import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.*;

/**
 * Write a description of class MazeWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MazeWorld extends myWorld
{
    public static final double powerupChance=0.001;

    /**
     * Size of each cell
     */
    public static final int p=1;

    private int xSize;
    private int ySize;
    private boolean runningGame;
    private List<String> deadEnds;
    private long finish;

    private Tank[] players;
    private int[] points;
    private UIElement[] scores;

    private boolean winnerAnnounced;

    private GreenfootSound fightSong;
    private GreenfootSound countdown;

    private MapInfo map;

    /**
     * Constructor for objects of class MazeWorld.
     * 
     */
    public MazeWorld()
    {
        this(10, 9);
    }

    public MazeWorld(int x, int y) {
        super(x*75/p, y*75/p, p);
        xSize=getWidth();
        ySize=getHeight();
        fightSong=new GreenfootSound("ellipse.mp3");
        countdown=new GreenfootSound("TheFinalCountdown.mp3");

        fightSong.setVolume(100);
        countdown.setVolume(100);

        makeMaze();
        map=new MapInfo(this);
        map.fillMap();
    }

    /**
     * Populates the world with a completely full grid of walls
     */
    public void makeWalls() {
        for(int x=0; x<=xSize; x+=75) {
            for(int y=37; y<ySize-75; y+=75) {
                Wall w=new Wall();
                addObject(w, x, y);
            }
        }
        for(int x=37; x<xSize; x+=75) {
            for(int y=0; y<=ySize-75; y+=75) {
                Wall w=new Wall();
                addObject(w, x, y);
                w.turn(90);
            }
        }
    }

    /**
     * Completely makes a fresh make
     */
    public void rebuildWorld() {
        resetWorld();
        makeMaze();
    }

    public void resetWorld() {
        removeObjects(getObjects(Actor.class));
    }

    /**
     * Removes existing walls, places new walls, and carves a maze through them.
     */
    public void makeMaze() {
        removeObjects(getObjects(Wall.class));
        makeWalls();
        MazeCarver mc=new MazeCarver();
        addObject(mc, (int)(Math.random()*(xSize-75)), (int)(Math.random()*(ySize-75)));

        if(myActor.debug) {
            System.out.println("("+mc.getX()/75+","+mc.getY()/75+")");
        }

        mc.makeMaze();
        removeObjects(getObjects(MazeCarver.class));

        deadEnds=mc.getDeadEnds();
        List<Wall> list=(List<Wall>)getObjects(Wall.class);
        for(Wall w:list) {
            w.handleEdges();   
        }
    }

    /**
     * Begins the game and spawns in player tanks.
     * Lets the game run for the given number of seconds.
     */
    public void startGame(int seconds, int hp, int cp, int[]dif) {
        newWait(1000);
        Greenfoot.stop();
        Greenfoot.start();
        if(seconds>61) {
            fightSong.playLoop();
        }
        else {
            countdown.play();
        }
        clearToMaze();
        /*for(int i=0; i<1000; i++) {
            System.out.println("\n\n");
        }*/
        winnerAnnounced=false;
        players=new Tank[hp+cp];
        points=new int[hp+cp];
        scores=new UIElement[hp+cp];
        
        Timer time=new Timer(seconds*1000L);
        addObject(time, 45, ySize-37);

        for(int i=0; i<hp+cp;i++) {
            points[i]=0;
            Tank t=new Tank(i);
            if(i>=hp) {
                t=new AITank(i, dif[i-hp]);
            }
            players[i]=t;
            spawnTank(t);
            UIElement ui=new UIElement(t.getImage());
            addObject(ui, 150*i+120, ySize-37);
            ui.setRotation(270);
            UIElement po=new UIElement("0");
            addObject(po, 150*i+163, ySize-37);
            scores[i]=po;
        }

        int kills=0;
        long start=System.currentTimeMillis();
        finish=start+seconds*1000;
        runningGame=true;
        Greenfoot.start();
    }

    public void startGame(int seconds, int hp, int cp) {
        startGame(seconds, hp, cp, new int[cp]);
    }

    public void spawnTank(Tank t) {
        int x,y;
        try {
            String loc=deadEnds.get((int)(Math.random()*deadEnds.size()));
            x=Integer.parseInt(loc.substring(1, loc.indexOf(",")));
            y=Integer.parseInt(loc.substring(loc.indexOf(",")+1, loc.indexOf(")")));
        }
        catch(IndexOutOfBoundsException e) {
            x=(int)(Math.random()*xSize/75);
            y=(int)(Math.random()*(ySize-75)/75);
        }
        if(getObjectsAt(x*75+37, y*75+37, myActor.class).isEmpty()) {
            addObject(t, x*75+37, y*75+37);
            t.setRotation(0);
        }
        else {
            addObject(t, (int)(Math.random()*xSize/75)*75+37, (int)(Math.random()*(ySize-75)/75)*75+37);
            t.setRotation(0);
        }
        if (t.getOneIntersectingObject(myActor.class)!=null) {
            t.setLocation((int)(Math.random()*xSize/75)*75+37, (int)(Math.random()*(ySize-75)/75)*75+37);
        }
    }

    public void spawnTanks(int num) {
        spawnTanks(num, false);
    }

    public void spawnTanks(int num, boolean ai) {
        for(int i=0; i<num; i++) {
            spawnTank(i, ai);
        }
    }

    private void spawnTank(int id) {
        spawnTank(id, false);
    }

    private void spawnTank(int id, boolean ai) {
        if(ai) {
            spawnTank(new AITank(id));
        }
        else {
            spawnTank(new Tank(id));
        }
    }

    public void discolorWall2s() {
        for(Wall2 w:(List<Wall2>)getObjects(Wall2.class)) {
            w.discolor();
        }
    }

    public void act() {
        if(myActor.debug) {
            System.out.println(System.currentTimeMillis());
        }
        if(runningGame) {
            if(System.currentTimeMillis()<finish) {
                if(Math.random()<powerupChance) {
                    addObject(new PowerupGiver(), 75*(int)(Math.random()*getWidth()/75)+37, 75*(int)(Math.random()*getHeight()/75-1)+37);
                }
                for(Tank p:players) {
                    int i=p.killed();
                    if(i>=0) {
                        if(myActor.debug) {
                            System.out.println("Tank "+p.getID()+" was killed by Tank "+i+".");
                        }
                        if(i!=p.getID()) {
                            points[i]++;
                            players[i].addKill();
                        }
                        points[p.getID()]--;
                        scores[i].changeText(""+points[i]);
                        scores[p.getID()].changeText(""+points[p.getID()]);
                        p.setKilled(-2);
                    }
                    if(i==-2) {
                        if(p.deadMilliseconds()>5000+(int)(2500*Math.random())) {
                            if(myActor.debug) {
                                System.out.println("Respawning tank "+p.getID());
                            }
                            spawnTank(p);
                            p.addedToWorld();
                        }
                    }
                }
                if(Math.abs(System.currentTimeMillis()-finish+61000)<100) {
                    fightSong.stop();
                }
                if(Math.abs(System.currentTimeMillis()-finish+60000)<100) {
                    countdown.play();
                }
            }
            else {
                if(!winnerAnnounced) {
                    countdown.stop();
                    String winner;
                    switch(maxIndex(points)%4) {
                        case 0:
                        winner="blue";
                        break;
                        case 1:
                        winner="green";
                        break;
                        case 2:
                        winner="red";
                        break;
                        case 3:
                        winner="yellow";
                        break;
                        default:
                        winner="";
                        break;
                    }
                    if(winner.equals("")) {
                        //System.out.println("It is a tie!");
                    }
                    else {
                        //System.out.println("The winner is the player in the "+winner+" tank.");
                    }
                    createCredits();
                }
            }
        }
    }

    public void startGame(int seconds, int hp) {
        startGame(seconds, hp, 0);
    }

    public int maxIndex(int[] n) {
        int t=0;
        for(int i=1; i<n.length; i++) {
            if(n[i]>n[t]) {
                t=i;
            }
        }
        for(int i=0; i<n.length; i++) {
            if(i!=t&&n[i]==n[t]) {
                //return -1;
            }
        }
        return t;
    }

    public void saveMaze(String name) throws FileNotFoundException {
        List<Actor> l=new ArrayList<Actor>();
        l.addAll(getObjects(Wall.class));
        FileIO.writeActorsToFile(l, name);
        FileIO.appendToFile(name, deadEnds.toString());
    }

    public void loadMazeFromFile(String name) {
        resetWorld();
        String layout;
        try {
            layout=FileIO.loadFile(name);
        }
        catch (FileNotFoundException f) {
            return;
        }
        String[]lines=layout.split("\n");
        layout=lines[0].substring(1, lines[0].length()-1);
        String[]actors=layout.split(",");
        for(String actor:actors) {
            String className=actor.substring(actor.indexOf("class")+6, actor.indexOf("@"));
            String position=actor.substring(actor.indexOf("@")+1);
            int x=Integer.parseInt(position.substring(position.indexOf("(")+1, position.indexOf(";")));
            int y=Integer.parseInt(position.substring(position.indexOf(";")+1, position.indexOf(")")));
            int rot=Integer.parseInt(position.substring(position.indexOf("facing")+7));
            Actor obj=new Bullet();
            if(className.equals("Wall")) {
                obj=new Wall();
            }
            if(className.equals("Wall2")) {
                obj=new Wall2();
            }
            addObject(obj, x, y);
            obj.setRotation(rot);
            if(myActor.debug) {
                System.out.println(obj);
            }
        }
        if(lines[1].length()>2) {
            String[]spots=lines[1].substring(1,lines[1].length()-1).split(",");
            deadEnds=new ArrayList<String>();
            for(String end:spots) {
                deadEnds.add(end);
            }
        }
        else {
            deadEnds=new ArrayList<String>();
        }
        updateMap();
    }

    public void clearToMaze () {
        List<Actor>objs=getObjects(Actor.class);
        objs.removeAll(getObjects(Wall.class));
        removeObjects(objs);
    }

    public MapInfo getLayout() {
        return map;
    }

    public void wait (int seconds, long millis) {
        long end=System.currentTimeMillis()+seconds*1000L+millis;
        while(System.currentTimeMillis()<end);
    }
    
    public ArrayList<Direction> path(int ax, int ay, int bx, int by) {
        return map.path(ax,ay, bx, by);
    }
    
    public void updateMap() {
        map=map.clone();
    }
    
    public Tank[] getTanks() {
        return players;
    }
    
    public Tank getTopTank() {
        return players[maxIndex(points)];
    }
    
    public void createCredits() {
        new CreditsWorld(this).setMain();
    }
}
