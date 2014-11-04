/*
 */

package org.dudunet.jcrawler.handler;

/**
 * @author dudu
 */
public class Message {

    /**
     */
    public Object obj;

    /**
     */
    public int what;
    
    /**
     */
    public Message(){
        
    }

    /**
     * @param what
     * @param obj
     */
    public Message(int what,Object obj) {
        this.what = what;
        this.obj = obj;
        
    }
}
