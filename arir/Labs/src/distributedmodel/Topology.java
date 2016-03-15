/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package distributedmodel;

/**
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class Topology {
    
    public static int meshCoordsToId(int row, int col, int nColumns){
        return row*nColumns+col;
    }
    
    public static int meshRowOfId(int nodeId, int nColumns){
        return nodeId/nColumns;
    }
    
    public static int meshColOfId(int nodeId, int nColumns){
        return nodeId - meshRowOfId(nodeId, nColumns)*nColumns;
    }
    
}
