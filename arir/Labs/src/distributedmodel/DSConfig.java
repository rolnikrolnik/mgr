/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package distributedmodel;

/** Immutable class that specifies a configuration of a distributed system to be
 * created.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class DSConfig {
    public static final long DEFAULT_CONNECT_DELAY = 20;
    public static final long DEFAULT_UNIT_TRANSMISSION_DELAY = 1;
    
    private final int mNNodes; 
    private final long mDelay_connect_milis;
    private final long mDelay_unit_transmission_milis;
    
    public DSConfig(int nNodes, long delay_connect_milis,
            long delay_unit_transmission_milis){
        mNNodes = nNodes;
        mDelay_connect_milis = delay_connect_milis;
        mDelay_unit_transmission_milis = delay_unit_transmission_milis;
    }
    
    public DSConfig(int nNodes){
        this(nNodes, DEFAULT_CONNECT_DELAY,
                DEFAULT_UNIT_TRANSMISSION_DELAY);
    }
    
    public int getNumberOfNodes(){
        return mNNodes;
    }
    
    public long getConnectionDelay(){
        return mDelay_connect_milis;
    }
    
    public long getUnitTransmissionDelay(){
        return mDelay_unit_transmission_milis;
    }
}
