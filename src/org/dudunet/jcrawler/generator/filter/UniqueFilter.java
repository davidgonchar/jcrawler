/*
 */

package org.dudunet.jcrawler.generator.filter;

import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.model.CrawlingData;

import java.util.HashSet;

/**
 * @author dudu
 */
public class UniqueFilter extends Filter{
    
    /**
     * @param generator
     */
    public UniqueFilter(Generator generator) {
        super(generator);
    }

    private HashSet<String> hashset=new HashSet<String>();
    
    /**
     * @param url
     */
    public void addUrl(String url){
         hashset.add(url);
    }

    /**
     * @return
     */

    @Override
    public CrawlingData next() {
        while(true){
        CrawlingData crawldata=generator.next();
        if(crawldata==null){
            return null;
        }
        String url=crawldata.getUrl();
        if(hashset.contains(url)){
            continue;
        }
        else{
            addUrl(url);
            return crawldata;
        }
        }
    }
    
}
