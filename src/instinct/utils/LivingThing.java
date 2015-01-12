package instinct.utils;

/**
 *
 * @author Pierre-Alain Curty <pierre-alain.curty@heig-vd.ch>
 */
public interface LivingThing extends Runnable {
    public void start();
    public void stop();
    
    @Override
    public void run();
}
