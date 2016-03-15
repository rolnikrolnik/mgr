/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package distributedmodel;

/** This class is a simple model of a network connecting computational nodes.
 * It is parameterized by the number of nodes (each of which has its network
 * channel) and two parameters of time delays of the network: start connection
 * delay and transmission delay.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class Network {
    private final SynchronousNetworkChannel[] mChannels;
    private final NetworkEndpoint[] mEndpoints;
    private final int nEndpoints;
    
    public Network(int nEndpoints, long delay_connect_milis, 
            long delay_transmit_milis)
    {
        this.nEndpoints = nEndpoints;
        
        mChannels = new SynchronousNetworkChannel[nEndpoints];
        for(int i = 0; i < mChannels.length; ++i)
            mChannels[i] = new SynchronousNetworkChannel(delay_connect_milis, 
                    delay_transmit_milis);
        
        mEndpoints = new NetworkEndpoint[nEndpoints];
        for(int i = 0; i < mEndpoints.length; ++i)
            mEndpoints[i] = new NetworkEndpoint(i, mChannels);
    }
    
    public NetworkEndpoint getEndpoint(int i){
        return mEndpoints[i];
    }
    
    public int getNetworkSize(){
        return nEndpoints;
    }
    
}
