/*
Benjamin Hafer
Cs 4345
Spring 2017
Program 1
*/


import java.util.*;
import java.lang.*;
import java.io.*;

class MatrixMult {
   public static void main(String[] args) 
        throws java.io.FileNotFoundException{
         
         //initiate variables to hold our dimensions as well as the matrices
         int m =0;
         int n =0;
         int x = 0;
         int y = 0;
         double[][] matrixA = new double[m][n];
         double[][] matrixB = new double[x][y];
         double[][] matrixC = new double[m][y];
 
         //get the user file
         Scanner input = new Scanner (new File(args[0]));
         
         //go through the data to get values
         while(input.hasNextInt()){
            //get matrix dimensions
            m = input.nextInt();
            n = input.nextInt();
            x = input.nextInt();
            y = input.nextInt();
            
            //assign matrix dimensions
            matrixA = new double[m][n];
            matrixB = new double[x][y];
            
         //loop through the remaining numbers to fill the matrices
         //matrixA
         for(int i = 0; i < m; i ++){
            for(int j = 0; j < n; j ++){
               matrixA[i][j] = input.nextInt();
            }
         }
         //fill matrixB
         for(int i = 0; i < x; i ++){
            for(int j = 0; j < y; j ++){
               matrixB[i][j] = input.nextInt();
            }
         }
      }//close while loop
      
      //gonna time it, also checking if dimensions are compatible 
      long start1 = System.nanoTime();
      if(n == x){
         matrixC = multiply(m,n,x,y,matrixA,matrixB);
      }
      else{
         System.out.println("\nSorry but these matrices cannot be multiplied...");
         System.exit(0);
      }
      long stop1 = System.nanoTime();
      double time1 = stop1 - start1;
      
       
      System.out.println("Matrix A");
      printMatrix(m,n,matrixA);
      System.out.println("\nMatrix B");
      printMatrix(x,y,matrixB);
      System.out.println("\nMatrix C solved without multithreading took "+ time1);
      printMatrix(m,y,matrixC);
      
      //create an array of threadds the size of the number of cells in the matrix
      Thread[] thrd = new Thread[m*y];
      double[][] matrixD = new double[m][y];
      int tc = 0;
      
      if(n == x){
      try{
         long start2 = System.nanoTime();
         //looping through the cells of the matrix to calculate the values of matrix D (matrix C)
         for(int i = 0; i < m; i ++){
            for(int j =0; j < y; j++){
               //call the Matrix helper which then calls the run method. 
               thrd[tc] = new Thread(new MatrixHelper(i,j,matrixA,matrixB,matrixD));
               thrd[tc].start();
               
               tc++;
            } 
         }
         //seemed to get better times if use a seperate loop to join the threads together
         for(int i = tc-1; i > -1; i--){
            thrd[i].join();
         }
         long stop2 = System.nanoTime();
         double time2 = stop2 - start2;
        

         System.out.println("\nHere is the matrix solved by multithreading took " + time2+" \n");
         printMatrix(m,y,matrixD);
      }
      catch(InterruptedException io){}
   }
   else{
      System.out.println("\n sorry could not multiply these....");
      System.exit(0);
   }
    }//close main
    
    //here is the non threaded multiply function, wanted to see time comparisons of the the two
    private static double[][] multiply(int m, int n, int x, int y, double[][] a, double[][] b){
      double[][] matrixC = new double[m][y];
      
      for(int i = 0; i < m; i ++){
         for(int j = 0; j < y; j++){
            for(int k =0; k < n; k++){
               matrixC[i][j] += a[i][k] * b[k][j];
            }
         }
      }
      return matrixC;
    }
    private static void printMatrix(int a, int b, double[][] m){
      for(int i = 0; i < a; i ++){
         for(int j = 0; j < b; j ++){
            System.out.print(m[i][j] + " ");
         }
         System.out.print("\n");
      }

    }

}//close class

class MatrixHelper implements Runnable {

   private int rows;
   private int cols;
   private double mA[][];
   private double mB[][];
   private double mC[][];
   
   public MatrixHelper(int r, int c, double a[][], double b[][], double product[][]){
      this.rows = r;
      this.cols = c;
      this.mA = a;
      this.mB = b;
      this.mC = product;
   }
   @Override
   public void run(){
      //loops across the the row of matrix A and down the column of matrix B to get one cell
      System.out.println("Thread " + rows + " " + cols + " starts calculation.");
      for(int i = 0; i < mB.length; i++){
         mC[rows][cols] +=mA[rows][i] * mB[i][cols];   
      }
      System.out.println("thread " + rows + ", " + cols + " returns " + mC[rows][cols]);
  }
}//close Matrix helper class//






