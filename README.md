# UCSD-CapstoneProject

Overview:

This project implements the algorithm for getting a minimum dominating set of Twitter users that are needed to broadcast some information to all Twitter users. The first part of the project is completed with the implementation of Greedy Search algorithm for finding a minimum dominating set (at least to get as minimum as possible). In the second part of the project, the goal will be to implement an order-based randomized local search algorithm to solve the minimum dominating set problem.

Data:

Twitter followers and the users they follow in file twitter_combined.txt provided by UCSD for this course (It is part of Stanford's Snap network database. Source: https://snap.stanford.edu/data/egonets-Twitter.html). There are 81306 nodes and 1768149 edges in the graph.

Easier question:

How to get a minimum dominating set efficiently since it is an NP-complete problem? One of the ways is to use the Greedy Search algorithm. It’s a quite quick solution though it may not find the smallest dominated set.

Harder question:

How to optimize a Greedy Search algorithm to find an even smaller dominating set? The task is to implement an order-based randomized local search algorithm for solving the minimum dominating set problem. That algorithm may be considered as a way to optimize a Greedy Search algorithm. 

Algorithms, Data Structures, and Answer to your Question:

Easier question:

Main Data Structure: The network of Twitter users is represented by the graph using adjacency list. Each node in the graph is a Twitter user with his/her followers, directed edges link the user with the followers. In initial data set edges link followers to the user they follow. I had to modify GraphLoader to transpose graph while loading to decrease total running time (at first, I loaded graph and then transposed it).

Algorithm:

Input: Sorted list of nodes in descending order by the number of node’s followers (sortedList).

Output: The Minimum dominating set of nodes that are needed to broadcast information to all users in the network.



Create a new HashMap<Integer, <GraphNode>> consists of all the nodes of the current graph as a set of uncovered nodes, and their number as a key in the map (uncoveredNodes).

Create a set of the nodes (setMDS) in the minimum dominated set

For each node in the sortedList:

            If there is no nodes in the uncoveredNodes break the loop

            If node is uncovered:

                     Add it to the setMDS and set it covered

                     Otherwise, skip this node

            Remove node from the uncoveredNodes

            For each follower of the node

                        If the node of the follower is in the uncoveredNodes

                                    Set the follower’s node covered

                                    Remove the follower’s node from the uncoveredNodes

Return setMDS

 

Answer: setMDS consists of minimum dominated set of nodes. Since for the dataset given the output include almost 25 thousand nodes, only the size of setMDS printed as an answer.



Harder question:

Main Data Structure: The same as for the easier question.

Algorithm:

Input: A permutation of nodes constructed from the result of Greedy Search.

Output: Minimum dominating set of nodes that are needed to broadcast information to all users in the network.

 

The detailed algorithm is described in the article by David Chalupa - https://arxiv.org/abs/1705.00318

 

Declare a HashSet<GraphNode> variable for a minimum dominating set (optMDS) and set it to the result of Greedy Search. Consider that the result of Greedy Search might be the best result.

Construct a new permutation of nodes as an ArrayList<GraphNode> (optPermutation) – first the nodes from the result of Greedy Search and then the other nodes shuffled.

Declare a counter with value 0. This is our boundary – it may be a number of iterations or a target size of the minimum dominating set.

Declare a variable for a temporary permutation (shiftedPermutation) and set it to the value of optPermutation.

While the boundary isn’t reached

            Change shiftedPermutation with “jump” operator (get the node with the random index from 2 to the last index in the list and put this node at the beginning of the list)

            Make a Greedy Search on shiftedPermutation and set it to the variable tempMDS

            Construct a new permutation from tempMDS similarly to the second step in this algorithm

            If the size of tempMDS is less than optMDS

                        Set optMDS to the value of tempMDS (Consider this is the best result)

                        Set optPermutation to the value shiftedPermutation (Consider this is the optimal permutation)

            Increase counter by 1

Return optMDS

 

Answer: optMDS consists of minimum dominated set of nodes. Since for the dataset given the output include almost 25 thousand nodes, only the size of optMDS printed as an answer.



Algorithm Analysis:

In my opinion, steps preparing initial data and structures have O(V+E)

Greedy Search algorithm will have the best case O(V) and the worst case O(V^2)

An order-based randomized local search algorithm’s running time depends on the running time of Greedy Search algorithm, so it will have the same running time as Greedy Search.

Testing:

·      I used small test file (small_test_graph.txt) and test file test_1.txt – test_10.txt provided by UCSD which help me to test some use cases, edge cases and help me find some bugs in my code.

·      However, test on twitter_combined.txt file failed and I had to create my own test file to test some other edge cases (like repeated edges in the file). I found some bugs with correct adding edges and vertices to list, set and map (bugs was in overriding of equals and hashCode methods)

·      I also wrote several print methods to check the correctness of my methods using small files

·      A couple of times I use the debugger in IDE to check the correctness of my methods. 

Reflection:

I have to change my second question, because n-Opt optimizations, which I considered initially, weren’t suitable for such a problem. They are useful for Greedy Search optimizations in solving The Salesperson Problem, but for finding a solution for a Minimum Dominating Set I needed another solution. Fortunately, I’ve found an appropriate article and managed to understand the new algorithm. So I implemented it in time.

The biggest challenge for me was an optimization of the performance of my code so it runs in a reasonable time, sometimes my code wasn't brilliant and I managed to make it 1000 times faster :)
Some statistics:
Greedy Search found a dominating set consists of 24669 nodes
An order-based randomized local search algorithm found a dominating set consists of 24636 nodes at the best (In general, this algorithm found a number between 24636 and 24643 nodes, which is also better than the result of greedy Search).

Code Overview:

Class name: CapGraph

Purpose and description of class: Represents the network graph. Stores a map where integer values of Twitter users IDs are mapped to GraphNodes containing that IDs respectively, and a list of GraphEdges. Also has an instance variable of type ArrayList for storing GraphNodes sorted in descending order by the number of followers. There are a constructor and methods for adding vertices and edges into the graph, several printing methods used for testing, method for creating ordered list (orderListOfNodes) of nodes and getter method for that list. Also there is a method for getting a minimum dominated set (searchMDS) which will be used in both “easier” and “harder” questions. Method orderListOfNodes is used for “easier” question to get a sorted list and then this list is passed as parameter to the method searchMDS – so we will perform Greedy Search that way. For “harder” question there is the method for an order-based randomized local search algorithm (rlsSearchMDS) and two helper methods for constructing permutations (constructPermutation) and for changing them for another iteration (jumpFunction).

 

Class name: GraphNode

Purpose and description of class: Represents a node in a graph. Stores an ID of a Twitter user represented by this node and a set of IDs of the user’s followers, and a boolean value to show if this node is covered while performing a search for minimum dominated set. Contain getter methods for size of followers set, ID of the user, and for a set of followers. There are also a method for adding a follower, and two method to set the node covered and to check if the node is covered. A method for printing a node. I wrote a compareTo method and overrid equals and hashCode methods to efficiently work with nodes while sorting them in the list.

 

Class name: GraphEdge

Purpose and description of class: Represents an edge in a graph. Stores references to each of the vertices the edge connects. Actually, edges aren’t used in my project, but they may be useful for future extensions.

 

Overall Design Justification:

The cornerstone of my project is a list of nodes sorted in some particular order. ArrayList lets me sort it with Collections.sort method for Greedy Search, and I can get an element with particular index in constant time which will be useful for my “harder” question. To efficiently work with nodes I need a HashMap of IDs as keys and GraphNodes as values instead of just HashSets of integers as values. I need to mark somehow these sets as covered, so I decided to write a new class (GraphNode) to store a set of followers and a mark if this node is covered. It helps me to compare and sort these nodes too, since HashSet, obviously, doesn’t have methods like compareTo, equals and hashCode. Additionally, HashMap lets me to quickly remove a node with particular value in constant time, so I use it to remove covered nodes from a temporary HashMap of uncovered nodes. 
