package cpen221.mp3.wikimediator;

import org.fastily.jwiki.core.Wiki;

import java.io.*;
import java.lang.Thread;

import org.fastily.jwiki.dwrap.Revision;
import org.fastily.jwiki.core.Wiki;

import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/** WikiMediator is a mediator service for Wikipedia.
 * This service will access Wikipedia to obtain pages and other relevant information. */
public class WikiMediator {

    /** Concrete Representation */
    /** Two integers to specify the capacity and timeout of the buffer */
    private final int capacity;
    private final int timeout;

    /** Synchronized map that stores pages and their time stamps */
    private final Map <String ,Long> pageAccess = Collections.synchronizedMap(new HashMap<>());

    /** Synchronized list of wiki pages that stores the page texts */
    private final List <String> pageList = Collections.synchronizedList(new ArrayList<>());

    /** Synchronized list of requests sent in getpage and search */
    private final List <String> requestList = Collections.synchronizedList(new ArrayList<>());

    /** Synchronized map that stores a request and the time of request */
    private final Map <Long, String> requestmap = Collections.synchronizedMap(new HashMap<>());

    /** Synchronized map that stores the program counter and the time that it updates
     * The program counter updates when executing a new instruction */
    private final AtomicInteger programCounter = new AtomicInteger(0);
    private final Map<AtomicInteger, Long> pcHistory = Collections.synchronizedMap(new HashMap<>());

    //  Representation Invariant
    //  pageAccess elements != null
    //  pageList elements != null
    //  requestList elements != null
    //  requestmap elements != null
    //  pcHistory elements != null

    //  Abstract Function
    //  Service's maximum number of stored objects and expiry time is
    //  represented by final integers capacity and timeout respectively
    //  capacity = N represents that the service can hold a maximum of N objects
    //  timeout = M represents that the service will expire after M seconds
    //  Example:
    //  capacity = 5 means the service can hold a maximum of 5 objects
    //  timeout = 3600 means the service will expire after 3600 seconds (1 hour)
    //
    //  Service's program counter system is represented by 
    //  Atomic Integer programCounter and Synchronized map pcHistory
    //  programCounter keeps track of the number of programs executed,
    //  programCounter starts from 0 and increases by 1 each time an instruction is called
    //  pcHistory keeps track of the time in milliseconds when program counter updates
    //
    //  Service's wikipage texts and requests are represented by Synchronized String lists:
    //  pageList and requestList respectively
    //  pageList.get(N) represents a string consisting of all the texts on one wiki page
    //  requestList.get(N) represents a string consisting of a request
    //  
    //  Service's time stamps when a page is accessed is represented by synchronized map pageAccess
    //  pageAccess<M, N> represent that all the texts on page M is accessed at time M (milliseconds)
    //  Service's request times are represented by synchronized map requestmap
    //  pageAccess<M, N> represent that request M happened at time N (milliseconds)
    
