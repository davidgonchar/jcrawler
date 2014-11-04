/*
 */

package org.dudunet.jcrawler.generator;


import org.dudunet.jcrawler.model.CrawlingData;

/**
 * @author dudu
 */
public interface Generator{
   
    /**
     * @return
     */
    public CrawlingData next();
 
    
}
