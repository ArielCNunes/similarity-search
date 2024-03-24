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
		while (choice != 4) { // 4 ends program
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

					// Calculate all distances
					VectorDistance distance = new VectorDistance(wordEmbeddings);

					// Get the top 5 and output them to a file
					searchForSimilarWord(distance, outputFile);
				}
			}
			choice = m.menu();
		}
		System.out.println("Program ended.");
	}

	/**
	 * Gets the name of the file with all of the words and embeddings. It also does
	 * some input validation (as to wether the file exists or not).
	 * 
	 * Big-O Notation: O(n) -> This method searches for the file which means it will
	 * loop through all files in the directory until it finds it, worst case
	 * scenario, the file does not exists.
	 * 
	 * @param N/A
	 * 
	 * @return the file path.
	 */
	private static String getFilePath() {
		Scanner scanner = new Scanner(System.in);
		String filePath;
		while (true) {
			System.out.print("Enter the file path: ");
			filePath = scanner.nextLine();
			File file = new File(filePath);
			if (file.exists()) {
				System.out.println("Content from " + filePath + " loaded into the map.");
				break; // Exit the loop
			} else {
				System.out.println("Incorrect file path or file does not exist. Please try again.");
			}
		}
		return filePath;
	}

	/**
	 * This method gets the name of the output file, or sets it to the default name.
	 * 
	 * Big-0 Notation: O(1) -> This method simply checks if the string variable
	 * holding the name of the file is null, if it is then it assigns a value to it.
	 * No loops involved.
	 * 
	 * @param N/A
	 * 
	 * @return the name of the ouput file.
	 */
	private static String getOutputFileName() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the name of the output file: ");
		String outputFile = scanner.nextLine().trim(); // remove any spaces
		if (outputFile.isEmpty()) {
			outputFile = "out.txt"; // Assign default name if user input is empty
		}
		return outputFile;
	}

	/**
	 * This method actually searches for the word in the map and finds the distance.
	 * 
	 * Big-O Notation: This method only calls other methods, no calculations here.
	 * 
	 * @param distance - the distances stored in the map. outputFile - the name of
	 *                 the ouput file.
	 * 
	 * @return N/A
	 */
	private static void searchForSimilarWord(VectorDistance distance, String outputFile) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a word or short text: ");
		String userWord = scanner.nextLine();
		distance.searchSimilarWord(userWord, outputFile);
	}
}
