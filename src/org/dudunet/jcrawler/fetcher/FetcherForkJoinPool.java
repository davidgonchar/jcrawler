package org.dudunet.jcrawler.fetcher;

import org.dudunet.jcrawler.generator.DbUpdater;
import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.handler.Handler;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.net.RequestFactory;
import org.dudunet.jcrawler.parser.ParserFactory;

import java.util.concurrent.ForkJoinPool;

/**
 * Created by david on 10/11/14.
 */
public class FetcherForkJoinPool implements Fetcher {

    private ForkJoinPool mainPool;
    private RequestFactory requestFactory;
    private ParserFactory parserFactory;
    private Handler handler;
    private DbUpdater dbUpdater;

    public FetcherForkJoinPool(int maxThreads) {
        mainPool = new ForkJoinPool(maxThreads);
    }

    @Override
    public void fetchAll(Generator generator) throws Exception {
        try {

            if (dbUpdater.isLocked()) {
                dbUpdater.merge();
                dbUpdater.unlock();
            }

            dbUpdater.initSegmentWriter();
            dbUpdater.lock();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        CrawlingData cd = generator.next();
        mainPool.invoke(new FetcherAction(cd.getUrl(), generator, this));

        dbUpdater.close();
        dbUpdater.merge();
        dbUpdater.unlock();

    }

    @Override
    public void stop() {

    }

    @Override
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void setDbUpdater(DbUpdater dbUpdater) {
        this.dbUpdater = dbUpdater;
    }

    @Override
    public void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public void setParserFactory(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public DbUpdater getDbUpdater() {
        return dbUpdater;
    }

    @Override
    public RequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    @Override
    public ParserFactory getParserFactory() {
        return this.parserFactory;
    }
}
