/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package software;

/** A functional interface for modeling the software that can be run on a shared
 * memory system.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
@FunctionalInterface
public interface SoftwareSM {
    
    /**
     * The time used by a 'computationally heavy operation' per data element 
     * in milliseconds
     */
    public static final long DEFAULT_SIMULATION_UNIT_PROCESSING_TIME = 5;
    
    public void instructions(double[] sharedData);
}
