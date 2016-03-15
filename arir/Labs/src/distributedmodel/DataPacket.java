/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package distributedmodel;

import java.util.Arrays;

/** Immutable class for transferring data
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
final public class DataPacket {
    private final double[] mData;
    private final int mSourceId;
    private final int mDestinationId;

    /** Clones the passed data
     * 
     * @param data 
     * @param srcId 
     * @param destId 
     */
    public DataPacket(double[] data, int srcId, int destId){
        mData = data.clone();
        mSourceId = srcId;
        mDestinationId = destId;
    }

    public double[] getData(){
        return mData;
    }
    
    public int getSourceId(){
        return mSourceId;
    }
    
    public int getDestinationId(){
        return mDestinationId;
    }
    
    @Override
    public String toString(){
        return String.format("[Src=%d, dest=%d] %s", 
                mSourceId, mDestinationId, Arrays.toString(mData));
    }
}
