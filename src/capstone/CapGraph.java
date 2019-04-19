package capstone;

import java.util.*;

public class CapGraph implements capstone.Graph{

    //Graph body
    private HashMap<Integer, GraphNode> graphBody;
    //List for ordered nodes
    private ArrayList<GraphNode> sortedNodes;
    //Set of edges - actually it is not used in my project at the moment
    private HashSet<GraphEdge> edges;

    public CapGraph() {
        graphBody = new HashMap<Integer, GraphNode>();
        sortedNodes = new ArrayList<GraphNode>();
        edges = new HashSet<GraphEdge>();
    }

    public ArrayList<GraphNode> getSortedNodes() {
        ArrayList<GraphNode> output = new ArrayList<GraphNode>(sortedNodes);
        return output;
    }


    public void addVertex(int num) {
        if (!graphBody.containsKey(num)) {
            graphBody.put(num, new GraphNode(num));
        }
    }

    public void addEdge(int from, int to) {
        graphBody.get(from).addFollower(to);
        edges.add(new GraphEdge(from, to));
    }

    public void printGraph() {
//		for (int n : graphBody.keySet()) {
//			graphBody.get(n).printNode();
//		}
        System.out.println("Vertices number - " + graphBody.size());
        System.out.println("*****************************");
    }

    public void printEdges() {
//		for (GraphEdge ge : edges) {
//			System.out.println(ge.getFrom() + " -> " + ge.getTo());
//		}
        System.out.println("Edges number - " + edges.size());
    }

    public HashMap<Integer, GraphNode> getGraphBody() {
        return graphBody;
    }

    public void printOrderedList() {
        for (int i = 0; i < sortedNodes.size(); i++) {
            System.out.println("Node #" + (i+1) + " - " + sortedNodes.get(i).getValue() + " with "
                                + sortedNodes.get(i).getFollowers().size() + " followers");
            if (i >= 49) {
                break;
            }
        }
        System.out.println("The size of ordered list is - " + sortedNodes.size());
    }

    //Set the list of nodes from the nodes with the biggest number of followers in descending order
    public void orderListOfNodes(boolean toSort) {
        sortedNodes.addAll(graphBody.values());
        if (toSort) {
            Collections.sort(sortedNodes, Collections.reverseOrder());
        }
    }

    //Set all the nodes in the graph as uncovered
    private void setAllNodesUncovered() {
        for (GraphNode gn : graphBody.values()) {
            gn.setCovered(false);
        }
    }

    //Helper method to construct a new permutation for RLS
    private ArrayList<GraphNode> constructPermutation(HashSet<GraphNode> domSet) {
        ArrayList<GraphNode> newPermutation = new ArrayList<>(domSet);
        Collections.sort(newPermutation, Collections.reverseOrder());
        HashSet<GraphNode> tempSet = new HashSet<>(graphBody.values());
        tempSet.removeAll(domSet);
        ArrayList<GraphNode> tempList = new ArrayList<>(tempSet);
        Collections.shuffle(tempList);
        newPermutation.addAll(tempList);
//        for (int i : graphBody.keySet()) {
//            if (!newPermutation.contains(graphBody.get(i))) {
//                newPermutation.add(graphBody.get(i));
//            }
//        }
        return newPermutation;
    }


    //Perturbation operator
    private ArrayList<GraphNode> jumpFunction(ArrayList<GraphNode> newPermutation) {
        Random randIdx = new Random();
        int j = randIdx.nextInt(newPermutation.size() - 2) + 1;
        ArrayList<GraphNode> jPermutation = new ArrayList<>();
        jPermutation.add(newPermutation.get(j));
        jPermutation.addAll(newPermutation.subList(0, j));
        jPermutation.addAll(newPermutation.subList(j + 1, newPermutation.size()));
        return jPermutation;
    }


    //Greedy Search on the passed list of nodes
    public HashSet<GraphNode> greedySearchMDS(ArrayList<GraphNode> sortedList) {
        //Get all the nodes as uncovered
        HashMap<Integer, GraphNode> uncoveredNodes = new HashMap<Integer, GraphNode>(graphBody);
        //Set for dominating set (MDS)
        HashSet<GraphNode> setMDS = new HashSet<GraphNode>();
        //Set all nodes as uncovered
        setAllNodesUncovered();

        for (int i = 0; i < sortedList.size(); i++) {
            //When there is no uncovered nodes - break
            if (uncoveredNodes.isEmpty()) {
                break;
            }
            //Take a node from the list
            GraphNode currNode = sortedList.get(i);
            //Check if the node is covered
            //If node is uncovered add it to the MDS list and set it covered
            //Otherwise skip this node
            if (!currNode.isCovered()) {
                setMDS.add(currNode);
                currNode.setCovered(true);
            }
            else {
                continue;
            }
            //Remove node from the uncovered set
            uncoveredNodes.remove(currNode.getValue());
            //Check the followers of the node
            //If there are uncovered followers - set them covered and delete from uncovered set
            for (int node : currNode.getFollowers()) {
                if (uncoveredNodes.containsKey(node)) {
                    uncoveredNodes.get(node).setCovered(true);
                    uncoveredNodes.remove(node);
                }
            }
        }
        //Return MDS
        return setMDS;
    }

    //Order-based randomised local search (RLS0) for MDS
    public HashSet<GraphNode> rlsSearchMDS(int bound) {
        //The result of Greedy approximation algorithm
        HashSet<GraphNode> optMDS = greedySearchMDS(this.sortedNodes);
        //New permutation constructed from Greedy mapping
        ArrayList<GraphNode> optPermutation = constructPermutation(optMDS);

        int counter = 0;
        ArrayList<GraphNode> shiftedPermutation = optPermutation;

        //Search for MDS
        while (counter < bound) {
            shiftedPermutation = jumpFunction(shiftedPermutation);
            HashSet<GraphNode> tempMDS = greedySearchMDS(shiftedPermutation);
            shiftedPermutation = constructPermutation(tempMDS);
            if (tempMDS.size() < optMDS.size()) {
                optMDS = tempMDS;
                optPermutation = shiftedPermutation;
            }
            counter++;
            //System.out.println("Counter - " + counter + ", MDS size - " + tempMDS.size());
        }
        return optMDS;
    }
}
