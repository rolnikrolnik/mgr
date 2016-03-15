/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package software;

import algorithms.distributed.BasicCommunication;
import distributedmodel.Node;

/**
 *
 * @author Karol
 */
public class BroadcastNaive implements SoftwareDS{

    @Override
    public void instructions(Node node) {
        BasicCommunication.broadcastNaive(node);
    }
    
}
