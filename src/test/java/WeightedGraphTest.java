import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

public class WeightedGraphTest
{
    public static void main(String[] args)
    {
        Graph<Integer, DefaultWeightedEdge> graph = GraphTypeBuilder
                .undirected().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(true)
                .edgeSupplier(SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER)
                .vertexSupplier(SupplierUtil.createIntegerSupplier()).buildGraph();

        for (int i = 0; i < 9; i++)
        {
            graph.addVertex();
        }

        graph.setEdgeWeight(graph.addEdge(0, 1), 2d);
        graph.setEdgeWeight(graph.addEdge(0, 5), 1d);
        graph.setEdgeWeight(graph.addEdge(1, 2), 3d);
        graph.setEdgeWeight(graph.addEdge(1, 4), 11d);
        graph.setEdgeWeight(graph.addEdge(2, 3), 4d);
        graph.setEdgeWeight(graph.addEdge(3, 4), 5d);
        graph.setEdgeWeight(graph.addEdge(3, 8), 10d);
        graph.setEdgeWeight(graph.addEdge(4, 5), 6d);
        graph.setEdgeWeight(graph.addEdge(4, 7), 12d);
        graph.setEdgeWeight(graph.addEdge(5, 6), 7d);
        graph.setEdgeWeight(graph.addEdge(6, 7), 8d);
        graph.setEdgeWeight(graph.addEdge(7, 8), 9d);

        JGraphTGraphVizWrapper.createGraphDOTFile("weighted_graph.dot", graph, "shape=circle", JGraphTGraphVizWrapper.DEFAULT_PROPERTIES);
        JGraphTGraphVizWrapper.printGraph("neato", "-Goverlap=false", "weighted_graph.dot", "pdf", "weighted_graph");
    }
}
