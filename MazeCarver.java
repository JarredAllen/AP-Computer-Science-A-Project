import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class mazeCarver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MazeCarver extends AlwaysTurningActor
{
    private boolean[][]map;
    private World world;
    private int worldX;
    private int worldY;

    private List<String> deadEnds;
    private List<String> loops;

    /**
     * Odds of taking a dead-end and removing a wall from it, making a loop.
     */
    public final double deadEnd=.5;

    /**
     * Time waited between each wall carving. Only used in debug mode, otherwise ignored.
     * Set to zero for final product.
     */
    public final int delay=0;

    /**
     * Determines if it should call <code>makeMaze()</code> upon calling <code>act()</code>
     */
    public final boolean makeMazeOnAct=false;//Set to false for real game.

    public List<String> getDeadEnds() {
        return deadEnds;
    }

    public MazeCarver() {
        super();
        deadEnds=new ArrayList<String>();
        loops=new ArrayList<String>();
    }

    /**
     * Calls <code>makeMaze()</code> if <code>makeMazeOnAct</code> is true
     */
    public void act() {
        if(makeMazeOnAct)
            makeMaze();
    }

    /**
     * Makes the maze for the game.
     */
    public void makeMaze() {
        world=getWorld();
        worldX=world.getWidth();
        worldY=world.getHeight();
        int x=getX()/75;
        int y=getY()/75;
        deadEnds.add("("+x+","+y+")");
        setLocation(75*x+37, 75*y+37);
        map=new boolean[worldX/75][worldY/75-1];
        for(int a=0; a<map.length; a++) {
            for(int b=0; b<map[a].length; b++) {
                map[a][b]=false;
            }
        }
        makeMazeHelp();
        if(Math.random()<deadEnd) {
            loops.add("("+x+","+y+")");
            //Removes makes a loop out of a dead-end
            List<Actor> no=new ArrayList<Actor>();
            no.addAll(world.getObjectsAt(getX(), getY()-37, Wall.class));
            List<Actor> so=new ArrayList<Actor>();
            so.addAll(world.getObjectsAt(getX(), getY()+38, Wall.class));
            List<Actor> ea=new ArrayList<Actor>();
            ea.addAll(world.getObjectsAt(getX()+38, getY(), Wall.class));
            List<Actor> we=new ArrayList<Actor>();
            we.addAll(world.getObjectsAt(getX()-37, getY(), Wall.class));
            boolean n=!no.isEmpty()&&y>0;
            boolean e=!ea.isEmpty()&&x<map.length-1;
            boolean s=!so.isEmpty()&&y<map[0].length-1;
            boolean w=!we.isEmpty()&&x>0;
            int num=0;
            if(n)
                num++;
            if(e)
                num++;
            if(s)
                num++;
            if(w)
                num++;
            if((x==0||x==map.length-1)&&(y==0||y==map.length-1)) { //corner
                if(num!=1) {
                    //System.out.println("Corner error of "+num+" at "+x+","+y);
                }
            }
            if((x==0||x==map.length-1)!=(y==0||y==map[0].length-1)) { //edge
                if(num!=2) {
                    //System.out.println("Edge error of "+num+" at "+x+","+y);
                }
                int c=(int)(2*Math.random());
                if(n) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(no);
                    }
                }
                if(e) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(ea);
                    }
                }
                if(s) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(so);
                    }
                }
                if(w) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(we);
                    }
                }
            }
            if(x!=0&&x!=map.length-1&&(y!=0&&y!=map[0].length-1)){ //neither corner nor edge
                if (num!=3) {
                    //System.out.println("Middle error of "+num+" at "+x+","+y);
                }
                int c=(int)(3*Math.random());
                if(n) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(no);
                    }
                }
                if(e) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(ea);
                    }
                }
                if(s) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(so);
                    }
                }
                if(w) {
                    c--;
                    if(c==-1) {
                        world.removeObjects(we);
                    }
                }
            }
        }
        else {
            deadEnds.add("("+x+","+y+")");
        }
        map[x][y]=true;
        if(debug) {
            System.out.println("Dead-ends at "+deadEnds);
            System.out.println("Loops at "+loops);
        }
    }

    private void makeMazeHelp() {
        int x=getX()/75;
        int y=getY()/75;
        //setup things
        if(map[x][y]) {
            return;
        }
        int num=0;
        boolean n=false,e=false,s=false,w=false;
        //check each direction
        if(x>0&&!map[x-1][y]) {
            w=true;
            num++;
        }
        if(y>0&&!map[x][y-1]) {
            n=true;
            num++;
        }
        if(x<map.length-1&&!map[x+1][y]) {
            e=true;
            num++;
        }
        if(y<map[0].length-1&&!map[x][y+1]) {
            s=true;
            num++;
        }

        /*
         * next to no unvisited squares
         */
        if(num==0) {
            if(Math.random()<deadEnd) {
                loops.add("("+x+","+y+")");
                //Removes makes a loop out of a dead-end
                List<Actor> no=new ArrayList<Actor>();
                no.addAll(world.getObjectsAt(getX(), getY()-37, Wall.class));
                List<Actor> so=new ArrayList<Actor>();
                so.addAll(world.getObjectsAt(getX(), getY()+38, Wall.class));
                List<Actor> ea=new ArrayList<Actor>();
                ea.addAll(world.getObjectsAt(getX()+38, getY(), Wall.class));
                List<Actor> we=new ArrayList<Actor>();
                we.addAll(world.getObjectsAt(getX()-37, getY(), Wall.class));
                n=!no.isEmpty()&&y>0;
                e=!ea.isEmpty()&&x<map.length-1;
                s=!so.isEmpty()&&y<map[0].length-1;
                w=!we.isEmpty()&&x>0;
                if(n)
                    num++;
                if(e)
                    num++;
                if(s)
                    num++;
                if(w)
                    num++;
                if((x==0||x==map.length-1)&&(y==0||y==map.length-1)) { //corner
                    if(num!=1) {
                        //System.out.println("Corner error of "+num+" at "+x+","+y);
                    }
                }
                if((x==0||x==map.length-1)!=(y==0||y==map[0].length-1)) { //edge
                    if(num!=2) {
                        //System.out.println("Edge error of "+num+" at "+x+","+y);
                    }
                    int c=(int)(2*Math.random());
                    if(n) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(no);
                        }
                    }
                    if(e) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(ea);
                        }
                    }
                    if(s) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(so);
                        }
                    }
                    if(w) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(we);
                        }
                    }
                }
                if(x!=0&&x!=map.length-1&&(y!=0&&y!=map[0].length-1)){ //neither corner nor edge
                    if (num!=3) {
                        //System.out.println("Middle error of "+num+" at "+x+","+y);
                    }
                    int c=(int)(3*Math.random());
                    if(n) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(no);
                        }
                    }
                    if(e) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(ea);
                        }
                    }
                    if(s) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(so);
                        }
                    }
                    if(w) {
                        c--;
                        if(c==-1) {
                            world.removeObjects(we);
                        }
                    }
                }
            }
            else {
                deadEnds.add("("+x+","+y+")");
            }
            map[x][y]=true;
            return;
        }

        //next to one or more unvisited squares
        int c=(int)(num*Math.random());//rng
        //recursive call(s)
        if(n) {
            c--;
            if(c==-1) {
                if(debug) {
                    System.out.println("Moving north");
                    long t=System.currentTimeMillis();
                    while(System.currentTimeMillis()<=t+delay) {}
                }
                setRotation(270);
                move(37);
                List<Wall>l=(List<Wall>)getIntersectingObjects(Wall.class);
                if(debug) {
                    System.out.println(l);
                }
                world.removeObjects(l);
                move(38);
                map[x][y]=true;
                makeMazeHelp();
                map[x][y]=false;
                setRotation(90);
                move(75);
            }
        }
        if(e) {
            c--;
            if(c==-1) {
                if(debug) {
                    System.out.println("Moving east");
                    long t=System.currentTimeMillis();
                    while(System.currentTimeMillis()<=t+delay) {}
                }
                setRotation(0);
                move(38);
                List<Actor> l=new ArrayList<Actor>();
                l.addAll(getIntersectingObjects(Wall.class));
                if(debug)
                    System.out.println(l);
                world.removeObjects(l);
                move(37);
                map[x][y]=true;
                makeMazeHelp();
                map[x][y]=false;
                setRotation(180);
                move(75);
            }
        }
        if(s) {
            c--;
            if(c==-1) {
                if(debug) {
                    System.out.println("Moving south");
                    long t=System.currentTimeMillis();
                    while(System.currentTimeMillis()<=t+delay) {}
                }
                setRotation(90);
                move(38);
                List<Actor> l=new ArrayList<Actor>();
                l.addAll(getIntersectingObjects(Wall.class));
                if(debug)
                    System.out.println(l);
                world.removeObjects(l);
                move(37);
                map[x][y]=true;
                makeMazeHelp();
                map[x][y]=false;
                setRotation(270);
                move(75);
            }
        }
        if(w) {
            c--;
            if(c==-1) {
                if(debug) {
                    System.out.println("Moving west");
                    long t=System.currentTimeMillis();
                    while(System.currentTimeMillis()<=t+delay) {}
                }
                setRotation(180);
                move(37);
                List<Actor> l=new ArrayList<Actor>();
                l.addAll(getIntersectingObjects(Wall.class));
                if(debug)
                    System.out.println(l);
                world.removeObjects(l);
                move(38);
                map[x][y]=true;
                makeMazeHelp();
                map[x][y]=false;
                setRotation(0);
                move(75);
            }
        }

        //check each direction
        if(w&&map[x-1][y]) {
            w=false;
            num--;
        }
        if(n&&map[x][y-1]) {
            n=false;
            num--;
        }
        if(e&&map[x+1][y]) {
            e=false;
            num--;
        }
        if(s&&map[x][y+1]) {
            s=false;
            num--;
        }

        if(num>0) {
            makeMazeHelp();
        }
        else {
            map[x][y]=true;
        }
    }

    private boolean notDone() {
        for(boolean[]a:map) {
            for(boolean b:a) {
                if(!b)
                    return true;
            }
        }
        return false;
    }
}
