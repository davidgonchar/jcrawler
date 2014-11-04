/*
 */

package org.dudunet.jcrawler.parser;

import org.dudunet.jcrawler.model.Page;

/**
 * @author dudu
 */
public interface Parser {

    /**
     * @param page
     * @return
     * @throws Exception
     */
    public ParseResult getParse(Page page) throws Exception;
}
