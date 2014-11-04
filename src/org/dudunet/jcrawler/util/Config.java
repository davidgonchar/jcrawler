/*
 */

package org.dudunet.jcrawler.util;

/**
 * @author dudu
 */
public class Config {
    public static final String old_info_path="crawldb/old/info.avro";
    public static final String current_info_path="crawldb/current/info.avro";
    public static final String new_info_path="crawldb/new/info.avro";
    public static final String lock_path="crawldb/lock";
    
    public static long requestMaxInterval=1000*60;

    /**
     */
    public static int maxsize=1000*1000;

    /**
     */
    public static long interval=-1;
    
    
    /**
     */
    public static Integer topN=null;
    
    /**
     */
    public static int segmentwriter_buffer_size=50;
}
