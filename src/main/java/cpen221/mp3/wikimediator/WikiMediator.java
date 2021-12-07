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
    private String str;               // so far no use
    private Map <String ,Long> map;  // map of pages and their time stamp
    private List <String> pageList; // a list of pages

    //method 3
    private List <String> requestList;   // a list of strings of request sent in getpage and search
    private Map <String, Integer> stringFrequency; // a map of requests and their frequency
    // private int limitNum;

    public WikiMediator(int capacity, int stalenessInterval){
        this.capacity= capacity;
        this.timeout = stalenessInterval;

        str = new String();
        map = new HashMap<>();


        pageList = new ArrayList<>();
        requestList = new ArrayList<>();
        stringFrequency = new HashMap<>();

    }

    private List<String> search(String query, int limit){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        List<String> searchlist = new ArrayList<>();
        searchlist = wiki.search(query,limit);

        // for method 3
        requestList.add(query);

        return searchlist;
    }

    private String getPage(String pageTitle){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // Gets the text of the main page and prints it.

        StringBuilder textInThePage = new StringBuilder();
        textInThePage.append(wiki.getPageText(pageTitle));



        for(int i = 0; i< map.size(); i++){
            if(map.get(pageList.get(i)) < System.currentTimeMillis()){
                map.remove(pageList.get(i));
            }
        }

        if(pageList.size() < capacity){
            pageList.add(textInThePage.toString());
            map.put(textInThePage.toString(), System.currentTimeMillis()+timeout*1000);
        }
        else if(pageList.size() == capacity){
            String idd = pageList.get(0);
            for(int i = 1 ;i <pageList.size(); i++){
                if(map.get(pageList.get(i)) < map.get(idd)){
                    idd = pageList.get(i);
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

        List<String> copy = new ArrayList<>();
        Set<String> copy2 = new HashSet<>();
        List<String> zeitlist = new ArrayList<>();

        for(String c : requestList){
            copy.add(c);
        }

        // a hashmap of string maps to frequency
        for(int i=0; i < copy.size(); i++){
            int k = 1;
            for(int j=0; j<copy.size();j++){
                if(i!=j && copy.get(i).equals(copy.get(j))){
                    k++;
                }
            }
            if(!stringFrequency.containsKey(copy.get(i))){
                stringFrequency.put(copy.get(i),k);
            }
        }


        copy2 = stringFrequency.keySet();
        copy.clear();
        for(String c :copy2){
            copy.add(c);
        }

        // sorting
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
        // return a list that is in descending order of the frequency of strings that appear in the query getPage and search
         if(copy.size()<limit) {
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
        long EndingTimeInMillis = System.currentTimeMillis();
        long StartingTimeInMillis =  System.currentTimeMillis() - timeLimitInSeconds*1000;

        List<String> copypage = new ArrayList<>();

        for(String c: pageList){
            copypage.add(c);
        }
        // sort
        for (int i = 0 ; i < copypage.size(); i ++){
            for (int j = 0 ; j < copypage.size(); j ++){
                if(stringFrequency.get(copypage.get(i)) < stringFrequency.get(copypage.get(j))){
                    String temp = copypage.get(i);
                    copypage.add(i, copypage.get(j));
                    copypage.add(j, temp);
                }
            }
        }

        List<String> returnlist = new ArrayList<>();
        List<String> ultimatereturnlist = new ArrayList<>();
        for (int i =0; i<copypage.size(); i++){
            if(StartingTimeInMillis<map.get(copypage.get(i)) && map.get(copypage.get(i)) < EndingTimeInMillis ){
                returnlist.add(copypage.get(i));
            }
        }
        for (int j =0 ; j< maxItems; j++){
            ultimatereturnlist.add(returnlist.get(j));
        }

        return ultimatereturnlist;
    }

    int windowedPeakLoad(int timeWindowInSeconds){

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
