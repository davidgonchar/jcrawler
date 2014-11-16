/*
 */
package org.dudunet.jcrawler.crawler;


import org.dudunet.jcrawler.fetcher.Fetcher;
import org.dudunet.jcrawler.fetcher.FetcherForkJoinPool;
import org.dudunet.jcrawler.generator.*;
import org.dudunet.jcrawler.generator.filter.IntervalFilter;
import org.dudunet.jcrawler.generator.filter.URLRegexFilter;
import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.output.FileSystemOutput;
import org.dudunet.jcrawler.util.CommonConnectionConfig;
import org.dudunet.jcrawler.util.LogUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dudu
 */
public class BreadthCrawler extends CommonCrawler {

    @Override
    public Fetcher createFetcher() {
        conconfig = new CommonConnectionConfig(useragent, cookie);

        Fetcher fetcher = new FetcherForkJoinPool(getThreads());

        return fetcher;
    }

    @Override
    public DbUpdater createDbUpdater() {
        return new FSDbUpdater(crawlPath);
    }

    @Override
    public Injector createInjector() {
        return new FSInjector(crawlPath);
    }

    
    @Override
    public Generator createGenerator() {

        Generator generator = new FSGenerator(crawlPath);
        generator=new URLRegexFilter(new IntervalFilter(generator), getRegexRule());
        return generator;
    }
}
