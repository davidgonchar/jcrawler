/*
 */

package org.dudunet.jcrawler.generator.filter;


import org.dudunet.jcrawler.generator.Generator;

/**
 * @author dudu
 */
public abstract class Filter implements Generator {
    Generator generator;

    /**
     * @param generator
     */
    public Filter(Generator generator){
        this.generator=generator;
    }
}
