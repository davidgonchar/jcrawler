/*
 */

package org.dudunet.jcrawler.net;

/**
 *
 * @author dudu
 */
public interface RequestFactory {
    public Request createRequest(String url) throws Exception;
    
}
