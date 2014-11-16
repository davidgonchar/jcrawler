/*
 */
package org.dudunet.jcrawler.crawler;


import org.dudunet.jcrawler.fetcher.Fetcher;
import org.dudunet.jcrawler.fetcher.FetcherThreadPool;
import org.dudunet.jcrawler.generator.DbUpdater;
import org.dudunet.jcrawler.generator.DbUpdaterFactory;
import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.generator.Injector;
import org.dudunet.jcrawler.handler.Handler;
import org.dudunet.jcrawler.handler.Message;
import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.net.RequestFactory;
import org.dudunet.jcrawler.parser.ParserFactory;
import org.dudunet.jcrawler.util.LogUtils;
import org.dudunet.jcrawler.util.RegexRule;

import java.util.ArrayList;

/**
 * @author dudu
 */
public abstract class Crawler implements RequestFactory, ParserFactory, DbUpdaterFactory {

    protected int status;
    public final static int RUNNING = 1;
    public final static int STOPPED = 2;
    protected boolean resume = false;

    protected int threads = 10;

    protected RegexRule regexRule=new RegexRule();
    protected ArrayList<String> seeds = new ArrayList<String>();
    protected Fetcher fetcher;

        
    public abstract Injector createInjector();

    
    public abstract Generator createGenerator();

   
    /**
     * @return
     */
    public abstract Fetcher createFetcher();

    /**
     * @param depth
     * @throws Exception
     */
    public void start(int depth) throws Exception {

        if (!resume) {
            DbUpdater clearDbUpdater = createDbUpdater();
            if (clearDbUpdater != null) {
                clearDbUpdater.clearHistory();
            }

            if (seeds.isEmpty()) {
                LogUtils.getLogger().info("error:Please add at least one seed");
                return;
            }

        }
        if (regexRule.isEmpty()) {
            LogUtils.getLogger().info("error:Please add at least one positive regex rule");
            return;
        }
        inject();

        status = RUNNING;
        for (int i = 0; i < depth; i++) {
            if (status == STOPPED) {
                break;
            }
            LogUtils.getLogger().info("starting depth " + (i + 1));
            Generator generator = createGenerator();
            fetcher = createFetcher();
            fetcher = updateFetcher(fetcher);
            if (fetcher == null) {
                return;
            }
            fetcher.fetchAll(generator);
        }
    }

  
    protected Fetcher updateFetcher(Fetcher fetcher) {
        try {
            DbUpdater dbUpdater = createDbUpdater();
            if (dbUpdater != null) {
                fetcher.setDbUpdater(dbUpdater);
            }
            fetcher.setRequestFactory(this);
            fetcher.setParserFactory(this);
            fetcher.setHandler(createFetcherHandler());
            return fetcher;

        } catch (Exception ex) {
            LogUtils.getLogger().info("Exception", ex);
            return null;
        }
    }

    /**
     * @throws Exception
     */
    public void stop() throws Exception {
        fetcher.stop();
        status = STOPPED;
    }

    /**
     * @throws Exception
     */
    public void inject() throws Exception {
        Injector injector = createInjector();
        injector.inject(seeds, true);
    }

    /**
     * @param page
     */
    public void visit(Page page) {

    }

    /**
     * @param page
     */
    public void failed(Page page) {

    }

    /**
     *
     * @return
     */
    public Handler createFetcherHandler() {
        Handler fetchHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Page page = (Page) msg.obj;
                switch (msg.what) {
                    case FetcherThreadPool.FETCH_SUCCESS:
                        visit(page);
                        break;
                    case FetcherThreadPool.FETCH_FAILED:
                        failed(page);
                        break;
                    default:
                        break;

                }
            }
        };
        return fetchHandler;
    }

    /**
     *
     * @param seed
     */
    public void addSeed(String seed) {
        seeds.add(seed);
    }

    /**
     *
     * @param regex
     */
    public void addRegex(String regex) {
        regexRule.addRule(regex);
    }

    /**
     * @return
     */
    public boolean isResume() {
        return resume;
    }

    /**
     * @param resume
     */
    public void setResume(boolean resume) {
        this.resume = resume;
    }

    /**
     * @return
     */
    public int getThreads() {
        return threads;
    }

    /**
     * @param threads
     */
    public void setThreads(int threads) {
        this.threads = threads;
    }

    public RegexRule getRegexRule() {
        return regexRule;
    }

    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

   

    

    /**
     * @return
     */
    public ArrayList<String> getSeeds() {
        return seeds;
    }

    /**
     * @param seeds
     */
    public void setSeeds(ArrayList<String> seeds) {
        this.seeds = seeds;
    }

}
