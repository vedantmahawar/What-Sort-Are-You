/*
 * Vedant Mahawar
 * November 15th 2023
 * B Period ADS
 * Guesses what sorting algorithm (or as close can be) a user is using to sort 
 * an integer array
 */

// Declares package
package Final;

// Imports random library to create randomized number array
import java.util.Random;
// Imports decimal format library to format probabilities to 2 decimal places
import java.text.DecimalFormat;
// Imports array library to print arrays
import java.util.Arrays;
// Imports scanner library to get user input
import java.util.Scanner;

public class WhatSortAreYou {

    public static void main(String[] args) {    
        // Gets array length based on user input
        int arrayLength = getUserArrayLength();
        // Gives user instructions
        giveUserInstructions();
        // Generates a randomized integer array for user to sort 
        // Minimum possible value to be generated is 0, this prevents negative 
        // numbers as they can be confusing to sort for the user as easily mixed
        // up with their respective positive versions
        // Maximum possible value to be generated is 15, I have found in my 
        // testing that this is a good number that is not too big for the user 
        // so it is not overwhelming, but also far enough away from 0 to prevent
        // too many duplicates
        int[] randomizedArray = generateRandomNumberArray(0, 15,
                arrayLength);
        // Gets the various iterations of the array the user has sorted and 
        // stores it in a 2-d userData array
        int[][] userData = getUserInput(randomizedArray);
        
        // If the user enters the same numbers that were provided
        if (matchingNumbers(randomizedArray, userData)) {
            // Finds what elements the user has swapped during their iterations
            // and stores the indexes in pairs (one in each column) in a 2-d
            // int array
            int[][] swappedElements = findSwappedElements(userData);
            // Calculates probabilities that a user is using the different
            // sorting algorithms and store in a 1-d double array
            double[] probabilities = calculateProbabilties(userData,
                    swappedElements, 0, 0 , 
                    0 , 0); 
            // Calculates how many times the user has to sort various arrays
            // Calculated so that the array length user provided multiplied by 
            // how many times they have to sort an array is equivalent too 
            // 20, this is because in testing I found the "sweet-spot" between 
            // accuracy and ease of sorting for user to be sorting a 5 length
            // array 4 times, 5*4 equals 20 and I want to try to best mirror
            // this ratio but still give the user options for how long of an 
            // array they want to sort, as some users might prefer longer arrays
            // but less sorts, and some might prefer vice versa
            int sortTimes = (20/arrayLength);
                     
            // Provides sortTimes more unsorted arrays and calculates respective
            // probabilties, compounding probability each time to include the
            // previously calculated probabilties
            // This ensures higher level of accuracy for program to guess
            // The first time I ask for userInput and calculate probabilities 
            // is not in the for loop as the intial value passed in for the 
            // probabilities is 0, then I start compounding probabilities within 
            // the loop
            for (int i = 0; i < sortTimes - 1; i++) {
                // Generates new randomized array
                randomizedArray = generateRandomNumberArray(0, 15,
                        arrayLength);
                // Gets the various iterations of the array the user has sorted
                userData = getUserInput(randomizedArray);
                
                // If the user enters the same numbers that were provided
                if (matchingNumbers(randomizedArray, userData)) {
                    // Finds what elements the user has swapped during their 
                    // iterations
                    swappedElements = findSwappedElements(userData);
                    // Calculates probabilities that a user is using the 
                    // different sorting algorithms
                    // Passes through the previously calculated probabilities
                    // to compound probabilities 
                    probabilities = calculateProbabilties(userData,
                            swappedElements, probabilities[0], probabilities[1],
                            probabilities[2], probabilities[3]);
                // If user doesn't input matching numbers 
                } else {
                    // Throws an error
                    throw new IllegalArgumentException("The numbers you input"
                            + "ted don't match the original numbers.");
                }
            }
            // Prints all the final probabilities in a user-friendly format
            printProbabilities(probabilities);
        // If user doesn't input matching numbers (this else is from the first 
        // if statement)
        } else {
            // Throws an error
            throw new IllegalArgumentException("The numbers you input"
                    + "ted don't match the original numbers.");
        }
    }

