/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package labs;

/** The class containing the main method of the project - this is the startup
 * point. Here you can choose, which lab exercises to start.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class ARIR {
    public static void main(String args[]){
        
        BaseLab broadcastLab = new Lab01B();
        broadcastLab.testAll();
        
    }
}
