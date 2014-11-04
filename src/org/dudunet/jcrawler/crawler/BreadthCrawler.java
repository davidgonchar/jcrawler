/*
 */
package org.dudunet.jcrawler.crawler;


import org.dudunet.jcrawler.generator.*;
import org.dudunet.jcrawler.generator.filter.IntervalFilter;
import org.dudunet.jcrawler.generator.filter.URLRegexFilter;
import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.output.FileSystemOutput;
import org.dudunet.jcrawler.util.LogUtils;

/**
 * @author dudu
 */
public class BreadthCrawler extends CommonCrawler {
    
    private String crawlPath = "crawl";
    private String root = "data";

    
    @Override
    public void visit(Page page) {
        FileSystemOutput fsoutput = new FileSystemOutput(root);
        LogUtils.getLogger().info("visit " + page.getUrl());
        fsoutput.output(page);
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

}
