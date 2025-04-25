/**
 * CPSC 450, HW-6
 *
 * NAME: David Giacobbi
 * DATE: Fall 2024
 *
 */ 

package cpsc450;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Deque; 
import java.util.Queue;


/** 
 * Suite of graph-based algorithms. 
 */
public class GraphAlgorithms {
  
  /**
   * Find the strongly components components of the given directed
   * graph using two (modified) DFS-Full searches.
   * @param g The given directed graph.
   * @return The strongly connected component that each vertex belongs
   * to, as a map from vertices to components, where components are
   * labeled 1, 2, ..., k.
   */
  public static Map<Integer,Integer> strongComponents(Graph g) {
    // Create a strong components map
    Map<Integer,Integer> strongComponentsMap= new HashMap<Integer, Integer>(g.vertices());

    //------------- DFS-Full #1: Get the linearization of graph ------------------//
    // Create array list for linearizationa and keep visited nodes global
    ArrayList<Integer> linearization = new ArrayList<Integer>();
    boolean visited[] = new boolean[g.vertices()];

    // Traverse unvisited nodes and run DFS
    for (int i=0; i<g.vertices(); i++){
      // If node has already been visited, continue to next iteration
      if (visited[i] == true)
        continue;

      // Create a stack for next explored vertex and add i to stack
      Deque<Integer> stack = new LinkedList<>();
      stack.addFirst(i);

      // While loop to visit each vertex in stack until stack is empty
      while (!stack.isEmpty()){
        // Get the vertex at top of stack (peek), set current vertex to visited
        int u = stack.getFirst();
        visited[u] = true;

        // Get the in-edge set for vertex u since it is graph inverse for first iteration
        Set<Integer> adjSet = g.in(u);
        // Set boolean that assumes all vertices in adjSet have been visited
        boolean all_adj_visited = true;

        // Traverse all edges that are adjacent to u in edge set
        for (int v : adjSet){
          // Check if v has not been visited yet
          if (!visited[v]){
            // Set v's parent to u and add to stack
            stack.addFirst(v);
            all_adj_visited = false;
            break;
          }
        }
        // If dead-end reached, remove vertex from stack and add to linearization
        if (all_adj_visited)
          linearization.addFirst(stack.removeFirst());
      }
    }

    //--------- DFS-Full #2: Traverse linearization to get component graphs ---------------//
    // Reset the visited list to all false and set component counter
    boolean visitedSecond[] = new boolean[g.vertices()];
    int nextComponent = 1;

    // Traverse linearization nodes and run DFS on each unvisited node
    for (int j : linearization){
      // If node has already been visited, continue to next iteration
      if (visitedSecond[j] == true)
        continue;

      // Create a visited array of vertices and stack for next explored vertex
      Deque<Integer> stack = new LinkedList<>();
      stack.addFirst(j);

      // While loop to visit each vertex in stack until stack is empty
      while (!stack.isEmpty()){
        // Get the vertex at top of stack (peek), set current vertex to visited
        int u = stack.getFirst();
        visitedSecond[u] = true;

        // Get out edge set of the original graph
        Set<Integer> adjSet = g.out(u);
        // Set boolean that assumes all vertices in adjSet have been visited
        boolean all_adj_visited = true;

        // Traverse all edges that are adjacent to u in edge set
        for (int v : adjSet){
          // Check if v has not been visited yet
          if (!visitedSecond[v]){
            // Set v's parent to u and add to stack
            stack.addFirst(v);
            all_adj_visited = false;
            break;
          }
        }
        // If dead-end reached, remove vertex from stack and add to linearization
        if (all_adj_visited)
          strongComponentsMap.put(stack.removeFirst(), nextComponent);
      }
      // Increment component count for map
      nextComponent++;
    }
    return strongComponentsMap;
  }

  
  /**
   * Return the transitive closure of the given graph by performing a
   * depth-first search from each vertex.
   * @param g The given directed graph
   * @return A copy of the original graph (as an adjacency list),
   * but transitively closed.
   */
  public static Graph transitiveClosure(Graph g) {
    // Create an empty graph to add edges for transitive closure representation
    Graph closureGraph = new AdjList(g.vertices());

    // Run DFS on each vertex, adding edges after a node is colored black
    for (int i=0; i<g.vertices(); i++){
      // Create a visited array of vertices and stack for next explored vertex
      boolean visited[] = new boolean[g.vertices()];
      Deque<Integer> stack = new LinkedList<>();

      // Set parent of s to -1, push s onto stack
      stack.addFirst(i);

      // While loop to visit each vertex in stack until stack is empty
      while (!stack.isEmpty()){
        // Get the vertex at top of stack (peek), set current vertex to visited
        int u = stack.getFirst();
        visited[u] = true;

        // Get the out-edge set for vertex u since always directed
        Set<Integer> adjSet = g.out(u);
        // Set boolean that assumes all vertices in adjSet have been visited
        boolean all_adj_visited = true;

        // Traverse all edges that are adjacent to u in edge set
        for (int v : adjSet){
          // Add any self edges from original graph
          if (v == i)
            closureGraph.addEdge(i, v);
          // Check if v has not been visited yet
          if (!visited[v]){
            // Add to stack
            stack.addFirst(v);
            all_adj_visited = false;
            break;
          }
        }
        // If dead-end reached, remove vertex from stack and add edge from it to vertex i
        if (all_adj_visited){
          int popVertex = stack.removeFirst();
          if (i != popVertex)
            closureGraph.addEdge(i, popVertex);
        }
      }
    }
    return closureGraph;
  }

  
  
