/*
 */

package org.dudunet.jcrawler.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 *
 * @author dudu
 */
public class FileUtils {
    
    public static void deleteDir(File dir){
        File[] filelist=dir.listFiles();
        for(File file:filelist){
            if(file.isFile()){
                file.delete();
            }else{
                deleteDir(file);
            }
        }
        dir.delete();
    }
    
    public static void copy(File origin,File newfile) throws FileNotFoundException, IOException{
        if(!newfile.getParentFile().exists()){
            newfile.getParentFile().mkdirs();
        }
        FileInputStream fis=new FileInputStream(origin);
        FileOutputStream fos=new FileOutputStream(newfile);
        byte[] buf=new byte[2048];
        int read;
        while((read=fis.read(buf))!=-1){
            fos.write(buf,0,read);
        }
        fis.close();
        fos.close();
    }
    
    public static void writeFile(String filename,byte[] content) throws FileNotFoundException, IOException{
        FileOutputStream fos=new FileOutputStream(filename);
        fos.write(content);
        fos.close();
    }
    
    public static void writeFileWithParent(String filename,byte[] content) throws FileNotFoundException, IOException{
        File file=new File(filename);
        File parent=file.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(content);
        fos.close();
    }
    
    public static void writeFileWithParent(File file,byte[] content) throws FileNotFoundException, IOException{
       
        File parent=file.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(content);
        fos.close();
    }
    
    public static byte[] readFile(File file) throws IOException{
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[2048];
        int read;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((read = fis.read(buf)) != -1) {
            bos.write(buf, 0, read);
        }

        fis.close();
        return bos.toByteArray();
    }
    
}
