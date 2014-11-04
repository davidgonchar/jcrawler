/*
 */

package org.dudunet.jcrawler.crawler;


import org.dudunet.jcrawler.fetcher.Fetcher;
import org.dudunet.jcrawler.net.HttpRequest;
import org.dudunet.jcrawler.net.Request;
import org.dudunet.jcrawler.parser.HtmlParser;
import org.dudunet.jcrawler.parser.Parser;
import org.dudunet.jcrawler.util.CommonConnectionConfig;
import org.dudunet.jcrawler.util.Config;
import org.dudunet.jcrawler.util.ConnectionConfig;

import java.net.Proxy;
import java.net.URL;

/**
 * @author dudu
 */
public abstract class CommonCrawler extends Crawler {
    private String cookie = null;
    private String useragent = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:26.0) Gecko/20100101 Firefox/26.0";

    private boolean isContentStored = true;
    private Proxy proxy = null;
    private ConnectionConfig conconfig = null;

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
        Fetcher fetcher = new Fetcher();
        fetcher.setNeedUpdateDb(true);
        fetcher.setIsContentStored(isContentStored);
        conconfig = new CommonConnectionConfig(useragent, cookie);
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
}
