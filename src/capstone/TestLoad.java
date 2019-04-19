package capstone;

import capstone.util.GraphLoader;

import java.util.HashSet;

public class TestLoad {
    public static void main(String[] args) {
        CapGraph testGraph = new CapGraph();
        double totalTime = 0;

        //Load the graph
        double startTime = System.nanoTime();
        //GraphLoader.loadGraph(testGraph, "data/small_test_graph_temp.txt");
        //GraphLoader.loadGraph(testGraph, "data/facebook_2000.txt");
        //GraphLoader.loadGraph(testGraph, "data/scc/test_10.txt");
        GraphLoader.loadGraph(testGraph, "data/twitter_combined.txt");
        //GraphLoader.loadGraph(testGraph, "data/twitter_higgs.txt");
        double endTime = System.nanoTime();
        double performTime = (endTime - startTime) / 1e9;
        totalTime += performTime;
        System.out.println("Graph loaded in " + performTime + " seconds");
        testGraph.printGraph();
        testGraph.printEdges();

        //Order the list of nodes
        startTime = System.nanoTime();
        testGraph.orderListOfNodes(true);
        endTime = System.nanoTime();
        performTime = (endTime - startTime) / 1e9;
        totalTime += performTime;
        System.out.println("Graph ordered in " + performTime + " seconds");
        testGraph.printGraph();
        testGraph.printEdges();
        //testGraph.printOrderedList();

        //Greedy Search
        startTime = System.nanoTime();
        HashSet<GraphNode> setMDS = testGraph.greedySearchMDS(testGraph.getSortedNodes());
        endTime = System.nanoTime();
        performTime = (endTime - startTime) / 1e9;
        totalTime += performTime;
        System.out.println("Greedy search performed in " + performTime + " seconds");
        System.out.println("Minimum dominating set consists of " + setMDS.size() + " nodes");
//        for (GraphNode gn : setMDS) {
//            System.out.println("Node - " + gn.getValue());
//        }


        //RLS search
        int minDS = setMDS.size();
        //I've found that the best value of MDS appeared in the first 10 iterations
        //So I run series of 10 iterations for 100 times to get the best results
        for (int i = 0; i < 1; i++) {
            startTime = System.nanoTime();
            HashSet<GraphNode> optMDS = testGraph.rlsSearchMDS(20000); // 10 - the number of iterations
            endTime = System.nanoTime();
            performTime = (endTime - startTime) / 1e9;
            totalTime += performTime;
            System.out.println("RLS search performed in " + performTime + " seconds");
            System.out.println("Minimum dominating set consists of " + optMDS.size() + " nodes");
            if (optMDS.size() < minDS) {
                minDS = optMDS.size();
            }
        }
        System.out.println("Minimum dominating set after 100 RLSs - " + minDS);



        System.out.println("Total time - " + totalTime + " seconds");
    }

}
