/*
 */
package org.dudunet.jcrawler.fetcher;

import org.dudunet.jcrawler.generator.DbUpdater;
import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.handler.Handler;
import org.dudunet.jcrawler.handler.Message;
import org.dudunet.jcrawler.model.Content;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.net.Request;
import org.dudunet.jcrawler.net.RequestFactory;
import org.dudunet.jcrawler.net.Response;
import org.dudunet.jcrawler.parser.ParseResult;
import org.dudunet.jcrawler.parser.Parser;
import org.dudunet.jcrawler.parser.ParserFactory;
import org.dudunet.jcrawler.util.Config;
import org.dudunet.jcrawler.util.HandlerUtils;
import org.dudunet.jcrawler.util.LogUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author dudu
 */
public class Fetcher {

    /**
     *
     */
    public DbUpdater dbUpdater = null;

    /**
     *
     */
    public Handler handler = null;

    /**
     *
     */
    public RequestFactory requestFactory = null;

    /**
     *
     */
    public ParserFactory parserFactory = null;
    private int retry = 3;
    private AtomicInteger activeThreads;
    private AtomicInteger spinWaiting;
    private AtomicLong lastRequestStart;
    private QueueFeeder feeder;
    private FetchQueue fetchQueue;
    private boolean needUpdateDb = true;

    /**
     *
     */
    public static final int FETCH_SUCCESS = 1;

    /**
     *
     */
    public static final int FETCH_FAILED = 2;
    private int threads = 10;
    private boolean isContentStored = true;
    private boolean parsing = true;

    /**
     *
     */
    public static class FetchItem {
        
        /**
         *
         */
        public CrawlingData datum;
        
        /**
         *
         * @param datum
         */
        public FetchItem(CrawlingData datum) {
            this.datum = datum;
        }
    }

    /**
     *
     */
    public static class FetchQueue {

        /**
         *
         */
        public AtomicInteger totalSize = new AtomicInteger(0);

        /**
         *
         */
        public List<FetchItem> queue = Collections.synchronizedList(new LinkedList<FetchItem>());
        
        /**
         *
         */
        public synchronized void clear() {
            queue.clear();
        }
        
        /**
         *
         * @return
         */
        public int getSize() {
            return queue.size();
        }
        
        /**
         *
         * @param item
         */
        public void addFetchItem(FetchItem item) {
            if (item == null) {
                return;
            }
            queue.add(item);
            totalSize.incrementAndGet();
        }
        
        /**
         *
         * @return
         */
        public synchronized FetchItem getFetchItem() {
            if (queue.size() == 0) {
                return null;
            }
            return queue.remove(0);
        }

        /**
         *
         */
        public synchronized void dump() {
            for (int i = 0; i < queue.size(); i++) {
                FetchItem it = queue.get(i);
                LogUtils.getLogger().info("  " + i + ". " + it.datum.getUrl());
            }
        }

    }

    /**
     *
     */
    public static class QueueFeeder extends Thread {

        /**
         *
         */
        public FetchQueue queue;

        /**
         *
         */
        public Generator generator;

        /**
         *
         */
        public int size;

        /**
         *
         * @param queue
         * @param generator
         * @param size
         */
        public QueueFeeder(FetchQueue queue, Generator generator, int size) {
            this.queue = queue;
            this.generator = generator;
            this.size = size;
        }

        @Override
        public void run() {

            boolean hasMore = true;
            while (hasMore) {

                int feed = size - queue.getSize();
                if (feed <= 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                    continue;
                }
                while (feed > 0 && hasMore) {

                    CrawlingData datum = generator.next();
                    hasMore = (datum != null);

                    if (hasMore) {
                        queue.addFetchItem(new FetchItem(datum));
                        feed--;
                    }

                }

            }

        }

    }

    private class FetcherThread extends Thread {

