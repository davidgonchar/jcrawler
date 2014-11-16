/*
 */
package org.dudunet.jcrawler.fetcher;


import org.dudunet.jcrawler.model.Content;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.parser.ParseResult;

/**
 * @author dudu
 */
public interface SegmentWriter {

   /**
     * @param fetch
     * @throws Exception
     */
    public  void writeFetch(CrawlingData fetch) throws Exception;

    /**
     * @param content
     * @throws Exception
     */
    public void writeContent(Content content) throws Exception;

    /**
     * @param parseresult
     * @throws Exception
     */
    public void writeParse(ParseResult parseresult) throws Exception;

    /**
     * @throws Exception
     */
    public void close() throws Exception;
}
