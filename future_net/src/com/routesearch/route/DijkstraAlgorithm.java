package com.routesearch.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {
	  private final List<Vertex> nodes;
	  private final List<Edge> edges;
	  private Set<Vertex> settledNodes;
	  private Set<Vertex> unSettledNodes;
	  private Map<Vertex, Vertex> predecessors;
	  private Map<Vertex, Integer> distance;

	  public DijkstraAlgorithm(Graph graph) {
	    // create a copy of the array so that we can operate on this array
	    this.nodes = new ArrayList<Vertex>(graph.getVertexes());
	    this.edges = new ArrayList<Edge>(graph.getEdges());
	  }

	  /**
	   * 功能：根据给出的源点寻找到其余顶点的最优路径
	   * @param source
	   */
	  public Map<Vertex, Integer> execute(Vertex source) {
	    settledNodes = new HashSet<Vertex>();
	    unSettledNodes = new HashSet<Vertex>();
	    distance = new HashMap<Vertex, Integer>();
	    predecessors = new HashMap<Vertex, Vertex>();
	    distance.put(source, 0);
	    unSettledNodes.add(source);
	    while (unSettledNodes.size() > 0) {
	      Vertex node = getMinimum(unSettledNodes);
	      settledNodes.add(node);
	      unSettledNodes.remove(node);
	      findMinimalDistances(node);
	    }
	    return distance;
	  }

	  /**
	   * 功能：
	   * @param node
	   */
	  private void findMinimalDistances(Vertex node) {
	    List<Vertex> adjacentNodes = getNeighbors(node);
	    for (Vertex target : adjacentNodes) {
	      if (getShortestDistance(target) > getShortestDistance(node)
	          + getDistance(node, target)) {
	        distance.put(target, getShortestDistance(node)
	            + getDistance(node, target));
	        predecessors.put(target, node);
	        unSettledNodes.add(target);
	      }
	    }

	  }

	  /**
	   * 功能：取指定两顶点间的边的权重
	   * @param node
	   * @param target
	   * @return
	   */
	  private int getDistance(Vertex node, Vertex target) {
	    for (Edge edge : edges) {
	      if (edge.getSource().equals(node)
	          && edge.getDestination().equals(target)) {
	        return edge.getWeight();
	      }
	    }
	    throw new RuntimeException("Should not happen");
	  }

	  /**
	   * 功能：在未处理的点集中取得与顶点node相连的点
	   * @param node
	   * @return
	   */
	  private List<Vertex> getNeighbors(Vertex node) {
	    List<Vertex> neighbors = new ArrayList<Vertex>();
	    for (Edge edge : edges) {
	      if (edge.getSource().equals(node)
	          && !isSettled(edge.getDestination())) {
	        neighbors.add(edge.getDestination());
	      }
	    }
	    return neighbors;
	  }

	  /**
	   * 功能：取得距离最短顶点
	   * @param vertexes
	   * @return
	   */
	  private Vertex getMinimum(Set<Vertex> vertexes) {
	    Vertex minimum = null;
	    for (Vertex vertex : vertexes) {
	      if (minimum == null) {
	        minimum = vertex;
	      } else {
	        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
	          minimum = vertex;
	        }
	      }
	    }
	    return minimum;
	  }

	  /**
	   * 功能：判断顶点是否被处理过
	   * @param vertex
	   * @return
	   */
	  private boolean isSettled(Vertex vertex) {
	    return settledNodes.contains(vertex);
	  }

	  /**
	   * 功能：在集合distance中取出顶点destination对应的距离最小值
	   * @param destination
	   * @return
	   */
	  private int getShortestDistance(Vertex destination) {
	    Integer d = distance.get(destination);
	    if (d == null) {
	      return Integer.MAX_VALUE;
	    } else {
	      return d;
	    }
	  }

	  /**
	   * 功能：根据终点，输出最短路径
	   * @param target
	   * @return
	   */
	  public LinkedList<Vertex> getPath(Vertex target) {
	    LinkedList<Vertex> path = new LinkedList<Vertex>();
	    Vertex step = target;
	    // check if a path exists
	    if (predecessors.get(step) == null) {
	      return null;
	    }
	    path.add(step);
	    while (predecessors.get(step) != null) {
	      step = predecessors.get(step);
	      path.add(step);
	    }
	    // Put it into the correct order
	    Collections.reverse(path);
	    return path;
	  }
}
