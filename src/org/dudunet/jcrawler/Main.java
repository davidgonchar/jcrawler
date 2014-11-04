package org.dudunet.jcrawler;

import org.dudunet.jcrawler.crawler.BreadthCrawler;
import org.dudunet.jcrawler.model.Page;

public class Main {

///*
    public static void main(String[] args) throws Exception {

        String crawl_path = "/media/david/vhd/crawler/jcrawler";
        String root = "/media/david/vhd/crawler";

        BreadthCrawler crawler = new BreadthCrawler();

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

        crawler.start(5);
    }
//    */

}
