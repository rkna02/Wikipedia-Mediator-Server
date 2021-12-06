package cpen221.mp3.wikimediator;

import org.fastily.jwiki.core.Wiki;

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
    String str;
    Map <String ,Long> map;
    List <String> pageList;

    //method 3
    List <String> requestList;
    Map <String, Integer> stringFrequency;
    private int limitNum;

    public WikiMediator(int capacity, int stalenessInterval){
        this.capacity= capacity;
        this.timeout = stalenessInterval;

        str = new String();
        map = new HashMap<>();
        pageList = new ArrayList<>();
//        requestList = new ArrayList<>();
//        stringFrequency = new HashMap<>();
        limitNum = 0;
    }

    public List<String> search(String query, int limit){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        List<String> searchlist = new ArrayList<>();
        searchlist = wiki.search(query,limit);
        // for method 3
        requestList.add(query);
        limitNum++;
        return searchlist;
    }

    public String getPage(String pageTitle){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // Gets the text of the main page and prints it.
        System.out.println( wiki.getPageText(pageTitle));
        StringBuilder textInThePage = new StringBuilder();
        textInThePage.append( wiki.getPageText(pageTitle));
        pageList.add(wiki.getPageText(pageTitle));

        for(int  i = 0; i< pageList.size(); i++){
            if(map.get(pageList.get(i))<System.currentTimeMillis()){
                map.remove(pageList.get(i));
            }
        }

        if(pageList.size()<capacity){
            str.concat(textInThePage.toString());
            pageList.add(str);
            map.put(str, System.currentTimeMillis()+timeout*1000);
        }
        else if(pageList.size() == capacity){
            String idd = pageList.get(0);
            for(int i = 1 ;i <pageList.size(); i++){
                if(map.get(pageList.get(i)) < map.get(idd)){
                    idd = pageList.get(i);
                }
            }
            pageList.remove(idd);
            str.concat(textInThePage.toString());
            pageList.add(str);
            map.put(str, System.currentTimeMillis()+timeout*1000);
        }


//        str.concat(textInThePage.toString());
//        map.put(str, System.currentTimeMillis()+timeout*1000);
        // for method 3
        requestList.add(pageTitle);
        limitNum++;
        return textInThePage.toString();
    }

    public List<String> zeitgeist(int limit){

        List<String> copy = new ArrayList<>();
        List<String> zeitlist = new ArrayList<>();
        for(String c:requestList){
            copy.add(c);
        }

        // a hashmap of string maps to frequency
        for(int i=0; i<copy.size(); i++){
            int k = 0;
            for(int j=0; j<copy.size();j++){
                if(i!=j&& copy.get(i)== copy.get(j)){
                    k++;
                }
            }
            if(!stringFrequency.containsKey(copy.get(i))){
                stringFrequency.put(copy.get(i),k);
            }
        }
        // sorting
        for (int i = 0 ; i < copy.size(); i ++){
            for (int j = 0 ; j < copy.size(); j ++){
                if(stringFrequency.get(copy.get(i)) > stringFrequency.get(copy.get(j))){
                    String temp = copy.get(i);
                    copy.add(i, copy.get(j));
                    copy.add(j, temp);
                }
            }
        }
        // return a list that is in descending order of the frequency of strings that appear in the query getPage and search
        for(int i=0; i<limitNum; i++){
            zeitlist.add(copy.get(i));
        }
        return zeitlist;
    }


    List<String> trending(int timeLimitInSeconds, int maxItems){
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

    public static void main(String arg[]){
        WikiMediator wk = new WikiMediator(5, 2);
    }
}
