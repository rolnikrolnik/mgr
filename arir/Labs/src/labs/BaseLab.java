/*
 *  This code is for Parallel and Distributed Algorithms
 *  laboratory at Gdansk University of Technology
 */
package labs;

import java.awt.Dimension;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;


/** A laboratory base class that should be the base for each of laboratory
 * classes. As a skeleton code for laboratories, it has some helper methods to
 * alleviate measurements and plotting of results.
 *
 * @author Karol Draszawka <kadr@eti.pg.gda.pl>
 */
public abstract class BaseLab {
    
    /** This is intended to be the only public method of all Lab subclasses.
     * 
     */
    public abstract void testAll();
    
    
    protected static double[] generateRandomData(int dataSize){
        double[] data = new double[dataSize];
        for(int i = 0; i<dataSize; ++i){
            data[i] = Math.random();
        }
        return data;
    }
    
    
    protected static double[][] generateRandomDataForNodes(int nNodes, int dataSize){
        double[][] data = new double[nNodes][];
        for(int i = 0; i<nNodes; ++i){
            data[i] = generateRandomData(dataSize);
        }
        return data;
    }
    
    
    protected static JFreeChart makeChart(XYSeriesCollection seriesCollection,
            String chartTitle, String xLabel, String yLabel) {
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            chartTitle, xLabel, yLabel, seriesCollection,
            PlotOrientation.VERTICAL,
            true, // Show Legend
            true, // Use tooltips
            false // Configure chart to generate URLs?
            );
       
        
        //adding diamond marks to series
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();
        for(int i = 0; i < plot.getSeriesCount(); ++i){
            r.setSeriesShape(i, ShapeUtilities.createDiamond(5));
            r.setSeriesShapesVisible(i, true);
        }
        
        return chart;
    }

    protected static void displayChart(JFreeChart chart) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(630, 480);
            }
        };
        f.add(chartPanel);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