    // Method that asks the user how long the arrays they are going to sort
    // should be
    // Explains to them the tradeoffs of different lengths and effect of the 
    // arrayLength on preciseness of the guess
    // No parameters and returns the arrayLength
    // Returns the arrayLength the user chose
    public static int getUserArrayLength() {      
        // Print statements explaining arrayLength and trade offs
        System.out.println("""
                        USER INSTRUCTIONS PART 1:
                           
                        How long do you want the array you are sorting to be?
                        
                        This will have an effect how many times you have to 
                        sort an array for you results to be published.
                        
                        The recommended length is five. This means you will have
                        to sort four arrays (To calculate how many arrays you
                        will have to sort based on length do 20/arrayLength).
                          """);
        
        // Creates scanner object to get user input
        Scanner arrayLengthScanner = new Scanner(System.in);
        // Intializes input variable to take in user input
        int input = 0;
        // Try catch to make sure user only inputs numbers
        try {
            // Assigns user input to input variable
            input = arrayLengthScanner.nextInt();  
            // Keeps asking for input till valid input is given
            while (input <= 1) {         
                System.out.println("Array length cannot be less than 2.");
                input = arrayLengthScanner.nextInt();           
            }
        // If they don't input just numbers
        } catch (Exception e) {
            // Throw an error
            throw new IllegalArgumentException("Looks like you didn't input a"
                    + " valid number.");
        }
        
        // Returns arrayLength user has provided
        return input;
    }
    
     // Gives user instructions for program
    public static void giveUserInstructions() {
        System.out.println("""
                        USER INSTRUCTIONS PART 2:

                        You will be given unsorted arrays (of your chosen 
                        length) and you must sort them by only swapping two 
                        elements at a time. Once you have thought of a swap to 
                        make enter the elements with a space between each 
                        element.
                        
                        Example:
                        Original Array:    [1, 7, 4]   
                        You Want To Swap:  7 & 4
                        You Would Input:   1 4 7
                           
                        Once all the arrays are sorted the probabilities that 
                        your algorithm matches Bogo, Bubble, Insertion, or 
                        Selection Sort will be published.
                        
                        Enjoy!
                          """);
    }

    // Method that generates a randomized array with numbers between two
    // specificed values and of a specified length
    // Returns the random number array
    // Takes in min and max value (exclusive) for random numbers and array  
    // length as parameters 
    public static int[] generateRandomNumberArray(int minValue, int maxValue, 
            int arrayLength) {
        // Creates array of user specified length
        int[] data = new int[arrayLength];
        
        // Checks if min/max Values and if arrayLength are valid inputs else
        // throws an error 
        if (minValue > maxValue || minValue < 0 || maxValue < 0) {
            throw new IllegalArgumentException("Invalid min and max values");
        } else if (arrayLength < 1) {
            throw new IllegalArgumentException("Invalid array length");
        // If all parameters are valid
        } else {
            // Creates random object to pick random numbers for array
            Random r = new Random();
            // Creates randomized array till the array doesn't appear sorted
            // This is so the user doesn't accidentally get a sorted array
            // Uses a do-while because array is intialized with all 0 values
            // which is technically sorted so fills array with random values
            // then checks condition
            do {
                // For loop that adds a random value to each index of array
                for (int i = 0; i < arrayLength; i++) {
                    // Picks a value between the minimum and maximum values
                    data[i] = r.nextInt(maxValue - minValue + 1) + minValue;
                }
            } while (isSorted(data, data.length));
        }
        
        // Returns randomized number array 
        return data;
    }

