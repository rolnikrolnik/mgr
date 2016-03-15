/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package algorithms.distributed;

import algorithms.Utils;
import distributedmodel.Node;
import javax.sql.rowset.serial.SerialArray;

/**
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class BasicCommunication {
    
     /** A subroutine that, if run on each of the nodes in the distributed 
     * system, broadcasts the data stored in node[0] to all the other nodes in
     * \Theta(log(nNodes)) time.
     * 
     * @param node - the node that takes part in broadcast operation
     * @param verbose -whether all sends and receives should be in verbose mode
     */
    public static void broadcast(Node node, boolean verbose){
        int nNodes = node.getNumberOfAllNodes();
        int myIdx = node.getMyId();
        
        int loopOffset;
        if (node.getMyData() != null){
            loopOffset = 0;
        }
        else{
            loopOffset = 1;
            node.receiveAndSet(verbose);
        }
        
        if(myIdx < nNodes / 2){
            for (int i = Utils.binlog(myIdx) + loopOffset; i < Utils.binlog(nNodes); i++) {
                node.send(myIdx + (1  << i), node.getMyData());
            }
        }
    }
    
    
    /** Verbose version of @see BasicCommunication#broadcast
     * 
     * @param node - the node that takes part in broadcast operation
     */
    public static void broadcast(Node node){
        broadcast(node, true);
    }
    
    
    /** \Theta(nNodes) version of @see BasicCommunication#broadcast
     * 
     * @param node 
     * @param verbose 
     */
    public static void broadcastNaive(Node node, boolean verbose){
        int nNodes = node.getNumberOfAllNodes();
        int myIdx = node.getMyId();

        if (node.getMyData() != null){
            node.sendMyData(myIdx+1, verbose);
        }
        else{
            node.receiveAndSet(verbose);
            if(myIdx != nNodes -1){
                node.sendMyData(myIdx+1, verbose);
            }
        }
    }
    
    /** Verbose version of @see BasicCommunication#broadcastNaive
     * 
     * @param node 
     */
    public static void broadcastNaive(Node node){
        broadcastNaive(node, true);
    }
    
}
