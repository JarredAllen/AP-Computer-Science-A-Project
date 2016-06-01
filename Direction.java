import greenfoot.*;
import java.util.*;

/**
 * Write a description of class Direction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Direction {
    private int up;
    private int right;
    private int down;
    private int left;
    
    private int angle;

    public Direction(int up, int right) {
        this.up=up;
        this.right=right;
        down=-up;
        left=-right;
        try {
            angle=(int)Math.toDegrees(Math.atan((down+.0)/right));
        }
        catch(ArithmeticException ae) {
            if(down>0) {
                angle=90;
            }
            else {
                angle=270;
            }
        }
        if(angle<0) {
            angle+=360;
        }
        if(angle>=360) {
            angle-=360;
        }
        if(angle==0) {
            if(left>0) {
                angle=180;
            }
        }
    }

    public boolean equals(Object obj) {
        try {
            Direction d=(Direction)obj;
            return up==d.up&&right==d.right;
        }
        catch(NullPointerException e) {
            return false;
        }
        catch(ClassCastException e) {
            return false;
        }
    }

    public Direction add(Direction d) {
        return new Direction(d.up+up, d.right+right);
    }

    /**
     * Opposite directions are not parallel
     */
    public boolean parallel(Direction d) {
        if(!(Math.signum(right)==Math.signum(d.right))) {
            return false;
        }
        try {
            return slope().equals(d.slope());
        }
        catch(NullPointerException npe) {
            return true;
        }
    }

    /**
     * Returns <code>null</code> for an undefined slope.</br>
     * Note: slope=down/right and <code>Direction.HERE.slope()</code> returns <code>null</code>.
     */
    public Double slope() {
        try {
            return new Double((down+.0)/right);
        }
        catch(ArithmeticException ae) {
            return null;
        }
    }

    /**
     * <code>Actor.setRotation(direction.getAngle());</code> makes the actor face along this direction</br>
     * <code>Direction.HERE.getAngle()</code> returns 270
     */
    public int getAngle() {
        return angle;
    }
    
    public double distance() {
        return Math.pow(Math.pow(up,2)+Math.pow(left,2), .5);
    }
    
    
    public static int pathLength(List<Direction> path) {
        int distance=0;
        for(Direction d:path) {
            distance+=(int)d.distance();
        }
        return distance;
    }

    public static Direction UP=new Direction(1,0);
    public static Direction DOWN=new Direction(-1,0);
    public static Direction LEFT=new Direction(0,-1);
    public static Direction RIGHT=new Direction(0,1);

    public static Direction HERE=new Direction(0,0);
}
