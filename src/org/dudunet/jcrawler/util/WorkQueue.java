/*
 */

package org.dudunet.jcrawler.util;

import java.util.LinkedList;


/**
 * @author dudu
 */
public class WorkQueue
{
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;
    public WorkQueue(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];
        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }
    
    public boolean isAlive(){
        synchronized(queue){
            if(!queue.isEmpty()){
                return true;
            }
        }
        for(PoolWorker thread:threads){
            if(thread.status==1)
                return true;
        }
        
        
        return false;
    }
    
 
    
    public void killAll(){
        for(int i=0;i<threads.length;i++){
            threads[i].stop();
        }
    }
    
    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);
            queue.notify();
        }
    }
    private class PoolWorker extends Thread {
        int status=0;
        public void run() {
            Runnable r;
            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }
                    r = (Runnable) queue.removeFirst();
                    status=1;
                }
                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                    r.run();
                }
                catch (Exception e) {
                  e.printStackTrace();
                }finally{
                    status=0;
                }
            }
        }
    }
}
