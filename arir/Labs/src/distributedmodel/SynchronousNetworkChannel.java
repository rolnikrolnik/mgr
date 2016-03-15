/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package distributedmodel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/** This class represents a simple model of synchronized communication channel 
 * in a network.
 *      Synchronization means that the transmission starts when both
 * nodes has called suitable functions (one called send() and the other called
 * receive()). Before this, one of the nodes (no matter whether sender or
 * receiver) must wait for the second node.
 *      It parameterized by start connection delay (ts) as well as
 * transmission time delay (tw) related to the size of the data being
 * transmitted (l). The overall transmission time is therefore tC = t2 + l*tw
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
class SynchronousNetworkChannel { 
    
    private final BlockingQueue<DataPacket> mQueue;
    private final long mDelayConnect;
    private final long mDelayTransmit;
    private final AtomicBoolean mEnterLock;
    private final AtomicBoolean mExitLock;
    
    /** Constructor of the SynchronousNetworkChannel.
     * 
     * @param delay_connect_milis - start connection delay
     * @param delay_transmit_milis - transmission delay
     */
    public SynchronousNetworkChannel(long delay_connect_milis, long delay_transmit_milis){
        mDelayConnect = delay_connect_milis;
        mDelayTransmit = delay_transmit_milis;
        mQueue = new SynchronousQueue<>();
        mEnterLock = new AtomicBoolean(true);
        mExitLock = new AtomicBoolean(true);
    }
    
    /** This send blocks until other thread (executing the code of the node at
     * the end of this channel) makes an attempt to receive from the channel.
     * 
     * @param dp - DataPacket to be sent
     * @throws InterruptedException 
     */
    public void send(DataPacket dp) throws InterruptedException
    {
        synchronized(mEnterLock){
            if(mEnterLock.get()){
                mEnterLock.wait();
            }
            mEnterLock.set(true);
        }
        
        Thread.sleep(mDelayConnect);
        
        mQueue.put(dp);
        
        Thread.sleep(dp.getData().length * mDelayTransmit);
        
        synchronized(mExitLock){
            mExitLock.set(false);
            mExitLock.notify();
        }
    }
    
    /** This receive blocks until other thread begins to send data.
     * 
     * @return - DataPacket sent by other node
     * @throws InterruptedException 
     */
    public DataPacket receive() throws InterruptedException
    {
        synchronized(mEnterLock){
            mEnterLock.set(false);
            mEnterLock.notify();
        }
        
        DataPacket dp = mQueue.take();
        
        synchronized(mExitLock){
            if (mExitLock.get()){
                mExitLock.wait();
            }
            mExitLock.set(true);
        }
        
        return dp;
    }
    
}
