package instinct.livingSigns;

import instinct.utils.Codes;
import instinct.Instinct;
import instinct.utils.LivingThing;

/**
 *
 * @author Pierre-Alain Curty <pierre-alain.curty@heig-vd.ch>
 */
public class Breath implements LivingThing {
    private Boolean live = false;
    private Thread thread;
    
    private final Instinct instinct;
    
    public Breath(Instinct instinct) {
        this.instinct = instinct;
    }

    @Override
    public void start() {
        if(this.live) System.out.println("Breath already running in Thread: " + this.thread.toString());
        else {
            this.live = true;
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    @Override
    public void stop() {
        if(this.live) this.live = false;
        else System.out.println("Breath is not running.");
    }

    @Override
    public void run() {
        Integer cursor = 0;
        while(this.live) {
            try {
                switch(cursor) {
                    case 0:
                        this.instinct.write(Codes.getInstance().UP);
                        Thread.sleep(1000);
                        break;
                    case 1:
                        this.instinct.write(Codes.getInstance().DOWN);
                        Thread.sleep(2000);
                        break;
                }
            }
            catch(InterruptedException ie) {
                System.out.println("Breath: Thread Pause Error.");
            }
            
            cursor = (cursor==1)?0:cursor+1;
        }
    }
}
