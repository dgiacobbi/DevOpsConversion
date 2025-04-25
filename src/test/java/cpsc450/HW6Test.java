/**
 * CPSC 450, HW-6
 * 
 * NAME: S. Bowers
 * DATE: Fall 2024
 *
 * Basic unit tests for strongly connected components, transitive
 * closure, transitive reduction, and euler paths.
 */

package cpsc450;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;


class HW6Test {

  //======================================================================
  // Adjacency Matrix Tests
  //======================================================================

  //----------------------------------------------------------------------
  // Strongly Connected Component Tests
  
  @Test
  void adjMatrixOneVertexSCC() {
    Graph g = new AdjMatrix(1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(1, cs.size());
    assertEquals(1, cs.get(0));
  }

  @Test
  void adjMatrixTwoVertexSCC() {
    Graph g = new AdjMatrix(2);
    g.addEdge(0, 1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(2, cs.size());
    assertTrue(cs.get(0) != cs.get(1));
    assertTrue(List.of(1,2).contains(cs.get(0)));
    assertTrue(List.of(1,2).contains(cs.get(1)));
  }

  @Test
  void adjMatrixTwoVertexCycleSCC() {
    Graph g = new AdjMatrix(2);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(2, cs.size());
    assertTrue(cs.get(0) == 1);
    assertTrue(cs.get(1) == 1);
  }

  @Test
  void adjMatrixThreeDisconnectedVerticesSCC() {
    Graph g = new AdjMatrix(3);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertEquals(3, Set.of(cs.get(0),cs.get(1),cs.get(2)).size());
  }

  @Test
  void adjMatrixThreeVertexThreeSCCs() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(2, 1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertEquals(3, Set.of(cs.get(0), cs.get(1), cs.get(2)).size());
  }
  
  @Test
  void adjMatrixThreeVertexTwoSCCs() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(2, 1);
    g.addEdge(1, 2);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertTrue(cs.get(1) == cs.get(2));
    assertTrue(cs.get(0) != cs.get(1));
  }

  @Test
  void adjMatrixThreeVertexOneSCC() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(0, 2);
    g.addEdge(2, 1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertTrue(cs.get(0) == cs.get(1) && cs.get(1) == cs.get(2));
  }

  @Test
  void adjMatrixFourVertexTwoDisconnectedSCCs() {
    Graph g = new AdjMatrix(4);
    g.addEdge(0, 3);
    g.addEdge(3, 0);
    g.addEdge(2, 1);
    g.addEdge(1, 2);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(4, cs.size());
    assertTrue(cs.get(0) == cs.get(3) && cs.get(1) == cs.get(2));
    assertTrue(cs.get(0) != cs.get(1));
  }
  
  @Test
  void adjMatrixFromQuizSCC() {
    Graph g = new AdjMatrix(8);
    g.addEdge(0, 4);
    g.addEdge(0, 1);
    g.addEdge(1, 3);
    g.addEdge(1, 5);
    g.addEdge(2, 0);
    g.addEdge(2, 3);
    g.addEdge(2, 7);
    g.addEdge(3, 1);
    g.addEdge(4, 0);
    g.addEdge(4, 5);
    g.addEdge(6, 2);
    g.addEdge(7, 3);
    g.addEdge(7, 6);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(8, cs.size());
    // 2, 6, 7 should be in the same component
    assertTrue(cs.get(2) == cs.get(6) && cs.get(6) == cs.get(7));
    // 1, 3 should be in the same component
    assertTrue(cs.get(1) == cs.get(3));
    // 0, 4 should be in the same component
    assertTrue(cs.get(0) == cs.get(4));
    // should be 4 separate components
    assertEquals(4, Set.of(cs.get(2),cs.get(1),cs.get(0),cs.get(5)).size());
  }

  @Test
  void adjMatrixFromClassSCC() {
    Graph g = new AdjMatrix(12);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 5);
    g.addEdge(4, 1);
    g.addEdge(4, 5);
    g.addEdge(4, 6);
    g.addEdge(5, 2);
    g.addEdge(5, 7);
    g.addEdge(6, 7);
    g.addEdge(6, 9);
    g.addEdge(7, 10);
    g.addEdge(8, 6);
    g.addEdge(9, 8);
    g.addEdge(10, 11);
    g.addEdge(11, 9);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(12, cs.size());
    // 1, 4 should be in the same component
    assertTrue(cs.get(1) == cs.get(4));
    // 2, 5 should be in the same component
    assertTrue(cs.get(2) == cs.get(5));
    // 6, 7, 8, 9, 10, 11 should be in the same component
    assertTrue(cs.get(6) == cs.get(7) && cs.get(7) == cs.get(8));
    assertTrue(cs.get(8) == cs.get(9) && cs.get(9) == cs.get(10));
    assertTrue(cs.get(10) == cs.get(11));
    // each component should be different
    assertEquals(4, Set.of(cs.get(1),cs.get(2),cs.get(6),cs.get(3)).size());
  }  

