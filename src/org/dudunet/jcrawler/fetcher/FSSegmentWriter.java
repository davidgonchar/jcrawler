/*
 */

package org.dudunet.jcrawler.fetcher;

import org.dudunet.jcrawler.generator.DbWriter;
import org.dudunet.jcrawler.model.Content;
import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.parser.ParseData;
import org.dudunet.jcrawler.parser.ParseResult;
import org.dudunet.jcrawler.parser.ParseText;
import org.dudunet.jcrawler.util.Config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dudu
 */
public class FSSegmentWriter implements SegmentWriter{
    

    private String segmentPath;

    /**
     * @param segmentPath
     */
    public FSSegmentWriter(String crawlPath,String segmentPath) {
        this.segmentPath =  segmentPath;
        count_fetch = 0;
        count_content = 0;
        count_parse = 0;

        try {
            fetchWriter = new DbWriter<CrawlingData>(CrawlingData.class, segmentPath + "/fetch/info.avro");
            contentWriter = new DbWriter<Content>(Content.class, segmentPath + "/content/info.avro");
            parseDataWriter = new DbWriter<ParseData>(ParseData.class, segmentPath + "/parse_data/info.avro");
            parseTextWriter = new DbWriter<ParseText>(ParseText.class, segmentPath + "/parse_text/info.avro");
        } catch (IOException ex) {
            Logger.getLogger(SegmentWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DbWriter<CrawlingData> fetchWriter;
    private DbWriter<Content> contentWriter;
    private DbWriter<ParseData> parseDataWriter;
    private DbWriter<ParseText> parseTextWriter;
    private int count_content;
    private int count_parse;
    private int count_fetch;

    /**
     * @param fetch
     * @throws java.io.IOException
     */
    public synchronized void writeFetch(CrawlingData fetch) throws IOException {
        fetchWriter.write(fetch);
        count_fetch = (count_fetch++) % Config.segmentwriter_buffer_size;
        if (count_fetch == 0) {
            fetchWriter.flush();
        }
    }

    /**
     * @param content
     * @throws java.io.IOException
     */
    public synchronized void writeContent(Content content) throws IOException {
        contentWriter.write(content);
        count_content = (count_content++) % Config.segmentwriter_buffer_size;
        if (count_content == 0) {
            contentWriter.flush();
        }
    }

    /**
     * @param parseresult
     * @throws java.io.IOException
     */
    public synchronized void writeParse(ParseResult parseresult) throws IOException {
        parseDataWriter.write(parseresult.getParsedata());
        parseTextWriter.write(parseresult.getParsetext());
        count_parse = (count_parse++) % Config.segmentwriter_buffer_size;
        if (count_parse == 0) {
            parseDataWriter.flush();
            parseTextWriter.flush();
        }
    }

    /**
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        fetchWriter.close();
        contentWriter.close();
        parseDataWriter.close();
        parseTextWriter.close();
    }
}