        @Override
        public void run() {
            activeThreads.incrementAndGet();
            FetchItem item = null;
            try {

                while (true) {
                    try {
                        item = fetchQueue.getFetchItem();
                        if (item == null) {
                            if (feeder.isAlive() || fetchQueue.getSize() > 0) {
                                spinWaiting.incrementAndGet();
                                try {
                                    Thread.sleep(500);
                                } catch (Exception ex) {
                                }
                                spinWaiting.decrementAndGet();
                                continue;
                            } else {
                                return;
                            }
                        }

                        lastRequestStart.set(System.currentTimeMillis());

                        CrawlingData crawldata = new CrawlingData();
                        String url = item.datum.getUrl();
                        crawldata.setUrl(url);

                        Request request = requestFactory.createRequest(url);
                        Response response = null;

                        for (int i = 0; i <= retry; i++) {
                            if (i > 0) {
                                LogUtils.getLogger().info("retry " + i + "th " + url);
                            }
                            try {
                                response = request.getResponse(crawldata);
                                break;
                            } catch (Exception ex) {

                            }
                        }

                        crawldata.setStatus(CrawlingData.STATUS_DB_FETCHED);
                        crawldata.setFetchTime(System.currentTimeMillis());

                        Page page = new Page();
                        page.setUrl(url);
                        page.setFetchTime(crawldata.getFetchTime());

                        if (response == null) {
                            LogUtils.getLogger().info("failed " + url);
                            HandlerUtils.sendMessage(handler, new Message(Fetcher.FETCH_FAILED, page), true);
                            continue;
                        }

                        page.setResponse(response);

                        LogUtils.getLogger().info("fetch " + url);

                        String contentType = response.getContentType();

                        if (parsing) {
                            try {
                                Parser parser = parserFactory.createParser(url, contentType);
                                if (parser != null) {
                                    ParseResult parseresult = parser.getParse(page);
                                    page.setParseResult(parseresult);
                                }
                            } catch (Exception ex) {
                                LogUtils.getLogger().info("Exception", ex);
                            }
                        }

                        if (needUpdateDb) {
                            try {
                                dbUpdater.getSegmentWriter().wrtieFetch(crawldata);
                                if (isContentStored) {
                                    Content content = new Content();
                                    content.setUrl(url);
                                    if (response.getContent() != null) {
                                        content.setContent(response.getContent());
                                    } else {
                                        content.setContent(new byte[0]);
                                    }
                                    content.setContentType(contentType);
                                    dbUpdater.getSegmentWriter().wrtieContent(content);
                                }
                                if (parsing && page.getParseResult() != null) {
                                    dbUpdater.getSegmentWriter().wrtieParse(page.getParseResult());
                                }

                            } catch (Exception ex) {
                                LogUtils.getLogger().info("Exception", ex);

                            }

                        }

                        HandlerUtils.sendMessage(handler, new Message(Fetcher.FETCH_SUCCESS, page), true);
                    } catch (Exception ex) {
                        LogUtils.getLogger().info("Exception", ex);
                    }
                }

            } catch (Exception ex) {
                LogUtils.getLogger().info("Exception", ex);

            } finally {
                activeThreads.decrementAndGet();
            }

        }

    }

    
    private void before() throws Exception {
        //DbUpdater recoverDbUpdater = createRecoverDbUpdater();

        if (needUpdateDb) {
            try {

                if (dbUpdater.isLocked()) {
                    dbUpdater.merge();
                    dbUpdater.unlock();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            dbUpdater.initSegmentWriter();
            dbUpdater.lock();
        }

        running = true;
    }

    /**
     * @param generator
     * @throws java.io.IOException
     */
    public void fetchAll(Generator generator) throws Exception {
        before();

        lastRequestStart = new AtomicLong(System.currentTimeMillis());

        activeThreads = new AtomicInteger(0);
        spinWaiting = new AtomicInteger(0);
        fetchQueue = new FetchQueue();
        feeder = new QueueFeeder(fetchQueue, generator, 1000);
        feeder.start();

        FetcherThread[] fetcherThreads=new FetcherThread[threads];
        for (int i = 0; i < threads; i++) {
            fetcherThreads[i]=new FetcherThread();
            fetcherThreads[i].start();
        }

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            LogUtils.getLogger().info("-activeThreads=" + activeThreads.get()
                    + ", spinWaiting=" + spinWaiting.get() + ", fetchQueue.size="
                    + fetchQueue.getSize());

            if (!feeder.isAlive() && fetchQueue.getSize() < 5) {
                fetchQueue.dump();
            }

            if ((System.currentTimeMillis() - lastRequestStart.get()) > Config.requestMaxInterval) {
                LogUtils.getLogger().info("Aborting with " + activeThreads + " hung threads.");
                break;
            }

        } while (activeThreads.get() > 0 && running);

        for(int i=0;i<threads;i++){
            if(fetcherThreads[i].isAlive()){
                fetcherThreads[i].stop();
            }
        }
        feeder.stop();
        fetchQueue.clear();
        after();

    }

    boolean running;

    /**
     */
    public void stop() {
        running = false;
    }

    private void after() throws Exception {

        if (needUpdateDb) {
            dbUpdater.close();
            dbUpdater.merge();
            dbUpdater.unlock();
        }
    }

    /**
     *
     * @return
     */
    public int getThreads() {
        return threads;
    }

    /**
     *
     * @param threads
     */
    public void setThreads(int threads) {
        this.threads = threads;
    }

    /**
     *
     * @return
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     *
     * @return
     */
    public boolean getNeedUpdateDb() {
        return needUpdateDb;
    }

    /**
     *
     * @param needUpdateDb
     */
    public void setNeedUpdateDb(boolean needUpdateDb) {
        this.needUpdateDb = needUpdateDb;
    }

    /**
     *
     * @return
     */
    public int getRetry() {
        return retry;
    }

    /**
     *
     * @param retry
     */
    public void setRetry(int retry) {
        this.retry = retry;
    }

    /**
     *
     * @return
     */
    public boolean isIsContentStored() {
        return isContentStored;
    }

    /**
     *
     * @param isContentStored
     */
    public void setIsContentStored(boolean isContentStored) {
        this.isContentStored = isContentStored;
    }

    /**
     * @return
     */
    public boolean isParsing() {
        return parsing;
    }

    /**
     * @param parsing
     */
    public void setParsing(boolean parsing) {
        this.parsing = parsing;
    }

    /**
     * @return
     */
    public DbUpdater getDbUpdater() {
        return dbUpdater;
    }

    /**
     * @param dbUpdater
     */
    public void setDbUpdater(DbUpdater dbUpdater) {
        this.dbUpdater = dbUpdater;
    }

    /**
     * @return
     */
    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    /**
     * @param requestFactory
     */
    public void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    /**
     * @return
     */
    public ParserFactory getParserFactory() {
        return parserFactory;
    }

    /**
     * @param parserFactory
     */
    public void setParserFactory(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

}
