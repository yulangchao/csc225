/* A5Algorithms.java
   CSC 225 - Summer 2015
   Programming Assignment 5 - Image Algorithms


   B. Bird - 06/22/2015
*/ 

import java.awt.Color;
import java.util.*;

public class A5Algorithms{

	/* FloodFillDFS(v, viewer, fillColour)
	   Traverse the component the vertex v using DFS and set the colour 
	   of the pixels corresponding to all vertices encountered during the 
	   traversal to fillColour.
	   
	   To change the colour of a pixel at position (x,y) in the image to a 
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static Color current=new Color(200,200,200);
	public static Stack s=new Stack();
	
	public static void FloodFillDFS(PixelVertex v, ImageViewerA5 viewer, Color fillColour){
		/* Your code here */
		
		int x=v.getX();
		int y=v.getY();
	    viewer.setPixel(x,y,fillColour);
	    
		v.visit=1;
		PixelVertex[] list=v.getNeighbours();
	    for(int i=0;i<list.length;i++){
			if(list[i].visit==0){
//				viewer.setPixel(list[i].getX(),list[i].getY(),fillColour);
				FloodFillDFS(list[i],viewer,fillColour);
			}
		}
	}
	
	
	/* FloodFillBFS(v, viewer, fillColour)
	   Traverse the component the vertex v using BFS and set the colour 
	   of the pixels corresponding to all vertices encountered during the 
	   traversal to fillColour.
	   
	   To change the colour of a pixel at position (x,y) in the image to a 
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void FloodFillBFS(PixelVertex v, ImageViewerA5 viewer, Color fillColour){
		/* Your code here */
		int x=v.getX();
		int y=v.getY();
		Queue queue = new LinkedList();
		queue.add(v);
		v.visit=1;
		PixelVertex[] g=new PixelVertex[5000];
		int j=0;
		g[j]=v;
		j++;	
	   // viewer.setPixel(x,y,fillColour);
		while(!queue.isEmpty()) {
			PixelVertex k=(PixelVertex)queue.remove();
			PixelVertex[] list=k.getNeighbours();
	        for(int i=0;i<list.length;i++){
			    if(list[i].visit==0){
				  list[i].visit=1;
				  g[j]=list[i];
			      j++;
				  queue.add(list[i]);
			    }
		    }	
			
			
		}
		for(int i=0;i<j;i++){
			viewer.setPixel(g[i].getX(),g[i].getY(),fillColour);
		}
	}
	
	/* OutlineRegionDFS(v, viewer, outlineColour)
	   Traverse the component the vertex v using DFS and set the colour 
	   of the pixels corresponding to all vertices with degree less than 4
	   encountered during the traversal to outlineColour.
	   
	   To change the colour of a pixel at position (x,y) in the image to a 
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void OutlineRegionDFS(PixelVertex v, ImageViewerA5 viewer, Color outlineColour){
		/* Your code here */
        dfs(v);
        while(s.size()>0){
        	PixelVertex w=(PixelVertex)s.pop();
        	if(w.visit==1 && w.getDegree()<4){
        		viewer.setPixel(w.getX(),w.getY(),outlineColour);
        	}
        }
	}
	
	public static void dfs(PixelVertex v){
		int x=v.getX();
		int y=v.getY();
		v.visit=1;
		s.push(v);
		PixelVertex[] list=v.getNeighbours();
	    for(int i=0;i<list.length;i++){
			if(list[i].visit==0){
				
				dfs(list[i]);
			}
		}
	}
	
	/* OutlineRegionBFS(v, viewer, outlineColour)
	   Traverse the component the vertex v using BFS and set the colour 
	   of the pixels corresponding to all vertices with degree less than 4
	   encountered during the traversal to outlineColour.
	   
	   To change the colour of a pixel at position (x,y) in the image to a 
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void OutlineRegionBFS(PixelVertex v, ImageViewerA5 viewer, Color outlineColour){
		/* Your code here */
				int x=v.getX();
		int y=v.getY();
		Queue queue = new LinkedList();
		queue.add(v);
		v.visit=1;
		PixelVertex[] g=new PixelVertex[5000];
		int j=0;
		g[j]=v;
		j++;
	   // viewer.setPixel(x,y,fillColour);
		while(!queue.isEmpty()) {
			PixelVertex k=(PixelVertex)queue.remove();
			PixelVertex[] list=k.getNeighbours();
	        for(int i=0;i<list.length;i++){
			    if(list[i].visit==0){
				  list[i].visit=1;
				  g[j]=list[i];
			      j++;
				  queue.add(list[i]);
			    }
		    }	
			
			
		}
		for(int i=0;i<j;i++){
			if(g[i].getDegree()<4){
			viewer.setPixel(g[i].getX(),g[i].getY(),outlineColour);
			}
		}
	}
	

	/* CountComponents(G)
	   Count the number of connected components in the provided PixelGraph 
	   object.
	*/
	public static int CountComponents(PixelGraph G){
		/* Your code here */
		Stack store=new Stack();
		int k=0;
		for(int x=0;x<G.getWidth();x++){
			for(int y=0;y<G.getHeight();y++){
				G.getPixelVertex(x,y).visit=0;
			}
		}
		for(int x=0;x<G.getWidth();x++){
			for(int y=0;y<G.getHeight();y++){
				PixelVertex v=G.getPixelVertex(x,y);
				if(v.visit==0){
					rdfs(G,store,v);
					k++;
				}
			}
		}
		
		
		return k;
	}
	
	public static void rdfs(PixelGraph G, Stack store, PixelVertex v){
		v.visit=1;
		PixelVertex[] list=v.getNeighbours();
	    for(int i=0;i<list.length;i++){
			if(list[i].visit==0){
				
				rdfs(G,store,list[i]);
			}
		}
		
		
		
		
		
		
	}
	/* A5Bonus(G, v, viewer, selectedColour)
	   [optional; up to 5 bonus marks available]
	   Given a PixelGraph G, a PixelVertex v (which has been selected by the 
	   user), an ImageViewerA5 instance and the currently selected colour, 
	   perform some kind of interesting graph-based image manipulation.
	   If you have an idea for an interesting bonus feature, contact the
	   instructor before the due date to determine how many bonus marks
	   your algorithm would receive.
	   
	   To change the colour of a pixel at position (x,y) in the image to a 
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void A5Bonus(PixelGraph G, PixelVertex v, ImageViewerA5 viewer, Color selectedColour){
		/* Your code here */
        dfs(v);
        while(s.size()>0){
        	PixelVertex w=(PixelVertex)s.pop();
        	if(w.visit==1 && w.getX()<(G.getWidth()/2)){
        		viewer.setPixel(w.getX(),w.getY(),selectedColour);
        	}
        	if(w.visit==1 && w.getX()>=(G.getWidth()/2)){
        		viewer.setPixel(w.getX(),w.getY(),current);
        	}
        	
        }
             

	}
	
	
}