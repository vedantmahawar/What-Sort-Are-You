/* Vedant Mahawar
 * October 30th 2023
 * B Period ADS
 * Sorts a integer array using Quick Sort
 */
// Declares Package
package Final;

// Imports random library for pivot choosing
import Sorts.*;
import java.util.Random;

public class QuickSort {
    // A sort method that takes in an integer array and the array length and 
    // then recursively sorts the array using quick sort
    // Returns nothing 
    public static void sort(int[] data, int n) {
        // Checks if data is null and is so throws an error 
        if (data == null) {
            throw new NullPointerException("Cannot have null array");
        }
        // If data is not null then sorts array
        sortR(data, n, 0);
    }

    // Recursive sort method for quick sort
    // Takes in an integer array, length of the array, and the start index to 
    // sort
    // Returns nothing
    public static void sortR(int[] data, int n, int start) {

        // Base cases if array is 1 or 0 indexs long (as array is already 
        // sorted)
        if (n == 0 || n == 1) {
            return;
        }
        // Base case if array is 2 indexs long 
        if (n == 2) {
            // If the two indexes our out of order, swap them
            if (data[start] > data[start + 1]) {
                SortingUtilityMethods.swap(data, n, start, start + 1);
            }
            // Once/if it is sorted return
            return;
        }
        // Picks a random pivot 
        Random rnd = new Random();
        int pivotIdx = rnd.nextInt(n) + start;
        // Partitions the elements and recives pivot Idx
        pivotIdx = partition(data, n, start, pivotIdx);
        //Sorts the elements before the pivot
        sortR(data, pivotIdx - start, start);
        //Sorts the elements after the pivot 
        sortR(data, n - pivotIdx + start, pivotIdx);
    }

    // Partitions the array by putting all the elements smaller than pivot to 
    // the left and all elements bigger to the right
    // Takes in an integer array (data), the length of the array, the start 
    // index, and the pivot index 
    // Returns pivot index
    public static int partition(int[] data, int n, int start, int pivotIdx) {
        // Defines pointers for partition 
        int rightIdx = start + n - 1;
        int leftIdx = start;
        // Makes the pivot the first element
        SortingUtilityMethods.swap(data, data.length, pivotIdx, 
                leftIdx);
        // Updates pivotIdx pointer after swap
        pivotIdx = leftIdx;
        // While loops that sorts the elements till the two left and right p
        // points collide 
        while (leftIdx < rightIdx) {
            // Iterates till finds an unsorted element from right to left 
            while (leftIdx < rightIdx && data[rightIdx] > data[leftIdx]) {
                rightIdx--;
            }
            // Swaps the unsorted element and pivot 
            if (leftIdx < rightIdx) {
                SortingUtilityMethods.swap(data, data.length, pivotIdx,
                        rightIdx);
                // Updates pointers
                leftIdx++;
                pivotIdx = rightIdx;
            }
            // Iterates till finds an unsorted element from left to right
            while (leftIdx < rightIdx && data[leftIdx] < data[rightIdx]) {
                leftIdx++;
            }
            // Swaps the unsorted element and pivot 
            if (leftIdx < rightIdx) {
                SortingUtilityMethods.swap(data, data.length, pivotIdx, 
                        leftIdx);
                
                // Updates pointers
                rightIdx--;
                pivotIdx = leftIdx;
            }
        }
        // Returns pivotIdx
        return pivotIdx;
    }
}
