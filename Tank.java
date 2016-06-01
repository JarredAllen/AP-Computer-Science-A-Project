import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Object class that handles player tanks.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tank extends multithreadedActor
{
    public final int numShards=5;

    private NavigableMap<Long, Integer> powerups;

    private long lastBullet;
    private byte numBullets;
    private int maxBullets=6;
    private int dead;
    private int id;
    private long destroyedTime;
    private int deadFrames;
    private int bulletSpeed=8;
    private int traps=0;

    private String q;
    private String w;
    private String a;
    private String s;
    private String d;

    private String fire;
    private String explosion;

    private int kills;
    private int deaths;

    /**
     * Spawns a tank with the given id number.
     */
    public Tank(int idNum) {
        super();
        lastBullet=-5000;
        numBullets=0;
        distance=6;
        dead=-1;
        id=idNum;
        setImage("images/Tank"+Math.abs(id%4)+".png");
        destroyedTime=-1;
        deadFrames=-1;
        Scanner controls=new Scanner("");
        try {
            controls=new Scanner(FileIO.readControls(false));
        }
        catch(Exception e){}
        String line=controls.nextLine();
        for(int i=0; i<Math.abs(id%4); i++) {
            line=controls.nextLine();
        }
        controls=new Scanner(line);
        q=controls.next();
        w=controls.next();
        a=controls.next();
        s=controls.next();
        d=controls.next();
        if(id==2) {
            q="enter";
        }
        fire="gunFiring.mp3";
        explosion="explosion.mp3";
        powerups=new TreeMap<Long, Integer>();
    }

    public void addedToWorld() {
        dead=-1;
        destroyedTime=-1;
        deadFrames=-1;
        setImage("images/Tank"+Math.abs(id%4)+".png");
        for(int i=0; i<4; i++) {
            if(getActorsInFront(37, Wall.class).isEmpty()) {
                break;
            }
            turn(90);
        }
    }

    public void run() 
    {
        if(dead==-1) {
            for(PowerupGiver powerup:(List<PowerupGiver>)getIntersectingObjects(PowerupGiver.class)) {
                addPowerup(powerup.getCode(), 20000L);
                getWorld().removeObject(powerup);
            }
            while(powerups.floorKey(System.currentTimeMillis())!=null) {
                long time=powerups.floorKey(System.currentTimeMillis());
                int i=powerups.get(time);
                removePowerup(time);
            }
            if(!w.equals("mouse")) {
                if(Greenfoot.isKeyDown(w)) {
                    move();
                }
                if(Greenfoot.isKeyDown(s)) {
                    move(-3);
                }
                if(Greenfoot.isKeyDown(d)) {
                    turn(8);
                }
                if(Greenfoot.isKeyDown(a)) {
                    turn(-8);
                }
                if(Greenfoot.isKeyDown(q)) {
                    shoot(Greenfoot.isKeyDown(w)&&!blocked());
                }
            }
            else {
                MouseInfo m=Greenfoot.getMouseInfo();
                if(m!=null) {
                    if(m.getButton()>0) {
                        shoot(!blocked());
                    }
                    int rot=rotationTo(m.getX(), m.getY());
                    if(Math.abs(rot)<170) {
                        turn(Math.max(-12, Math.min(12, rot)));
                        if((Math.pow(Math.pow(getX()-m.getX(),2)+Math.pow(getY()-m.getY(),2), .5))>30) {
                            move();
                        }
                    }
                    else {
                        turn((int)Math.max(-8, Math.min(8, (180-Math.abs(rot))*Math.signum(-rot))));
                        if((Math.pow(Math.pow(getX()-m.getX(),2)+Math.pow(getY()-m.getY(),2), .5))>30) {
                            move(-3);
                        }
                    }
                }
            }
        }
        else {
            deadFrames++;
            if(deadFrames>=14) {
                getWorld().removeObject(this);
            }
            else {
                setImage("images/Tank"+Math.abs(id%4)+"Ex"+deadFrames+".png");
            }
        }
    }    

    public void endBullet(Bullet b) {
        if(numBullets>0) {
            numBullets--;
        }
    }

    public Bullet shoot() {
        return shoot(false);
    }

    public Bullet shoot(boolean moving) {
        if(System.currentTimeMillis()-lastBullet>=250&&(id<0||numBullets<maxBullets)) {
            lastBullet=System.currentTimeMillis();
            if(!(powerups.containsValue(2)||powerups.containsValue(3))) {
                Bullet a;
                if(powerups.containsValue(0)) {
                    a=new Bullet(this, 12);
                }
                else {
                    a=new Bullet(this);
                }
                a.setRotation(getRotation());
                getWorld().addObject(a, getX(), getY());
                a.move(12);
                /*if(moving) {
                /*    while(intersects(a)) {
                /*         a.move(1);
                /*    }
                }*/
                numBullets++;
                new GreenfootSound(fire).play();
                return a;
            }
            if(powerups.containsValue(2)) {
                shootLaser();
                removeValue(powerups, 2);
                return null;
            }
            if(powerups.containsValue(3)) {
                Trap t=new Trap(this);
                getWorld().addObject(t, getX(), getY());
                traps--;
                if(traps==0) {
                    removeValue(powerups, 3);
                }
                return null;
            }
        }
        return null;
    }
    
    public void shootLaser() {
        LaserTracer lt=new LaserTracer(this);
        getWorld().addObject(lt, getX(), getY());
        lt.setRotation(getRotation());
        lt.shootLaser();
    }

    public void destroyed(Bullet bullet) {
        destroyed(bullet, true);
    }

    /**
     * Handles the tank being destroyed. The argument should be the bullet that destroyed it.
     */
    public void destroyed(Bullet bullet, boolean remove) {
        if(dead==-1) {
            if(id<0) {
                return;
            }
            powerups.clear();
            new GreenfootSound(explosion).play();
            try {
                dead=bullet.getTank().getID();
                bullet.getTank().endBullet(bullet);
            }
            catch(NullPointerException e){
                dead=0;
            }
            for(int i=0;i<numShards;i++) {
                getWorld().addObject(new Shard(this), getX(), getY());
            }
            try {
                if(remove) {
                    getWorld().removeObject(bullet);
                }
            }
            catch (NullPointerException npe) {}
            if(debug) {
                System.out.println(toString()+" is destroyed by "+bullet.toString());
            }
            destroyedTime=System.currentTimeMillis();
            deaths++;
        }
    }

    /**
     * Returns -1 if added to a world but not dead and the id
     * of the tank that killed it if it is dead.
     * Returns -2 once the world knows that it is dead.
     */
    public int killed() {
        return dead;
    }

    /**
     * Only to be called by the world
     */
    protected void setKilled(int i) {
        dead=i;
        if(debug) {
            System.out.println("Killed stat is changed by the world to "+i);
        }
    }

    public int getID() {
        return id;
    }

    public long deadMilliseconds() {
        return System.currentTimeMillis()-destroyedTime;
    }

    @Override
    public boolean intersectsTanks() {
        return true;
    }

    @Override
    public void move() {
        if(!blocked()) {
            super.move();
        }
    }

    @Override
    public void move(int d) {
        if(!blocked(d)) {
            super.move(d);
        }
    }

    /**
     * Adds the given powerup to the tank for the default amount of time (30 seconds) </br>
     * See addPowerup(int,long) for relationship between the codes and the powerups.
     */
    public void addPowerup(int code) {
        addPowerup(code, 30000);
    }

    /**
     * Adds the given powerup to the tank for the given number of milliseconds </br>
     * </br>
     * <table>
     * <tr>
     * <th>Powerup Name</th>
     * <th>Powerup Code</th>
     * </tr>
     * <tr>
     * <td>Faster Bullets</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>More Bullets</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>Laser Gun</td>
     * <td>2</td>
     * </tr>
     * <tr>
     * <td>Traps</td>
     * <td>3</td>
     * </tr>
     * </table>
     */
    public void addPowerup(int code, long milliseconds) {
        if(code==3) {
            powerups.put(Long.MAX_VALUE, code);
        }
        else {
            powerups.put(System.currentTimeMillis()+milliseconds, code);
        }
        switch(code) {
            case 0:
            bulletSpeed=12;
            break;

            case 1:
            maxBullets=10;
            break;

            case 2:
            break;

            case 3:
            traps=5;
            break;
        }
    }

    public void removePowerup(long millis) {
        int code=powerups.remove(millis);
        switch(code) {
            case 0:
            bulletSpeed=8;
            break;

            case 1:
            maxBullets=6;
            break;
        }
    }

    public Map<Actor,List<Direction>> paths (Class<? extends Actor> cls) {
        List<Actor> actors=getWorld().getObjects((Class<Actor>)cls);
        Map<Actor,List<Direction>>paths=new HashMap<Actor, List<Direction>>();
        for(Actor a:actors) {
            paths.put(a, pathTo(a));
        }
        return paths;
    }

    public Map<Actor,List<Direction>> toOtherTanks() {
        Map<Actor, List<Direction>> path=paths(((Actor)this).getClass());
        Set<Actor> toRemove=new HashSet<Actor>();
        for(Actor t:path.keySet()) {
            if(((Tank)t).killed()!=-1) {
                toRemove.add(t);
            }
        }
        for(Actor a:toRemove) {
            path.remove(a);
        }
        return path;
    }

    public Set<Tank> otherAliveTanks() {
        Set<Tank> tanks=(Set<Tank>)getWorld().getObjects(Tank.class);
        Set<Tank> toRemove=new HashSet<Tank>();
        for(Tank t:tanks) {
            if(t.killed()!=-1||t==this) {
                toRemove.add(t);
            }
        }
        tanks.removeAll(toRemove);
        return tanks;
    }

    public void addKill() {
        kills++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getPoints() {
        return kills-deaths;
    }

    public String getControls() {
        if(w.equals("mouse")) {
            return "mouse";
        }
        return w+a+s+d+q;
    }

    public Collection<Integer> getPowerups() {
        return powerups.values();
    }
}
