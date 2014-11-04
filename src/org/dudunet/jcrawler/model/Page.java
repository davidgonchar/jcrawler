/*
 */

package org.dudunet.jcrawler.model;

import org.dudunet.jcrawler.net.Response;
import org.dudunet.jcrawler.parser.ParseResult;
import org.jsoup.nodes.Document;


/**
 * @author dudu
 */
public class Page {
    private Response response=null;
    private String url=null;  
    private String html=null;
    private Document doc=null;  
    private long fetchTime;
    private ParseResult parseResult=null;
    
    /**
     * @param response
     */
    public void setResponse(Response response){
        this.response=response;
    }
    
    /**
     * @return
     */
    public Response getResponse(){
        return response;
    }
    
    /**
     * @return
     */
    public byte[] getContent() {
        if(response==null)
            return null;
        return response.getContent();
    }

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
    public String getHtml() {
        return html;
    }

    /**
     * @param html
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * @return
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * @param doc
     */
    public void setDoc(Document doc) {
        this.doc = doc;
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

    /**
     * @return
     */
    public ParseResult getParseResult() {
        return parseResult;
    }

    /**
     * @param parseResult
     */
    public void setParseResult(ParseResult parseResult) {
        this.parseResult = parseResult;
    }
    
    
    
    
}
