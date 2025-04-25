/**
 * CPSC 450, HW-3
 * 
 * NAME: David Giacobbi
 * DATE: Fall, 2024 
 */

package cpsc450;

import java.net.http.WebSocket.Listener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Basic adjacency List implementation of the Graph interface.
 */
public class AdjList implements Graph {

  private int vertexCount;                     // total number of vertices
  private int edgeCount;                       // running count of number of edges
  private Map<Integer,Set<Integer>> outEdges;  // storage for the out edges
  private Map<Integer,Set<Integer>> inEdges;   // storage for the in edges

  /**
   * Create an adjacency list (graph) given a specific (fixed) number
   * of vertices.
   * @param vertices The number of vertices of the graph.
   */
  public AdjList(int vertices) throws GraphException {
    // Check if vertices is valid count
    if (vertices <= 0) throw new GraphException("Invalid vertex count");

    // Declare vertex and edge counts
    this.vertexCount = vertices;
    this.edgeCount = 0;
    this.outEdges = new HashMap<Integer,Set<Integer>>(this.vertexCount);
    this.inEdges = new HashMap<Integer,Set<Integer>>(this.vertexCount);

    // Create the two hash tables with hash sets for each vertex
    for (int i=0; i<this.vertexCount; i++){
      this.outEdges.put(i, new HashSet<>());
      this.inEdges.put(i, new HashSet<>());
    }
  }

  @Override
  public void addEdge(int x, int y) {
    // Check if x and y are valid vertices
    if (!this.hasVertex(x)) return;
    if (!this.hasVertex(y)) return;

    // Check if x does not already have an edge to y
    if (!this.hasEdge(x, y)){
      this.outEdges.get(x).add(y);
      this.inEdges.get(y).add(x);
      this.edgeCount += 1;
    } 
    return;
  }

  @Override
  public void removeEdge(int x, int y) {
    // Check if x does not already have an edge to y
    if (this.hasEdge(x, y)){
      this.outEdges.get(x).remove(y);
      this.inEdges.get(y).remove(x);
      edgeCount -= 1;
    } 
    return;
  }

  @Override
  public Set<Integer> out(int x) {
    // Create out edges set and check if x is a valid vertex
    Set<Integer> outSet = new HashSet<>();
    if (!this.hasVertex(x)) return outSet; 

    // Get the set of edges for x from hash table and return
    outSet = new HashSet<Integer>(this.outEdges.get(x));
    return outSet;
  }

  @Override
  public Set<Integer> in(int x) {
    // Create out edges set and check if x is a valid vertex
    Set<Integer> inSet = new HashSet<>();
    if (!this.hasVertex(x)) return inSet; 

    // Get the set of edges for x from hash table and return
    inSet = new HashSet<Integer>(this.inEdges.get(x));
    return inSet;
  }

  @Override
  public Set<Integer> adj(int x) {
    // Create combine set and check if x is valid vertex
    Set<Integer> combineSet = new HashSet<>();
    if (!this.hasVertex(x)) return combineSet;

    // Get the set of in and out edges
    Set<Integer> inSet = this.in(x);
    Set<Integer> outSet = this.out(x);

    // Combine the in and out set and return
    combineSet = inSet;
    combineSet.addAll(outSet);
    return combineSet;
  }

  @Override
  public boolean hasEdge(int x, int y) {
    // Check if x and y are valid vertices
    if (!this.hasVertex(x)) return false;
    if (!this.hasVertex(y)) return false;

    // Check if y is in x edge set
    if (this.outEdges.get(x).contains(y) && this.inEdges.get(y).contains(x)) return true;
    return false;
  }

  @Override
  public boolean hasVertex(int x) {
    // Check if x is not in valid vertex range, otherwise true
    if (x < 0 || x >= this.vertexCount) return false;
    return true;
  }

  @Override
  public int vertices() {
    return this.vertexCount;
  }
  
  @Override
  public int edges() {
    return this.edgeCount;
  }
  
}
