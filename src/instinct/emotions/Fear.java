package instinct.emotions;

import com.phidgets.*;
import com.phidgets.event.*;

import instinct.Instinct;
import instinct.utils.Codes;
import instinct.utils.LivingThing;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Pierre-Alain Curty <pierre-alain.curty@heig-vd.ch>
 */
public class Fear implements LivingThing {
    private Boolean live = false;
    private Thread thread;
    
    private final Instinct instinct;
    private InterfaceKitPhidget device = null;
    private Boolean feared = false;
    private Timer timer = null;

    public Fear(Instinct instinct) {
        this.instinct = instinct;
        this.timer = new Timer();
        
        try {
            this.device = new InterfaceKitPhidget();

            //New device
            device.addAttachListener((AttachEvent event) -> {
                try {
                    //Config device
                    device.setDataRate(0, 992);
                    device.setSensorChangeTrigger(0, 10);
                    
                    if(this.instinct.getDebug()) {
                        System.out.println("A new device has been plugged in");
                        System.out.println("InputCount : " + device.getInputCount());
                        System.out.println("OuputCount : " + device.getOutputCount());
                        System.out.println("SensorCount : " + device.getSensorCount());
                        
                        System.out.println("DataRate : " + device.getDataRate(0));
                        System.out.println("DataRateMin : " + device.getDataRateMin(0));
                        System.out.println("DataRateMax : " + device.getDataRateMax(0));
                        System.out.println("InputState : " + device.getInputState(0));
                        System.out.println("Ratiometric : " + device.getRatiometric());
                        
                        System.out.println("SensorChangeTrigger : " + device.getSensorChangeTrigger(0));
                    }
                }
                catch (PhidgetException ex) {
                    System.out.println("Fear: Error when setting device.");
                }
            });

            //Value change
            this.device.addSensorChangeListener(new SensorChangeListener() {
                int count = 0;

                @Override
                public void sensorChanged(SensorChangeEvent sensorEvent) {
                    
                    if(Fear.this.instinct.getDebug())
                        System.out.println("Fear: new sensor's value: " + sensorEvent.getValue());
                    
                    try {
                        int sensorValue = Fear.this.device.getSensorValue(0);

                        if (sensorValue > 510) {
                            //If HexaPod is not feared
                            if(!Fear.this.feared) {
                                Fear.this.instinct.write(Codes.getInstance().OFF);
                                
                                //Then wait 20 sec. defore get up.
                                Fear.this.timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Fear.this.feared = false;
                                        Fear.this.instinct.write(Codes.getInstance().ON);
                                    }
                                }, 20*1000);
                            }
                        }
                    }
                    catch (PhidgetException ex) {
                        System.out.println("Fear: Error when reading device.");
                    }

                }
            });

            //Detach device
            this.device.addDetachListener((DetachEvent event) -> {
                if(this.instinct.getDebug())
                    System.out.println("Fear: Device has been plugged out.");
            });
        }
        catch (PhidgetException e) {
            System.out.println("Fear: Can't use Phidget device.");
        }
    }

    @Override
    public void start() {
        if(this.live) System.out.println("Fear already running in Thread: " + this.thread.toString());
        else {
            this.live = true;
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    @Override
    public void stop() {
        if(this.live) {
            try {
                this.live = false;
                
                this.device.close();
                if(this.instinct.getDebug())
                    System.out.println("Fear: Sensor closed.");
                
            } catch (PhidgetException ex) {
                System.out.println("Fear: Error while closing sensor.");
            }
        }
        else System.out.println("Fear is not running.");
    }

    @Override
    public void run() {
        while(this.live) {
            try {
                device.openAny();
                System.in.read();
                
                if(this.instinct.getDebug())
                    System.out.println("Fear: Sensor device started.");
                
            } catch (PhidgetException | IOException ex) {
                System.out.println("Fear: Error while reading sensor.");
            }
        }
    }
}
