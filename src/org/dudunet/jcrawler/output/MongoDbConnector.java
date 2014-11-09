package org.dudunet.jcrawler.output;

import java.io.IOException;

/**
 * Created by david on 06/11/14.
 */
public class MongoDbConnector {

    private static volatile MongoDbConnector _instance; // volatile variable

    private Object mongoDb = null;
    private String host;
    private String db;
    private String user;
    private String pass;

    public static MongoDbConnector getInstance() throws IOException {

        if (_instance == null) {
            synchronized (MongoDbConnector.class) {
                if (_instance == null)
                    _instance = new MongoDbConnector();
            }
        }
        return _instance;
    }

    private MongoDbConnector() throws IOException {
        if (mongoDb == null) {
            connect();
        }
    }

    private void connect() throws IOException {
        mongoDb = new Object();
    }

    public Object getMongoDb() {
        return mongoDb;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
