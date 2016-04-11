package com.routesearch.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	   * ���ܣ����ݸ�����Դ��Ѱ�ҵ����ඥ�������·��
	   * @param source
	   */
	  public Map<Vertex, Integer> execute(Vertex source, Set<Vertex> passNodesMark) {
	    settledNodes = new HashSet<Vertex>();
	    unSettledNodes = new HashSet<Vertex>();
	    Iterator<Vertex> it = passNodesMark.iterator();
        while(it.hasNext()){
        	// ��passNodes��Ѱ�����·������    
        	settledNodes.add(it.next());
        }
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
	   * ���ܣ�
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
	   * ���ܣ�ȡָ���������ıߵ�Ȩ��
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
	   * ���ܣ���δ����ĵ㼯��ȡ���붥��node�����ĵ�
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
	   * ���ܣ�ȡ�þ�����̶���
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
	   * ���ܣ��ж϶����Ƿ񱻴����
	   * @param vertex
	   * @return
	   */
	  private boolean isSettled(Vertex vertex) {
	    return settledNodes.contains(vertex);
	  }

	  /**
	   * ���ܣ��ڼ���distance��ȡ������destination��Ӧ�ľ�����Сֵ
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
	   * ���ܣ��жϵ�ĳ���������·�����Ƿ񾭹�����ָ������
	   *       ���������򷵻�true;
	   *       �����������򷵻�false.
	   * @param target
	   * @param passNodes
	   * @return
	   */
	  public boolean isPassOthers(Vertex target, Set<Vertex> passNodes){
		    Vertex step = target;
		    // �ȼ��·���Ƿ����
		    if (predecessors.get(step) == null) {
		      return false;
		    }
		    while (predecessors.get(step) != null) {
		      step = predecessors.get(step);
		      if(passNodes.contains(step))
		    	  return true;
		    }
		  return false;
	  }
	  
	  /**
	   * ���ܣ������յ㣬������·��
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
