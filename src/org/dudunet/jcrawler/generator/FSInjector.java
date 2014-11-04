/*
 */

package org.dudunet.jcrawler.generator;


import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.util.Config;
import org.dudunet.jcrawler.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



/**
 * @author hu
 */
public class FSInjector extends BasicInjector{
    
    private String crawlPath;

    /**
     * @param crawlPath
     */
    public FSInjector(String crawlPath){
        this.crawlPath=crawlPath;
    }
    
 
    
    
    public void inject(ArrayList<String> urls,boolean append) throws IOException{
         
        
        String info_path= Config.current_info_path;
        File inject_file=new File(crawlPath,info_path);
        if(!inject_file.getParentFile().exists()){
            inject_file.getParentFile().mkdirs();
        }
        DbWriter<CrawlingData> writer;
        if(inject_file.exists())
            writer=new DbWriter<CrawlingData>(CrawlingData.class,inject_file,append);
        else
            writer=new DbWriter<CrawlingData>(CrawlingData.class,inject_file,false);
        for(String url:urls){
            CrawlingData crawldata=new CrawlingData();
            crawldata.setUrl(url);
            crawldata.setStatus(CrawlingData.STATUS_DB_UNFETCHED);
            writer.write(crawldata);
            LogUtils.getLogger().info("inject "+url);
        }
        writer.close();
        
   
    }
    
    /*
    public static void main(String[] args) throws IOException{
        Injector inject=new Injector("/home/hu/data/crawl_avro");
        inject.inject("http://www.xinhuanet.com/");
    }
    */
}
