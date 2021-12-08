package cpen221.mp3.wikimediator;

import org.fastily.jwiki.core.Wiki;

import java.io.*;
import java.lang.Thread;

//
import org.fastily.jwiki.dwrap.Revision;
import org.fastily.jwiki.core.Wiki;

import java.util.List;
//
import java.util.*;

public class WikiMediator {

    /* TODO: Implement this datatype

        You must implement the methods with the exact signatures
        as provided in the statement for this mini-project.

        You must add method signatures even for the methods that you
        do not plan to implement. You should provide skeleton implementation
        for those methods, and the skeleton implementation could return
        values like null.
     */

    private int capacity;
    private int timeout;

    private Map <String ,Long> map;  // map of pages and their time stamp
    private List <String> pageList; // a list of Wiki pages that contain text

    //method 3
    private List <String> requestList;   // a list of strings of request sent in getpage and search
    private Map<Long, String> requestmap;
    private Map <String, Integer> stringFrequency; // a map of requests and their frequency


    private int count;
    private Map<Integer, Long> countReq;

    public WikiMediator(int capacity, int stalenessInterval){
        this.capacity= capacity;
        this.timeout = stalenessInterval;


        map = Collections.synchronizedMap(new HashMap<>());
        pageList = Collections.synchronizedList(new ArrayList<>());

        // for method 3 and 4 to obtain information across methods
        requestList =  Collections.synchronizedList(new ArrayList<>());
        requestmap = Collections.synchronizedMap(new HashMap<>());
        // for method 5 and 6
        count = 0;
        countReq = Collections.synchronizedMap(new HashMap<>());

    }

    /**
     *
     * @param query
     * @param limit
     * @return
     */
    private List<String> search(String query, int limit){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // method 5 and 6
        long tim = System.currentTimeMillis();
        count++;
        countReq.put(count,tim); // store it's current time
        //--------------------------------------
        List<String> searchlist = new ArrayList<>();
        searchlist = wiki.search(query,limit);

        // for method 3
        requestList.add(query);
        requestmap.put(tim,query);

        return searchlist;
    }

    private String getPage(String pageTitle){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // Gets the text of the main page and prints it.

        //method 5 and 6
        long tim = System.currentTimeMillis();
        count++;
        countReq.put(count, tim); // store it's current time
        //
        StringBuilder textInThePage = new StringBuilder();
        textInThePage.append(wiki.getPageText(pageTitle));


        synchronized (this){
            for(int i = 0; i< map.size(); i++){
                if(map.get(pageList.get(i)) < System.currentTimeMillis()){
                    map.remove(pageList.get(i));
                }
            }
        }

        if(pageList.size() < capacity){
            pageList.add(textInThePage.toString());
            map.put(textInThePage.toString(), System.currentTimeMillis()+timeout*1000);
        }
        else if(pageList.size() == capacity){
            String idd = pageList.get(0);
            synchronized (this){
                for(int i = 1 ;i <pageList.size(); i++){
                    if(map.get(pageList.get(i)) < map.get(idd)){
                        idd = pageList.get(i);
                    }
                }
            }
            pageList.remove(idd);
            pageList.add(textInThePage.toString());
            map.put(textInThePage.toString(), System.currentTimeMillis()+timeout*1000);
        }
        // for method 3
        requestList.add(pageTitle);
        requestmap.put(tim, pageTitle);

        return textInThePage.toString();
    }

