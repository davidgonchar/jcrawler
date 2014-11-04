/*
 */

package org.dudunet.jcrawler.model;


import org.apache.avro.reflect.Nullable;


/**
 * @author dudu
 */
public class CrawlingData {
    /**
     */
    public static final int STATUS_DB_UNDEFINED=-1;
    /**
     */
    public static final int STATUS_DB_UNFETCHED=1;
    /**
     */
    public static final int STATUS_DB_FETCHED=2;
    
    
    /**
     */
    public static final long FETCHTIME_UNDEFINED=1;
    
    
    
    @Nullable private String url;
    @Nullable private int status= CrawlingData.STATUS_DB_UNDEFINED;
    @Nullable private long fetchTime= CrawlingData.FETCHTIME_UNDEFINED;

    /**
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return
     */
    public long getFetchTime() {
        return fetchTime;
    }

    /**
     * @param fetchTime
     */
    public void setFetchTime(long fetchTime) {
        this.fetchTime = fetchTime;
    }
}
