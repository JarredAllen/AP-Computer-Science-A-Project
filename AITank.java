import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class AITank here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AITank extends Tank
{
    private int deadFrames;

    private int mv;
    private int turn;
    private boolean shoot;

    private final int turnDistance=8;
    private final int moveDistance=6;

    //Implemented skill levels: [-2,1]
    //Planned skill levels: [2,x] for x>=2
    private int skill;

    public final double moveChance=.1;
    public final double turnChance=.1;

    private int mode;

    Map<Bullet, Tank> targeting;
    Collection<Tank> ignoreList;

    public AITank(int id) {
        this(id, 0);
    }

    public AITank(int id, int s) {
        super(id);
        mv=s<=-1 ? 0 : 1;
        turn= s<=-2 ? 0 : 1;
        shoot=false;
        skill=s;
        deadFrames=-1;
        mode=0;
        targeting=new HashMap<Bullet, Tank>();
        ignoreList=new HashSet<Tank>();
    }

    @Override
    public void addedToWorld() {
        super.addedToWorld();
        deadFrames=-1;
    }

    public void run() 
    {
        if(killed()==-1) {
            switch (skill) {
                case 0:
                //this handles moving
                switch(mv) {
                    case -1:
                    case 1:
                    if(Math.random()<moveChance) {
                        mv=0;
                    }
                    break;
                    case 0:
                    if(Math.random()<moveChance) {
                        mv=-1;
                    }
                    else {
                        if(Math.random()<moveChance/(1-moveChance)) {
                            mv=1;
                        }
                    }
                    break;
                }
                //this handles turning
                switch(turn) {
                    case -1:
                    case 1:
                    if(Math.random()<turnChance) {
                        turn=0;
                    }
                    break;

                    case 0:
                    if(Math.random()<turnChance) {
                        turn=-1;
                    }
                    else {
                        if(Math.random()<turnChance/(1-turnChance)) {
                            turn=1;
                        }
                    }
                    break;
                }
                boolean laser=getPowerups().contains(new Integer(2));
                boolean trap=getPowerups().contains(new Integer(3))&&!laser;
                if(!(laser||trap)) {
                    if(aimingAtAnotherTank()) {
                        shoot=true;
                    }
                }
                if(laser) {
                    if(aimingLaser()) {
                        shoot=true;
                    }
                }
                if(trap) {
                    shoot=true;
                }
                break;

                case 1:
                Map<Integer, Boolean> dirs=new HashMap<Integer, Boolean>();
                for(int i=getRotation()+8; Math.abs(i-getRotation())>4; i=(i+8)%360) {
                    dirs.put(i, hitAnotherTank(i));
                }
                int closestDir=-9;
                for(int direction:dirs.keySet()) {
                    if(dirs.get(direction).equals(new Boolean(true))) {
                        if(rotationTo(direction)<rotationTo(closestDir)||closestDir==-9) {
                            closestDir=direction;
                        }
                    }
                }
                switch(mv) {
                    case -1:
                    case 1:
                    if(Math.random()<moveChance) {
                        mv=0;
                    }
                    break;
                    case 0:
                    if(Math.random()<moveChance) {
                        mv=-1;
                    }
                    else {
                        if(Math.random()<moveChance/(1-moveChance)) {
                            mv=1;
                        }
                    }
                    break;
                }
                //next AI up
                if(aimingAtAnotherTank()) {
                    shoot=true;
                }
                else {
                    if(closestDir==-9) {
                        switch(turn) {
                            case -1:
                            case 1:
                            if(Math.random()<turnChance) {
                                turn=0;
                            }
                            break;

                            case 0:
                            if(Math.random()<turnChance) {
                                turn=-1;
                            }
                            else {
                                if(Math.random()<turnChance/(1-turnChance)) {
                                    turn=1;
                                }
                            }
                            break;
                        }
                    }
                    else {
                        if(rotationTo(closestDir)>=8) {
                            turn=1;
                        }
                        else {
                            if(rotationTo(closestDir)<=-8) {
                                turn=-1;
                            }
                            else {
                                turn=0;
                                turn(closestDir);
                            }
                        }
                    }
                }
                break;

                case 2:
                if(getWorld().getObjects(Tank.class).size()>1) {
                    if(aimingAtAnotherTank()) {
                        shoot=true;
                    }
                    //next AI up
                    Map<Integer, Boolean> dir=new HashMap<Integer, Boolean>();
                    for(int i=getRotation()+8; Math.abs(i-getRotation())>4; i=(i+8)%360) {
                        dir.put(i, hitAnotherTank(i));
                    }
                    int closest=-9;
                    for(int direction:dir.keySet()) {
                        if(dir.get(direction).equals(new Boolean(true))) {
                            if(rotationTo(direction)<rotationTo(closest)||closest==-9) {
                                closest=direction;
                            }
                        }
                    }
                    if(closest==-9) {
                        List<Tank> otherTanks=new ArrayList<Tank>();
                        otherTanks.addAll(getWorld().getObjects(Tank.class));
                        otherTanks.remove(this);
                        Map<Tank,List<Direction>>paths=new HashMap<Tank,List<Direction>>();
                        for(Tank tank:otherTanks) {
                            paths.put(tank,pathTo(tank));
                        }
                        Tank target=otherTanks.get(0);
                        int length=Direction.pathLength(paths.get(target));
                        for(Tank t:paths.keySet()) {
                            int d=Direction.pathLength(paths.get(t));
                            if(d<length) {
                                length=d;
                                target=t;
                            }
                        }
                        List<Direction> path=paths.get(target);
                        if(facing(path)) {
                            turn=0;
                            mv=1;
                            break;
                        }
                        closest=paths.get(target).get(0).getAngle();
                    }
                    else {
                        mv=1;
                        move(-moveDistance+1);
                    }
                    if(rotationTo(closest)>=8) {
                        turn=1;
                    }
                    else {
                        if(rotationTo(closest)<=-8) {
                            turn=-1;
                        }
                        else {
                            turn(rotationTo(closest));
                            turn=0;
                        }
                    }
                }
                break;

                case -1:
                if(!canTurn(turn*turnDistance)) {
                    turn*=-1;
                }
                case -2:
                ignoreList=targeting.values();
                Tank t=tankAimingAt();
                if(t!=this&&t!=null&&t.killed()==-1) {
                    if(!targeting.containsValue(t)) {
                        Bullet b=shoot(true);
                        if(b!=null) {
                            targeting.put(b, t);
                        }
                    }
                }
                Set<Bullet>toRemove=new HashSet<Bullet>();
                for(Bullet b:targeting.keySet()) {
                    if(targeting.get(b).killed()!=-1) {
                        toRemove.add(b);
                    }
                }
                for(Bullet b:toRemove) {
                    targeting.remove(b);
                }
                break;
            }
            if(shoot) {
                shoot(true);
                shoot=false;
            }
            turn(turn*turnDistance);
            if(mv==1) {
                distance=moveDistance;
            }
            if(mv==-1) {
                distance=moveDistance/-2;
            }
            if(mv!=0) {
                if(blocked()) {
                    distance*=-1;
                    mv*=-1;
                }
                move();
            }
        }
        else {
            deadFrames++;
            if(deadFrames>=14) {
                getWorld().removeObject(this);
            }
            else {
                setImage("images/Tank"+Math.abs(getID()%4)+"Ex"+deadFrames+".png");
            }
        }
    }

    public void endBullet(Bullet b) {
        super.endBullet(b);
        if(skill==-1) {
            targeting.remove(b);
        }
    }

    public boolean LOSAnotherTank() {
        return seeAnotherTank(getRotation());
    }

    public boolean seeAnotherTank(int d) {
        return tankNotThis(lineOfSightTank(d));
    }

    public Tank lineOfSightTank(int d) {
        PathTracer p=new PathTracer();
        getWorld().addObject(p, getX(), getY());
        p.setRotation(d);
        while(getIntersectingObjects(Actor.class).contains(p)) {
            p.move(1);
            if(p.blocked(1)) {
                getWorld().removeObject(p);
                return this;
            }
        }
        return p.lineOfSight();
    }

    public boolean aimingAtAnotherTank() {
        return hitAnotherTank(getRotation());
    }

    public boolean hitAnotherTank(int d) {
        return tankNotThis(tankHit(d));
    }

    public Tank tankAimingAt() {
        return tankHit(getRotation());
    }

    public Tank tankHit(int d) {
        PathTracer p=new PathTracer(ignoreList);
        getWorld().addObject(p, getX(), getY());
        p.setRotation(d);
        while(getIntersectingObjects(Actor.class).contains(p)) {
            p.move(1);
            if(p.blocked(1)) {
                getWorld().removeObject(p);
                return this;
            }
        }
        Tank t=p.followPath();
        return t;
    }

    public boolean tankNotThis(Tank t) {
        return t!=null&&t!=this;
    }

    public boolean facing(List<Direction> path) {
        return facing(path.get(0));
    }

    public boolean facing(Direction d) {
        int rot=rotationTo(d.getAngle());
        if(Math.abs(rot)<=8) {
            turn(rot);
            turn=0;
            return true;
        }
        return rot==0;
    }

    public List<Tank> tanksSeen() {
        List<Tank> hits=new ArrayList<Tank>();
        for(int i=0; i<360;i+=8) {
            Tank t=tankHit(i);
            if(t!=null&&t!=this&&!hits.contains(t)) {
                hits.add(t);
            }
        }
        return hits;
    }
    
    public boolean aimingLaser() {
        PathTracer p=new PathTracer(ignoreList);
        getWorld().addObject(p, getX(), getY());
        p.setRotation(getRotation());
        while(getIntersectingObjects(Actor.class).contains(p)) {
            p.move(1);
            if(p.blocked(1)) {
                getWorld().removeObject(p);
                return false;
            }
        }
        Tank t=p.followLaserPath();
        return tankNotThis(t);
    }
}
