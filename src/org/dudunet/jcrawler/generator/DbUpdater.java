/*
 */
package org.dudunet.jcrawler.generator;

import org.dudunet.jcrawler.fetcher.SegmentWriter;

import java.io.IOException;




/**
 * @author dudu
 */
public interface DbUpdater {
    
    
  
    public void lock() throws Exception;
    public boolean isLocked() throws Exception;
    public void unlock() throws Exception;

    public void initSegmentWriter() throws Exception;
    public void close() throws Exception;
    public void merge() throws Exception;
    
    public SegmentWriter getSegmentWriter();
    //public void setSegmentWriter(SegmentWriter segmentWriter);
    public void clearHistory();
   
}
