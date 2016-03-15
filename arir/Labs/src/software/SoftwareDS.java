/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package software;

import distributedmodel.Node;

/** A functional interface for modeling the software that can be loaded to and
 * run by each of the nodes in a distributed system.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
@FunctionalInterface
public interface SoftwareDS {
    
    /**
     * The time used by a 'computationally heavy operation' per data element 
     * in milliseconds
     */
    public static final long DEFAULT_SIMULATION_UNIT_PROCESSING_TIME = 1;
    
    public void instructions(Node node);
}
