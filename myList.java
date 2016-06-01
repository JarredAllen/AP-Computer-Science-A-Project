import java.util.*;

/**
 * My extension on ArrayList.
 * It does not accept nulls
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myList<E> extends ArrayList<E> 
{
    public myList(int initialCapacity) {
        super(initialCapacity);
    }

    public myList() {
        super();
    }

    public myList(E e) {
        super();
        add(e);
    }

    public myList(Collection<E> e) {
        super(e);
    }

    public boolean add(E e) {
        if(e==null) {
            return false;
        }
        return super.add(e);
    }

    public void add(int i, E e) {
        if(e==null) {
        }
        else {
            super.add(i,e);
        }
    }
}
