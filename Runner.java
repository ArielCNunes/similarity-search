package ie.atu.sw;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class
 */
public class Runner {
	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Menu m = new Menu();
		int choice = m.menu();

		String filePath = null;
		String outputFile = "out.txt";

		// Run methods based on user choice
		while (choice != 5) {
			// Choose file to get the words from
			if (choice == 1) {
				filePath = getFilePath();
			} else if (choice == 2) {
				// If file with hasn't been chosen, go back to option 1
				if (filePath == null) {
					System.out.print(ConsoleColour.RED_BOLD_BRIGHT); // "Error"
					System.out.println("No file has been loaded.");
				} else {
					outputFile = getOutputFileName();
				}
			} else if (choice == 3) {
				if (filePath == null) {
					System.out.print(ConsoleColour.RED_BOLD_BRIGHT); // "Error"
					System.out.println("No file has been loaded. Choose option 1.");
				} else {
					// Parse words into our local map
					Map<String, double[]> wordEmbeddings = FileParser.readLineFromFile(filePath);

					// Initialise constructor and pass the list
					VectorDistance distance = new VectorDistance(wordEmbeddings);

					// Search for the most/least similar word(s)
					searchWord(distance, outputFile, choice);
				}
			} else if (choice == 4) {
				if (filePath == null) {
					System.out.print(ConsoleColour.RED_BOLD_BRIGHT); // "Error"
					System.out.println("No file has been loaded. Choose option 1.");
				} else {
					// Parse words into our local map here
					Map<String, double[]> wordEmbeddings = FileParser.readLineFromFile(filePath);

					// Calculate all distances
					VectorDistance distance = new VectorDistance(wordEmbeddings);

					// Get the top 5 and output them to a file
					searchWord(distance, outputFile, choice);
				}
			}
			choice = m.menu();
		}
		System.out.print(ConsoleColour.YELLOW);
		System.out.println("Program ended.");
	}

	/**
	 * This method gets the name of the file (or path leading to it) contaning the
	 * words and embeddings. It also checks whether the file exists or not.
	 * 
	 * Big-O Notation: O(n) -> This method searches for the file in the directory
	 * which means it will loop through all files in there until it finds it, worst
	 * case scenario, the file does not exist. But it will loop through an n number
	 * of files.
	 * 
	 * @param N/A
	 * 
	 * @return filePath - the file path or name.
	 */
	private static String getFilePath() {
		Scanner scanner = new Scanner(System.in);
		String filePath;
		while (true) {
			System.out.print(ConsoleColour.RESET);
			System.out.print("Enter the file path: ");
			filePath = scanner.nextLine();

			File file = new File(filePath);

			// Check if path leads to a file
			if (file.exists()) {
				System.out.print(ConsoleColour.BLUE_BOLD_BRIGHT);
				System.out.println(filePath + " was found successfully.");
				break; // Exit the loop
			} else {
				System.out.print(ConsoleColour.RED_BOLD_BRIGHT); // not found
				System.out.println("Incorrect file path or file does not exist. Please try again.");
			}
		}
		return filePath;
	}

	/**
	 * This method gets the name of the output file, or sets it to the default name.
	 * 
	 * Big-0 Notation: O(n) -> This method simply checks if the string variable
	 * holding the name of the file is null, if it is then it assigns a value to it.
	 * The reason it's O(n) is because it removes all the spaces from the string,
	 * which means it needs to loop through the whole word, the more white spaces,
	 * the longer it'll take.
	 * 
	 * @param N/A
	 * 
	 * @return outputFile - the name of the ouput file.
	 */
	private static String getOutputFileName() {
		Scanner scanner = new Scanner(System.in);
		String outputFile = "";
		do {
			System.out.print("Enter the name of the output file (eg.: filename.txt): ");
			outputFile = scanner.nextLine().trim(); // remove any spaces
		} while (!outputFile.endsWith(".txt")); // it needs to be a .txt file.

		return outputFile;
	}

	/**
	 * This method compares the distance between the user word and the words in the
	 * map. It also asks how many words the user wants to see in the file.
	 * 
	 * Big-O Notation: This method only calls other methods, no calculations are
	 * done here.
	 * 
	 * @param distance   - the map contaning the distances
	 * @param outputFile - the name of the ouput file
	 * @param choice     - the user choice from the menu
	 * 
	 * @return N/A
	 */
	private static void searchWord(VectorDistance distance, String outputFile, int choice) {
		// Get the word
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter a word: ");
		String userWord = scanner.nextLine();

		// Get how many words to display. Make sure it's a positive int
		int topN;
		while (true) {
			System.out.print("How many words do you want to output to the file: ");
			try {
				topN = scanner.nextInt();
				if (topN <= 0) {
					System.out.println("Please enter a positive number.");
					continue; // Ask for input again
				}
				break; // Exit loop if input is valid
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid integer.");
				scanner.next(); // Clear the invalid input
			}
		}

		// Calculate most similar or least similar word
		if (choice == 3) {
			distance.searchSimilarWord(userWord, outputFile, topN);
		} else if (choice == 4) {
			distance.searchDifferentWord(userWord, outputFile, topN);
		}
	}
}