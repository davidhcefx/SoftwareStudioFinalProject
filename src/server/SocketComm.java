package server;

/**
 * Created by davidhcefx on 2017/6/15.
 *
 * the language for Socket Communication
 */
public enum SocketComm {
    error(-1), keyPressed(0), keyReleased(1), seedReq(2), seedReply(3), ready(4), start(5);

    private int val;
    private SocketComm(int v){ val = v; }
    public int getVal(){ return val; }
    public static SocketComm toSocketComm(int i){
        switch (i){
            case 0: return keyPressed;
            case 1: return keyReleased;
            case 2: return seedReq;
            case 3: return seedReply;
            case 4: return ready;
            case 5: return start;
            default: return error;
        }
    }
}

