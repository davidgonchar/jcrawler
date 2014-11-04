/*
 */
package org.dudunet.jcrawler.generator;

import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.util.Config;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author dudu
 */
public class FSGenerator implements Generator {

    private String crawlPath;
    private DbReader<CrawlingData> dbreader;
    private boolean isStarted = false;

    /**
     * 构造一个广度遍历爬取任务生成器，从制定路径的文件夹中获取任务
     *
     * @param crawlPath 存储爬取信息的文件夹
     */
    public FSGenerator(String crawlPath) {
        this.crawlPath = crawlPath;

    }

    @Override
    public CrawlingData next() {
        if (!isStarted) {
            try {
                File oldfile = new File(crawlPath, Config.current_info_path);
                DbReader<CrawlingData> r = new DbReader<CrawlingData>(CrawlingData.class, oldfile);

                dbreader = new DbReader<CrawlingData>(CrawlingData.class, oldfile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            isStarted = true;

        }

        if (!dbreader.hasNext()) {
            return null;
        }

        CrawlingData crawldata = dbreader.readNext();

        if (crawldata == null) {
            return null;
        }

        if (shouldFilter(crawldata.getUrl())) {
            return next();
        }
        return crawldata;
    }

    /**
     *
     * @param url
     * @return
     */
    protected boolean shouldFilter(String url) {
        return false;
    }

    /*
     public static void main(String[] args) throws IOException {
     Injector inject=new Injector("/home/hu/data/crawl_avro");
     inject.inject("http://www.xinhuanet.com/");
     String crawl_path = "/home/hu/data/crawl_avro";
     StandardGenerator bg = new StandardGenerator(null) {
     @Override
     public boolean shouldFilter(String url) {
     if (Pattern.matches("http://news.xinhuanet.com/world/.*", url)) {
     return false;
     } else {
     return true;
     }
     }

     };
     
       

     }
     */
}
