package cpen221.mp3.fsftbuffer;
import java.util.*;

public class FSFTBuffer<T extends Bufferable> {

    /* the default buffer size is 32 objects */
    public static final int DSIZE = 32;

    /* the default timeout value is 3600s */
    public static final int DTIMEOUT = 3600;

    /* TODO: Implement this datatype */
    private int capacity;
    private int timeout;
    private List<T> list;
    private Map <String, Long> map;


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
        // TODO: implement this constructor
        this.capacity = capacity;
        this.timeout = timeout;

        list = new ArrayList<>();
        map = new HashMap<>();


    }

    /**
     * Create a buffer with default capacity and timeout values.
     */
    public FSFTBuffer() {
        this(DSIZE, DTIMEOUT);
        // added
        //queue = new PriorityQueue<>();
        list = new ArrayList<>();
        map = new HashMap<>();
    }


    /**
     * Add a value to the buffer.
     * If the buffer is full then remove the least recently accessed
     * object to make room for the new object.
     */
    public boolean put(T t) {
        // TODO: implement this method

        // remove any object that has already timeout
        for(int i=0; i<list.size(); i++){
            if((map.get(list.get(i).id()) > System.currentTimeMillis())){
                list.remove(i);
            }
        }

        if(list.size() < capacity) {
            list.add(t);
            long tim = System.currentTimeMillis()+(timeout*1000);
            map.put(t.id(), tim);
            return true;
        }
        else if (list.size() == capacity ) {
            // remove first and then add
            // find lru
            String idd = list.get(0).id();
            int track = 0;
            for(int i = 1 ;i <list.size(); i++){
                if(map.get(list.get(i).id())<map.get(idd)){
                    idd = list.get(i).id();
                    track = i;
                }
            }
            list.remove(list.get(track));
            list.add(t);
            long tim = System.currentTimeMillis()+(timeout*1000);
            map.put(t.id(), tim);
            return true;
        }
        return false;
    }



    /**
     * @param id the identifier of the object to be retrieved
     * @return the object that matches the identifier from the
     * buffer
     */
    public T get(String id) throws NullPointerException {
        /* TODO: change this */

        /* Do not return null. Throw a suitable checked exception when an object
            is not in the cache. You can add the checked exception to the method
            signature. */

        for(int i=0; i< list.size();i++){
            if(list.get(i).id().equals(id) ){
                // update the object's timeout time
                map.put(list.get(i).id(), System.currentTimeMillis()+(timeout*1000));
                return list.get(i);
            }
        }

        throw new NullPointerException(); // what kind of exception should we throw tho
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
        long timeInSeconds = System.currentTimeMillis()+(timeout*1000);

        for(int i = 0 ;i <list.size(); i++){
            if(list.get(i).id().equals(id)){
                map.put(list.get(i).id(), timeInSeconds);
                return true;
            }
        }

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

        long timeInSeconds = System.currentTimeMillis()+(timeout*1000);

        for(int i = 0 ;i <list.size(); i++){
            if(list.get(i).id().equals(t.id())){
                map.put(list.get(i).id(), timeInSeconds);
                return true;
            }
        }

        return false;
    }
}
