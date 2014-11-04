/*
 */

package org.dudunet.jcrawler.util;


import org.dudunet.jcrawler.handler.Handler;
import org.dudunet.jcrawler.handler.Message;

/**
 *
 * @author hu
 */
public class HandlerUtils {
    public static void sendMessage(Handler handler,Message msg){
        sendMessage(handler, msg,false);
    }
    public static void sendMessage(Handler handler,Message msg,boolean checknull){
        if(checknull){
            if(handler==null){
                return;
            }
        }
        handler.sendMessage(msg);
    }
}
