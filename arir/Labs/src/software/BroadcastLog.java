/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package software;

import algorithms.distributed.BasicCommunication;
import distributedmodel.Node;

/**
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class BroadcastLog implements SoftwareDS{

    @Override
    public void instructions(Node node) {
        BasicCommunication.broadcast(node);
    }
    
}
