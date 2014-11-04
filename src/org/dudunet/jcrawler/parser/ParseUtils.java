/*
 */

package org.dudunet.jcrawler.parser;

import java.io.UnsupportedEncodingException;

import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.util.CharsetDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dudu
 */
public class ParseUtils {
    
    
    public static Document parseDocument(byte[] content,String url){
        String charset= CharsetDetector.guessEncoding(content);
        String html;
        try {
            html = new String(content,charset);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
        Document doc=Jsoup.parse(html);
        doc.setBaseUri(url);
        return doc;
    } 
    
    public static Page parseDocument(Page page){
        Document doc=parseDocument(page.getContent(), page.getUrl());
        page.setDoc(doc);
        return page;
    }
}
