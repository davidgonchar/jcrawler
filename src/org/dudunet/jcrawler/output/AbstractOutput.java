package org.dudunet.jcrawler.output;

import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.util.FileUtils;
import org.dudunet.jcrawler.util.LogUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by david on 06/11/14.
 */
public abstract class AbstractOutput {

    public void output(Page page) {
        URL url = null;
        try {
            url = new URL(page.getUrl());
            String query = "";
            if (url.getQuery() != null) {
                query = "_" + url.getQuery();
            }
            String path = url.getPath();
            if (path.length() == 0) {
                path = "index.html";
            } else {
                if (path.charAt(path.length() - 1) == '/') {
                    path = path + "index.html";
                } else {

                    for (int i = path.length() - 1; i >= 0; i--) {
                        if (path.charAt(i) == '/') {
                            if (!path.substring(i + 1).contains(".")) {
                                path = path + ".html";
                            }
                        }
                    }
                }
            }
            path += query;

            doOutput(page, url, path);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (OutputException e) {
            e.printStackTrace();
        }
    }

    protected abstract void doOutput(Page page, URL url, String path) throws OutputException;

}