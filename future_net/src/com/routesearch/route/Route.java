/**
 * 实现代码文件
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
     * 你需要完成功能的入口
     * 
     * @author XXX
     * @since 2016-3-4
     * @version V1
     */
	private static List<Vertex> nodes;
	private static List<Edge> edges;
	static int vlen = 660; // 顶点个数
	static int elen ; // 边个数
    static BufferedReader br;
    static String line; 
    private static Set<Vertex> passNodes = new HashSet<Vertex>(); // 必须经过的点集      
    private static Set<Vertex> passNodesMark = new HashSet<Vertex>(); // 将passNodes点集中已经经过的点做个标记
    private static LinkedList<Vertex> optimalPath;
    private static Map<Vertex, Integer> distance;
    private static Vertex node;
    static int total;
    private static String resultStr;
    private static Vertex firstNode;
    private static Vertex lastNode;
    private static Vertex tempNode; 
	  
    public static String searchRoute(String graphContent, String condition)
    {   	
    	resultStr = Route.testExcute(graphContent, condition);
    	return resultStr;
    }    
    
    public static String testExcute(String graphContent, String condition) {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        passNodes = new HashSet<Vertex>();
        optimalPath = new LinkedList<Vertex>();
        distance = new HashMap<Vertex, Integer>();  
        
        // 初始化顶点
        for (int i = 0; i < vlen; i++) {
          Vertex location = new Vertex(Integer.toString(i), Integer.toString(i));
          nodes.add(location);
        }

        // 初始化边
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
        
        // 初始化必须经过的顶点
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
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph); // 导入图信息
        distance = dijkstra.execute(firstNode, passNodesMark); // 给出源点
        LinkedList<Vertex> path = null;
        
        // 分段求最短路径    
        Iterator<Vertex> it = passNodes.iterator();
        while(it.hasNext()){
        	// 在passNodes中寻找最短路径顶点    
            node = it.next();
            for(Vertex vertex:passNodes){
            	if(distance.get(node) > distance.get(vertex))
            		node = vertex;	
            }
            
            path = dijkstra.getPath(node); // 根据终点输出最短路径 
        	if(assertNotNull(path)){
        		//path.remove(nodes.get(0));
                for (Vertex vertex : path) {                	
                	optimalPath.add(vertex);
                	if(passNodes.contains(vertex)){
                		passNodesMark.add(node); // 将经过的点保存下来
                		passNodes.remove(node);	// 删除经过的点
                	}
                }                
                optimalPath.remove(node);
        	}           
            
            it = passNodes.iterator();
            distance = dijkstra.execute(node, passNodesMark); 
        }
        
        // passNodes最后一个顶点连接终点路径
        distance = dijkstra.execute(node, passNodesMark); 
        path = dijkstra.getPath(lastNode); 
    	if(assertNotNull(path)){
            for (Vertex vertex : path) {                	
            	optimalPath.add(vertex);
            }
    	}
        
    	optimalPath.removeFirst(); // 去除第一个点
    	String resultStr = "";
        for (Vertex vertex : optimalPath) {
        	resultStr = resultStr + vertex + "|";        	
        }     
        System.out.println(resultStr.substring(0,resultStr.length()-1));
        // 输出总权重
    	outputTotalCost();
    	// 输出最优路径
    	if(optimalPath == null){
    		return "NA";
    	}
    	else
    	return outputOptPath();
    	
           
    }

	private static void addLane(String laneId, int sourceLocNo, int destLocNo,
    	      int duration) {
    	    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
    	    edges.add(lane);
    	  }
 
	/**
	 * 功能：输出总权重
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
	 * 功能：输出最优路径
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
	 * 功能：取得两顶点间边的ID
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
	 * 功能：取得边对应的权重
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
	
	private static boolean assertNotNull(LinkedList<Vertex> path) {
		// TODO Auto-generated method stub
		if(path == null)
			return false;
		else
			return true;
	}
}