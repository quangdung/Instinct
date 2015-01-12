package instinct.utils;

/**
 *
 * @author Pierre-Alain Curty <pierre-alain.curty@heig-vd.ch>
 */
public class Codes {
    private static Codes instance = null;
    
    //BASICS
    public byte[] START;
    public byte[] STOP;
    public byte[] ALIVE;
    public byte[] UP;
    public byte[] DOWN;
    public byte[] SLOW;
    public byte[] FAST;
    public byte[] FORWARD;
    public byte[] BACKWARD;
    public byte[] TURN_LEFT;
    public byte[] TURN_RIGHT;
    public byte[] STEP_LEFT;
    public byte[] STEP_RIGHT;
    
    //MODES & ACTIONS
    public byte[] ON;
    public byte[] OFF;
    public byte[] ACTIVE_LEG;
    public byte[] GYRO_MODE;
    public byte[] ANGLE_MODE;
    public byte[] MILITARY_MARCH;
    
    private Codes() {
        //No movements byte[0-3] = 127
        //No actions byte[4-5] = 0
        byte[] tempCode = new byte[6];
        tempCode[0] = tempCode[1] = tempCode[2] = tempCode[3] = (byte)127;
        tempCode[4] = tempCode[5] = 0x0;
        
        //STAY: 01111111|01111111|01111111|01111111|00000000|00000000|
        this.ALIVE = tempCode.clone();
        
        //FORWARD: 01111111|01111111|01111111|00000000|00000000|00000000|
        tempCode[3] = 0x0;
        this.FORWARD = tempCode.clone();
        
        //BACKWARD: 01111111|01111111|01111111|11111110|00000000|00000000|
        tempCode[3] = 0x7F;
        this.BACKWARD = tempCode.clone();
        
        //TURN_LEFT: 00000000|01111111|01111111|01111111|00000000|00000000|
        tempCode[3] = (byte)127;
        tempCode[0] = 0x0;
        this.TURN_LEFT = tempCode.clone();
        
        //TURN_RIGHT: 11111110|01111111|01111111|01111111|00000000|00000000|
        tempCode[0] = 0x7F;
        this.TURN_RIGHT = tempCode.clone();
        
        //STEP_LEFT: 01111111|01111111|00000000|01111111|00000000|00000000|
        tempCode[0] = (byte)127;
        tempCode[2] = 0x0;
        this.STEP_LEFT = tempCode.clone();
        
        //STEP_RIGHT: 01111111|01111111|11111110|01111111|00000000|00000000|
        tempCode[2] = 0x7F;
        this.STEP_RIGHT = tempCode.clone();
        
        
        //////////////////////////////////////////////////////////////////////
        
        
        //START & STOP: 01111111|01111111|01111111|01111111|00000000|00000100|
        tempCode[2]= (byte)127;
        tempCode[5] = 0x4;
        this.START = this.STOP = tempCode.clone();
        
        //UP: 01111111|01111111|01111111|01111111|00010000|00000000|
        tempCode[4] = 0x10;
        tempCode[5] = 0x0;
        this.UP = tempCode.clone();
        
        //DOWN: 01111111|01111111|01111111|01111111|01000000|00000000|
        tempCode[4] = 0x0;
        tempCode[5] = 0x0;
        this.DOWN = tempCode.clone();
        
        //SLOW: 01111111|01111111|01111111|01111111|00100000|00000000|
        tempCode[4] = 0x0;
        tempCode[5] = 0x0;
        this.SLOW = tempCode.clone();
        
        //FAST: 01111111|01111111|01111111|01111111|10000000|00000000|
        tempCode[4] = 0x0;
        tempCode[5] = 0x0;
        this.FAST = tempCode.clone();
        
        
        //////////////////////////////////////////////////////////////////////
        
        
        
        //ON & OFF: 01111111|01111111|01111111|01111111|00000001|00000000|
        tempCode[4] = 0x1;
        tempCode[5] = 0x0;
        this.ON = this.OFF = tempCode.clone();
        
        //ACTIVE_LEG: 01111111|01111111|01111111|01111111|00000010|00000000|
        tempCode[4] = 0x2;
        tempCode[5] = 0x0;
        this.ACTIVE_LEG = tempCode.clone();
        
        //GYRO_MODE: 01111111|01111111|01111111|01111111|00000000|10000000|
        tempCode[4] = 0x0;
        tempCode[5] = (byte)0x80;
        this.STEP_RIGHT = tempCode.clone();
        
        //ANGLE_MODE: 01111111|01111111|01111111|01111111|00000000|00100000|
        tempCode[4] = 0x0;
        tempCode[5] = 0x20;
        this.STEP_RIGHT = tempCode.clone();
        
        //MILITARY_MARCH: 01111111|01111111|01111111|01111111|00000000|01000000|
        tempCode[4] = 0x0;
        tempCode[5] = 0x40;
        this.STEP_RIGHT = tempCode.clone();
        
    }
    
    public static Codes getInstance() {
        if(Codes.instance == null)
            Codes.instance = new Codes();
        
        return Codes.instance;
    }
}
