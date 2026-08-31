import org.jgrapht.Graph;
import org.jgrapht.alg.clustering.GirvanNewmanClustering;
import org.jgrapht.alg.interfaces.ClusteringAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;
import java.util.List;

public class SimpleGraphWithClustersTest
{
    public static void main(String[] args)
    {
        Graph<Integer, DefaultEdge> graph = GraphTypeBuilder
                .undirected().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(false)
                .edgeSupplier(SupplierUtil.DEFAULT_EDGE_SUPPLIER)
                .vertexSupplier(SupplierUtil.createIntegerSupplier()).buildGraph();

        // Create 15 vertexes: 14 connected with edges, 1 disconnected
        for (int i = 0; i < 14; i++)
        {
            graph.addVertex();
        }

        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(1,2);
        graph.addEdge(2,6);
        graph.addEdge(3,5);
        graph.addEdge(3,4);
        graph.addEdge(4,5);
        graph.addEdge(5,6);
        graph.addEdge(6,7);
        graph.addEdge(7,8);
        graph.addEdge(7,11);
        graph.addEdge(8,9);
        graph.addEdge(8,10);
        graph.addEdge(11,12);
        graph.addEdge(11,13);
        graph.addEdge(9,10);
        graph.addEdge(12,13);

        int k = 4;
        ClusteringAlgorithm.Clustering<Integer> clustering = new GirvanNewmanClustering<>(graph, k).getClustering();

        JGraphTGraphVizWrapper.createGraphDOTFile("simple_graph_clusters.dot", graph, clustering, List.of("aqua","coral", "gold", "brown1"), "shape=circle", JGraphTGraphVizWrapper.DEFAULT_PROPERTIES);
        JGraphTGraphVizWrapper.printGraph("neato", "-Goverlap=false", "simple_graph_clusters.dot", "pdf", "simple_graph_clusters");
    }
}
