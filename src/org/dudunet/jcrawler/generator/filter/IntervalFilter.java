/*
 */

package org.dudunet.jcrawler.generator.filter;


import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.util.Config;

/**
 * @author dudu
 */
public class IntervalFilter extends Filter{

    /**
     * @param generator
     */
    public IntervalFilter(Generator generator) {
        super(generator);
    }

    /**
     *
     * @return
     */
    @Override
    public CrawlingData next() {
        while(true){
        CrawlingData crawldata=generator.next();
        
         if(crawldata==null){
            return null;
        }
         
        
        if(crawldata.getStatus()== CrawlingData.STATUS_DB_UNFETCHED){
            return crawldata;
        }
        if(Config.interval==-1){
            continue;
        }
       
        Long lasttime=crawldata.getFetchTime();
        if(lasttime+Config.interval>System.currentTimeMillis()){
            continue;
        }
        return crawldata;
        }
    }
}
