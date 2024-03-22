package ie.atu.sw;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) throws Exception {
        Menu m = new Menu();
        int choice = m.menu();
        String filePath = null;
        String outputFile = "out.txt"; // Default output file name

        // Run methods based on user choice
        while (choice != 5) { // -5 ends program
        	// Choose file to get the words from
            if (choice == 1) {
                filePath = getFilePath();
            } else if (choice == 2) {
            	// If file with hasn't been chosen, go back to option 1
            	if (filePath == null) {
                    System.out.println("Please choose Option 1 to load the file first.");
                } else {
                	outputFile = getOutputFileName();
                }
            } else if (choice == 3) {
                if (filePath == null) {
                    System.out.println("Please choose Option 1 to load the file first.");
                } else {
                	// Parse words into our local map here
                    Map<String, double[]> wordEmbeddings = FileParser.readLineFromFile(filePath);
                    VectorDistance distance = new VectorDistance(wordEmbeddings);
                    searchForSimilarWord(distance, outputFile);
                }
            }
            choice = m.menu();
        }
        System.out.println("Program ended.");
    }

    // METHODS
    
    /*
     * This method simply gets the name of the file with all of the words and embeddings.
     * It also does some input validation (as to wether the file exists or not).
     * It returns the file path.
     */
    private static String getFilePath() {
        Scanner scanner = new Scanner(System.in);
        String filePath;
        while (true) {
            System.out.print("Enter the file path: ");
            filePath = scanner.nextLine();
            File file = new File(filePath);
            if (file.exists()) {
                break; // Exit the loop
            } else {
                System.out.println("File does not exist. Please try again.");
            }
        }
        return filePath;
    }

    /*
     * This method gets the name of the output file, or sets it to the default name.
     */
    private static String getOutputFileName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the output file: ");
        String outputFile = scanner.nextLine().trim();
        if (outputFile.isEmpty()) {
            outputFile = "out.txt"; // Assign default name if user input is empty
        }
        return outputFile;
    }

    /*
     * This method actually searches for the word in the map and finds the distance.
     */
    private static void searchForSimilarWord(VectorDistance distance, String outputFile) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word or short text: ");
        String userWord = scanner.nextLine();
        distance.searchSimilarWord(userWord, outputFile);
    }
}
