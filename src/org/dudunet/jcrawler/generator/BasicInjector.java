/*
 */

package org.dudunet.jcrawler.generator;

import java.util.ArrayList;

/**
 *
 * @author dudu
 */
public abstract class BasicInjector implements Injector{
 /**
     * @param url
     * @throws java.io.IOException
     */
    @Override
    public void inject(String url) throws Exception{
        inject(url,false);
    }
    
    /**
     * @param urls
     * @throws java.io.IOException
     */
    @Override
    public void inject(ArrayList<String> urls) throws Exception{
        inject(urls,false);
    }
    
    /**
     * @param url
     * @param append
     * @throws java.io.IOException
     */
    @Override
    public void inject(String url,boolean append) throws Exception{
        ArrayList<String> urls=new ArrayList<String>();
        urls.add(url);
        inject(urls,append);
    }
    

    @Override
    public abstract void inject(ArrayList<String> urls, boolean append) throws Exception; 
    
}
