/* PixelGraph.java
   CSC 225 - Summer 2015
   Programming Assignment 4 - Pixel Graph Data Structure

   B. Bird - 04/08/2015
*/ 

import java.awt.Color;

public class PixelGraph{

	
	 
	/* PixelGraph constructor
	   Given a 2d array of colour values (where element [x][y] is the colour 
	   of the pixel at position (x,y) in the image), initialize the data
	   structure to contain the pixel graph of the image. 
	*/
	private int width;
	private int height;
	private Color[][] imagePixels;
	private PixelVertex[][] vertex;

	
	public PixelGraph(Color[][] imagePixels){
		/* Your code here */
		this.width=imagePixels.length;
		this.height=imagePixels[0].length;
		this.imagePixels=imagePixels;
		vertex=new PixelVertex[this.width][this.height];
		
	for(int x=0;x<this.width;x++){
      for(int y=0;y<this.height;y++){	
        PixelVertex a=new PixelVertex(x,y);	  
		vertex[x][y]=a;
		if(y>0 && (imagePixels[x][y].equals(imagePixels[x][y-1]))){
			a.addNeighbour(vertex[x][y-1]);	
			vertex[x][y-1].addNeighbour(a);	
		}
		if(x>0 && (imagePixels[x][y].equals(imagePixels[x-1][y]))){
			a.addNeighbour(vertex[x-1][y]);		
			vertex[x-1][y].addNeighbour(a);				
		}
	}
	}
		
	}
	
	/* getPixelVertex(x,y)
	   Given an (x,y) coordinate pair, return the PixelVertex object 
	   corresponding to the pixel at the provided coordinates.
	   This method is not required to perform any error checking (and you may
	   assume that the provided (x,y) pair is always a valid point in the 
	   image).
	*/
	public PixelVertex getPixelVertex(int x, int y){
		/* Your code here */
		
			
		return vertex[x][y];
	}
	
	/* getWidth()
	   Return the width of the image corresponding to this PixelGraph 
	   object.
	*/
	public int getWidth(){
		/* Your code here */
		return this.width;
	}
	
	/* getHeight()
	   Return the height of the image corresponding to this PixelGraph 
	   object.
	*/
	public int getHeight(){
		/* Your code here */
		return this.height;
	}
	
}