package cpen221.mp3.wikimediator;

import org.fastily.jwiki.core.Wiki;

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
    private Map <String, Integer> stringFrequency; // a map of requests and their frequency
    // private int limitNum;

    private int count;
    private Map<Integer, Long> countReq;

    public WikiMediator(int capacity, int stalenessInterval){
        this.capacity= capacity;
        this.timeout = stalenessInterval;


        map = Collections.synchronizedMap(new HashMap<>());
        pageList = Collections.synchronizedList(new ArrayList<>());

        // for method 3 and 4 to obtain information across methods
        requestList =  Collections.synchronizedList(new ArrayList<>());

        // for method 5 and 6
        count = 0;
        countReq = Collections.synchronizedMap(new HashMap<>());

    }

    private List<String> search(String query, int limit){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // method 5 and 6
        count++;
        countReq.put(count,System.currentTimeMillis()); // store it's current time
        //--------------------------------------
        List<String> searchlist = new ArrayList<>();
        searchlist = wiki.search(query,limit);

        // for method 3
        requestList.add(query);


        return searchlist;
    }

    private String getPage(String pageTitle){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // Gets the text of the main page and prints it.

        //method 5 and 6
        count++;
        countReq.put(count,System.currentTimeMillis()); // store it's current time
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


        return textInThePage.toString();
    }

    private List<String> zeitgeist(int limit){
        // method 5 and 6
        count++;
        countReq.put(count,System.currentTimeMillis()); // store it's current time
        //------------------------------
        StringBuilder commonStr = new StringBuilder();
        stringFrequency = new HashMap<>();
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


        long EndingTimeInMillis = System.currentTimeMillis();
        long StartingTimeInMillis =  System.currentTimeMillis() - timeLimitInSeconds*1000;

        stringFrequency = new HashMap<>();  // shared across

        List<String> copypage = new ArrayList<>();              // local arraylist
        List<String> returnlist = new ArrayList<>();           // local arraylist
        List<String> ultimatereturnlist = new ArrayList<>();  // local arraylist
        List<String> copy = new ArrayList<>();

        //
        synchronized (this){
            for(String c : requestList){
                copy.add(c);
            }
        }
        //
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

        //-----------------------------------------------------------
        synchronized (this){
            for(String c: stringFrequency.keySet()){
                copypage.add(c);
            }
        }

        // sort
        for (int i = 0 ; i < copypage.size(); i ++){
            for (int j = i+1 ; j < copypage.size(); j ++){
                if(stringFrequency.get(copypage.get(i)) < stringFrequency.get(copypage.get(j))){
                    String temp = copypage.get(i);
                    copypage.add(i, copypage.get(j));
                    copypage.add(j, temp);
                }
            }
        }

        for (int i =0; i < copypage.size(); i++){
            if(StartingTimeInMillis < (map.get(copypage.get(i))-timeout*1000) && (map.get(copypage.get(i))-timeout*1000) < EndingTimeInMillis ){
                returnlist.add(copypage.get(i));
            }
        }

        // returning
        if (maxItems>returnlist.size()){
            for (int j =0 ; j< returnlist.size(); j++){
                ultimatereturnlist.add(returnlist.get(j));
            }
            return ultimatereturnlist;
        }
        else{
            for (int j =0 ; j< maxItems; j++){
                ultimatereturnlist.add(returnlist.get(j));
            }
            return ultimatereturnlist;
        }
    }

    int windowedPeakLoad(int timeWindowInSeconds){
        // method 5 and 6
        count++;
        countReq.put(count,System.currentTimeMillis()); // store it's current time
        //----------------------------------------------------------------------------
        int num;
        Set<Integer> keyset = new HashSet<>();
        keyset = countReq.keySet();

        return 0;
    }

    int windowedPeakLoad(){

        return 0;
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

        List<String> ll1 = new ArrayList<>();
        List<String> ll2 = new ArrayList<>();
        ll1 = wk.zeitgeist(10);
        ll2 = wk.zeitgeist(5);
        System.out.println("Task3");
        System.out.println(ll1);
        System.out.println(ll2);

        WikiMediator wtt = new WikiMediator(3, 10);
        List<String> wl1 = new ArrayList<>();
        List<String> wl2 = new ArrayList<>();
        List<String> wl3 = new ArrayList<>();
        List<String> wl4 = new ArrayList<>();
        wl1 = wtt.search("Napolean", 5);
        wl2 = wtt.search("Napolean", 5);
        wl3 = wtt.search("Napolean", 5);
        wl4 = wtt.search("Napolean", 5);
        List<String> tt = new ArrayList<>();
        tt= wtt.zeitgeist(10);
        System.out.println(tt);



        System.exit(0);
    }
}
