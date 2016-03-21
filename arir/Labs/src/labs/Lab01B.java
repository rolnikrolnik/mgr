/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package labs;

import distributedmodel.DistributedSystem;
import distributedmodel.Node;
import algorithms.distributed.BasicCommunication;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/** Lab01 class with broadcast exercise.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public class Lab01B extends BaseLabDS{
    
    private final static int DATASIZE = 16;
    private double[] initialNode0Data;
    
    
    @Override
    public void testAll() {
        
        boolean verboseCommunication = true;
        boolean validateResults = true;
        boolean printProcessingTime = false;
        boolean printStatusBeforeAndAfter = false;
        
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        
        XYSeries exp1 = seriesOfRuns("Naive version",
           (Node node)-> {BasicCommunication.broadcastNaive(node, verboseCommunication);},
            validateResults, printProcessingTime, printStatusBeforeAndAfter);
        seriesCollection.addSeries(exp1);
        
        XYSeries exp2 = seriesOfRuns("Log version",
            (Node node)-> {BasicCommunication.broadcast(node, verboseCommunication);},
            validateResults, printProcessingTime, printStatusBeforeAndAfter);
        seriesCollection.addSeries(exp2);
        
        JFreeChart chart = makeChart(seriesCollection, "Broadcast performance",
                "Number of nodes", "Execution time [s]");      
        
        displayChart(chart);
    }

    @Override
    protected void initNodesWithData(DistributedSystem ds) {
        initialNode0Data = generateRandomData(DATASIZE);
        ds.getNode(0).setMyData(initialNode0Data);
    }
    
    
    @Override
    protected void validateDSState(DistributedSystem ds)
    {
        for (Node n : ds.getNodes()){
            
            double[] nodeData = n.getMyData();
            
            if (nodeData == null || nodeData.length != DATASIZE){
                failedValidationInfo();
                return;
            }
            
            for (int i = 0; i < n.getMyData().length; ++i){
                if (nodeData[i] != initialNode0Data[i]){
                    failedValidationInfo();
                    return;
                }
            }
        }
        
        successfulValidationInfo();
    }
    
}
