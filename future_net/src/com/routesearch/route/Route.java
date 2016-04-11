/**
 * ʵ�ִ����ļ�
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Route
{
    /**
     * ����Ҫ��ɹ��ܵ����
     * 
     * @author XXX
     * @since 2016-3-4
     * @version V1
     */
	private static List<Vertex> nodes = new ArrayList<Vertex>();
	private static List<Edge> edges = new ArrayList<Edge>();
	static int vlen = 660; // �������
	static int elen ; // �߸���
    static BufferedReader br;
    static String line; 
    private static Set<Vertex> passNodes = new HashSet<Vertex>(); // ���뾭���ĵ㼯      
    private static Set<Vertex> passNodesMark = new HashSet<Vertex>(); // ��passNodes�㼯���Ѿ������ĵ��������
    private static LinkedList<Vertex> optimalPath = new LinkedList<Vertex>();
    private static Map<Vertex, Integer> distance = new HashMap<Vertex, Integer>();
    private static Vertex node;
    static int total;
    private static String resultStr;
    private static Vertex firstNode;
    private static Vertex lastNode;
    private static Vertex tempNode; 
	
    /**
     * ���ܣ�Ѱ������·��
     * @param graphContent
     * @param condition
     * @return
     */
    public static String searchRoute(String graphContent, String condition)
    {   	
    	resultStr = Route.testExcute(graphContent, condition);
    	return resultStr;
    }    
    
    public static String testExcute(String graphContent, String condition) {
        // ��ʼ������
        for (int i = 0; i < vlen; i++) {
          Vertex location = new Vertex(Integer.toString(i), Integer.toString(i));
          nodes.add(location);
        }

        // ��ʼ����
        br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(graphContent.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        try {
			while ( (line = br.readLine()) != null ) {  
			    String item[] = line.split(",");
			    addLane(item[0], Integer.parseInt(item[1]), Integer.parseInt(item[2]), Integer.parseInt(item[3]));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // ��ʼ�����뾭���Ķ���
        br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(condition.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        try {
			while ( (line = br.readLine()) != null ) {  
			    String item[] = line.split(",");
			    firstNode = nodes.get(Integer.parseInt(item[0]));
			    lastNode = nodes.get(Integer.parseInt(item[1]));
			    String item1[] = item[2].split("\\|");
			    for(int i = 0;i<item1.length;i++){
			    	passNodes.add(new Vertex(item1[i], item1[i]));
			    }			    
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph); // ����ͼ��Ϣ
        passNodesMark.add(lastNode); // ����յ�
        distance = dijkstra.execute(firstNode, passNodesMark); // ����Դ��
        LinkedList<Vertex> path = null;
        
        // �ֶ������·��    
        Iterator<Vertex> it = passNodes.iterator();
        while(it.hasNext()){
        	// ��passNodes��Ѱ�����·������    
            node = it.next();
            for(Vertex vertex:passNodes){
            	if(distance.get(vertex) != null && distance.get(node) != null){
            		if(distance.get(node) > distance.get(vertex))
                		node = vertex;  
            	}       	
            }
            
            path = dijkstra.getPath(node); // �����յ�������·�� 
        	if(assertNotNull(path)){
        		//path.remove(nodes.get(0));
                for (Vertex vertex : path) {                	
                	optimalPath.add(vertex);
                	passNodesMark.add(vertex); // �������ĵ㱣������
                	passNodes.remove(vertex); // ɾ�������ĵ�
                }                
                optimalPath.remove(node);
                it = passNodes.iterator();
        	}           
        	distance = dijkstra.execute(node, passNodesMark); 
        }
        
        passNodesMark.remove(lastNode); // �Ƴ��յ�
        // passNodes���һ�����������յ�·��
        distance = dijkstra.execute(node, passNodesMark); 
        path = dijkstra.getPath(lastNode); 
    	if(assertNotNull(path)){
            for (Vertex vertex : path) {                	
            	optimalPath.add(vertex);
            }
    	}
        
    	optimalPath.removeFirst(); // ȥ����һ����
    	String resultStr = "";
        for (Vertex vertex : optimalPath) {
        	resultStr = resultStr + vertex + "|";        	
        }     
        System.out.println(resultStr.substring(0,resultStr.length()-1));
        // �����Ȩ��
    	outputTotalCost();
    	// �������·��
    	if(optimalPath == null){
    		return "NA";
    	}
    	else
    	return outputOptPath();      
    }

   
    
    /**
     * ���ܣ���ʼ���ߵ���Ϣ
     */
	private static void addLane(String laneId, int sourceLocNo, int destLocNo,
    	      int duration) {
    	    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
    	    edges.add(lane);
    	  }
 
	/**
	 * ���ܣ������Ȩ��
	 * @return
	 */
	private static void outputTotalCost() {
		int totalCost = 0;
    	tempNode = firstNode;
        for (Vertex vertex : optimalPath) {        	
        	totalCost = totalCost + getWeight(tempNode, vertex);     
        	tempNode = vertex;
        }     
        System.out.println(totalCost);
	}
	
	/**
	 * ���ܣ��������·��
	 */
	private static String outputOptPath() {
		String resultStr = "";
    	tempNode = firstNode;
        for (Vertex vertex : optimalPath) {        	
        	resultStr = resultStr + getLinkID(tempNode, vertex) + "|";     
        	tempNode = vertex;
        }     
        System.out.println(resultStr.substring(0,resultStr.length()-1));
        return resultStr.substring(0,resultStr.length()-1);
	}
	
	/**
	 * ���ܣ�ȡ���������ߵ�ID
	 * @param sourceLocNo
	 * @param destLocNo
	 * @return
	 */
	private static String getLinkID(Vertex sourceLocNo, Vertex destLocNo){
		for(Edge edge:edges){
			if(edge.getSource().equals(sourceLocNo) && edge.getDestination().equals(destLocNo)){
				return edge.getId();			
			}
		}
		return "-1";
	}
	
	/**
	 * ���ܣ�ȡ�ñ߶�Ӧ��Ȩ��
	 * @param sourceLocNo
	 * @param destLocNo
	 * @return
	 */
	private static int getWeight(Vertex sourceLocNo, Vertex destLocNo) {
		for(Edge edge:edges){
			if(edge.getSource().equals(sourceLocNo) && edge.getDestination().equals(destLocNo)){
				return edge.getWeight();
			}
		}
		return 0;
	}
	
	/**
	 * ���ܣ��ж�·���Ƿ�Ϊ��
	 */
	private static boolean assertNotNull(LinkedList<Vertex> path) {
		// TODO Auto-generated method stub
		if(path == null)
			return false;
		else
			return true;
	}
}