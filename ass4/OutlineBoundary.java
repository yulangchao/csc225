/* OutlineBoundary.java
   CSC 225 - Summer 2015
   Programming Assignment 4 - Container for OutlineBoundary method


   B. Bird - 04/08/2015
*/ 

import java.awt.Color;

public class OutlineBoundary{
	/* OutlineBoundary(G, viewer, outlineColour)
	   Find all vertices of G which have degree less than 4 and change the 
	   colour of the corresponding pixel to outlineColour.
	   
	   To change the colour of a pixel at position (x,y) in the image to a 
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void OutlineBoundary(PixelGraph G, ImageViewerA4 viewer, Color outlineColour){
		/* Your code here */
		for(int x=0;x<G.getWidth();x++){
			for(int y=0;y<G.getHeight();y++){
				
		      if(G.getPixelVertex(x,y).getDegree()<4){
				  viewer.setPixel(x,y,outlineColour);
			  }
				  
		    }
		}
	}
}