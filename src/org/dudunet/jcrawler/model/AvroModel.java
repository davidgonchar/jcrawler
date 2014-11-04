/*
 */

package org.dudunet.jcrawler.model;


import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

/**
 *
 * @author dudu
 */
public class AvroModel {


    public static Schema getSchema(Class type){       
        Schema schema=ReflectData.get().getSchema(type);       
        return schema;
    }
}
