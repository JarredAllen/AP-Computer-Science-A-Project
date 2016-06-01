import greenfoot.*;

/**
 * Represents a location
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LocationInfo {
    private int x,y;

    private boolean north,south,east,west;
    
    private MapInfo map;

    /**
     * X and Y should be in co-ordinates where (6,6) and (7,6) can have a wall between them.
     */
    public LocationInfo(int x, int y, boolean up, boolean down, boolean left, boolean right, MapInfo map) {
        this.x=x;
        this.y=y;
        this.map=map;
        north=up;
        south=down;
        east=left;
        west=right;
    }

    /**
     * X and Y should be in co-ordinates where (6,6) and (7,6) can have a wall between them.
     * World is the world in which it represents a location
     */
    public LocationInfo(int x, int y, MazeWorld w) {
        north=w.getObjectsAt(x*75+37,y*75, Wall.class).isEmpty();
        south=w.getObjectsAt(x*75+37,y*75+75, Wall.class).isEmpty();
        east=w.getObjectsAt(x*75,y*75+37, Wall.class).isEmpty();
        west=w.getObjectsAt(x*75+75,y*75+37, Wall.class).isEmpty();
        this.x=x;
        this.y=y;
        map=w.getLayout();
    }

    /**
     * X and Y should be in co-ordinates where (6,6) and (7,6) can have a wall between them.
     * Parent is the AITank relying on it
     */
    public LocationInfo(int x, int y, AITank parent) {
        this(x,y,(MazeWorld)parent.getWorld());
    }
    
    public boolean clearUp() {
        return north;
    }
    
    public boolean clearLeft() {
        return east;
    }
    
    public boolean clearDown() {
        return south;
    }
    
    public boolean clearRight() {
        return west;
    }
    
    public int x() {
        return x;
    }
    
    public int y() {
        return y;
    }
    
    public MapInfo getMap() {
        return map;
    }
}
