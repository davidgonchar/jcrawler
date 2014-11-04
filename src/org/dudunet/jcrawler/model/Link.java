/*
 */

package org.dudunet.jcrawler.model;

/**
 * @author dudu
 */
public class Link {
    
    /**
     *
     */
    private String anchor;
    
    /**
     *
     */
    private String url;
    
    public Link(){
        
    }
    public Link(String anchor, String url) {
        this.anchor = anchor;
        this.url = url;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    
    
    
    
    
    
}
