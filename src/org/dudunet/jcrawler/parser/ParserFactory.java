/*
 */

package org.dudunet.jcrawler.parser;

/**
 *
 * @author dudu
 */
public interface ParserFactory {
    public Parser createParser(String url, String contentType) throws Exception; 
}
