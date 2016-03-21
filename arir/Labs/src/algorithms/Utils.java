/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package algorithms;

/** A class of general static routines which may be helpful in the development
 * of parallel algorithms.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class Utils {
    
    /** log base 2 for integers. Taken from:
     * http://stackoverflow.com/questions/3305059/how-do-you-calculate-log-base-2-in-java-for-integers
     *
     * @param bits - the log2 argument
     * @return - log2(bits)
     */
    public static int binlog( int bits ) // returns 0 for bits=0
    {
        int log = 0;
        if( ( bits & 0xffff0000 ) != 0 ) { bits >>>= 16; log = 16; }
        if( bits >= 256 ) { bits >>>= 8; log += 8; }
        if( bits >= 16  ) { bits >>>= 4; log += 4; }
        if( bits >= 4   ) { bits >>>= 2; log += 2; }
        return log + ( bits >>> 1 );
    }
    
    
    /** Checks whether a and b are close to each other. "close" defined by eps.
     * Taken from:
     * http://stackoverflow.com/questions/9090500/how-to-compare-that-sequence-of-doubles-are-all-approximately-equal-in-java
     * 
     * @param a
     * @param b
     * @param eps
     * @return 
     */
    public static boolean almostEqual(double a, double b, double eps){
        return Math.abs(a-b)<eps;
    }
    
    
    /** Returns flat version of d2 array, e.g. [[1,2,3], [3,4]] -> [1,2,3,3,4]
     * 
     * @param array
     * @return 
     */
    public static double[] flatten2dArray(double[][] array) {
        int size = 0;
        for(double[] e : array){ size += e.length; }
        double[] result = new double[size];
        int pos = 0;
        for(double[] e : array){
            System.arraycopy(e, 0, result, pos, e.length);
            pos += e.length;
        }
        return result;
    }
    
    
    /** Merges two sorted arrays into one sorted array in 0(n) time.
     * 
     * @param a1 - a sorted array
     * @param a2 - a sorted array
     * @return - merged sorted array
     */
    public static double[] merge(double[] a1, double[] a2){
        if(!isSorted(a1) || !isSorted(a2)){
            throw new RuntimeException("One of the arrays is not sorted.");
        }
        double[] result = new double[a1.length+a2.length];
        int i1 = 0;
        int i2 = 0;
        for(int j = 0; j<result.length; ++j){
            if(i1<a1.length && (i2==a2.length || a1[i1]<=a2[i2])){
                result[j] = a1[i1++];
            }else{
                result[j] = a2[i2++];
            }
        }
        return result;
    }
    
    public static boolean isSorted(double[] a) {
        for (int i = 0; i < a.length - 1; i++) { 
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
}
