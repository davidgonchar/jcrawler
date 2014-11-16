/*
 */

package org.dudunet.jcrawler.crawler;


import org.dudunet.jcrawler.fetcher.Fetcher;
import org.dudunet.jcrawler.fetcher.FetcherThreadPool;
import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.net.HttpRequest;
import org.dudunet.jcrawler.net.Request;
import org.dudunet.jcrawler.output.FileSystemOutput;
import org.dudunet.jcrawler.parser.HtmlParser;
import org.dudunet.jcrawler.parser.Parser;
import org.dudunet.jcrawler.util.CommonConnectionConfig;
import org.dudunet.jcrawler.util.Config;
import org.dudunet.jcrawler.util.ConnectionConfig;
import org.dudunet.jcrawler.util.LogUtils;

import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dudu
 */
public abstract class CommonCrawler extends Crawler {

    private static volatile AtomicLong pagesVisited = new AtomicLong(0);
    private static volatile AtomicLong pagesFailed = new AtomicLong(0);

    protected String crawlPath = "crawl";
    protected String root = "data";

    protected String cookie = null;
    protected String useragent = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:26.0) Gecko/20100101 Firefox/26.0";
    protected ConnectionConfig conconfig = null;

    private boolean isContentStored = true;
    private Proxy proxy = null;

    /**
     * @param url
     * @return
     * @throws Exception
     */
    @Override
    public Request createRequest(String url) throws Exception {
        HttpRequest request = new HttpRequest();
        URL _URL = new URL(url);
        request.setURL(_URL);
        request.setProxy(proxy);
        request.setConnectionConfig(conconfig);
        return request;
    }

    /**
     * @param url
     * @param contentType
     * @return
     * @throws Exception
     */
    @Override
    public Parser createParser(String url, String contentType) throws Exception {
        if (contentType == null) {
            return null;
        }
        if (contentType.contains("text/html")) {
            HtmlParser parser=new HtmlParser(Config.topN);
            parser.setRegexRule(getRegexRule());
            return parser;
        }
        return null;
    }


    @Override
    public Fetcher createFetcher() {
        conconfig = new CommonConnectionConfig(useragent, cookie);

        FetcherThreadPool fetcher = new FetcherThreadPool();
        fetcher.setNeedUpdateDb(true);
        fetcher.setIsContentStored(isContentStored);
        fetcher.setThreads(getThreads());

        return fetcher;
    }

    /**
     * @return User-Agent
     */
    public String getUseragent() {
        return useragent;
    }

    /**
     * @param useragent
     */
    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    /**
     *
     * @return
     */
    public ConnectionConfig getConconfig() {
        return conconfig;
    }

    /**
     *
     * @param conconfig
     */
    public void setConconfig(ConnectionConfig conconfig) {
        this.conconfig = conconfig;
    }

    /**
     * @return
     */
    public boolean getIsContentStored() {
        return isContentStored;
    }

    /**
     * @param isContentStored
     */
    public void setIsContentStored(boolean isContentStored) {
        this.isContentStored = isContentStored;
    }

    /**
     * @return
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * @param proxy
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * @return Cookie
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * @param cookie Cookie
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    /**
     * @return
     */
    public String getCrawlPath() {
        return crawlPath;
    }

    /**
     * @param crawlPath
     */
    public void setCrawlPath(String crawlPath) {
        this.crawlPath = crawlPath;
    }



    /**
     * @return
     */
    public String getRoot() {
        return root;
    }

    /**
     * @param root
     */
    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    public void visit(Page page) {
        FileSystemOutput fsoutput = new FileSystemOutput(root);
//        LogUtils.getLogger().info("visit " + page.getUrl());
        fsoutput.output(page);
        long visited = pagesVisited.incrementAndGet();
        if (visited % 100 == 0) {
            LogUtils.getLogger().info("number of visited pages: " + visited);
        }
    }

    @Override
    public void failed(Page page) {
        LogUtils.getLogger().info("failed " + page.getUrl());
        long failed = pagesFailed.incrementAndGet();
        LogUtils.getLogger().info("number of failed pages: " + failed);
    }

}
