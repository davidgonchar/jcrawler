/*
 */
package org.dudunet.jcrawler.generator;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.reflect.ReflectDatumReader;
import org.dudunet.jcrawler.model.CrawlingData;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author dudu
 * @param <T>
 */
public class DbReader<T> {

    Class<T> type;
    Iterator<T> iterator;
    DataFileReader<T> dataFileReader;

    /**
     * @param type
     * @param dbfile
     * @throws java.io.IOException
     */
    public DbReader(Class<T> type,File dbfile) throws IOException {
        this.type=type;
        DatumReader<T> datumReader = new ReflectDatumReader<T>(type);
        dataFileReader = new DataFileReader<T>(dbfile, datumReader);
        iterator = dataFileReader.iterator();
    }

    /**
     * @param type
     * @param dbpath
     * @throws java.io.IOException
     */
    public DbReader(Class<T> type,String dbpath) throws IOException {
        this(type,new File(dbpath));
    }

    /**
     * @return
     */
    public T readNext() {
        return iterator.next();
    }

    /**
     * @return
     */
    public boolean hasNext(){
        return iterator.hasNext();
    }
    
    /**
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        dataFileReader.close();
    }

    
    public static void main(String[] args) throws IOException{
        if(args.length==0){
            System.err.println("Usage dbpath");           
            main(new String[]{"/home/hu/data/crawl_hfut1/crawldb/current/info.avro"});
            return;
        }
        String dbpath=args[0];
        DbReader<CrawlingData> reader=new DbReader<CrawlingData>(CrawlingData.class,dbpath);
        int sum=0;
        int sum_fetched=0;
        int sum_unfetched=0;
        
        
        CrawlingData crawldata=null;

        System.out.println("start read:");
        while(reader.hasNext()){
            crawldata=reader.readNext();
            System.out.println(crawldata.getUrl());
            sum++;
            switch(crawldata.getStatus()){
                case CrawlingData.STATUS_DB_FETCHED:
                    sum_fetched++;
                    break;
                case CrawlingData.STATUS_DB_UNFETCHED:
                    sum_unfetched++;
                    break;
                    
            }
            
         
        }
        reader.close();
    }
}
