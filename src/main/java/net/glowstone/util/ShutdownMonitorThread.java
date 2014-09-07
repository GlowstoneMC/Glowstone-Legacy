package net.glowstone.util;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.glowstone.GlowServer;

/**
 * Thread started on shutdown that monitors for and kills rogue non-daemon threads.
 */
public class ShutdownMonitorThread extends Thread {

    /**
     * The delay in milliseconds until leftover threads are killed.
     */
    private static final int DELAY = 8000;

    /**
     * Toggle for if ShutdownMonitor should be sleeping threads.
     */
    private volatile boolean run;
    
    private ExecutorService execute;
    
    public ShutdownMonitorThread() {
        setName("ShutdownMonitorThread");
        setDaemon(true);
        
    }

    @Override
    public void run() {
        this.run = true;
        while(this.run){
            try {
                Thread.sleep((long) DELAY);
            } catch (InterruptedException e) {
                GlowServer.logger.severe("ShutdownMonitor interrupted");
                this.run = false;
                return;
            }
        }
        
        GlowServer.logger.warning("Still running after shutdown, finding rogue threads...");
        
        final Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> entry : traces.entrySet()) {
            final Thread thread = entry.getKey();
            final StackTraceElement[] stack = entry.getValue();

            if (thread.isDaemon() || !thread.isAlive() || stack.length == 0) {
                // won't keep JVM from exiting
                continue;
            }

            GlowServer.logger.log(Level.WARNING, "Rogue thread: {0}", thread);
            for (StackTraceElement trace : stack) {
                GlowServer.logger.log(Level.WARNING, "\tat {0}", trace);
            }
                // really get it out of there
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ShutdownMonitorThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
        }
            //stop the scheduler and tasks
            execute.shutdownNow();
    }

}
