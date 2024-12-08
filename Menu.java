package ie.atu.sw;

import java.util.Scanner;

/**
 * Class containing the menu
 */
public class Menu {
	/**
	 * Menu containing all options for the user.
	 * 
	 * @return option chosen by the user.
	 * @throws Exception if the user enters a word instead of an integer.
	 */
	public int menu() throws Exception {
		// Scanner object
		Scanner s = new Scanner(System.in);

		// You should put the following code into a menu or Menu class
		System.out.println(ConsoleColour.GREEN_BOLD_BRIGHT);
		System.out.println("************************************************************");
		System.out.println("*                                                          *");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*          Similarity Search with Word Embeddings          *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Embedding File");
		System.out.println("(2) Specify an Output File (default: ./out.txt)");
		System.out.println("(3) Enter a Word (Find most similar matches)");
		System.out.println("(4) Enter a Word (Find least similar matches)");
		System.out.println("(5) Quit");

		// Output a menu of options and solicit text from the user
		System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
		System.out.print("Select Option [1-5]>");
		System.out.println();

		// Get valid user choice
		int choice = 0;
		boolean validInput = false;

		while (!validInput) {
			try {
				choice = s.nextInt();

				// Message if option does not exist
				if (choice < 1 || choice > 5) {
					System.out.print(ConsoleColour.RED_BOLD_BRIGHT);
					System.out.println("This option does not exist.");
				}

				validInput = true; // Set it to true if input is valid
			} catch (Exception e) {
				System.out.print(ConsoleColour.RED_BOLD_BRIGHT); // "Error"
				System.out.println("Enter a valid number, please.");
				// Consume the invalid input
				s.nextLine();
			}
		}
		// s.close(); - not closing this on purpose!
		return choice;
	}

	/*
	 * Terminal Progress Meter ----------------------- You might find the progress
	 * meter below useful. The progress effect works best if you call this method
	 * from inside a loop and do not call System.out.println(....) until the
	 * progress meter is finished.
	 * 
	 * Please note the following carefully:
	 * 
	 * 1) The progress meter will NOT work in the Eclipse console, but will work on
	 * Windows (DOS), Mac and Linux terminals.
	 * 
	 * 2) The meter works by using the line feed character "\r" to return to the
	 * start of the current line and writes out the updated progress over the
	 * existing information. If you output any text between calling this method,
	 * i.e. System.out.println(....), then the next call to the progress meter will
	 * output the status to the next line.
	 * 
	 * 3) If the variable size is greater than the terminal width, a new line escape
	 * character "\n" will be automatically added and the meter won't work properly.
	 */
	public static void printProgress(int index, int total) {
		if (index > total)
			return; // Out of range
		int size = 50; // Must be less than console width
		char done = '█'; // Change to whatever you like.
		char todo = '░'; // Change to whatever you like.

		// Compute basic metrics for the meter
		int complete = (100 * index) / total;
		int completeLen = size * complete / 100;

		/*
		 * A StringBuilder should be used for string concatenation inside a loop.
		 * However, as the number of loop iterations is small, using the "+" operator
		 * may be more efficient as the instructions can be optimized by the compiler.
		 * Either way, the performance overhead will be marginal.
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append((i < completeLen) ? done : todo);
		}

		/*
		 * The line feed escape character "\r" returns the cursor to the start of the
		 * current line. Calling print(...) overwrites the existing line and creates the
		 * illusion of an animation.
		 */
		System.out.print("\r" + sb + "] " + complete + "%");

		// Once the meter reaches its max, move to a new line.
		if (done == total)
			System.out.println("\n");
	}
}