/*
 */

package org.dudunet.jcrawler.output;

import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.util.FileUtils;
import org.dudunet.jcrawler.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * @author dudu
 */
public class FileSystemOutput extends AbstractOutput {

    public String root;

    public FileSystemOutput(String root) {
        this.root = root;
    }

    @Override
    protected void doOutput(Page page, URL url, String path) throws OutputException {
        try {
            File domain_path = new File(root, url.getHost());
            File f = new File(domain_path, path);

            FileUtils.writeFileWithParent(f, page.getContent());

            LogUtils.getLogger().info("output "+f.getAbsolutePath());

        } catch (IOException e) {
            throw new OutputException(e.getMessage());
        }
    }


}
