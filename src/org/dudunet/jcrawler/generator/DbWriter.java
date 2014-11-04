/*
 */

package org.dudunet.jcrawler.generator;

import java.io.File;
import java.io.IOException;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.dudunet.jcrawler.model.AvroModel;

/**
 * @author dudu
 * @param <T>
 */
public class DbWriter<T> {
    
    
    
    private DataFileWriter<T> dataFileWriter;
 
    private Class<T> type;

    /**
     * @param type
     * @param dbfile avro
     * @param append
     * @throws java.io.IOException
     */
    public DbWriter(Class<T> type,File dbfile,boolean append) throws IOException{
        this.type=type;
        DatumWriter<T> datumWriter = new ReflectDatumWriter<T>(type);
        dataFileWriter = new DataFileWriter<T>(datumWriter);
        if(!append){
            if(!dbfile.getParentFile().exists()){
                dbfile.getParentFile().mkdirs();
            }
            dataFileWriter.create(AvroModel.getSchema(type), dbfile);
            
        }else{
            dataFileWriter.appendTo(dbfile);
        }
    }
    
    /**
     * @param type
     * @param dbpath avro
     * @param append
     * @throws java.io.IOException
     */
    public DbWriter(Class<T> type,String dbpath,boolean append) throws IOException{
        this(type,new File(dbpath),append);
    }
    
    /**
     *
     * @param type
     * @param dbpath avro
     * @throws java.io.IOException
     */
    public DbWriter(Class<T> type,String dbpath) throws IOException{
        this(type,dbpath,false);
    }
    
    /**
     * @param type
     * @param dbfile avro
     * @throws java.io.IOException
     */
    public DbWriter(Class<T> type,File dbfile) throws IOException{
        this(type,dbfile,false);
    }
    
    /**
     * @throws java.io.IOException
     */
    public void flush() throws IOException{
        dataFileWriter.flush();
    }
    
    /**
     * @param data
     * @throws java.io.IOException
     */
    public void write(T data) throws IOException{
        dataFileWriter.append(data);
    }
    
    /**
     * @throws java.io.IOException
     */
    public void close() throws IOException{
        dataFileWriter.close();
    }
    
}
