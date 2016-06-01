import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class myActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class myActor extends Actor
{
    protected int distance;
    public static final boolean debug=false;
    public static final boolean debugCurrent=true;
    private Collection<Actor> ignoreBlocking;
    
    public myActor() {
        this(new HashSet<Actor>());
    }
    
    public myActor(Collection<Actor> noClipList) {
        super();
        ignoreBlocking=noClipList;
    }

    public List<Actor> getActorsInFront(int d, Class x) {
        move(d, true);
        List<Actor> actors=new ArrayList<Actor>();
        actors.addAll(getIntersectingObjects(x));
        move(-d,true);
        return actors;
    }

    public Actor getOneIntersectingObject(Class x) {
        return super.getOneIntersectingObject(x);
    }
    
    public List<Actor> getIntersectingActors(Class<? extends Actor> cls) {
        return super.getIntersectingObjects((Class<Actor>)cls);
    }

    public void move(int d, boolean force) {
        if(!force) {
            move(d);
        }
        else {
            int t=distance;
            distance=d;
            move(true);
            distance=t;
        }
    }

    @Override
    public void move(int d) {
        int t=distance;
        distance=d;
        move();
        distance=t;
    }

    public void move(boolean force) {
        if(force) {
            int d=getRotation();
            int t=distance;
            double sinD=Math.sin(Math.toRadians(d));
            double cosD=Math.cos(Math.toRadians(d));
            int x=getX()+(int)(Math.round(distance*cosD));
            int y=getY()+(int)(Math.round(t*sinD));

            setLocation(x, y);
        }
        else {
           move(); 
        }
    }

    public void move() {
        int d=getRotation();
        int t=distance;
        double sinD=Math.sin(Math.toRadians(d));
        double cosD=Math.cos(Math.toRadians(d));
        int x=getX()+(int)(Math.round(distance*cosD));
        int y=getY()+(int)(Math.round(t*sinD));

        setLocation(x, y);
    }

    public boolean canTurn(int deg) {
        if(!getIntersectingObjects(Wall.class).isEmpty()) {
            return true;
        }
        boolean b=true;
        turn(deg, true);
        List<Actor> objs=new ArrayList<Actor>();
        objs.addAll(getIntersectingObjects(Wall.class));
        if(intersectsTanks()) {
            objs.addAll(getIntersectingObjects(Tank.class));
        }
        objs.removeAll(ignoreBlocking);
        if(!objs.isEmpty()) {
            b=false;
        }
        turn(-1*deg, true);
        return b;
    }

    public void turn(int deg) {
        if(canTurn(deg)) {
            super.turn(deg);
        }
    }

    public void turn(int deg, boolean force) {
        if(force) {
            super.turn(deg);
        }
        else {
            turn(deg);
        }
    }

    public boolean blocked(int d) {
        int t=distance;
        distance=d;
        boolean blocked=blocked();
        distance=t;
        return blocked;
    }

    public boolean blocked() {
        //Calculates location if it moves to be (x,y)
        int d=getRotation();
        int t=distance;
        double sinD=Math.sin(Math.toRadians(d));
        double cosD=Math.cos(Math.toRadians(d));
        int cX=getX();
        int cY=getY();
        int x=cX+(int)Math.round(t*cosD);
        int y=cY+(int)Math.round(t*sinD);

        //Gets walls at (x,y)
        setLocation(x,y);
        List<Actor> objs=new ArrayList<Actor>();
        objs.addAll(getIntersectingObjects(Wall.class));
        if(intersectsTanks()) {
            objs.addAll(getIntersectingObjects(Tank.class));
        }
        objs.removeAll(ignoreBlocking);
        setLocation(cX,cY);
        objs.addAll(getIntersectingObjects(Wall.class));

        //returns if there are any walls
        return !objs.isEmpty();
    }

    public int bounceAngle() {
        //Calculates location if it moves to be (x,y)
        int d=getRotation();
        int t=distance;
        double sinD=Math.sin(Math.toRadians(d));
        double cosD=Math.cos(Math.toRadians(d));
        int x=getX()+(int)Math.round(t*cosD);
        int y=getY()+(int)Math.round(t*sinD);
        //Gets walls at current location
        List<Wall> walls=new ArrayList<Wall>();
        walls.addAll(getIntersectingObjects(Wall.class));

        if(walls.size()==0) {//Gets walls at where it will move to if not next to any walls
            move();
            walls=getIntersectingObjects(Wall.class);
            distance*=-1;
            move();
            distance*=-1;
        }

        //Case of clear front
        if(walls.isEmpty()) {
            if(debug) {
                System.out.println("Clear");
            }
            return d;
        }
        Wall tempWall=walls.get(0);
        if(walls.size()==1) {
            Wall wall=walls.get(0);
            int wallD=wall.getRotation();
            if(wallD==90) { //Case of horizontal wall
                if(debug) {
                    System.out.println("Horizontal");
                }
                return 360-d;
            }

            //Case of vertical wall
            if(d<=180) {
                if(debug) {
                    System.out.println("Vertical");
                }
                return 180-d;
            }
            return 180+(180-(d-180));
        }

        if(walls.size()>=2) {
            Set<Integer> dirs=new HashSet<Integer>();
            for(int i=walls.size()-1;i>=0;i--) {
                Wall wall=walls.get(i);
                if(wall.isWall2()) {
                    boolean side=true;
                    if(wall.getRotation()==0) {
                        if(getX()<wall.getX()) {
                            side=false;
                        }
                    }
                    if(wall.getRotation()==90) {
                        if(getY()<wall.getY()) {
                            side=false;
                        }
                    }
                    if(wall.getRotation()==180) {
                        if(getX()>wall.getX()) {
                            side=false;
                        }
                    }
                    if(wall.getRotation()==270) {
                        if(getY()>wall.getY()) {
                            side=false;
                        }
                    }
                    if (side) {
                        walls=new ArrayList<Wall>(1);
                        walls.add(wall);
                        break;
                    }
                    else {
                        walls.remove(wall);
                        continue;
                    }
                }
                if(!dirs.add(wall.getRotation())) {
                    walls.remove(i);
                }
                else {
                    if(wall.getRotation()==0) {
                        if(getY()>wall.getY()+39||getY()<wall.getY()-39) {
                            walls.remove(i);
                        }
                    }
                    else {
                        if(getX()>wall.getX()+39||getX()<wall.getX()-39) {
                            walls.remove(i);
                        }
                    }
                }
            }
        }

        //Case of walking into a single wall
        if(walls.size()==0) {
            walls.add(tempWall);
        }
        int wallD=walls.get(0).getRotation();
        if(wallD==90) { //Case of horizontal wall
            if(debug) {
                System.out.println("Horizontal");
            }
            return 360-d;
        }

        //Case of vertical wall
        if(d<=180) {
            if(debug) {
                System.out.println("Vertical");
            }
            return 180-d;
        }
        return 180+(180-(d-180));
    }

    public String toString() {
        try {
            return "object of "+getClass().toString()+"@("+getX()+";"+getY()+") facing "+getRotation();
        }
        catch(IllegalStateException e) {
            return "object of"+getClass().toString()+" not in a world";
        }
    }

    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    public boolean isWall2() {
        return false;
    }

    public boolean intersectsTanks() {
        return false;
    }

    public int rotationTo(int x, int y) {
        int d=getRotation();
        turnTowards(x,y);
        int nextD=getRotation();
        setRotation(d);
        return rotationTo(nextD);
    }

    public int rotationTo(int nextD) {
        int d=getRotation();
        int rot=nextD-d;
        if(rot>180) {
            rot-=360;
        }
        if(rot<-180) {
            rot+=360;
        }
        return rot;
    }
    
    /**
     * Precondition: In a MazeWorld
     */
    public List<Direction> pathTo(Actor a) {
        return ((MazeWorld)getWorld()).path(getX()/75, getY()/75, a.getX()/75, a.getY()/75);
    }
    
    /**
     * See <code>pathTo(Actor)</code>
     */
    public List<Direction> pathTo(int x, int y) {
        return ((MazeWorld)getWorld()).path(getX()/75, getY()/75, x, y);
    }
    
    /**
     * Called by <code>myWorld</code> when removing a myActor from the world.</br>
     * Should be used to handle removing any dependent actors.
     */
    public void removedFromWorld() {}
    
    public void removeValue(Map map, Object value) {
        Set toRemove=new HashSet();
        for(Object obj:map.keySet()) {
            if(map.get(obj).equals(value)) {
                toRemove.add(obj);
            }
        }
        for(Object obj:toRemove) {
            map.remove(obj);
        }
    }
}
