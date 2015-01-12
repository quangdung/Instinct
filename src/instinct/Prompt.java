package instinct;

import instinct.utils.LivingThing;

import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Pierre-Alain Curty <pierre-alain.curty@heig-vd.ch>
 */
public class Prompt implements LivingThing {
    private Boolean live = false;
    private Thread thread;
    
    private final Scanner IOInput;
    private final ArrayList<LivingThing> livingThings;
    private final Instinct instinct;
    
    public Prompt(Instinct instinct) {
        this.instinct = instinct;
        
        this.IOInput = new Scanner(System.in);
        this.livingThings = new ArrayList<>();
    }
    
    public void addLivingThing(LivingThing lt) {
        this.livingThings.add(lt);
    }
    
    public void startLivingThings() {
        for(LivingThing lt : this.livingThings)
            lt.start();
    }
    
    @Override
    public void start() {
        if(this.live) System.out.println("Prompt already running in Thread: " + this.thread.toString());
        else {
            this.live = true;
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    @Override
    public void stop() {
        try {
            this.live = false;
            this.IOInput.close();
            this.instinct.stop();
        }
        catch (Exception e) {
			System.err.println(e.toString());
		}
    }
    
    @Override
    public void run() {     
        while(this.live) {
            String in;
            Byte b;
        
            byte[] toSend = new byte[6];
            toSend[0] = toSend[1] = toSend[2] = toSend[3] = (byte)127;
                        
            System.out.print("Command / bytes / exit ? [c/b/q] : ");
            
            in = this.IOInput.nextLine();
            
            if(in.isEmpty()) {
                System.out.println("Incorrect value");
                continue;
            }
            
            if(in.equals("q")) break;
            else if(in.equals("b")) {
                System.out.print("5th byte (hex): ");
                b = this.IOInput.nextByte();
                toSend[4] = b;
                
                System.out.println("");
                
                System.out.print("6th byte (hex): ");
                b = this.IOInput.nextByte();
                toSend[4] = b;
            }
            else if(in.equals("c")) {
                System.out.print("Command: ");
                in = this.IOInput.nextLine();
                
                if(in.isEmpty()) {
                    System.out.println("Incorrect value");
                    continue;
                }
                
                switch(in) {
                    case "start":
                    case "stop":
                        toSend[4] = (byte)0;
                        toSend[5] = (byte)4;
                        break;
                    case "freeze":
                        toSend[4] = (byte)1;
                        toSend[5] = (byte)0;
                        break;
                }
            }
            
           this.instinct.write(toSend);
        }
        
        this.stop();
        
        for(LivingThing lt : this.livingThings)
            lt.stop();
    }
}