/*
 */
package org.dudunet.jcrawler.generator;

import org.dudunet.jcrawler.fetcher.FSSegmentWriter;
import org.dudunet.jcrawler.fetcher.SegmentUtils;
import org.dudunet.jcrawler.fetcher.SegmentWriter;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.model.Link;
import org.dudunet.jcrawler.parser.ParseData;
import org.dudunet.jcrawler.util.BloomFilter;
import org.dudunet.jcrawler.util.Config;
import org.dudunet.jcrawler.util.FileUtils;
import org.dudunet.jcrawler.util.LogUtils;

import java.io.File;
import java.io.IOException;


/**
 *
 * @author dudu
 */
public class FSDbUpdater implements DbUpdater {

    private SegmentWriter segmentWriter = null;
    private String crawlPath;

    private String segmentName;

    /**
     *
     * @param crawlPath
     */
    public FSDbUpdater(String crawlPath) {
        this.crawlPath = crawlPath;

    }

    protected String getLastSegmentName() {
        String[] segment_list = new File(crawlPath, "segments").list();
        if (segment_list == null) {
            return null;
        }
        String segment_path = null;
        long max = 0;
        for (String segment : segment_list) {
            long timestamp = Long.valueOf(segment);
            if (timestamp > max) {
                max = timestamp;
                segment_path = segment;
            }
        }
        return segment_path;
    }

    /**
     *
     * @throws java.io.IOException
     */
    public void backup() throws IOException {
        LogUtils.getLogger().info("backup " + getCrawlPath());
        File oldfile = new File(crawlPath, Config.old_info_path);
        File currentfile = new File(crawlPath, Config.current_info_path);
        FileUtils.copy(currentfile, oldfile);
    }

    /**
     *
     * @return
     * @throws java.io.IOException
     */
    @Override
    public boolean isLocked() throws IOException {
        File lockfile = new File(crawlPath + "/" + Config.lock_path);
        if (!lockfile.exists()) {
            return false;
        }
        String lock = new String(FileUtils.readFile(lockfile), "utf-8");
        return lock.equals("1");
    }

    /**
     *
     * @throws java.io.IOException
     */
    @Override
    public void lock() throws IOException {
        FileUtils.writeFile(crawlPath + "/" + Config.lock_path, "1".getBytes("utf-8"));
    }

    /**
     *
     * @throws java.io.IOException
     */
    @Override
    public void unlock() throws IOException {
        FileUtils.writeFile(crawlPath + "/" + Config.lock_path, "0".getBytes("utf-8"));
    }
    // DataFileWriter<CrawlingData> dataFileWriter;

    

    /**
     *
     * @throws java.io.IOException
     */
    @Override
    public void close() throws Exception {
        if (segmentWriter != null) {
            segmentWriter.close();
        }
    }

    int mergeCount = 0;

    public void reportMergeCount() {
        mergeCount++;
        if (mergeCount % 10000 == 0) {
            LogUtils.getLogger().info(mergeCount + " crawlingdata merged");
        }
    }
    
    private void install() throws IOException{
        File file_old = new File(crawlPath, Config.old_info_path);
        File file_new = new File(crawlPath, Config.new_info_path);
        File file_current = new File(crawlPath, Config.current_info_path);
        
        if(file_old.exists()){
            file_old.delete();
        }
        
        if(!file_old.getParentFile().exists()){
            file_old.getParentFile().mkdirs();
        }

        
        FileUtils.copy(file_current, file_old);
        file_current.delete();
        FileUtils.copy(file_new, file_current);
        file_new.delete();
    }

    /**
     * @throws java.io.IOException
     */
    @Override
    public void merge() throws IOException {
        if (segmentName == null) {
            segmentName = getLastSegmentName();
        }
        if (segmentName == null) {
            return;
        }

        /*
        try {
            backup();
        } catch (IOException ex) {
            LogUtils.getLogger().info("Exception", ex);
        }
        */

//        LogUtils.getLogger().info("merge " + getSegmentPath());
        mergeCount = 0;
        File file_fetch = new File(getSegmentPath(), "fetch/info.avro");
        if (!file_fetch.exists()) {
            return;
        }

        File file_current = new File(crawlPath, Config.current_info_path);
        DbReader<CrawlingData> reader_current = new DbReader<CrawlingData>(CrawlingData.class, file_current);
        DbReader<CrawlingData> reader_fetch = new DbReader<CrawlingData>(CrawlingData.class, file_fetch);

        File file_new = new File(crawlPath, Config.new_info_path);
        if (!file_new.getParentFile().exists()) {
            file_new.getParentFile().mkdirs();
        }
        DbWriter<CrawlingData> writer = new DbWriter<CrawlingData>(CrawlingData.class, file_new);

        BloomFilter bloomFilter = new BloomFilter();
        CrawlingData datum = null;
        while (reader_fetch.hasNext()) {
            datum = reader_fetch.readNext();

            bloomFilter.add(datum.getUrl());
            writer.write(datum);
            reportMergeCount();
        }

        reader_fetch.close();

        while (reader_current.hasNext()) {
            datum = reader_current.readNext();
            if (bloomFilter.contains(datum.getUrl())) {
                continue;
            }
            bloomFilter.add(datum.getUrl());
            writer.write(datum);
            reportMergeCount();
        }

        reader_current.close();

        File file_parse = new File(getSegmentPath(), "parse_data/info.avro");
        if (file_parse.exists()) {
            DbReader<ParseData> reader_parse = new DbReader<ParseData>(ParseData.class, file_parse);
            ParseData parseresult = null;
            while (reader_parse.hasNext()) {
                parseresult = reader_parse.readNext();
                for (Link link : parseresult.getLinks()) {
                    if (bloomFilter.contains(link.getUrl())) {
                        continue;
                    }
                    datum = new CrawlingData();
                    datum.setUrl(link.getUrl());
                    datum.setStatus(CrawlingData.STATUS_DB_UNFETCHED);
                    bloomFilter.add(datum.getUrl());
                    writer.write(datum);
                    reportMergeCount();
                }
            }
            reader_parse.close();
        }
        
        writer.close();
        
        install();

    }

    @Override
    public SegmentWriter getSegmentWriter() {
        return segmentWriter;
    }

    public String getSegmentPath() {
        return crawlPath + "/segments/" + segmentName;
    }

    public String getCrawlPath() {
        return crawlPath;
    }

    public void setCrawlPath(String crawlPath) {
        this.crawlPath = crawlPath;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    @Override
    public void clearHistory() {

        File file = new File(crawlPath);
        LogUtils.getLogger().info("clear " + file.getAbsolutePath());
        if (file.exists()) {
            FileUtils.deleteDir(file);
        }

    }

    @Override
    public void initSegmentWriter() throws Exception {
        segmentName = SegmentUtils.createSegmengName();
        segmentWriter = new FSSegmentWriter(crawlPath, getSegmentPath());
    }

}