  /**
   * Compute the transitive reduction of the given directed graph
   * given the algorithm from class (which finds connected components,
   * can add new edges as needed, etc.).
   * @param g The given directed graph.
   * @returns The transitively reduced graph (as an adjacency list).
   */
  public static Graph transitiveReduction(Graph g) {
    // Create a new graph to return transitive reduction
    Graph reductionGraph = new AdjList(g.vertices());

    // Get the strongly connected components of g
    Map<Integer,Integer> graphSCC = strongComponents(g);

    // Find the number of components
    int compCount = 0;
    for (int compId : graphSCC.values()){
      if (compId > compCount)
        compCount = compId;
    }

    // Create a meta-graph of all the connected components
    Graph metaGraph = new AdjList(compCount);
    for (int vertex : graphSCC.keySet()){
      // Add a bridge to each of the components
      for (int outVertex : g.out(vertex)){
        // Remove any self edges
        if (outVertex == vertex)
          continue;
        // Check if the metagraph already has edge between components (if none, add one in both graphs)
        if (!metaGraph.hasEdge(graphSCC.get(vertex)-1, graphSCC.get(outVertex)-1)){
          metaGraph.addEdge(graphSCC.get(vertex)-1, graphSCC.get(outVertex)-1);
        }
      }
    }

    // Create a map of all the components to create the simple cycles
    Map<Integer,ArrayList<Integer>> componentSets = new HashMap<Integer,ArrayList<Integer>>();
    // Initialize lists for each component
    for (int i=1; i<=compCount; i++)
      componentSets.put(i, new ArrayList<Integer>());
    // Put each vertex into corresponding set
    for (int i=0; i<g.vertices(); i++){
      componentSets.get(graphSCC.get(i)).add(i);
    }
    // Iterate through each component and create a simple cycle
    for (int key=1; key<=compCount; key++){
      // Check for one vertex component
      if (componentSets.get(key).size() < 2)
        continue;
      // Otherwise, add edge between every node that does not exist
      for (int j=1; j<componentSets.get(key).size(); j++){
        if (!reductionGraph.hasEdge(componentSets.get(key).get(j-1), componentSets.get(key).get(j)))
          reductionGraph.addEdge(componentSets.get(key).get(j-1), componentSets.get(key).get(j));
      } 
      if (!reductionGraph.hasEdge(componentSets.get(key).get(componentSets.get(key).size()-1), componentSets.get(key).get(0)))
        reductionGraph.addEdge(componentSets.get(key).get(componentSets.get(key).size()-1), componentSets.get(key).get(0));
    }

    // Compute irreducible kernel over the meta-graph
    for (int k=0; k<metaGraph.vertices(); k++){
      // Traverse each edge
      for (int out : metaGraph.out(k)){
        // Remove edge from graph to check if reachability still valid
        metaGraph.removeEdge(k, out);
        // If still reachable, add edge to reduction graph and back to meta-graph
        if (!reachable(metaGraph, k, out)){
          metaGraph.addEdge(k, out);
          reductionGraph.addEdge(componentSets.get(k+1).get(0), componentSets.get(out+1).get(0));
        }
      }
    }

    return reductionGraph;
  }


