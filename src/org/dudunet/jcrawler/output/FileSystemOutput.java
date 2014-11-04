/*
 */

package org.dudunet.jcrawler.output;

import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.util.FileUtils;
import org.dudunet.jcrawler.util.LogUtils;

import java.io.File;
import java.net.URL;


/**
 * @author dudu
 */
public class FileSystemOutput {

    public String root;

    public FileSystemOutput(String root) {
        this.root = root;
    }

    public void output(Page page) {
        try {
            URL _URL = new URL(page.getUrl());
            String query = "";
            if (_URL.getQuery() != null) {
                query = "_" + _URL.getQuery();
            }
            String path = _URL.getPath();
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
            File domain_path = new File(root, _URL.getHost());
            File f = new File(domain_path, path);           
            FileUtils.writeFileWithParent(f, page.getContent());
            LogUtils.getLogger().info("output "+f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   

}
