import java.util.*;
import java.awt.Color;

public class PixelVertex{

    private   int x;
    private   int y;
    private   ArrayList adj;
	public   int degree;
	/* Add a constructor here (if necessary) */
	public PixelVertex(int x,int y ){
        		this.x=x;
        		this.y=y;
        		this.adj=new ArrayList();
				this.degree=0;
    }

	/* getX()
	   Return the x-coordinate of the pixel corresponding to this vertex.
	*/
	public int getX(){
		/* Your code here */
		return this.x;
	}

	/* getY()
	   Return the y-coordinate of the pixel corresponding to this vertex.
	*/
	public int getY(){
		/* Your code here */
		return this.y;
	}

	/* getNeighbours()
	   Return an array containing references to all neighbours of this vertex.
	*/
	public PixelVertex[] getNeighbours(){
		/* Your code here */
       PixelVertex[] a=new PixelVertex[100];
	   for(int i=0;i<this.degree;i++){
		   a[i]=(PixelVertex)this.adj.get(i);
	   }
      return a;
	}

	/* addNeighbour(newNeighbour)
	   Add the provided vertex as a neighbour of this vertex.
	*/
	public void addNeighbour(PixelVertex newNeighbour){
		/* Your code here */
	
		this.adj.add(newNeighbour);
		newNeighbour.adj.add(this);
	    this.degree++;
	}

	/* removeNeighbour(neighbour)
	   If the provided vertex object is a neighbour of this vertex,
	   remove it from the list of neighbours.
	*/
	public void removeNeighbour(PixelVertex neighbour){
		/* Your code here */

        
         this.adj.remove(neighbour);
		 neighbour.adj.remove(this);
		 this.degree--;
	}

	/* getDegree()
	   Return the degree of this vertex.
	*/
	public int getDegree(){
		/* Your code here */
		return this.degree;
	}


	/* isNeighbour(otherVertex)
	   Return true if the provided PixelVertex object is a neighbour of this
	   vertex and false otherwise.
	*/
	public boolean isNeighbour(PixelVertex otherVertex){
		/* Your code here */
	     return this.adj.contains(otherVertex);
	}

}