    // A method that checks if an array is sorted (least -> greatest)
    // Takes in an integer array and length of array as parameters
    // Returns a boolean value depening if array is sorted
    public static boolean isSorted(int[] data, int arrayLength) {
        // Loops through the array till it checks all elements
        for (int i = 0; i < arrayLength - 1; i++) {
            // Checks if the value of a data point is greater than the next
            // If so the array is unsorted and returns false
            if (data[i] > data[i + 1]) {
                return false;
            }
        }
        // If it passes the if statment (checking if it is sorted) return true
        return true;
    }
    
    // Checks if the user input has the same values for each element as the 
    // intial provided array
    // Takes in intial unsortedArray and user data as parameters
    // Returns if the user provided numbers matching what they were given
    public static boolean matchingNumbers(int[] unsortedArray, 
            int[][] userData) {    
        // Defines variable to track if user inputs same numbers as provided
        // Defaults to true 
        boolean matchingNumbers = true;
        // Defines int array to track the user data row by row to check for 
        // valid input
        int[] userDataSingleRow = new int[userData[0].length];
        
        // Checks for valid input for every row of userData
        for (int i = 0; i < userData.length; i++) {
            // Transfers data from current row of userData to single row array
            for (int j = 0; j < userData[0].length; j++) {
                userDataSingleRow[j] = userData[i][j];
            }
            // Sorts both arrays using quickSort
            QuickSort.sort(unsortedArray, unsortedArray.length);
            QuickSort.sort(userDataSingleRow, userDataSingleRow.length);
            // Uses Arrays.equals to compare that both arrays have the same
            // length, elements, and order of elements
            if (!Arrays.equals(unsortedArray, userDataSingleRow)) {
                // If arrays don't match up (meaning the user inputted different 
                // numbers than provied set matchingNumbers to false
                matchingNumbers = false;
            }  
        }
        // Return if user input is valid or not
        return matchingNumbers;
    }
    
    // A method that gets the user input 
    // Takes in the unsorted array as a parameters
    // Returns the 2-d array with all the user input for later processing
    public static int[][] getUserInput(int[] unsortedArray) {
        // Print statements asking for input and providing the unsorted array
        System.out.println("Here is a unsorted array:");
        System.out.println(Arrays.toString(unsortedArray));
        // This quits the program as any non-number is also just not a valid 
        // input (killing two birds with one stone)
        System.out.println("To quit, press any non-number key.");
        
        // Calculates array length to be used later
        int arrayLength = unsortedArray.length;
        // Creates scanner object to take in user input 
        Scanner userInputScanner = new Scanner(System.in);
        // Initializes arrays to store user input
        // 2-d integer array to store all the data with a max height of 1000
        int[][] maxUserInput = new int[1000][arrayLength];
        // Creates an integer "userChanges" to track how many times user has 
        // changed the array
        // Will be used later for the final array
        int userChanges = 0;
        // Sets the first row of the 2-d int array to be the intial unsorted
        // array
        // Necessary as other functions compare what changes the user made
        // to the array, meaning they need the intial data
        maxUserInput[0] = unsortedArray;
        
        // Uses a do-while loop (till the user enters a sorted array) to keep 
        // asking for input
        // A do-while is necessary as the array storing the userInput initalizes 
        // with all values of 0 meaning the array is technically sorted so the 
        // program will not ask for input. With a do-while this issue does not 
        // arise as you take in the user input that check the isSorted condition
        do {
            // Gets input from user
            for (int i = 0; i < arrayLength; i++) {
                // Try catch to ensure user input is valid
                try {
                    // Gets user input
                    int input = userInputScanner.nextInt();
                    // Transfers user input to maxUserInput array 
                    maxUserInput[userChanges + 1][i] = input;
                } catch (Exception e) {
                    throw new IllegalArgumentException("Looks like your not "
                            + "inputting an integer");
                }                         
            }
            // Increments userChanges as the user has inputed one swap
            userChanges++;
        // While condition that checks if array is sorted 
        } while (!isSorted(maxUserInput[userChanges], arrayLength));
        
        // Once user input is done, create new final array of correct size
        int[][] userData = new int[userChanges + 1][arrayLength];
        // Copy data from original big array (maxUserInput) to correctly sized 
        // array, first for loop traverses the different rows
        for (int rowIdx = 0; rowIdx < userChanges + 1; rowIdx++) {
            // Inside for loop that traverses the columns
            for (int columnIdx = 0; columnIdx < arrayLength; columnIdx++) {
                userData[rowIdx][columnIdx] = maxUserInput[rowIdx][columnIdx];
            }
        }
        
        // Defines variable to see how many elements user has made
        int userSwaps = 0;
        // Checks if the user didn't make any swaps, by checking if all the 
        // elements from one iteration are the same as the next iteration
        // Loops through all the rows
        for (int rowIdx2 = 0; rowIdx2 < userData.length - 1; rowIdx2++) {
            // Loops through each column for all the rows
            for (int columnIdx2 = 0; columnIdx2 < userData[0].length; 
                    columnIdx2++) {
                // Compares current index to next provided iteration
                if (userData[rowIdx2][columnIdx2] != 
                        userData[rowIdx2 + 1][columnIdx2]) {
                    // If the values of the index don't match then increase swap
                    // counter
                    userSwaps++;
                }
            }
        }
        
        // Recalculates userSwaps to include the true number of user swaps
        // before user swaps double counted each swap, because two indexes are 
        // different each time (because you swap two things)
        userSwaps = userSwaps/2;
        // If userSwaps is not equal to the userData length + 1 than throw an 
        // error
        // +1 because there should be one less swap than the iterations the user
        // provided of the array
        // This is because a swap is "in between" two iterations of the array
        // Kind of how if you cut a rope three times, you have three cuts (or 
        // swaps) but you actually have four parts of the rope (or rows)
        // If the userSwaps and the length of userData are not equal that means
        // at some point the user didn't make a swap (meaning they just entered
        // the same array they were given)
        if (!(userSwaps + 1 == userData.length)) {
            throw new IllegalArgumentException("You didn't swap elements "
                    + "at one point in the array");
        }
        // Returns 2-d array containing all user data
        return userData; 
    }