  //----------------------------------------------------------------------
  // Transitive Closure Tests

  @Test
  void adjMatrixOneVertexNoEdgeClosure() {
    Graph g = new AdjMatrix(1);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(1, c.vertices());
    assertEquals(0, c.edges());
  }

  @Test
  void adjMatrixOneVertexOneEdgeClosure() {
    Graph g = new AdjMatrix(1);
    g.addEdge(0, 0);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(1, c.vertices());
    assertEquals(1, c.edges());
    assertTrue(c.hasEdge(0, 0));
  }

  @Test
  void adjMatrixTwoVertexOneEdgeClosure() {
    Graph g = new AdjMatrix(2);
    g.addEdge(0, 1);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(2, c.vertices());
    assertEquals(1, c.edges());
    assertTrue(c.hasEdge(0, 1));    
  }

  @Test
  void adjMatrixThreeVertexTwoEdgeClosure() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(3, c.vertices());
    assertEquals(3, c.edges());
    assertTrue(c.hasEdge(0, 1));
    assertTrue(c.hasEdge(1, 2));        
    assertTrue(c.hasEdge(0, 2));
  }

  @Test
  void adjMatrixThreeVertexCycleClosure() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(2, 0);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(3, c.vertices());
    assertEquals(9, c.edges());
    for (int x : List.of(0, 1, 2))
      for (int y : List.of(0, 1, 2))
        assertTrue(c.hasEdge(x, y));
  }

  @Test
  void adjMatrixSixVertexBarbellClosure() {
    Graph g = new AdjMatrix(6);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(2, 0);
    g.addEdge(3, 5);
    g.addEdge(4, 3);
    g.addEdge(5, 4);
    g.addEdge(1, 3);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(6, c.vertices());
    assertEquals(27, c.edges());
    for (int x : List.of(0, 1, 2))
      for (int y : List.of(0, 1, 2, 3, 4, 5))
        assertTrue(c.hasEdge(x, y));
    for (int x : List.of(3, 4, 5))
      for (int y : List.of(3, 4, 5))
        assertTrue(c.hasEdge(x, y));
  }
  
  //----------------------------------------------------------------------
  // Transitive Reduction Tests

  @Test
  void adjMatrixOneVertexReduction() {
    Graph g = new AdjMatrix(1);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(1, r.vertices());
    assertEquals(0, r.edges());
  }

  @Test
  void adjMatrixOneVertexSelfEdgeReduction() {
    Graph g = new AdjMatrix(1);
    g.addEdge(0, 0);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(1, r.vertices());
    assertEquals(0, r.edges());
  }
  
  @Test
  void adjMatrixThreeVertexTransitiveEdgeReduction() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(0, 2);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(3, r.vertices());
    assertEquals(2, r.edges());
    assertTrue(r.hasEdge(0, 1));
    assertTrue(r.hasEdge(1, 2));
  }

  @Test
  void adjMatrixOneComponentReduction() {
    Graph g = new AdjMatrix(2);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(2, r.vertices());
    assertEquals(2, r.edges());
    assertTrue(r.hasEdge(0, 1));
    assertTrue(r.hasEdge(1, 0));
  }
  
  @Test
  void adjMatrixTwoComponentReduction() {
    Graph g = new AdjMatrix(4);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 2);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(4, r.vertices());
    assertEquals(5, r.edges());
    assertTrue(r.hasEdge(0, 1));
    assertTrue(r.hasEdge(1, 0));
    assertTrue(r.hasEdge(2, 3));
    assertTrue(r.hasEdge(3, 2));
    assertTrue(r.hasEdge(0, 2) || r.hasEdge(0, 3) ||
               r.hasEdge(1, 2) || r.hasEdge(1, 3));
  }

  @Test
  void adjMatrixFourVertexComponentReduction() {
    Graph g = new AdjMatrix(4);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    g.addEdge(2, 1);
    g.addEdge(0, 3);
    g.addEdge(3, 0);
    g.addEdge(1, 3);
    g.addEdge(3, 1);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(4, r.vertices());
    assertEquals(4, r.edges());
    for (int x : List.of(0, 1, 2, 3))
      for (int y : List.of(0, 1, 2, 3))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
  }

  @Test
  void adjMatrixExampleFromClassReduction() {
    Graph g = new AdjMatrix(12);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 5);
    g.addEdge(4, 1);
    g.addEdge(4, 5);
    g.addEdge(4, 6);
    g.addEdge(5, 2);
    g.addEdge(5, 7);
    g.addEdge(6, 7);
    g.addEdge(6, 9);
    g.addEdge(7, 10);
    g.addEdge(8, 6);
    g.addEdge(9, 8);
    g.addEdge(10, 11);
    g.addEdge(11, 9);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(12, r.vertices());
    assertEquals(14, r.edges());
    // component edges
    assertTrue(r.hasEdge(1,4) && r.hasEdge(4,1));
    assertTrue(r.hasEdge(2,5) && r.hasEdge(5,2));
    for (int x : List.of(6,7,8,9,10,11))
      for (int y : List.of(6,7,8,9,10,11))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // cross component edges
    assertTrue(r.hasEdge(0,1) || r.hasEdge(0,4));
    assertTrue(r.hasEdge(1,3) || r.hasEdge(4,3));
    for (int x : List.of(1,4))
      for (int y : List.of(2,5))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    for (int x : List.of(2,5))
      for (int y : List.of(6,7,8,9,10,11))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // non-edges
    for (int x : List.of(6,7,8,9,10,11))
      assertTrue(!r.hasEdge(1, x) && !r.hasEdge(4,x));
  }

  @Test
  void adjMatrixEightNodeFourComponentReduction() {
    Graph g = new AdjMatrix(8);
    g.addEdge(0, 1);
    g.addEdge(0, 4);
    g.addEdge(1, 3);
    g.addEdge(1, 5);
    g.addEdge(2, 0);
    g.addEdge(2, 3);
    g.addEdge(2, 7);
    g.addEdge(3, 1);
    g.addEdge(4, 0);
    g.addEdge(4, 5);
    g.addEdge(6, 2);
    g.addEdge(7, 3);
    g.addEdge(7, 6);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(8, r.vertices());
    assertEquals(10, r.edges());
    // component edges
    assertTrue(r.hasEdge(1, 3) && r.hasEdge(3, 1));
    assertTrue(r.hasEdge(0, 4) && r.hasEdge(4, 0));
    for (int x : List.of(2, 6, 7))
      for (int y : List.of(2, 6, 7))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // cross component edges
    assertTrue(r.hasEdge(1, 5) || r.hasEdge(3, 5));
    assertTrue(r.hasEdge(0, 1) || r.hasEdge(0, 3) ||
               r.hasEdge(4, 1) || r.hasEdge(4, 3));
    for (int x : List.of(2,6,7))
      for (int y : List.of(0,4))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // non-edges
    for (int x : List.of(2, 6, 7))
      assertFalse(r.hasEdge(x, 1) && r.hasEdge(x, 3));
    assertFalse(r.hasEdge(0, 5) && r.hasEdge(4, 5));
  }

  //----------------------------------------------------------------------
  // Euler Paths

  @Test
  void adjMatrixOneVertexEulerPath() {
    Graph g = new AdjMatrix(1);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(1, path.size());
    assertEquals(0, path.get(0));
  }

  @Test
  void adjMatrixTwoVertexOneEdgeEulerPath() {
    Graph g = new AdjMatrix(2);
    g.addEdge(0, 1);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(2, path.size());
    assertEquals(List.of(0,1), path);
  }

  @Test
  void adjMatrixTwoVertexCycleEulerPath() {
    Graph g = new AdjMatrix(2);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(3, path.size());
    assertEquals(List.of(0,1,0), path);
  }
  
  @Test
  void adjMatrixThreeVertexSimplePathEulerPath() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(3, path.size());
    assertEquals(List.of(0,1,2), path);
  }

  @Test
  void adjMatrixThreeVertexCycleEulerPath() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(4, path.size());
    assertEquals(List.of(1,0,1,2), path);
  }

  @Test
  void adjMatrixThreeVertexTwoEulerPaths() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    g.addEdge(2, 1);
    g.addEdge(2, 0);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(6, path.size());
    assertTrue(path.equals(List.of(2,1,2,0,1,0)) ||
               path.equals(List.of(2,1,0,1,2,0)) ||
               path.equals(List.of(2, 0, 1, 2, 1, 0)));
  }

  @Test
  void adjMatrixFiveVertexThreeEulerPaths() {
    Graph g = new AdjMatrix(5);
    g.addEdge(0, 2);
    g.addEdge(1, 0);
    g.addEdge(1, 3);
    g.addEdge(2, 1);
    g.addEdge(2, 3);
    g.addEdge(3, 1);
    g.addEdge(3, 4);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(8, path.size());
    assertTrue(path.equals(List.of(2,3,1,0,2,1,3,4)) || 
               path.equals(List.of(2,1,0,2,3,1,3,4)) ||
               path.equals(List.of(2,1,3,1,0,2,3,4)));
  }
  
  @Test
  void adjMatrixBadThreeVertexEulerPath() {
    Graph g = new AdjMatrix(3);
    g.addEdge(0, 2);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(0, path.size());
  }

  @Test
  void adjMatrixBadFourVertexEulerPath() {
    Graph g = new AdjMatrix(4);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(2, 1);
    g.addEdge(2, 3);
    g.addEdge(3, 0);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(0, path.size());
  }

  //======================================================================
  // Adjacency List Tests
  //======================================================================

  //----------------------------------------------------------------------
  // Strongly Connected Component Tests
  
  @Test
  void adjListOneVertexSCC() {
    Graph g = new AdjList(1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(1, cs.size());
    assertEquals(1, cs.get(0));
  }

  @Test
  void adjListTwoVertexSCC() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(2, cs.size());
    assertTrue(cs.get(0) != cs.get(1));
    assertTrue(List.of(1,2).contains(cs.get(0)));
    assertTrue(List.of(1,2).contains(cs.get(1)));
  }

  @Test
  void adjListTwoVertexCycleSCC() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(2, cs.size());
    assertTrue(cs.get(0) == 1);
    assertTrue(cs.get(1) == 1);
  }

  @Test
  void adjListThreeDisconnectedVerticesSCC() {
    Graph g = new AdjList(3);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertEquals(3, Set.of(cs.get(0),cs.get(1),cs.get(2)).size());
  }

  @Test
  void adjListThreeVertexThreeSCCs() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(2, 1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertEquals(3, Set.of(cs.get(0), cs.get(1), cs.get(2)).size());
  }
  
  @Test
  void adjListThreeVertexTwoSCCs() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(2, 1);
    g.addEdge(1, 2);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertTrue(cs.get(1) == cs.get(2));
    assertTrue(cs.get(0) != cs.get(1));
  }

  @Test
  void adjListThreeVertexOneSCC() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(0, 2);
    g.addEdge(2, 1);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(3, cs.size());
    assertTrue(cs.get(0) == cs.get(1) && cs.get(1) == cs.get(2));
  }

  @Test
  void adjListFourVertexTwoDisconnectedSCCs() {
    Graph g = new AdjList(4);
    g.addEdge(0, 3);
    g.addEdge(3, 0);
    g.addEdge(2, 1);
    g.addEdge(1, 2);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(4, cs.size());
    assertTrue(cs.get(0) == cs.get(3) && cs.get(1) == cs.get(2));
    assertTrue(cs.get(0) != cs.get(1));
  }
  
  @Test
  void adjListFromQuizSCC() {
    Graph g = new AdjList(8);
    g.addEdge(0, 4);
    g.addEdge(0, 1);
    g.addEdge(1, 3);
    g.addEdge(1, 5);
    g.addEdge(2, 0);
    g.addEdge(2, 3);
    g.addEdge(2, 7);
    g.addEdge(3, 1);
    g.addEdge(4, 0);
    g.addEdge(4, 5);
    g.addEdge(6, 2);
    g.addEdge(7, 3);
    g.addEdge(7, 6);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(8, cs.size());
    // 2, 6, 7 should be in the same component
    assertTrue(cs.get(2) == cs.get(6) && cs.get(6) == cs.get(7));
    // 1, 3 should be in the same component
    assertTrue(cs.get(1) == cs.get(3));
    // 0, 4 should be in the same component
    assertTrue(cs.get(0) == cs.get(4));
    // should be 4 separate components
    assertEquals(4, Set.of(cs.get(2),cs.get(1),cs.get(0),cs.get(5)).size());
  }

  @Test
  void adjListFromClassSCC() {
    Graph g = new AdjList(12);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 5);
    g.addEdge(4, 1);
    g.addEdge(4, 5);
    g.addEdge(4, 6);
    g.addEdge(5, 2);
    g.addEdge(5, 7);
    g.addEdge(6, 7);
    g.addEdge(6, 9);
    g.addEdge(7, 10);
    g.addEdge(8, 6);
    g.addEdge(9, 8);
    g.addEdge(10, 11);
    g.addEdge(11, 9);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(12, cs.size());
    // 1, 4 should be in the same component
    assertTrue(cs.get(1) == cs.get(4));
    // 2, 5 should be in the same component
    assertTrue(cs.get(2) == cs.get(5));
    // 6, 7, 8, 9, 10, 11 should be in the same component
    assertTrue(cs.get(6) == cs.get(7) && cs.get(7) == cs.get(8));
    assertTrue(cs.get(8) == cs.get(9) && cs.get(9) == cs.get(10));
    assertTrue(cs.get(10) == cs.get(11));
    // each component should be different
    assertEquals(4, Set.of(cs.get(1),cs.get(2),cs.get(6),cs.get(3)).size());
  }  

  //----------------------------------------------------------------------
  // Transitive Closure Tests

  @Test
  void adjListOneVertexNoEdgeClosure() {
    Graph g = new AdjList(1);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(1, c.vertices());
    assertEquals(0, c.edges());
  }

  @Test
  void adjListOneVertexOneEdgeClosure() {
    Graph g = new AdjList(1);
    g.addEdge(0, 0);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(1, c.vertices());
    assertEquals(1, c.edges());
    assertTrue(c.hasEdge(0, 0));
  }

  @Test
  void adjListTwoVertexOneEdgeClosure() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(2, c.vertices());
    assertEquals(1, c.edges());
    assertTrue(c.hasEdge(0, 1));    
  }

  @Test
  void adjListThreeVertexTwoEdgeClosure() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(3, c.vertices());
    assertEquals(3, c.edges());
    assertTrue(c.hasEdge(0, 1));
    assertTrue(c.hasEdge(1, 2));        
    assertTrue(c.hasEdge(0, 2));
  }

  @Test
  void adjListThreeVertexCycleClosure() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(2, 0);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(3, c.vertices());
    assertEquals(9, c.edges());
    for (int x : List.of(0, 1, 2))
      for (int y : List.of(0, 1, 2))
        assertTrue(c.hasEdge(x, y));
  }

  @Test
  void adjListSixVertexBarbellClosure() {
    Graph g = new AdjList(6);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(2, 0);
    g.addEdge(3, 5);
    g.addEdge(4, 3);
    g.addEdge(5, 4);
    g.addEdge(1, 3);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(6, c.vertices());
    assertEquals(27, c.edges());
    for (int x : List.of(0, 1, 2))
      for (int y : List.of(0, 1, 2, 3, 4, 5))
        assertTrue(c.hasEdge(x, y));
    for (int x : List.of(3, 4, 5))
      for (int y : List.of(3, 4, 5))
        assertTrue(c.hasEdge(x, y));
  }
  
  //----------------------------------------------------------------------
  // Transitive Reduction Tests

  @Test
  void adjListOneVertexReduction() {
    Graph g = new AdjList(1);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(1, r.vertices());
    assertEquals(0, r.edges());
  }

  @Test
  void adjListOneVertexSelfEdgeReduction() {
    Graph g = new AdjList(1);
    g.addEdge(0, 0);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(1, r.vertices());
    assertEquals(0, r.edges());
  }
  
  @Test
  void adjListThreeVertexTransitiveEdgeReduction() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(0, 2);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(3, r.vertices());
    assertEquals(2, r.edges());
    assertTrue(r.hasEdge(0, 1));
    assertTrue(r.hasEdge(1, 2));
  }

  @Test
  void adjListOneComponentReduction() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(2, r.vertices());
    assertEquals(2, r.edges());
    assertTrue(r.hasEdge(0, 1));
    assertTrue(r.hasEdge(1, 0));
  }
  
  @Test
  void adjListTwoComponentReduction() {
    Graph g = new AdjList(4);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 2);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(4, r.vertices());
    assertEquals(5, r.edges());
    assertTrue(r.hasEdge(0, 1));
    assertTrue(r.hasEdge(1, 0));
    assertTrue(r.hasEdge(2, 3));
    assertTrue(r.hasEdge(3, 2));
    assertTrue(r.hasEdge(0, 2) || r.hasEdge(0, 3) ||
               r.hasEdge(1, 2) || r.hasEdge(1, 3));
  }

  @Test
  void adjListFourVertexComponentReduction() {
    Graph g = new AdjList(4);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    g.addEdge(2, 1);
    g.addEdge(0, 3);
    g.addEdge(3, 0);
    g.addEdge(1, 3);
    g.addEdge(3, 1);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(4, r.vertices());
    assertEquals(4, r.edges());
    for (int x : List.of(0, 1, 2, 3))
      for (int y : List.of(0, 1, 2, 3))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
  }

  @Test
  void adjListExampleFromClassReduction() {
    Graph g = new AdjList(12);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 5);
    g.addEdge(4, 1);
    g.addEdge(4, 5);
    g.addEdge(4, 6);
    g.addEdge(5, 2);
    g.addEdge(5, 7);
    g.addEdge(6, 7);
    g.addEdge(6, 9);
    g.addEdge(7, 10);
    g.addEdge(8, 6);
    g.addEdge(9, 8);
    g.addEdge(10, 11);
    g.addEdge(11, 9);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(12, r.vertices());
    assertEquals(14, r.edges());
    // component edges
    assertTrue(r.hasEdge(1,4) && r.hasEdge(4,1));
    assertTrue(r.hasEdge(2,5) && r.hasEdge(5,2));
    for (int x : List.of(6,7,8,9,10,11))
      for (int y : List.of(6,7,8,9,10,11))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // cross component edges
    assertTrue(r.hasEdge(0,1) || r.hasEdge(0,4));
    assertTrue(r.hasEdge(1,3) || r.hasEdge(4,3));
    for (int x : List.of(1,4))
      for (int y : List.of(2,5))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    for (int x : List.of(2,5))
      for (int y : List.of(6,7,8,9,10,11))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // non-edges
    for (int x : List.of(6,7,8,9,10,11))
      assertTrue(!r.hasEdge(1, x) && !r.hasEdge(4,x));
  }

  @Test
  void adjListEightNodeFourComponentReduction() {
    Graph g = new AdjList(8);
    g.addEdge(0, 1);
    g.addEdge(0, 4);
    g.addEdge(1, 3);
    g.addEdge(1, 5);
    g.addEdge(2, 0);
    g.addEdge(2, 3);
    g.addEdge(2, 7);
    g.addEdge(3, 1);
    g.addEdge(4, 0);
    g.addEdge(4, 5);
    g.addEdge(6, 2);
    g.addEdge(7, 3);
    g.addEdge(7, 6);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(8, r.vertices());
    assertEquals(10, r.edges());
    // component edges
    assertTrue(r.hasEdge(1, 3) && r.hasEdge(3, 1));
    assertTrue(r.hasEdge(0, 4) && r.hasEdge(4, 0));
    for (int x : List.of(2, 6, 7))
      for (int y : List.of(2, 6, 7))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // cross component edges
    assertTrue(r.hasEdge(1, 5) || r.hasEdge(3, 5));
    assertTrue(r.hasEdge(0, 1) || r.hasEdge(0, 3) ||
               r.hasEdge(4, 1) || r.hasEdge(4, 3));
    for (int x : List.of(2,6,7))
      for (int y : List.of(0,4))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // non-edges
    for (int x : List.of(2, 6, 7))
      assertFalse(r.hasEdge(x, 1) && r.hasEdge(x, 3));
    assertFalse(r.hasEdge(0, 5) && r.hasEdge(4, 5));
  }

  //----------------------------------------------------------------------
  // Euler Paths

  @Test
  void adjListOneVertexEulerPath() {
    Graph g = new AdjList(1);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(1, path.size());
    assertEquals(0, path.get(0));
  }

  @Test
  void adjListTwoVertexOneEdgeEulerPath() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(2, path.size());
    assertEquals(List.of(0,1), path);
  }

  @Test
  void adjListTwoVertexCycleEulerPath() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(3, path.size());
    assertEquals(List.of(0,1,0), path);
  }
  
  @Test
  void adjListThreeVertexSimplePathEulerPath() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(3, path.size());
    assertEquals(List.of(0,1,2), path);
  }

  @Test
  void adjListThreeVertexCycleEulerPath() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(4, path.size());
    assertEquals(List.of(1,0,1,2), path);
  }

  @Test
  void adjListThreeVertexTwoEulerPaths() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    g.addEdge(2, 1);
    g.addEdge(2, 0);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(6, path.size());
    assertTrue(path.equals(List.of(2,1,2,0,1,0)) ||
               path.equals(List.of(2,1,0,1,2,0)) ||
               path.equals(List.of(2, 0, 1, 2, 1, 0)));
  }

  @Test
  void adjListFiveVertexThreeEulerPaths() {
    Graph g = new AdjList(5);
    g.addEdge(0, 2);
    g.addEdge(1, 0);
    g.addEdge(1, 3);
    g.addEdge(2, 1);
    g.addEdge(2, 3);
    g.addEdge(3, 1);
    g.addEdge(3, 4);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(8, path.size());
    assertTrue(path.equals(List.of(2,3,1,0,2,1,3,4)) || 
               path.equals(List.of(2,1,0,2,3,1,3,4)) ||
               path.equals(List.of(2,1,3,1,0,2,3,4)));
  }
  
  @Test
  void adjListBadThreeVertexEulerPath() {
    Graph g = new AdjList(3);
    g.addEdge(0, 2);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(0, path.size());
  }

  @Test
  void adjListBadFourVertexEulerPath() {
    Graph g = new AdjList(4);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(2, 1);
    g.addEdge(2, 3);
    g.addEdge(3, 0);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(0, path.size());
  }

  
  //======================================================================
  // TODO: Design and implement the following unit tests. Note that
  // each graph below must be different. You must also draw and
  // include each graph (correctly labeled to the unit test) in your
  // write up. Your test graphs must be "interesting" (i.e., not
  // trivial for the unit test) and you need to state why you think
  // they are interesting for the test cases in your write up.
  //
  //  1. Create 2 strongly-connected components using different
  //     non-trivial graphs.
  //
  //  2. Create 2 transitive closure tests using dfferent non-trivial
  //     graphs. 
  //
  //  3. Create 2 transitive reduction tests using different
  //     non-trivial graphs with multiple connected components having
  //     an "interesting" meta-graph (for reduction).
  //
  //  4. Create 2 non-trivial euler path tests: one graph where a path
  //     exists and one graph where a path doesn't exist.
  //
  //======================================================================
  @Test
  void adjMatrixManySCCsWithDisconnectedComponent() {
    Graph g = new AdjMatrix(10);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(1, 2);
    g.addEdge(2, 0);
    g.addEdge(2, 1);
    g.addEdge(2, 6);
    g.addEdge(3, 1);
    g.addEdge(3, 0);
    g.addEdge(3, 5);
    g.addEdge(4, 2);
    g.addEdge(4, 6);
    g.addEdge(5, 1);
    g.addEdge(5, 2);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(10, cs.size());
    // 7,6,4,5,3 should be their own component
    assertEquals(5, Set.of(cs.get(7),cs.get(6),cs.get(4),cs.get(5),cs.get(3)).size());
    // 0,1,2 should be in the same component
    assertTrue(cs.get(0) == cs.get(1));
    assertTrue(cs.get(0) == cs.get(2));
  }

  @Test
  void adjMatrixFewSCCsWithManyVertices() {
    Graph g = new AdjMatrix(10);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(0, 6);
    g.addEdge(1, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 4);
    g.addEdge(4, 5);
    g.addEdge(4, 7);
    g.addEdge(5, 6);
    g.addEdge(6, 7);
    g.addEdge(6, 3);
    g.addEdge(7, 0);
    g.addEdge(8, 9);
    g.addEdge(9, 8);
    Map<Integer,Integer> cs = GraphAlgorithms.strongComponents(g);
    assertEquals(10, cs.size());
    // 0-7 should be in the same component
    assertTrue(cs.get(0) == cs.get(1));
    assertTrue(cs.get(0) == cs.get(2));
    assertTrue(cs.get(0) == cs.get(3));
    assertTrue(cs.get(0) == cs.get(4));
    assertTrue(cs.get(0) == cs.get(5));
    assertTrue(cs.get(0) == cs.get(6));
    assertTrue(cs.get(0) == cs.get(7));
    // 8-9 should be in the same component
    assertTrue(cs.get(8) == cs.get(9));
  }

  @Test
  void adjMatrixClosureWithAllDisconnectedVertices() {
    Graph g = new AdjMatrix(10);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(10, c.vertices());
    assertEquals(0, c.edges());
    List<Integer> vertexList = List.of(0,1,2,3,4,5,6,7,8,9);
    for (int x : vertexList)
      for (int y : vertexList)
        assertFalse(c.hasEdge(x, y));
  }

  @Test
  void adjMatrixClosureWithSomeDisconnectedSubgraphs() {
    Graph g = new AdjMatrix(10);
    g.addEdge(0, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 1);
    g.addEdge(1, 0);
    g.addEdge(4, 5);
    g.addEdge(5, 6);
    g.addEdge(6, 7);
    Graph c = GraphAlgorithms.transitiveClosure(g);
    assertEquals(10, c.vertices());
    // Subgraph loop
    for (int x : List.of(0, 1, 2,3))
      for (int y : List.of(0, 1, 2, 3))
        assertTrue(c.hasEdge(x, y));
    // Link List
    for (int x : List.of(5, 6, 7))
      assertTrue(c.hasEdge(4, x));
    for (int x : List.of(6, 7))
      assertTrue(c.hasEdge(5, x));
    assertTrue(c.hasEdge(6, 7));
    // Disconnected components
    assertFalse(c.hasEdge(8, 9));
    assertFalse(c.hasEdge(9, 8));
  }

  @Test
  void adjListLargeCompleteGraphReduction() {
    Graph g = new AdjList(8);
    for (int i=0; i<g.vertices(); i++){
      for (int j=0; j<g.vertices(); j++){
        g.addEdge(i, j);
      }
    }
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(8, r.vertices());
    assertEquals(8, r.edges());
    // component edges
    List<Integer> vertexList = List.of(0,1,2,3,4,5,6,7);
    for (int x : vertexList)
      for (int y : vertexList)
        assertTrue(GraphAlgorithms.reachable(r, x, y));
  }

  @Test
  void adjListMultiSCCDisconnectionReduction() {
    Graph g = new AdjList(8);
    g.addEdge(1, 0);
    g.addEdge(2, 0);
    g.addEdge(4, 0);
    g.addEdge(3, 1);
    g.addEdge(5, 7);
    g.addEdge(7, 6);
    g.addEdge(6, 7);
    g.addEdge(6, 5);
    Graph r = GraphAlgorithms.transitiveReduction(g);
    assertEquals(8, r.vertices());
    assertEquals(7, r.edges());
    // component edges
    for (int x : List.of(5,6,7))
      for (int y : List.of(5,6,7))
        assertTrue(GraphAlgorithms.reachable(r, x, y));
    // cross component edges
    assertTrue(r.hasEdge(1, 0));
    assertTrue(r.hasEdge(2, 0));
    assertTrue(r.hasEdge(4, 0));
    assertTrue(r.hasEdge(3, 1));
  }

  @Test
  void adjListBadTenVertexEulerPath() {
    Graph g = new AdjList(10);
    g.addEdge(0, 1);
    g.addEdge(1, 4);
    g.addEdge(4, 2);
    g.addEdge(2, 5);
    g.addEdge(5, 3);
    g.addEdge(3, 6);
    g.addEdge(6, 7);
    g.addEdge(7, 5);
    g.addEdge(5, 8);
    g.addEdge(8, 6);
    g.addEdge(8, 9);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(0, path.size());
  }

  @Test
  void adjListEightVertexThreeEulerPaths() {
    Graph g = new AdjList(8);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 4);
    g.addEdge(3, 7);
    g.addEdge(7, 4);
    g.addEdge(4, 5);
    g.addEdge(5, 3);
    g.addEdge(4, 6);
    g.addEdge(6, 3);
    List<Integer> path = GraphAlgorithms.eulerPath(g);
    assertEquals(11, path.size());
    assertTrue(path.equals(List.of(0,1,2,3,4,6,3,7,4,5,3)) || 
               path.equals(List.of(0,1,2,3,7,4,5,3,4,6,3)) ||
               path.equals(List.of(0,1,2,3,4,5,3,7,4,6,3)));
  }
  
}
