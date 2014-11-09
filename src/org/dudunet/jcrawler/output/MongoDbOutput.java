package org.dudunet.jcrawler.output;

import org.dudunet.jcrawler.model.Page;
import org.dudunet.jcrawler.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by david on 06/11/14.
 */
public class MongoDbOutput extends AbstractOutput {
    private String host;
    private String db;
    private String user;
    private String pass;

    private MongoDbConnector connector;

    public MongoDbOutput(String host, String db, String user, String pass) throws IOException {
        this.host = host;
        this.db = db;
        this.user = user;
        this.pass = pass;

        connector = MongoDbConnector.getInstance();
    }

    @Override
    protected void doOutput(Page page, URL url, String path) {

    }
}
