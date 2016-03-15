/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package labs;

import distributedmodel.DSConfig;
import distributedmodel.DistributedSystem;
import java.util.function.Consumer;
import software.SoftwareDS;
import org.jfree.data.xy.XYSeries;


/** This is a base class for all lab classes that deal only with testing
 * algorithms for a distributed system (non-shared memory).
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public abstract class BaseLabDS extends BaseLab
{
    public static long DELAY_CONNECT_MILLIS = 100;
    public static long DELAY_TRANSMIT_MILLIS = 10;
    
    
    /** Measures time (in seconds) needed to run a given software on a given
     * distributed system.
     * 
     * @param ds
     * @param soft
     * @return - The time (in seconds) of executing soft on ds
     */
    protected double singleMeasurement(DistributedSystem ds,
            SoftwareDS soft)
    {
        ds.loadProgramToNodes(soft);    
        long startTime = System.currentTimeMillis();
        ds.runSystem();
        long stopTime = System.currentTimeMillis();
        double elapsedTime = (double)(stopTime - startTime) / 1000;       
        return elapsedTime;
    }
    
    
    
    
    protected double singleTestRun(int nNodes, SoftwareDS soft, 
            boolean validate, boolean printProcessingTime, boolean printStatus)
    {
        DSConfig config = new DSConfig(nNodes, DELAY_CONNECT_MILLIS,
                                DELAY_TRANSMIT_MILLIS);
        DistributedSystem ds = new DistributedSystem(config);
        initNodesWithData(ds);
        
        if(printStatus){
            printDSStateBefore(ds);
        }
        
        double exTime = singleMeasurement(ds, soft);
        
        if(printProcessingTime){
            System.out.printf("Processing took: %.2f sec.%n", exTime);
        }
        
        if(printStatus){
            printDSStateAfter(ds);
        }
        
        if(validate){
            validateDSState(ds);
        }
        
        return exTime;
    }
    
    protected double singleTestRun(int nNodes, SoftwareDS soft)
    {
        return singleTestRun(nNodes, soft, true, true, true);
    }
    
    
    
    
    
    protected double singleTestRun(DSConfig conf,
        SoftwareDS soft,
        Consumer<DistributedSystem> nodeInitCode,
        Consumer<DistributedSystem> printDSBefore,
        Consumer<DistributedSystem> printDSAfter,
        Consumer<DistributedSystem> validationCode,
        boolean printProcessingTime)
    {
        DistributedSystem ds = new DistributedSystem(conf);
        nodeInitCode.accept(ds);
        
        printDSBefore.accept(ds);
        
        double exTime = singleMeasurement(ds, soft);
        
        if(printProcessingTime){
            System.out.printf("Processing took: %.2f sec.%n", exTime);
        }
        
        printDSAfter.accept(ds);
        
        validationCode.accept(ds);
        
        return exTime;
    }
    
    
    
    /** Measures the performance of a given Software soft on the series of
     * distributed system configurations differing in the number of nodes in
     * a system.
     * 
     * @param expName - the name of the series (will be visible in 
     * a legend of a chart to which the resulting series will be added.
     * @param soft - the software to test
     * @param validate
     * @param printProcessingTime
     * @param printStatus
     * @return - the XYSeries that is ready to be added to a chart
     */
    protected XYSeries seriesOfRuns(String expName, 
            SoftwareDS soft, boolean validate, boolean printProcessingTime,
            boolean printStatus)
    {
        int[] nNodes = new int[] {2, 4, 8, 16};
        double[] exTimes = new double[nNodes.length];
             
        for(int i = 0; i < nNodes.length; ++i){
            exTimes[i] = singleTestRun(nNodes[i], soft, validate,
                    printProcessingTime, printStatus);
        }
        
        XYSeries series = new XYSeries(expName);
        for(int i = 0; i< nNodes.length; ++i){
            series.add(nNodes[i], exTimes[i]);
        }
        return series;
    }
    
    
    /** Measures the performance of a given Software soft on the series of
     * distributed system configurations differing in the number of nodes in
     * a system.
     * 
     * @param expName - the name of the series (will be visible in 
     * a legend of a chart to which the resulting series will be added.
     * @param soft - the software to test
     * @param printStatus
     * @return - the XYSeries that is ready to be added to a chart
     */
    protected XYSeries seriesOfRuns(String expName, 
            SoftwareDS soft, boolean printStatus)
    {
        return seriesOfRuns(expName, soft, false, false, printStatus);
    }
    
    
    
    
    
    protected void initNodesWithData(DistributedSystem ds){
        System.out.println("No initialization code supplemented.");
    }; 
    
    
    protected void printDSStateBefore(DistributedSystem ds){
        System.out.printf("BEFORE: %s%n", ds.toString());
    }
    
    protected void printDSStateAfter(DistributedSystem ds){
        System.out.printf("AFTER: %s%n", ds.toString());
    }
    
    protected void validateDSState(DistributedSystem ds) {
        System.out.println("No validation code supplemented.");
    }
    
    protected void successfulValidationInfo(){
        System.out.println("Results are CORRECT.");
    }
    
    protected void failedValidationInfo(){
        System.out.println("Results are WRONG.");
    }
    
}