    private List<String> zeitgeist(int limit){
        // method 5 and 6
        count++;
        countReq.put(count,System.currentTimeMillis()); // store it's current time
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

        // return a list that is in descending order of the frequency of strings that appear in the query getPage and search
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


    private List<String> trending(int timeLimitInSeconds, int maxItems){
        // method 5 and 6
        count++;
        countReq.put(count,System.currentTimeMillis()); // store it's current time
//-----------------------------------------------------------------------------------------------------------------
        long start = System.currentTimeMillis()-timeLimitInSeconds*1000L;
        long end = (System.currentTimeMillis());

        Set<Long> tempset = new HashSet<>();
        List<String> templist = new ArrayList<>();
        tempset = requestmap.keySet();
        synchronized (this){
            for(Long x: tempset){
                if( x >= start && x <end ){
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
        // changing yy so that yy has a descending order
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

    private int windowedPeakLoad(int timeWindowInSeconds){
        // method 5 and 6
        count++;
        countReq.put(count,System.currentTimeMillis()); // store it's current time
        //----------------------------------------------------------------------------
        // sort the countReq
        Set<Integer> keyset = new HashSet<>();
        keyset = countReq.keySet();
        List<Integer> targetList = new ArrayList<>(keyset);


        for(int i=0; i < keyset.size(); i++){
            for(int j = i+1; j< keyset.size();j++){
                if(countReq.get(targetList.get(i))<countReq.get(targetList.get(j))){
                    int temp = targetList.get(i);
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
            long time = countReq.get(targetList.get(i)) - timeWindowInSeconds*1000L;
            for(int j=i+1; j<targetList.size(); j++){
                if(countReq.get(targetList.get(j)) > time){
                    num++;
                }
            }
            numarr[i] = num;
        }

        Arrays.sort(numarr);

        return numarr[targetList.size()-1];
    }

    private int windowedPeakLoad(){
        // method 5 and 6
//        count++;
//        countReq.put(count,System.currentTimeMillis()); // store it's current time
        //----------------------------------------------------------------------------

        // sort the countReq
        Set<Integer> keyset = new HashSet<>();
        keyset = countReq.keySet();
        List<Integer> targetList = List.copyOf(keyset);

        for(int i=0; i < keyset.size(); i++){
            for(int j = i+1; j< keyset.size();j++){
                if(countReq.get(targetList.get(i))<countReq.get(targetList.get(j))){
                    int temp = targetList.get(i);
                    targetList.add(i,targetList.get(j));
                    targetList.remove(i+1);
                    targetList.add(j,temp);
                    targetList.remove(j+1);
                }
            }
        }

        int [] numarr2 = new int[targetList.size()];

        for(int i=0; i<targetList.size();i++){
            int num =1;
            long time = countReq.get(targetList.get(i)) - 30L*1000L;
            for(int j=i+1; j<targetList.size(); j++){
                if(countReq.get(targetList.get(j)) > time){
                    num++;
                }
            }
            numarr2[i] = num;
        }

        Arrays.sort(numarr2);
        return numarr2[targetList.size()-1];

    }

    public static void main(String[] args){

        WikiMediator wk = new WikiMediator(5, 2);
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        l1 = wk.search("NCAA", 5);
        l2 = wk.search("China", 10);
        System.out.println(l1);
        System.out.println(l2);

        String s1 = new String();
        String s2 = new String();
        s1 = wk.getPage("NBA");

        s2 = wk.getPage("China");
        System.out.println(s1);
        System.out.println(s2);

        List<String> ll1 = new ArrayList<>(); List<String> ll2 = new ArrayList<>();
        ll1 = wk.zeitgeist(10);
        ll2 = wk.zeitgeist(5);
        System.out.println("Task3");
        System.out.println(ll1);
        System.out.println(ll2);
        System.out.println("Task4");
        List<String> www = new ArrayList<>();
        www = wk.trending(4,0);
        System.out.println(www);
        //lt2 = wk.trending(1,2);
        int cou;
        cou = wk.windowedPeakLoad(4);
        System.out.println(cou);
//        WikiMediator wtt = new WikiMediator(3, 10);
//        List<String> wl1 = new ArrayList<>();
//        List<String> wl2 = new ArrayList<>();
//        List<String> wl3 = new ArrayList<>();
//        List<String> wl4 = new ArrayList<>();
//        wl1 = wtt.search("Napolean", 5);
//        wl2 = wtt.search("Napolean", 5);
//        wl3 = wtt.search("Napolean", 5);
//        wl4 = wtt.search("Napolean", 5);
//        List<String> tt = new ArrayList<>();
//        tt= wtt.zeitgeist(10);
//        System.out.println(tt);



        System.exit(0);
    }
}
