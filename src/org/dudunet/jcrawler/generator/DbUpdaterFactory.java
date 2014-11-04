/*
 */

package org.dudunet.jcrawler.generator;

/**
 *
 * @author dudu
 */
public interface DbUpdaterFactory {
    public DbUpdater createDbUpdater() throws Exception;
    
}
