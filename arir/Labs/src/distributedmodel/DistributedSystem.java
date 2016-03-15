/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package distributedmodel;

import java.util.concurrent.CyclicBarrier;
import software.SoftwareDS;
import java.util.logging.Level;
import java.util.logging.Logger;

/** A class representing a distributed system of n Node final objects connected
 * with a network Network (also final). 
 *
 * @author Karol
 */
public class DistributedSystem {
    
    private final Network mNet;
    private final Node[] mNodes;
    private final DSConfig mConfig;
    private final CyclicBarrier mBarrier;
    
    /** Sets up a distributed system without setting data of its nodes. This can
     * be done later by inserting data to chosen nodes individually using
     * getNode(i).setMydata(data)
     * 
     * @param config - configuration of the system
     */
    public DistributedSystem(DSConfig config)
    {
        mConfig = config;
        mNet = new Network(config.getNumberOfNodes(),
                            config.getConnectionDelay(), 
                            config.getUnitTransmissionDelay());
        
        mBarrier = new CyclicBarrier(config.getNumberOfNodes());
        
        //the nodes do not have initial data
        mNodes = new Node[mNet.getNetworkSize()];
        for(int i = 0; i<mNet.getNetworkSize(); ++i){
            mNodes[i] = new Node(mNet.getEndpoint(i), mBarrier);
        }
        
        
    }
    
    /** Sets up a distributed system with setting initial data of the nodes. 
     * 
     * @param config - configuration of the system
     * @param initial_data_states - double[nNodes][dataSize]
     */
    public DistributedSystem(DSConfig config,
            double[][] initial_data_states){
        
        mConfig = config;
        mNet = new Network(config.getNumberOfNodes(),
                            config.getConnectionDelay(), 
                            config.getUnitTransmissionDelay());
        
        mBarrier = new CyclicBarrier(config.getNumberOfNodes());
        mNodes = new Node[mNet.getNetworkSize()];
        for(int i = 0; i<mNet.getNetworkSize(); ++i){
            mNodes[i] = new Node(mNet.getEndpoint(i), mBarrier, initial_data_states[i]);
        }
        
        
    }
    
    public void loadProgramToNodes(SoftwareDS s){
        for(int i = 0; i<mNet.getNetworkSize(); ++i){
            mNodes[i].loadSoftware(s);
        }
    }
    
    public Node getNode(int i){
        return mNodes[i];
    }
    
    public Node[] getNodes(){
        return mNodes;
    }
    
    public DSConfig getConfiguration(){
        return mConfig;
    }
    
    public void runSystem() {
        
        //start all the nodes
        for(int i = 0; i<mNet.getNetworkSize(); ++i){
            mNodes[i].startSoftware();
        }
        
        //return from all the nodes
        for(int i = 0; i<mNet.getNetworkSize(); ++i){
            try {
                mNodes[i].exitSoftware();
            } catch (InterruptedException ex) {
                Logger.getLogger(DistributedSystem.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                "\nData in the system of nodes:\n");
        for(Node node : mNodes){
            sb.append(node.toString());
        }
        return sb.toString();
    }
    
    
    
}
