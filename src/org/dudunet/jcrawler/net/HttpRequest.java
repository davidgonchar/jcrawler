/*
 */

package org.dudunet.jcrawler.net;

import org.dudunet.jcrawler.model.CrawlingData;
import org.dudunet.jcrawler.util.Config;
import org.dudunet.jcrawler.util.ConnectionConfig;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;


/**
 * @author dudu
 */
public class HttpRequest implements Request {

    private URL url=null;
    private Proxy proxy=null;
    private ConnectionConfig config=null;

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public void setURL(URL url) {
        this.url=url;
    }

    @Override
    public Response getResponse(CrawlingData datum) throws Exception {
        HttpResponse response=new HttpResponse(url);
        HttpURLConnection con;
        
        if(proxy==null){
            con=(HttpURLConnection) url.openConnection();
        }else{
            con=(HttpURLConnection) url.openConnection(proxy);
        }

        con.setDoInput(true);
        con.setDoOutput(true);
        
        if(config!=null){
            config.config(con);
        }
        
        
        InputStream is;
  
        response.setCode(con.getResponseCode());
        
        
        is=con.getInputStream();
            

        byte[] buf = new byte[2048];
        int read;
        int sum=0;
        int maxsize= Config.maxsize;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((read = is.read(buf)) != -1) {
            if(maxsize>0){
            sum=sum+read;
                if(sum>maxsize){
                    read=maxsize-(sum-read);
                    bos.write(buf, 0, read);                    
                    break;
                }
            }
            bos.write(buf, 0, read);
        }

        is.close();       
        
        response.setContent(bos.toByteArray());
        response.setHeaders(con.getHeaderFields());
        bos.close();
        return response;
    }
    

   
    /**
     * @param proxy
     */
    public void setProxy(Proxy proxy) {
        this.proxy=proxy;
    }

    /**
     * @return
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * @param config
     */
    public void setConnectionConfig(ConnectionConfig config) {
        this.config=config;
    }
    
    /**
     * @return
     */
    public ConnectionConfig getConconfig() {
        return config;
    }
    
    
}
