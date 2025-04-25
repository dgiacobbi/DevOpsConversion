/**
 * CPSC 450, Fall 2024
 * 
 * NAME: David Giacobbi
 * DATE: Fall 2024
 */

package cpsc450;

import java.util.HashSet;
import java.util.Set;

/**
 * Adjacency Matrix implementation of the Graph interface. 
 */
public class AdjMatrix implements Graph {

  private int vertexCount;      // total number of vertices
  private int edgeCount;        // running count of number of edges
  private boolean matrix[];     // storage for the matrix as "flattened" 2D array

  /**
   * Create an adjacency matrix (graph) given a specific (fixed)
   * number of vertices.
   * @param vertices The number of vertices in the graph. 
   */ 
  public AdjMatrix(int vertices) throws GraphException {
    // Check if vertices is valid count
    if (vertices <= 0) throw new GraphException("Invalid vertex count");

    // Allocate memory for matrix
    this.matrix = new boolean[vertices*vertices];

    // Assign current counts
    this.vertexCount = vertices;
    this.edgeCount = 0;
  }

  @Override
  public void addEdge(int x, int y) {
    // Check if x and y are valid vertices
    if (!this.hasVertex(x)) return;
    if (!this.hasVertex(y)) return;

    // Check if edge is present, otherwise no edge added
    if (!this.hasEdge(x, y)){
      this.matrix[(this.vertexCount*x) + y] = true;
      this.edgeCount += 1;
    }
    return;
  }

  @Override
  public void removeEdge(int x, int y) {
    // Check if edge is present, remove - otherwise no edge added
    if (this.hasEdge(x, y)){
      this.matrix[(this.vertexCount*x) + y] = false;
      this.edgeCount -= 1;
    }
    return;
  }

  @Override
  public Set<Integer> out(int x) {
    // Create an empty set of out edges for x
    Set<Integer> outSet = new HashSet<>();

    // First check if x is a valid vertex
    if (!this.hasVertex(x)) return outSet;

    // Traverse possible out edges for x
    for (int i=0; i<this.vertexCount; i++){
      if (this.hasEdge(x, i)) outSet.add(i);
    }

    return outSet;
  }

  @Override
  public Set<Integer> in(int x) {
    // Create an empty set of in edges for x
    Set<Integer> inSet = new HashSet<>();

    // First check if x is a valid vertex
    if (!this.hasVertex(x)) return inSet;

    // Traverse possible in edges for x
    for (int i=0; i<this.vertexCount; i++){
      if (this.hasEdge(i, x)) inSet.add(i);
    }

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

    // Access location in matrix for edge, check true or false
    if (this.matrix[x*this.vertexCount + y]) return true;
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
