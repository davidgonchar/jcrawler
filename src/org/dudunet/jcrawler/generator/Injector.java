/*
 */

package org.dudunet.jcrawler.generator;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



/**
 * @author dudu
 */
public interface Injector{
    
    /**
     * @param url
     * @throws java.io.IOException
     */
    public void inject(String url) throws Exception;
    
    /**
     * @param urls
     * @throws java.io.IOException
     */
    public void inject(ArrayList<String> urls) throws Exception;
    
    /**
     * @param url
     * @param append
     * @throws java.io.IOException
     */
    public void inject(String url, boolean append) throws Exception;
    
        
    /**
     * @param urls
     * @param append
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    public void inject(ArrayList<String> urls, boolean append) throws Exception;
    
    /*
    public static void main(String[] args) throws IOException{
        Injector inject=new Injector("/home/hu/data/crawl_avro");
        inject.inject("http://www.xinhuanet.com/");
    }
    */
}
