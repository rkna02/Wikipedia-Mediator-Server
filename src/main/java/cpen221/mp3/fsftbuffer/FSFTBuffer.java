package cpen221.mp3.fsftbuffer;
import java.util.*;
import java.util.Collections;

/** FSFTBuffer represents a finite-state finite-time buffer
 *  which stores a finite amount of objects for a finite amount of time */
public class FSFTBuffer<T extends Bufferable> {

    /* the default buffer size is 32 objects */
    public static final int DSIZE = 32;

    /* the default timeout value is 3600s */
    public static final int DTIMEOUT = 3600;

    /** Concrete Representation */
    /** Two integers to specify the capacity and timeout of the buffer */
    private final int capacity;
    private final int timeout;

    /** Synchronized list that stores objects of type T */
    private final List<T> list = Collections.synchronizedList(new ArrayList<>());

    /** Synchronized map that stores object ids and object expiry times */
    private final Map <String, Long> map = Collections.synchronizedMap(new HashMap<>());

    //Representation Invariant:
    //  list != null
    //  map != null

    //Abstract Function:
    //  Buffer's maximum number of stored objects and expiry time is
    //  represented by capacity and timeout respectively
    //  capacity = N represents that the buffer can hold a maximum of N objects
    //  timeout = M represents that the buffer will expire after M seconds
    //  Example:
    //  capacity = 5 means the buffer can hold a maximum of 5 objects
    //  timeout = 3600 means the buffer will expire after 3600 seconds (1 hour)
    //
    //  Storage for objects is represented by a synchronized list
    //  list.get(N) represents an object of type T that is in the buffer
    //  Please note that this object may be a stale object since stale objects
    //  are not removed immediately after expiry
    //
    //  Object details are represented by a map,
    //  object ids as key and object expiry times as value
    //  map<N, M> represents that the object id is N and the object expiry time is at M seconds
    //  Please note that this object may be a stale object since stale objects
    //  are not removed immediately after expiry

    //Thread Safety Argument:
    //  capacity and timeout are final, so those variables are immutable and thread-safe
    //  list and map are final, so those variables are immutable and thread-safe
    //  list and map points to thread safe datatypes


    /**
     * Checks the representation invariant.
     *
     * effects: no effects if this satisfies rep invariant,
     * 		    otherwise throws a runtime exception.
     */
    private void checkRep() {
        if (list == null) {
            throw new RuntimeException("ERROR: instantiated list is null");
        }
        if (map == null) {
            throw new RuntimeException("ERROR: instantiated map is null");
        }
    }


    /**
     * Create a buffer with a fixed capacity and a timeout value.
     * Objects in the buffer that have not been refreshed within the
     * timeout period are removed from the cache.
     *
     * @param capacity the number of objects the buffer can hold
     * @param timeout  the duration, in seconds, an object should
     *                 be in the buffer before it times out
     */
    public FSFTBuffer(int capacity, int timeout) {
        this.capacity = capacity;
        this.timeout = timeout;
    }


    /**
     * Create a buffer with default capacity and timeout values.
     * Objects in the buffer that have not been refreshed within the
     * timeout period are removed from the cache.
     */
    public FSFTBuffer() {
        this(DSIZE, DTIMEOUT);
    }

    /**
     * Add a value to the buffer.
     * If the buffer is full then remove the least recently accessed
     * object to make room for the new object.
     */
    public boolean put(T t) {
        // TODO: implement this method
        return false;
    }

    /**
     * @param id the identifier of the object to be retrieved
     * @return the object that matches the identifier from the
     * buffer
     */
    public T get(String id) {
        /* TODO: change this */
        /* Do not return null. Throw a suitable checked exception when an object
            is not in the cache. You can add the checked exception to the method
            signature. */
        return null;
    }

    /**
     * Update the last refresh time for the object with the provided id.
     * This method is used to mark an object as "not stale" so that its
     * timeout is delayed.
     *
     * @param id the identifier of the object to "touch"
     * @return true if successful and false otherwise
     */
    public boolean touch(String id) {
        /* TODO: Implement this method */
        return false;
    }

    /**
     * Update an object in the buffer.
     * This method updates an object and acts like a "touch" to
     * renew the object in the cache.
     *
     * @param t the object to update
     * @return true if successful and false otherwise
     */
    public boolean update(T t) {
        /* TODO: implement this method */
        return false;
    }
}
