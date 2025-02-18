package Final;

import Sorts.*;
import java.util.*;
import java.io.*;

public class SortingUtilityMethods {

   // Fills in an array as "sorted" by storing integers 0..n
   // in the respective spots. We will use this, and pre-shuffle to
   // set up test cases for for sorting arrays.  
   public static void fillSorted(int[] data, int n){
      // Your code here
      for (int i = 0; i < n; i++) {
          data[i] = i;
      }
   }

   // Fills in an array as "reverse sorted" by storing integers 0..n
   // in the exact opposit spots. We will use this as a worst case test
   public static void fillRevSorted(int[] data, int n){
      // Your code here
      for (int i = 0; i < n; i++) {
          data[i] = n - i - 1;
      }
   }   
   
   // Shuffles an array of integers by performing n random swaps
   public static void shuffle(int[] data, int n) {
      Random r = new Random();
      int a, b;
      for(int i = 0; i < n; i++) {
         a = r.nextInt(n);
         b = r.nextInt(n);
         swap(data, n, a, b); 
      }
   }
   
   // Swaps the data at index a (idxA) and index b (idxB) in an array
   public static void swap(int[] data, int n, int idxA, int idxB) {
      // Your code here
      // Check if indexes are in bounds
      if (idxA < data.length && idxB < data.length && idxA >= 0 && idxB >= 0) {
          // Swaps idxA and idxB
          int tmp = data[idxA];
          data[idxA] = data[idxB];
          data[idxB] = tmp;
      }
      // Leave this call to log here!
      log(1, idxA, idxB);
   }
   
   // Logs compares and swaps to be visualized later. 
   // Ms. Lewellen will implement, do not provide your own copy
   public static void log(int i, int idxA, int idxB) {}   
   
   public static void main(String[] args) {
      int n = 10;
      int[] data = new int[n];
      
      fillSorted(data, n);
      System.out.println(Arrays.toString(data));

      shuffle(data, n);
      System.out.println(Arrays.toString(data));          
    
   }
   
}
