import greenfoot.*;
import java.util.*;

/**
 * Write a description of class MapInfo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MapInfo implements Cloneable
{
    private LocationInfo[][] map;
    private MazeWorld world;
    
    public MapInfo(MazeWorld mzw) {
        world=mzw;
        map=new LocationInfo[world.getWidth()/75][world.getHeight()/75-1];
    }
    
    public void fillMap() {
        for(int a=0;a<world.getWidth()/75;a++) {
            for(int b=0;b<world.getHeight()/75-1;b++) {
                map[a][b]=new LocationInfo(a,b,world);
            }
        }
    }
    
    public MapInfo clone() {
        MapInfo m=new MapInfo(world);
        m.fillMap();
        return m;
    }
    
    /**
     * Returns the path between the two locations. If no such path exists, it returns null.
     * The path is an <code>ArrayList<Direction></code> with consecutive <code>Direction</code> objects combined if they
     * are parallel.
     */
    public ArrayList<Direction> path(LocationInfo start, LocationInfo end) {
        Map<LocationInfo, ArrayList<Direction>> paths=new HashMap<LocationInfo, ArrayList<Direction>>();
        paths.put(start, new ArrayList<Direction>());
        for(int i=0;!paths.containsKey(end);i++) {
            if(i>map.length*map[0].length) {
                return null;
            }
            Map<LocationInfo, ArrayList<Direction>>newPaths=new HashMap<LocationInfo, ArrayList<Direction>>();
            for(LocationInfo li:paths.keySet()) {
                Map<LocationInfo, Direction> next=unitPaths(li);
                for(LocationInfo ne:next.keySet()) {
                    ArrayList<Direction> list=(ArrayList<Direction>)paths.get(li).clone();
                    list.add(next.get(ne));
                    newPaths.put(ne, list);
                }
            }
            paths=newPaths;
        }
        ArrayList<Direction> path=paths.get(end);
        for(int i=path.size()-1; i>0; i--) {
            if(path.get(i).parallel(path.get(i-1))) {
                path.set(i, path.get(i).add(path.get(i-1)));
                path.remove(i-1);
            }
        }
        if(path.isEmpty()) {
            path.add(Direction.HERE);
        }
        return path;
    }
    public ArrayList<Direction> path(int sX, int sY, int eX, int eY) {
        return path(map[sX][sY], map[eX][eY]);
    }
    
    public Set<LocationInfo> adjacent (LocationInfo li) {
        return adjacent(li.x(), li.y());
    }
    public Set<LocationInfo> adjacent(int x, int y) {
        LocationInfo li=map[x][y];
        Set<LocationInfo>adj=new HashSet<LocationInfo>();
        if(li.clearUp()) {
            adj.add(map[x][y-1]);
        }
        if(li.clearLeft()) {
            adj.add(map[x-1][y]);
        }
        if(li.clearDown()) {
            adj.add(map[x][y+1]);
        }
        if(li.clearRight()) {
            adj.add(map[x+1][y]);
        }
        return adj;
    }
    
    public Map<LocationInfo, Direction> unitPaths(int x, int y) {
        LocationInfo li=map[x][y];
        Map<LocationInfo, Direction>adj=new HashMap<LocationInfo, Direction>();
        if(li.clearUp()) {
            adj.put(map[x][y-1], Direction.UP);
        }
        if(li.clearLeft()) {
            adj.put(map[x-1][y], Direction.LEFT);
        }
        if(li.clearDown()) {
            adj.put(map[x][y+1], Direction.DOWN);
        }
        if(li.clearRight()) {
            adj.put(map[x+1][y], Direction.RIGHT);
        }
        return adj;
    }
    public Map<LocationInfo, Direction> unitPaths(LocationInfo li) {
        return unitPaths(li.x(), li.y());
    }
}