    //  Thread-safe Arguments
    //  capacity and timeout are final, so those variables are immutable and thread-safe
    //  pageAccess, pageList, requestList, requestmap, programCounter, pcHistory
    //  are final, so those variables are immutable and thread-safe
    //  pageAccess, pageList, requestList, requestmap, programCounter, pcHistory
    //  are synchronized datatypes
    //  pageAccess, pageList, requestList, requestmap, programCounter, pcHistory
    //  points to thread safe datatypes
    
    
    synchronized private void checkRep() {
        for (int i = 0; i < pageAccess.size(); i++) {
            if (pageAccess.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
        for (int i = 0; i < pageList.size(); i++) {
            if (pageList.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
        for (int i = 0; i < requestList.size(); i++) {
            if (requestList.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
        for (int i = 0; i < requestmap.size(); i++) {
            if (requestmap.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
        for (int i = 0; i < pcHistory.size(); i++) {
            if (pcHistory.get(i) == null) {
                throw new RuntimeException("ERROR: instantiated list is null");
            }
        }
    }
    
    /**
     * Create a wikiMediator with a fixed capacity and a timeout value.
     *
     * @param capacity          the number of wikipages the mediator can hold
     *                          requires: capacity >= 1
     * @param stalenessInterval the duration, in seconds, a page should be in the 
     *                          mediator before it times out
     *                          requires: timeout >= 1
     */
    public WikiMediator(int capacity, int stalenessInterval) {
        this.capacity= capacity;
        this.timeout = stalenessInterval;
    }
    
    /**
     *
     * @param query content that clients can search on wikipedia
     * @param limit the numbers of title that the list should return
     * @return a list that contains limit numbers the response from the searching keyword
     */
    public List<String> search(String query, int limit){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // method 5 and 6
        long tim = System.currentTimeMillis();
        programCounter.getAndAdd(1);
        pcHistory.put(programCounter,tim); // store it's current time
        //--------------------------------------
        List<String> searchlist = new ArrayList<>();
        searchlist = wiki.search(query,limit);

        // for method 3
        requestList.add(query);
        requestmap.put(tim,query);

        return searchlist;
    }
    
    /**
     *
     * @param pageTitle the title of the page on wikipedia
     * @return return all the text on that page
     */
    public String getPage(String pageTitle){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // Gets the text of the main page and prints it.

        long tim = System.currentTimeMillis();
        programCounter.getAndAdd(1);
        pcHistory.put(programCounter, tim); // store it's current time
        //
        StringBuilder textInThePage = new StringBuilder();
        textInThePage.append(wiki.getPageText(pageTitle));

        synchronized (this){
            for(int i = 0; i< pageAccess.size(); i++){
                if(pageAccess.get(pageList.get(i)) < System.currentTimeMillis()){
                    pageAccess.remove(pageList.get(i));
                }
            }
        }

        if(pageList.size() < capacity){
            pageList.add(textInThePage.toString());
            pageAccess.put(textInThePage.toString(), System.currentTimeMillis()+timeout*1000L);
        }
        else if(pageList.size() == capacity){
            String idd = pageList.get(0);
            synchronized (this){
                for(int i = 1 ;i <pageList.size(); i++){
                    if(pageAccess.get(pageList.get(i)) < pageAccess.get(idd)){
                        idd = pageList.get(i);
                    }
                }
            }
            pageList.remove(idd);
            pageList.add(textInThePage.toString());
            pageAccess.put(textInThePage.toString(), System.currentTimeMillis()+timeout*1000L);
        }
        // for method 3
        requestList.add(pageTitle);
        requestmap.put(tim, pageTitle);

        return textInThePage.toString();
    }
    
    /**
     *
     * @param limit the numbers of queries that this method should return
     * @return a list containing limit number of requests that ranked in non-ascending order in terms of frequency
     */
    public List<String> zeitgeist(int limit){
        // method 5 and 6
        programCounter.getAndAdd(1);
        pcHistory.put(programCounter,System.currentTimeMillis()); // store it's current time
        //------------------------------
        StringBuilder commonStr = new StringBuilder();
        Map<String,Integer>stringFrequency = new HashMap<>();
        List<String> copy = new ArrayList<>();
        Set<String> copy2 = new HashSet<>();
        List<String> zeitlist = new ArrayList<>();
        // local string builder has all the request strings
        synchronized (this){
            for(String c : requestList){
                //copy.add(c);
                commonStr.append(c);
                commonStr.append(" ");
            }
        }
        // split the string
        String[] splited = commonStr.toString().split("\\s+");
        // make copy obtains all the strings
        for(int i=0; i < splited.length;i++){
            copy.add(splited[i]);
        }

        // a hashmap of string maps to frequency
        for(int i=0; i < copy.size(); i++){
            int k = 1;
            for(int j=i+1; j<copy.size();j++){
                if(i!=j && copy.get(i).equals(copy.get(j))){
                    k++;
                }
            }
            if(!stringFrequency.containsKey(copy.get(i))){
                stringFrequency.put(copy.get(i),k);
            }
        }
        // make sure copy does not have duplicates
        copy2 = stringFrequency.keySet();
        copy.clear();
        for(String c :copy2){
            copy.add(c);
        }

        // sorting in descending: most common to least common strings
        synchronized (this){
            for (int i = 0 ; i < stringFrequency.size(); i ++){
                for (int j = i+1 ; j < stringFrequency.size(); j ++){
                    if(stringFrequency.get(copy.get(i)) < stringFrequency.get(copy.get(j))){
                        String temp = copy.get(i);
                        copy.add(i, copy.get(j));
                        copy.remove(i+1);
                        copy.add(j, temp);
                        copy.remove(j+1);
                    }
                }
            }
        }

        // return a list that is in descending order of the frequency of strings 
        // that appear in the query getPage and search
        if(copy.size() < limit) {
            for(int i=0; i<copy.size(); i++){
                zeitlist.add(copy.get(i));
            }
            return zeitlist;
        }
        else{
            for(int i=0; i < limit; i++){
                zeitlist.add(copy.get(i));
            }
            return zeitlist;
        }
    }

    /**
     *
     * @param timeLimitInSeconds time intervals in seconds
     * @param maxItems the number of elements that list can return
     * @return return a list of most frequent request in the time interval
     */
    public List<String> trending(int timeLimitInSeconds, int maxItems){
        // method 5 and 6
        programCounter.getAndAdd(1);
        pcHistory.put(programCounter,System.currentTimeMillis()); // store it's current time
//------------------------------------------------------------------------------------------------
        long start = System.currentTimeMillis()-timeLimitInSeconds*1000L;
        long end = (System.currentTimeMillis());

        Set<Long> tempset = new HashSet<>();
        List<String> templist = new ArrayList<>();
        tempset = requestmap.keySet();
        synchronized (this){
            for(Long x: tempset){
                if( x >= start && x < end ){
                    templist.add(requestmap.get(x));
                }
            }
        }
        Map<String, Integer> tempmap = new HashMap<>();
        // creating a map now that maps string to frequency
        for(int i=0; i<templist.size();i++){
            int track= 1;
            for(int j=i+1; j<templist.size();j++){
                if(templist.get(i).equals(templist.get(j))){
                    track++;
                }
            }
            tempmap.put(templist.get(i),track);
        }

        for(int i=0; i<tempmap.size(); i++){
            for(int j= i+1; j< tempmap.size(); j++){
                if(tempmap.get(templist.get(i)) < tempmap.get(templist.get(j))){
                    String temp = templist.get(i);
                    templist.add(i, templist.get(j));
                    templist.remove(i+1);
                    templist.add(j, temp);
                    templist.remove(j+1);
                }
            }
        }
        //returning
        List<String> ultimatereturnlist = new ArrayList<>();  // local arraylist
        if (maxItems > templist.size()){
            for (int j =0 ; j< templist.size(); j++){
                ultimatereturnlist.add(templist.get(j));
            }
            return ultimatereturnlist;
        }
        else{
            for (int j =0 ; j < maxItems; j++){
                ultimatereturnlist.add(templist.get(j));
            }
            return ultimatereturnlist;
        }
    }
    
     /**
     *
     * @param timeWindowInSeconds a interval of time in seconds
     * @return  the max number of request done in time intervals of timeWindowInSeconds seconds
     */
    public int windowedPeakLoad(int timeWindowInSeconds){
        // method 5 and 6
        programCounter.getAndAdd(1);
        pcHistory.put(programCounter,System.currentTimeMillis()); // store it's current time
        //----------------------------------------------------------------------------
        // sort the countReq
        Set<AtomicInteger> keyset = new HashSet<>();
        keyset = pcHistory.keySet();
        List<AtomicInteger> targetList = new ArrayList<>(keyset);


        for(int i=0; i < keyset.size(); i++){
            for(int j = i+1; j< keyset.size();j++){
                if(pcHistory.get(targetList.get(i))<pcHistory.get(targetList.get(j))){
                    AtomicInteger temp = targetList.get(i);
                    targetList.add(i,targetList.get(j));
                    targetList.remove(i+1);
                    targetList.add(j,temp);
                    targetList.remove(j+1);
                }
            }
        }

        int [] numarr = new int[targetList.size()];

        for(int i=0; i<targetList.size();i++){
            int num =1;
            long time = pcHistory.get(targetList.get(i)) - timeWindowInSeconds*1000L;
            for(int j=i+1; j<targetList.size(); j++){
                if(pcHistory.get(targetList.get(j)) > time){
                    num++;
                }
            }
            numarr[i] = num;
        }

        Arrays.sort(numarr);

        return numarr[targetList.size()-1];
    }
    
    /**
     *
     * @return the max number of request done in time intervals of 30 seconds
     */
    public int windowedPeakLoad(){

        // sort the countReq
        Set<AtomicInteger> keyset = new HashSet<>();
        keyset = pcHistory.keySet();
        List<AtomicInteger> targetList = List.copyOf(keyset);

        for(int i=0; i < keyset.size(); i++){
            for(int j = i+1; j< keyset.size();j++){
                if(pcHistory.get(targetList.get(i))<pcHistory.get(targetList.get(j))){
                    AtomicInteger temp = targetList.get(j);
                    targetList.add(i,targetList.get(i));
                    targetList.remove(i+1);
                    targetList.add(j,temp);
                    targetList.remove(j+1);
                }
            }
        }

        int [] numarr2 = new int[targetList.size()];

        for(int i=0; i<targetList.size();i++){
            int num =1;
            long time = pcHistory.get(targetList.get(i)) - 30L*1000L;
            for(int j=i+1; j<targetList.size(); j++){
                if(pcHistory.get(targetList.get(j)) > time){
                    num++;
                }
            }
            numarr2[i] = num;
        }

        Arrays.sort(numarr2);
        return numarr2[targetList.size()-1];

    }

}

