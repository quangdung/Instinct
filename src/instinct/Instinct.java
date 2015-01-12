package instinct;

import instinct.utils.Codes;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import instinct.livingSigns.Breath;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 * @author Pierre-Alain Curty <pierre-alain.curty@heig-vd.ch>
 * @author Nicolas Butticaz-Leal <nicolas.butticaz@heig-vd.ch>
 */
public class Instinct implements SerialPortEventListener {
    private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 57600;
    
    private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/tty.usbserial-A4WTR3JS", // Mac OS X Maverick
            "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
    
    private final Boolean debug = true;
    private final Boolean serialDebug = true;
    
	private OutputStream HPOutput;
    private BufferedReader HPInput;
    private SerialPort serialPort;
    private CommPortIdentifier portId = null;
    
    private Boolean robotFound = false;
    
    private final LinkedList<byte[]> actions;
    
    //Living Things
    private final Prompt prompt;
    private final Breath breath;
    
    Instinct() {
        // Testing & loading components
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
        
        if (portId == null) {
            System.out.println("Could not find COM port.");
			System.exit(1);
        }
        else {
			try {
                // Open serial port, and use class name for the appName.
                this.serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

                // Set port parameters
                this.serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, 
                        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                // Open the stream
                this.HPInput = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                this.HPOutput = this.serialPort.getOutputStream();

                // add event listeners
                this.serialPort.addEventListener(this);
                this.serialPort.notifyOnDataAvailable(true);
            }
            catch (Exception e) {
                System.out.println("Error when using COM port.");
                System.exit(1);
            }
        }
        
        //Initializations
        this.actions = new LinkedList<>();

        //Manual controller
        this.prompt = new Prompt(this);
        //this.prompt.start();

        //Initialize HexaPod Living signs
        this.breath = new Breath(this);

        //Give access to living parts
        this.prompt.addLivingThing(this.breath);
    }
    
    public synchronized void write(byte[] code) {
        if(code.length == 6)
            this.actions.add(code);
    }
    
    public void stop() {
        try {
            this.HPInput.close();
            this.HPOutput.close();
            this.serialPort.removeEventListener();
            this.serialPort.close();
        }
        catch (Exception e) {
			System.err.println(e.toString());
		}
    }

    public void start() {
        //Start HexaPod
        this.write(Codes.getInstance().START);

        //Give live to living parts !
//        this.prompt.startLivingThings();
    }
    
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = HPInput.readLine();
                
				if(serialDebug)
                    System.out.println(inputLine);
                
//				byte[] temp = inputLine.getBytes("US-ASCII");
//				System.out.println("Receiving..." + temp.length + ":" + Arrays.toString(temp));
				
				if (inputLine.equals("*")) {
                    byte[] code = new byte[6];
                    
                    if(this.actions.isEmpty())
                        code = Codes.getInstance().ALIVE;
                    else
                        code = this.actions.removeFirst();
                    
                    if(serialDebug) {
                        System.out.print("Code send: |");
                        for(Byte b : code) {
                            System.out.print(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
                            System.out.print("|");
                        }
                        System.out.println("");
                    }
                    
                    try {
                        this.HPOutput.write(code);
                    }
                    catch (Exception e) {
                        System.err.println(e.toString());
                    }
                }
            }
            catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    public Boolean getDebug() {
        return debug;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Instinct life = new Instinct();
        
//        life.start();
        while(true){}
    }
}
