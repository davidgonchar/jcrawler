package org.dudunet.jcrawler.fetcher;

import org.dudunet.jcrawler.generator.Generator;
import org.dudunet.jcrawler.handler.Message;
import org.dudunet.jcrawler.model.Content;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.model.Link;
import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.net.Request;
import org.dudunet.jcrawler.net.Response;
import org.dudunet.jcrawler.parser.LinkUtils;
import org.dudunet.jcrawler.parser.ParseResult;
import org.dudunet.jcrawler.parser.Parser;
import org.dudunet.jcrawler.util.HandlerUtils;
import org.dudunet.jcrawler.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * Created by david on 10/11/14.
 */
public class FetcherAction extends RecursiveAction {
    private String url;
    private Fetcher fetcher;
    private Generator generator;
    private int retry = 3;


    public FetcherAction(String url, Generator generator, Fetcher fetcher) {
        this.url = url;
        this.generator = generator;
        this.fetcher = fetcher;
    }

    @Override
    protected void compute() {
        try {
            List<RecursiveAction> actions = new ArrayList<RecursiveAction>();

            CrawlingData crawldata = null;
            if (url != null) {
                crawldata = new CrawlingData();
                crawldata.setUrl(url);
            } else {
                crawldata = generator.next();
                url = crawldata.getUrl();
            }

            Request request = fetcher.getRequestFactory().createRequest(url);
            Response response = null;

            for (int i = 0; i <= retry; i++) {
                if (i > 0) {
//                    LogUtils.getLogger().info("retry " + i + "th " + url);
                }
                try {
                    response = request.getResponse(crawldata);
                    break;
                } catch (Exception ex) {
//                    LogUtils.getLogger().info("retry failed " + url);
                }
            }

            crawldata.setStatus(CrawlingData.STATUS_DB_FETCHED);
            crawldata.setFetchTime(System.currentTimeMillis());

            Page page = new Page();
            page.setUrl(url);
            page.setFetchTime(crawldata.getFetchTime());

            if (response == null) {
                LogUtils.getLogger().info("failed " + url);
                HandlerUtils.sendMessage(fetcher.getHandler(), new Message(FetcherThreadPool.FETCH_FAILED, page), true);
                return;
            }

            page.setResponse(response);
            String contentType = response.getContentType();

            try {
                Parser parser = fetcher.getParserFactory().createParser(url, contentType);
                if (parser != null) {
                    ParseResult parseresult = parser.getParse(page);
                    page.setParseResult(parseresult);
                }

                fetcher.getDbUpdater().getSegmentWriter().writeFetch(crawldata);
                Content content = new Content();
                content.setUrl(url);
                if (response.getContent() != null) {
                    content.setContent(response.getContent());
                } else {
                    content.setContent(new byte[0]);
                }
                content.setContentType(contentType);
                fetcher.getDbUpdater().getSegmentWriter().writeContent(content);
                if (page.getParseResult() != null) {
                    fetcher.getDbUpdater().getSegmentWriter().writeParse(page.getParseResult());
                }

            } catch (Exception ex) {
                LogUtils.getLogger().info("Exception", ex);

            }

            HandlerUtils.sendMessage(fetcher.getHandler(), new Message(FetcherThreadPool.FETCH_SUCCESS, page), true);

            ArrayList<Link> links = page.getParseResult().getParsedata().getLinks();
            if (links != null) {
                for (Link link : links) {
                    String currUrl = link.getUrl();
                    if (!currUrl.equals(url) && !currUrl.isEmpty())
                        actions.add(new FetcherAction(currUrl, generator, fetcher));
                }
                //invoke recursively
                invokeAll(actions);
            }

        } catch (Exception e) {
            //ignore 404, unknown protocol or other server errors
            LogUtils.getLogger().info("failed " + url + ": " + e.getMessage());
        }

    }
}
