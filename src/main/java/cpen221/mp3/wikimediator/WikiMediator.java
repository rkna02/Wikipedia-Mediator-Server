package cpen221.mp3.wikimediator;

import org.fastily.jwiki.core.Wiki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public WikiMediator(int capacity, int stalenessInterval){
        this.capacity= capacity;
        this.timeout = stalenessInterval;
        str = new String();
        map = new HashMap<>();

    }

    public List<String> search(String query, int limit){
        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        List<String> searchlist = new ArrayList<>();
        searchlist = wiki.search(query,limit);
        return searchlist;
    }

    public String getPage(String pageTitle){


        Wiki wiki = new Wiki.Builder().withDomain("en.wikipedia.org").build();
        // Gets the text of the main page and prints it.
        System.out.println( wiki.getPageText(pageTitle) );
        StringBuilder textInThePage = new StringBuilder() ;
        textInThePage.append( wiki.getPageText(pageTitle) );
        str.concat(textInThePage.toString());
        map.put(str, System.currentTimeMillis()+timeout*1000);
        return textInThePage.toString();
    }

    public List<String> zeitgeist(int limit){



        // return a list that is in descending order of the frequency of strings that appear in  getPage and search
        return null;
    }

}