    // A method that calculates the elements that were swapped by the user by
    // comparing the row of the userData array with the row under it 
    // Takes in user data and returns a 2-d array containing the indexes of the
    // swapped elements as pairs in each row
    // Returns an array contating all the indexes of the swapped elements
    public static int[][] findSwappedElements(int[][] userData) {
        // Calculates array height and width 
        int userDataHeight = userData.length;
        int userDataWidth = userData[0].length;
        // Creates an array that stores the indexes of the swapped elements
        // Has one less row than userData has as a swap can be considered "in-
        // between" two rows, meaning you need one less than the height
        // Has two columns for the two indexes that are swapped
        int[][] swappedElements = new int[userDataHeight - 1][2];
        
        // For loop that loops through each row of the array comparing to the 
        // row after it
        // Stop parameter has a minus one as there is no row under the last row
        for (int rowIdx = 0; rowIdx < userDataHeight - 1; rowIdx++) {
            // Defines variable to track current column that is being compared 
            // with row under it
            int columnIdx = 0;
            // While loop that iterates till mismatch between rows is found, 
            // essentially finding the "swapped" element
            while (columnIdx < userDataWidth && userData[rowIdx + 1][columnIdx] 
                    == userData[rowIdx][columnIdx]) {
                columnIdx++;
            }
            // Sets the index of the first swapped element to the correct row 
            // and the first column in the swapped elements array
            swappedElements[rowIdx][0] = columnIdx;
            // Defines variable to track current column that is being compared
            // with row under it but to find the second index that was swapped
            int secondColumnIdx = columnIdx + 1;
            // While loop that iterates till mismatch between rows is found, 
            // essentially finding the "swapped" element
            while (secondColumnIdx < userDataWidth && userData[rowIdx + 1]
                    [secondColumnIdx] == userData[rowIdx][secondColumnIdx]) {
                secondColumnIdx++;
            }
            // Sets the index of the second swapped element to the correct row 
            // and the second column in the swapped elements array
            swappedElements[rowIdx][1] = secondColumnIdx;
        }
        // Returns 2-d array with all the swapped elements with the columns 
        // being the swapped elements and the rows being the various pairs
        // of swapped elements
        return swappedElements;
    }

