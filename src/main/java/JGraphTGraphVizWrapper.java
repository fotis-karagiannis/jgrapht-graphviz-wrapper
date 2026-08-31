import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ClusteringAlgorithm.Clustering;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A wrapper class that allows printing JGraphT graphs and their clusters by using GraphViz layout engines
 */
public final class JGraphTGraphVizWrapper
{
    public static final String DEFAULT_PROPERTIES ="";
    public static final List<String> DEFAULT_COLORS = new ArrayList<>(List.of("aqua", "darkseagreen", "cornflowerblue", "brown1", "coral", "gold", "darkorchid", "deeppink", "coral4", "azure4"));
    private static final List<String> LAYOUT_ENGINES = new ArrayList<>(List.of("dot", "neato", "twopi", "circo", "fdp", "sfdp", "osage", "patchwork"));

    /**
     * Constructor that doesn't allow the class to be instantiated
     */
    private JGraphTGraphVizWrapper()
    {
        throw new UnsupportedOperationException("Error: Class cannot be instantiated!");
    }

    /**
     * Convert a graph to a DOT-format string
     *
     * Most of the parameters are options to be printed on the DOT file
     * Default static values exist for most options
     *
     * @param graph : input graph
     * @param clustering : clusters of the graph
     * @param clusterColors : colors for each cluster
     * @param vertexProperties : vertex properties
     * @param edgeProperties : edge properties
     *
     * @return : a string containing the graph and given options in DOT-format
     */
    private static <V, E> String convertGraphToDOT(Graph<V, E> graph, Clustering<V> clustering, List<String> clusterColors, String vertexProperties, String edgeProperties)
    {
        // Find if a graph is directed or not and print accordingly
        String edgeType = " -- ";
        String graphType = "graph G";
        if(graph.getType().isDirected())
        {
            edgeType = " -> ";
            graphType = "digraph ";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(graphType).append("\n{\n");

        // Add graph settings
        sb.append("\t").append("node ").append("[").append(vertexProperties).append("];\n");
        sb.append("\t").append("edge ").append("[").append(edgeProperties).append("];\n\n");
        // Add graph data
        // If a vertex is not connected to an edge, and is not part of a cluster, it must be printed separately to appear in the printed graph
        for(V v : graph.vertexSet())
        {
            boolean notInCluster = true;
            if(clustering!=null)
            {
                for(Set<V> cluster : clustering.getClusters())
                {
                    if(cluster.contains(v))
                    {
                        notInCluster = false;
                        break;
                    }
                }
                if(graph.outgoingEdgesOf(v).size()==0 && graph.incomingEdgesOf(v).size()==0 && notInCluster)
                {
                    sb.append("\t").append(v).append(";\n");
                }
            }
            else // if the graph has no clusters, and a vertex is not connected to any edge, it must still be printed separately
            {
                if(graph.outgoingEdgesOf(v).size()==0 && graph.incomingEdgesOf(v).size()==0)
                {
                    sb.append("\t").append(v).append(";\n");
                }
            }
        }

        if(clustering != null)
        {
            for (int i=0; i<clustering.getClusters().size(); i++)
            {
                sb.append("\t").append("subgraph cluster_").append(i).append("\n");
                sb.append("\t{\n");

                // Only a fixed amount of colors is provided
                if(i<clusterColors.size())
                {
                    sb.append("\t\tnode [fillcolor=").append(clusterColors.get(i)).append("];\n");
                }
                else // No color assigned if the number of clusters exceeds the colors given
                {
                    sb.append("\t\tnode [];\n");
                }

                for(V vertex : clustering.getClusters().get(i))
                {
                    sb.append("\t\t").append(vertex).append(";\n");
                }
                sb.append("\t}\n");
            }
            sb.append("\n");
        }

        if (graph.getType().isWeighted())
        {
            for (E e : graph.iterables().edges())
            {
                sb.append("\t").append(graph.getEdgeSource(e)).append(edgeType).append(graph.getEdgeTarget(e)).append("[fontsize=12.0, label=").append(Math.round(graph.getEdgeWeight(e))).append("];").append("\n");
            }
        }
        else
        {
            for (E e : graph.iterables().edges())
            {
                sb.append("\t").append(graph.getEdgeSource(e)).append(edgeType).append(graph.getEdgeTarget(e)).append(";").append("\n");
            }
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Creates a file containing the DOT-format of a graph on the working directory
     *
     * Most of the parameters are options to be printed on the DOT file
     * Default static values exist for most options
     *
     * @param outputFileName : output file name
     * @param graph : input graph
     * @param vertexProperties : vertex properties
     * @param edgeProperties : edge properties
     */
    public static <V, E> void createGraphDOTFile(String outputFileName, Graph<V, E> graph, String vertexProperties, String edgeProperties)
    {
        try
        {
            FileWriter writer = new FileWriter(outputFileName, false);
            writer.write(convertGraphToDOT(graph, null, null, vertexProperties, edgeProperties));
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates a file containing the DOT-format of a graph with clusters on the working directory
     *
     * Most of the parameters are options to be printed on the DOT file
     * Default static values exist for most options
     *
     * @param outputFileName : output file name
     * @param graph : input graph
     * @param clustering : clusters of the graph
     * @param clusterColors : colors for each cluster
     * @param vertexProperties : vertex properties
     * @param edgeProperties : edge properties
     */
    public static <V, E> void createGraphDOTFile(String outputFileName, Graph<V, E> graph, Clustering<V> clustering, List<String> clusterColors, String vertexProperties, String edgeProperties)
    {
        try
        {
            FileWriter writer = new FileWriter(outputFileName, false);
            // Style is filled to allow cluster colors
            if(!vertexProperties.contains("style=filled"))
            {
                if(vertexProperties.isEmpty())
                {
                    vertexProperties = "style=filled";
                }
                else
                {
                    vertexProperties = "style=filled, " + vertexProperties;
                }
            }
            writer.write(convertGraphToDOT(graph, clustering, clusterColors, vertexProperties, edgeProperties));
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Executes a GraphViz layout engine on the command line to produce a graph image
     *
     * @param algorithmName : layout engine name
     * @param algorithmArguments : layout engine arguments
     * @param dotFileName : dot file name
     * @param outputFileType : resulting file type
     * @param outputFileName : resulting file name
     */
    public static void printGraph(String algorithmName, String algorithmArguments, String dotFileName, String outputFileType, String outputFileName)
    {
        // Do not allow execution of any command unless it's a graphviz layout engine
        if(!LAYOUT_ENGINES.contains(algorithmName))
        {
            System.out.println("Error: "+algorithmName+" is not a supported layout engine command : The process will terminate\n");
            return;
        }

        ProcessBuilder processBuilder =
                new ProcessBuilder("cmd.exe", "/c", algorithmName + " " + algorithmArguments + " " + dotFileName + " -T" + outputFileType + " -o " + outputFileName + "." + outputFileType).redirectErrorStream(true);
        try
        {
            Process process = processBuilder.start();
            // Show command execution output/error
            StringBuilder result = new StringBuilder(80);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream())))
            {
                while (true)
                {
                    String line = in.readLine();
                    if (line == null)
                        break;
                    result.append(line).append("\n");
                }
            }
            System.out.println(result);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
