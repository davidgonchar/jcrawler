/*
 */

package org.dudunet.jcrawler.parser;

import org.apache.avro.reflect.Nullable;

/**
 * @author dudu
 */
public class ParseText {
    @Nullable
    private String url=null;
    @Nullable
    private String text=null;
    
    public ParseText(){
        
    }

    public ParseText(String url,String text) {
        this.url=url;
        this.text=text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
