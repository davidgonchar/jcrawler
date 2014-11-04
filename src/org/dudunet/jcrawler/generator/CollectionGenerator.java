/*
 */

package org.dudunet.jcrawler.generator;

import org.dudunet.jcrawler.model.CrawlingData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author dudu
 */
public class CollectionGenerator implements Generator {
    private ArrayList<CrawlingData> data=new ArrayList<CrawlingData>();
    public  CollectionGenerator(){  
        iterator=data.iterator();
    }
    
    public  CollectionGenerator(Collection<CrawlingData> crawldatas){
        for(CrawlingData crawldata : crawldatas){
            data.add(crawldata);
        }
        iterator=data.iterator();
    }
    
    public void addUrls(Collection<String> urls){
        for(String url:urls){
            CrawlingData crawldata = new CrawlingData();
            crawldata.setUrl(url);
            crawldata.setFetchTime(CrawlingData.FETCHTIME_UNDEFINED);
            crawldata.setStatus(CrawlingData.STATUS_DB_UNFETCHED);
            data.add(crawldata);
        }
        iterator=data.iterator();
    }
    
    public void addUrl(String url){
       
        CrawlingData crawlingData=new CrawlingData();
        crawlingData.setUrl(url);
        crawlingData.setFetchTime(CrawlingData.FETCHTIME_UNDEFINED);
        crawlingData.setStatus(CrawlingData.STATUS_DB_UNFETCHED);
        data.add(crawlingData);
        iterator=data.iterator();
    }
    
    public void addCrawlData(Collection<CrawlingData> crawldata){
        data.addAll(crawldata);
        iterator=data.iterator();
    }
    
    

    private Iterator<CrawlingData> iterator;
    @Override
    public CrawlingData next() {
        if(iterator.hasNext()){
            return iterator.next();
        }else{
            return null;
        }
    }
    
    /*
    public static void main(String[] args){
        CollectionGenerator generator=new CollectionGenerator();
        generator.addUrl("http://abc1.com");
        generator.addUrl("http://abc2.com");
        CrawlingData crawldatum;
        while((crawldatum=generator.next())!=null){
            System.out.println(crawldatum.getUrl());
        }
    }
    */
    
}