    // Calculates the probability that the sorting algorithm the user is using 
    // matches Bogo, Bubble, Insertion, or Selection Sort
    // Takes in userData and swappedElement arrays as parameters
    // Also takes in probabilities that all four sorts are matching to the 
    // user algorithm as parameters, this allows for compounding of probabilties
    // as the user sorts multiple arrays
    // Returns an array that stores all the probabilities of the various sorts
    public static double[] calculateProbabilties(int[][] userData, 
            int[][] swappedElements, double bogoProbability, 
            double bubbleProbability, double insertionProbability, 
            double selectionProbability) {

        // Array that stores all the probabilities for each sorting algorithm
        // matching with user algorithm
        double[] probabilities = new double[4];       
        // Calculates userData array height and width 
        // Used to calculate probabilities for each sorting algorithm
        int userDataArrayHeight = userData.length;
        int userDataArrayWidth = userData[0].length;       
        
        // Defines variable to track index of element with biggest values
        // Used to determine selection sort probability
        int maxIndex = 0;        
        // Defines variable to track distance between two swapped elements
        int swappedElementsDistance = 0;
        // Defines variables that track value of the swapped elements
        // First swapped element (was originally after the other element, 
        // got moved forward)
        int firstSwappedElement= 0;
        // Value of the second swapped element (was originally before the other 
        // element, got moved backward)
        int secondSwappedElement = 0;
        // Boolean that tracks if a matching sorting algorithm has been 
        // found for the current iteration, if not then bogoSort probability
        // will be increased as no possible other matching sorts
        boolean matchingSortFound = false;
        
        // Used to check if a swapped element is being put into the correct spot
        int correctSpot = 0;
        // Array that will store which variables are in the correct spot 
        // in the original unsorted array
        // Used to calculate probability of insertion sort
        int[] correctSpots = new int[userDataArrayWidth];
        // Finds and transfers data from userData array to 
        // correctSpots array
        for (int i = 0; i < userDataArrayWidth; i++) {
            // If the data is in the incorrect spot at start of array
            // transfer it over to correct spot in correctSpots array
            if (userData[0][i] != userData[userDataArrayHeight - 1][i]) {
                correctSpots[i] = userData[userDataArrayHeight - 1][i];        
            } else {
                // If its in the correct spot set the value of the index at 
                // the correctSpots array to -1, as this value will never be
                // generated in the randomized array
                correctSpots[i] = -1;
            }
        }
        
        // For loop that iterates through each row and changes probabilities
        // that user algorithm is matching sorting algorithms
        for (int swapNumber = 0; swapNumber < userDataArrayHeight - 1; 
                swapNumber++) {
            // Updates distance between swapped elements
            swappedElementsDistance = swappedElements[swapNumber][1] - 
                    swappedElements[swapNumber][0];
            // Updates value of swapped elements
            firstSwappedElement = userData[swapNumber + 1]
                    [swappedElements[swapNumber][0]];      
            secondSwappedElement = userData[swapNumber + 1]
                    [swappedElements[swapNumber][1]];
            // Updates maxIndex, but make sure it can't go below 0 
            // because invalid index
            if ((userDataArrayWidth - 1 - swapNumber) >= 0) {
                maxIndex = userData[userDataArrayHeight - 1][userDataArrayWidth 
                        - 1 - swapNumber];
            }
   
            // Starts changing probabilities that a user algorithm is a matching
            // sorting algorithm based on various conditions
            
            // BubbleSort Probabilty Checks
            // If the swapped elements are next to each other 
            // (characteristic of bubbleSort), and they are swapping into the 
            // correct order, than increase probability of bubbleSort.
            if (swappedElementsDistance == 1 && firstSwappedElement < 
                    secondSwappedElement) {
                // Increase probability that user algorithm is bubbleSort
                bubbleProbability++;
                // A matching sort has been found, so BogoSort probability will
                // not increase
                matchingSortFound = true;
            }      
            
            // InsertionSort Probability Checks
            // Resets correctSpot variable (as it will be a different variable
            // from the previous iteration of the for loop)
            correctSpot = 0;
            // Tests if a swapped element is the element that should be swapped
            // next according to the logic that insertion sort follows if so
            // increase probability of insertionSort.           
            if (firstSwappedElement == correctSpots[correctSpot]) {
                // If the swapped element is in the correct spot then start 
                // looking for the next correct element 
                correctSpot++;
                // Increase probability that user algorithm is insertionSort
                insertionProbability++;
                // A matching sort has been found, so BogoSort probability will
                // not increase
                matchingSortFound = true;               
            }        
            
            // SelectionSort Probability Checks
            // If the second swapped element is the biggest swapped element 
            // which is what the user should swap if following selectionSort 
            // logic, then increase probability of selection sort        
            // Checks if the second swapped element is biggest element
            if (secondSwappedElement == maxIndex) {
                // Increase probability that user algorithm is selectionSort
                selectionProbability++;
                // A matching sort has been found, so BogoSort probability will
                // not increase
                matchingSortFound = true;               
            } 
            
            // BubbleSort Probability Checks
            // if no other sort has been found to be matching for that swap
            // increase bogoSort Probability
            if (matchingSortFound == false) {
                // Increase probability that user algorithm is bogoSort
                bogoProbability++;
            }
        }       
        
        // Updates probability array once all probabilities have been calculated
        probabilities[0] = bogoProbability;
        probabilities[1] = bubbleProbability;
        probabilities[2] = insertionProbability;
        probabilities[3] = selectionProbability; 
        // Returns probability array
        return probabilities;
    }
    
