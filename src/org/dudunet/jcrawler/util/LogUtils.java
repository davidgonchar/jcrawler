/*
 */
package org.dudunet.jcrawler.util;


import java.io.PrintWriter;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author dudu
 */
public class LogUtils {
   
    private static Logger logger=null;
    static{
        logger=createCommonLogger("default");
    }
   
     public static Logger createCommonLogger(String defaultLogName) {
        Logger logger=Logger.getLogger(defaultLogName);
        ConsoleAppender ca = new ConsoleAppender();
        ca.setName("default");
        ca.setWriter(new PrintWriter(System.out));
        ca.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %p %c %x - %m%n "));
        logger.addAppender(ca);
        return logger;
    }

    public static Logger getLogger() {  
        return logger;
    }

    public static void setLogger(Logger logger) {
        LogUtils.logger = logger;
    }
    
   
    
    
   

    
}
