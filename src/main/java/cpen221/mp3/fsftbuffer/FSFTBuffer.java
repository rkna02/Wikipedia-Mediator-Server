package cpen221.mp3.fsftbuffer;
import com.sun.source.tree.SynchronizedTree;

import java.util.*;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

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
    private final List<T> objList = Collections.synchronizedList(new ArrayList<>());

    /** Synchronized map that stores object ids and object expiry times */
    private final Map <String, Long> idExpiry = Collections.synchronizedMap(new HashMap<>());

    /** Synchronized map that stores object ids and object's time of last usage */
    private final Map <String, Long> idUsage = Collections.synchronizedMap(new HashMap<>());

    //Representation Invariant:
    //  objList elements != null
    //  idExpiry elements != null
    //  idUsage elements != null
    //  objList.size() == idExpiry.size() == idUsage.size()

    //Abstract Function:
    //  Buffer's maximum number of stored objects and expiry time is
    //  represented by capacity and timeout respectively
    //  capacity = N represents that the buffer can hold a maximum of N objects
    //  timeout = M represents that the buffer will expire after M seconds
    //  Example:
    //  capacity = 5 means the buffer can hold a maximum of 5 objects
    //  timeout = 3600 means the buffer will expire after 3600 seconds (1 hour)
    //
    //  Storage of objects is represented by a synchronized list
    //  objList.get(N) represents an object of type T that is in the buffer
    //
    //  Synchronized map: idExpiry, represents object ids as key and object expiry times as value
    //  idExpiry<N, M> represents that the object id is N
    //  and the object expiry time is at M seconds
    //
    //  Synchronized map: idUsage, represents object ids as key and object time of last usage as value
    //  idUsage<N, M> represents that the object id is N
    //  and the object last usage time is at M seconds

    //  Thread Safety Argument:
    //  capacity and timeout are final, so those variables are immutable and thread-safe
    //  objList, idExpiry, and idUsage are final, so those variables are immutable and thread-safe
    //  objList, idExpiry, and idUsage are synchronized datatypes 
    //  objList, idExpiry, and idUsage points to thread safe datatypes

    /**
     * Checks the representation invariant.
     *
     * effects: no effects if this satisfies rep invariant,
     * 		    otherwise throws a runtime exception.
     */
    synchronized private void checkRep() {
        for (int i = 0; i < objList.size(); i++) {
            if (objList.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
        for (int i = 0; i < idExpiry.size(); i++) {
            if (idExpiry.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
        for (int i = 0; i < idUsage.size(); i++) {
            if (idUsage.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
        if (idUsage.size() != idExpiry.size() ||
            idUsage.size() != objList.size() ||
            idExpiry.size() != objList.size()) {
            throw new RuntimeException("ERROR: missing or extra data in buffer");
        }
    }

    /**
     * Create a buffer with a fixed capacity and a timeout value.
     * Objects in the buffer that have not been refreshed within the
     * timeout period are removed from the cache.
     *
     * @param capacity the number of objects the buffer can hold
     *                 requires: capacity >= 1
     * @param timeout  the duration, in seconds, an object should
     *                 be in the buffer before it times out
     *                 requires: timeout >= 1
     */
    public FSFTBuffer(int capacity, int timeout) {
        this.capacity = capacity;
        this.timeout = timeout;
        checkRep();
    }

    /**
     * Create a buffer with default capacity and timeout values.
     * Objects in the buffer that have not been refreshed within the
     * timeout period are removed from the cache.
     */
    public FSFTBuffer() {
        this(DSIZE, DTIMEOUT);
        checkRep();
    }

    /**
     * Add a value to the buffer.
     * If the buffer is full then remove the least recently accessed
     * object to make room for the new object.
     *
     * Requires: Only one put instruction is allowed per object t
     *           Cannot put more than one of the same object in the buffer
     */
    public boolean put(T t) {
        long timeInSeconds = System.currentTimeMillis()+(timeout * 1000L);

        if (t == null) {
            return false;
        }

        // remove all stale objects
        synchronized (this) {
            for(int i=0; i<objList.size(); i++){
                if(idExpiry.get(objList.get(i).id()) < (System.currentTimeMillis() / 1000L)) {
                    objList.remove(i);
                    idExpiry.remove(objList.get(i).id());
                    idUsage.remove(objList.get(i).id());
                }
            }
        }

        //Put object
        if (objList.size() < capacity) {
            synchronized (this) {
                objList.add(t);
                idExpiry.put(t.id(), timeInSeconds);
                idUsage.put(t.id(), timeInSeconds);
            }
            checkRep();
            return true;
        } else if (objList.size() == capacity) {
            // remove LRU and then add
            long minimum = idUsage.get(objList.get(0).id());
            int LRU = 0;
            synchronized (this) {
                for(int i=0; i < objList.size(); i++) {
                    if (idUsage.get(objList.get(i).id()) < minimum) {
                        minimum = idUsage.get(objList.get(i).id());
                        LRU = i;
                    }
                }
            }
            synchronized (this) {
                objList.remove(LRU);
                objList.add(t);
                idExpiry.remove(objList.get(LRU).id());
                idExpiry.put(t.id(), timeInSeconds);
                idUsage.remove(objList.get(LRU).id());
                idUsage.put(t.id(), timeInSeconds);
            }
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * Retrieve an object from the buffer, but does not remove it from the buffer.
     * This method does not retrieve stale objects
     *
     * @param id the identifier of the object to be retrieved
     * @return the object that matches the identifier from the
     * buffer
     * @throws RuntimeException if the object is a stale object,
     *                          or the object id is not found in the buffer
     */
    public T get(String id) throws RuntimeException {
        long timeInSeconds = (System.currentTimeMillis() / 1000L) + timeout;

        synchronized (this){
            for(int i=0; i< objList.size();i++){
                if(objList.get(i).id().equals(id)){
                    if (idExpiry.get(objList.get(i).id()) < (System.currentTimeMillis() / 1000L)) {
                        throw new RuntimeException("Stale object, unable to retrieve");
                    } else {
                        idUsage.remove(objList.get(i).id());
                        idUsage.put(objList.get(i).id(), timeInSeconds);
                        checkRep();
                        return objList.get(i);
                    }
                }
            }
            throw new RuntimeException("Object not in the cache");
        }
    }

    /**
     * Update the last refresh time for the object with the provided id.
     * This method is used to mark an object as "not stale" so that its
     * timeout is delayed. This method does not work on stale objects.
     *
     * @param id the identifier of the object to "touch"
     * @return true if successful and false otherwise
     */
    public boolean touch(String id) {
        long timeInSeconds = (System.currentTimeMillis() / 1000L) + timeout;

        synchronized (this) {
            for(int i=0; i <objList.size(); i++){
                if(objList.get(i).id().equals(id)){
                    if (idExpiry.get(objList.get(i).id()) < (System.currentTimeMillis() / 1000L)) {
                        return false;
                    } else {
                        idExpiry.remove(objList.get(i).id());
                        idExpiry.put(objList.get(i).id(), timeInSeconds);
                        checkRep();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Update an object in the buffer.
     * This method updates an object and acts like a "touch" to
     * renew the object in the cache. This method does not work on stale objects.
     *
     * @param t the object to update
     * @return true if successful and false otherwise
     */
    public boolean update(T t) {
        long timeInSeconds = (System.currentTimeMillis() / 1000L) + timeout;

        // remove any object that has already timed out
        synchronized (this){
            for(int i=0; i<objList.size(); i++){
                if(idExpiry.get(objList.get(i).id()) < (System.currentTimeMillis() / 1000L)) {
                    objList.remove(i);
                    idExpiry.remove(objList.get(i).id());
                    idUsage.remove(objList.get(i).id());
                }
            }
        }

        // update buffer
        synchronized (this){
            for(int i=0 ;i <objList.size(); i++){
                if(objList.get(i).id().equals(t.id())){
                    objList.remove(i);
                    objList.add(t);
                    idExpiry.remove(objList.get(i).id());
                    idExpiry.put(objList.get(i).id(), timeInSeconds);
                    checkRep();
                    return true;
                }
            }
        }
        return false;
    }
}
