/* PairUp.java
   CSC 225 - Summer 2015
   Programming Assignment 1 - Template for PairUp

   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java PairUp

   To conveniently test the algorithm with a large input, create a
   text file containing space-separated integer values and run the program with
	java PairUp file.txt
   where file.txt is replaced by the name of the text file.

   B. Bird - 03/09/2015
*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;

//Do not change the name of the PairUp class
public class PairUp{
	/* PairUp225(A)
		The input array A will contain non-negative integers. If
		the array A can be completely divided into pairs (x,y) where
		  x + y = 225,
		then the function will return true. Otherwise, the function
		will return false.

		The function is permitted to modify the input array A.

		Do not change the function signature (name/parameters).
	*/
	public static boolean PairUp225(int[] A){
	    int n = A.length;

        //check if n is odd
        if(n/2!=(n+1)/2){
			return false;
		}



	    // check if any number >225
        for(int i=0;i<n;i++){
           if(A[i]>225){
		      return false;
		   }
        }


        // new array which is 225-A[i]
        int[] b=new int[n];
        for(int i=0;i<n;i++){
			b[i]=225-A[i];
		}


        //count times of numbers in array
        int[] num=new int[226];
        int[] num1=new int[226];
        for(int i=0;i<226;i++){
			for(int j=0;j<n;j++){
			if(b[j]==i){
			num[i]++;
		    }
		    if(A[j]==i){
			num1[i]++;
		    }
		}
		}


        //check the times,same true,or false
        for(int k=0;k<226;k++){
			if(num1[k]!=num[k]){
				return false;
			}
		}
		return true;
	}

	/* main()
	   Contains code to test the PairUp225 function. Nothing in this function
	   will be marked. You are free to change the provided code to test your
	   implementation, but only the contents of the PairUp225() function above
	   will be considered during marking.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);

		int[] array = new int[inputVector.size()];

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.currentTimeMillis();

		boolean canPairUp = PairUp225(array);

		long endTime = System.currentTimeMillis();

		double totalTimeSeconds = (endTime-startTime)/1000.0;

		System.out.printf("Array values %s be paired up.\n",canPairUp? "can":"cannot");
		System.out.printf("Total Time (seconds): %.2f\n",totalTimeSeconds);
	}
}
