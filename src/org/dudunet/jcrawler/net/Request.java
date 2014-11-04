/*
 */

package org.dudunet.jcrawler.net;


import org.dudunet.jcrawler.model.CrawlingData;

import java.net.URL;

/**
 * @author dudu
 */
public interface Request {
    public URL getURL();
    public void setURL(URL url); 
    
    public Response getResponse(CrawlingData datum) throws Exception;

}
