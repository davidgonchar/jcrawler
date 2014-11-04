/*
 */

package org.dudunet.jcrawler.model;

import org.apache.avro.reflect.Nullable;

/**
 *
 * @author dudu
 */
public class Content {
    @Nullable
    private String url=null;
    private byte[] content=null;
    @Nullable
    private String contentType=null;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
