import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

public class DirectedGraphTest
{
    public static void main(String[] args)
    {
        Graph<Integer, DefaultEdge> graph = GraphTypeBuilder
                .directed().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(false)
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

        JGraphTGraphVizWrapper.createGraphDOTFile("directed_graph.dot", graph, "shape=circle", "arrowhead=vee");
        JGraphTGraphVizWrapper.printGraph("neato", "-Goverlap=false", "directed_graph.dot", "pdf", "directed_graph");
    }
}
