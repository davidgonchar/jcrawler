package org.dudunet.jcrawler;

import org.dudunet.jcrawler.crawler.BreadthCrawler;
import org.dudunet.jcrawler.crawler.CommonCrawler;
import org.dudunet.jcrawler.generator.*;
import org.dudunet.jcrawler.generator.filter.IntervalFilter;
import org.dudunet.jcrawler.generator.filter.URLRegexFilter;
import org.dudunet.jcrawler.model.Page;

public class Main {

///*
    public static CommonCrawler getCrawler(String crawlerType) {
        if ("BFS".equalsIgnoreCase(crawlerType)) {
            return new BreadthCrawler();
        } else {
            return new CommonCrawler() {
                @Override
                public Injector createInjector() {
                    return new FSInjector(crawlPath);
                }

                @Override
                public Generator createGenerator() {
                    Generator generator = new FSGenerator(crawlPath);
                    generator = new URLRegexFilter(new IntervalFilter(generator), getRegexRule());
                    return generator;
                }

                @Override
                public DbUpdater createDbUpdater() throws Exception {
                    return new FSDbUpdater(crawlPath);
                }
            };
        }
    }

    public static void main(String[] args) throws Exception {

        String crawl_path = "/media/david/vhd/crawler/jcrawler";
        String root = "/media/david/vhd/crawler";

        CommonCrawler crawler = getCrawler("bfs");

        crawler.addSeed("http://www.javaworld.com/");

        crawler.addRegex("http://www.javaworld.com/.*");
        crawler.addRegex("-.*#.*");
        crawler.addRegex("-.*png.*");
        crawler.addRegex("-.*jpg.*");
        crawler.addRegex("-.*gif.*");
        crawler.addRegex("-.*js.*");
        crawler.addRegex("-.*css.*");

        crawler.setCrawlPath(crawl_path);
        crawler.setRoot(root);
        crawler.setResume(false);
        crawler.setThreads(100);

        crawler.start(5);
    }
//    */

}
