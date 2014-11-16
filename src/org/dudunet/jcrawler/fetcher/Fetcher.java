package org.dudunet.jcrawler.fetcher;

import org.dudunet.jcrawler.generator.DbUpdater;
import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.handler.Handler;
import org.dudunet.jcrawler.net.RequestFactory;
import org.dudunet.jcrawler.parser.ParserFactory;

/**
 * Created by david on 10/11/14.
 */
public interface Fetcher {
    public void fetchAll(Generator generator) throws Exception;
    public void stop();
    public void setHandler(Handler handler);
    public void setDbUpdater(DbUpdater dbUpdater);
    public void setRequestFactory(RequestFactory requestFactory);
    public void setParserFactory(ParserFactory parserFactory);
    public Handler getHandler();
    public DbUpdater getDbUpdater();
    public RequestFactory  getRequestFactory();
    public ParserFactory  getParserFactory();
}
