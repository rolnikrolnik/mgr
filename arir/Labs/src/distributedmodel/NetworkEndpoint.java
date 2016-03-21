/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */

package distributedmodel;

/** A class modeling a node in a distributed memory system
 * for basic communication algorithms
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
class NetworkEndpoint {
    private final int mId;
    private final SynchronousNetworkChannel[] mChannels;
    
    public NetworkEndpoint(int id, SynchronousNetworkChannel[] channels)
    {
        mId = id;
        mChannels = channels;
    }
    
    int getId(){
        return mId;
    }
    
    int getNumberOfChannels(){
        return mChannels.length;
    }
    
    DataPacket receive() throws InterruptedException{
        return mChannels[mId].receive();
    }
    
    void send(int destinationId, double[] data) throws InterruptedException{
        DataPacket dp = new DataPacket(data, mId, destinationId);
        send(dp);
    }
    
    void send(DataPacket dp) throws InterruptedException{
        if(dp.getDestinationId() == mId){
            throw(new RuntimeException(String.format(
                    "Node %d tries to send data to itself.", mId)));
        }
        else{
            if(dp.getDestinationId() >= getNumberOfChannels()){
                throw(new RuntimeException(String.format(
                        "Node %d tries to send data to nonexisting node %d.",
                            mId, dp.getDestinationId())));
            }else
            {
                mChannels[dp.getDestinationId()].send(dp);
            }
        }
    }
}
