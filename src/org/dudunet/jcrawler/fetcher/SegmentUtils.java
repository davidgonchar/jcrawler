/*
 */

package org.dudunet.jcrawler.fetcher;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author dudu
 */
public class SegmentUtils {
     /**
     * @return
     */
    public static synchronized String createSegmengName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String datestr = sdf.format(date);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return datestr;
    }
}