    // Prints all the probabilities in a user friendly format
    // Takes in the previously calculated probability array as a parameter
    public static void printProbabilities (double[] probabilities) {
        // Defines variable to hold the sum of all the probabilities
        // Used to calculate percentage probabilities
        double sum = 0;
        // Calculates sum of probabilities
        // Used to give a percentage probability 
        for (int i = 0; i < probabilities.length; i++) {
             sum = sum + probabilities[i];
        }
        // Creates object to format numbers to include 2 decimal places
        DecimalFormat decFormat = new DecimalFormat("####0.00");
        
        // Prints all probabilities to 2 decimal places in a percentage form
        // and does a quick explanation for what each sort is
        System.out.println("Probabilty That Your Algorithm Is One Of The "
                + "Following Algorithms:");
        // Prints bogo probability and explanation
        System.out.println("BogoSort: " + decFormat.format(
                (probabilities[0]/sum)*100) + "%");
        System.out.println("This sort is where you just randomly swap "
                + "elements.");
        System.out.println();
        
        // Prints bubble probability and explanation
        System.out.println("BubbleSort: " + decFormat.format(
                (probabilities[1]/sum)*100) + "%");
        System.out.println("This sort is where you swap elements with their "
                + "neighbor, ");
        System.out.println("one by one, essentially 'bubbling-up' elements");
        System.out.println();
        
        // Prints insertion probability and explanation
        System.out.println("InsertionSort: " + decFormat.format(
                (probabilities[2]/sum)*100) + "%");
        System.out.println("This sort is where you slowly build up a sorted"
                + " array by putting elements in their correct place.");
        System.out.println();
        
        // Prints selection probability and explanation
        System.out.println("SelectionSort: " + decFormat.format(
                (probabilities[3]/sum)*100) + "%"); 
        System.out.println("This sort is where you pick the biggest element "
                + "and move it too the end.");
        System.out.println();
        
        // Gives disclaimer about probability rounding
        System.out.println("***Probabilities may not add up to 100 (+- 0.01%) "
                + "due to format rounding***");
    }
} 
    
    