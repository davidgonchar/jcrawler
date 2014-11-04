/*
 */

package org.dudunet.jcrawler.net;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author dudu
 */
public interface Response {
   
  public URL getUrl();

 
  /**
   * @return
   */
  public int getCode();


  /**
   * @param name
   * @return
   */
  public List<String> getHeader(String name);
 
  /**
   * @return
   */
  public Map<String,List<String>> getHeaders();
  
  /**
   * @param headers
   */
  public void setHeaders(Map<String, List<String>> headers);
  
  /**
   * @return
   */
  public String getContentType();
  
  
  /**
   * @return
   */
  public byte[] getContent();
  
}
