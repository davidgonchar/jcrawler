/*
 */

package org.dudunet.jcrawler.util;

import java.net.HttpURLConnection;

/**
 *
 * @author dudu
 */
public class CommonConnectionConfig implements ConnectionConfig{

    public CommonConnectionConfig(String userAgent,String cookie) {
        this.userAgent=userAgent;
        this.cookie=cookie;
    }

    @Override
    public void config(HttpURLConnection con) {
        if (userAgent != null) {
            con.setRequestProperty("User-Agent", userAgent);
        }
        if (cookie != null) {
            con.setRequestProperty("Cookie", cookie);
        }
    }

    private String userAgent=null;
    private String cookie=null;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