  /**
   * Determine if the given graph has an Euler path, and if so, return
   * it.
   * @param g The given directed graph.
   * @return A (Linked) list representing an Euler path, or an empty
   * path if the graph does not have an Euler path.
   */
  public static List<Integer> eulerPath(Graph g) {
    // Create linked list of path to return
    LinkedList<Integer> eulerList = new LinkedList<Integer>();

    // Determine if the graph meets all the constraints of a valid Euler graph
    int sourceVertex = -1;
    int sinkVertex = -1;
    for (int i=0; i<g.vertices(); i++){
      // Check if source vertex has been found
      if (g.out(i).size() - g.in(i).size() == 1){
        if (sourceVertex > -1)
          return eulerList;
        sourceVertex = i;
      }
      // Check if sink vertex has been found
      else if (g.in(i).size() - g.out(i).size() == 1){
        if (sinkVertex > -1)
          return eulerList;
        sinkVertex = i;
      }
      // Make sure the rest of the vertices have equivalent in and out edges
      else {
        if (g.in(i).size() != g.out(i).size())
          return eulerList;
      }
    }

    // Select the source vertex to perform DFS
    if (sourceVertex == -1)
      sourceVertex = 0;

    // Perform DFS and append to the eulerList as nodes colored black
    // Create a visited array of vertices and stack for next explored vertex
    Deque<Integer> stack = new LinkedList<>();

    // Push source onto stack
    stack.addFirst(sourceVertex);

    // While loop to visit each vertex in stack until stack is empty
    while (!stack.isEmpty()){
      // Get the vertex at top of stack (peek), set current vertex to visited
      int u = stack.getFirst();

      // Get the out-edge set for vertex u since always directed
      Set<Integer> adjSet = g.out(u);
      // Set boolean that assumes all vertices in adjSet have been visited
      boolean all_adj_visited = true;

      // Traverse all edges that are adjacent to u in edge set
      for (int v : adjSet){
        // Set v's parent to u and add to stack
        stack.addFirst(v);
        all_adj_visited = false;
        // Once edge has been visited, remove from graph
        g.removeEdge(u, v);
        break;
      }
      // If dead-end reached, remove vertex from stack and add to end of linked list
      if (all_adj_visited)
        eulerList.addFirst(stack.removeFirst());
    }
    return eulerList;
  }


  /**
   * Checks if a source (src) vertex can reach a destination (dst)
   * vertex (i.e., dst is reachable from src) in a directed graph
   * using a modified version of the BFS algorithm. Assumes all
   * vertices can trivially reach themselves (even if no self edge).
   * @param g The directed graph to search
   * @param src The source vertex to search from
   * @param dst The destination vertex (for a path from src to dst)
   * @returns True if reachable and false otherwise.
   */
  public static boolean reachable(Graph g, int src, int dst) {
    // Check if src = dst and return array list of 1
    if (src == dst)
      return true;

    // Create a visited array of vertices and queue for next explored vertex
    boolean visited[] = new boolean[g.vertices()];
    Queue<Integer> Q = new LinkedList<>();

    // Mark s as visited, set parent of s to -1, add s to queue
    visited[src] = true;
    Q.add(src);

    // For loop to visit each vertex in queue until queue is empty
    while (!Q.isEmpty()){
      
      // Get the vertex at front of Q (dequeue)
      int u = Q.poll();

      // Check if u is the dst
      if (u == dst){
        return true;
      }
      
      // Get the out-edge set for vertex u since always directed
      Set<Integer> adjSet = g.out(u);

      // Traverse all edges that are adjacent to u in edge set
      for (int v : adjSet){
        // Check if v has not been visited yet
        if (!visited[v]){
          // Mark v visited, set v's parent to u, add v to Q
          visited[v] = true;
          Q.add(v);
        }
      }
    }
    return false;
  }


}

