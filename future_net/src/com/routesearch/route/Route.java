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
	private static List<Vertex> nodes;
	private static List<Edge> edges;
	static int vlen = 300; // �������
	static int elen ; // �߸���
    static BufferedReader br;
    static String line; 
    private static Set<Vertex> passNodes = new HashSet<Vertex>(); // ���뾭���ĵ㼯      
    private static Set<Vertex> passNodesMark = new HashSet<Vertex>(); // ��passNodes�㼯���Ѿ������ĵ��������
    private static LinkedList<Vertex> optimalPath;
    private static Map<Vertex, Integer> distance;
    private static Vertex node;
    static int total;
	  
    public static String searchRoute(String graphContent, String condition)
    {   	
    	Route.testExcute(graphContent, condition);
    	return "";
    }    
    
    public static void testExcute(String graphContent, String condition) {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        passNodes = new HashSet<Vertex>();
        optimalPath = new LinkedList<Vertex>();
        distance = new HashMap<Vertex, Integer>();
        
        // ��ʼ�����뾭���Ķ���
        passNodes.add(new Vertex("Node_" + 55, "Node_" + 55));
        passNodes.add(new Vertex("Node_" + 22, "Node_" + 22));
        passNodes.add(new Vertex("Node_" + 33, "Node_" + 33));
        passNodes.add(new Vertex("Node_" + 15, "Node_" + 15));
        passNodes.add(new Vertex("Node_" + 198, "Node_" + 198));
        passNodes.add(new Vertex("Node_" + 123, "Node_" + 123));
        passNodes.add(new Vertex("Node_" + 134, "Node_" + 134));
        passNodes.add(new Vertex("Node_" + 156, "Node_" + 156));
        passNodes.add(new Vertex("Node_" + 255, "Node_" + 255));
        passNodes.add(new Vertex("Node_" + 258, "Node_" + 258));
        passNodes.add(new Vertex("Node_" + 236, "Node_" + 236));
        passNodes.add(new Vertex("Node_" + 77, "Node_" + 77));
        passNodes.add(new Vertex("Node_" + 27, "Node_" + 27));
        passNodes.add(new Vertex("Node_" + 233, "Node_" + 233));
        passNodes.add(new Vertex("Node_" + 85, "Node_" + 85));
        passNodes.add(new Vertex("Node_" + 20, "Node_" + 20));
        passNodes.add(new Vertex("Node_" + 66, "Node_" + 66));
        passNodes.add(new Vertex("Node_" + 222, "Node_" + 222));
        passNodes.add(new Vertex("Node_" + 238, "Node_" + 238));
        passNodes.add(new Vertex("Node_" + 79, "Node_" + 79));
        
        // ��ʼ������
        for (int i = 0; i < vlen; i++) {
          Vertex location = new Vertex("Node_" + i, "Node_" + i);
          nodes.add(location);
        }

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
        
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph); // ����ͼ��Ϣ
        distance = dijkstra.execute(nodes.get(19)); // ����Դ��
        LinkedList<Vertex> path = null;
        
        // �ֶ������·��    
        Iterator<Vertex> it = passNodes.iterator();
        while(it.hasNext()){
        	// ��passNodes��Ѱ�����·������    
            node = it.next();
            for(Vertex vertex:passNodes){
            	if(distance.get(node) > distance.get(vertex))
            		node = vertex;	
            }
            
            path = dijkstra.getPath(node); // �����յ�������·�� 
        	if(assertNotNull(path)){
        		//path.remove(nodes.get(0));
                for (Vertex vertex : path) {                	
                	optimalPath.add(vertex);
                	if(passNodes.contains(vertex)){
                		passNodesMark.add(node); // �������ĵ㱣������
                		passNodes.remove(node);	// ɾ�������ĵ�
                	}
                }                
                optimalPath.remove(node);
        	}           
            
            //passNodes.remove(node);
            it = passNodes.iterator();
            distance = dijkstra.execute(node); 
        }
        
        // passNodes���һ�����������յ�·��
        distance = dijkstra.execute(node); 
        path = dijkstra.getPath(nodes.get(87)); 
    	if(assertNotNull(path)){
            for (Vertex vertex : path) {                	
            	optimalPath.add(vertex);
            }
    	}
        
        // �������·��
        for (Vertex vertex : optimalPath) {
        	System.out.println(vertex);
        }     
        
        // ��·����Ȩ��

           
    }

	private static void addLane(String laneId, int sourceLocNo, int destLocNo,
    	      int duration) {
    	    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
    	    edges.add(lane);
    	  }

	private static boolean assertNotNull(LinkedList<Vertex> path) {
		// TODO Auto-generated method stub
		if(path == null)
			return false;
		else
			return true;
	}
}