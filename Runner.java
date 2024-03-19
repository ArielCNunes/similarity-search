package ie.atu.sw;

import java.util.Map;

public class Runner {
	public static void main(String[] args) throws Exception {
		// Output menu
		Menu m = new Menu();
		int choice = m.menu();

		// User choice will determine what to do
		if (choice == 1) {
			testCase(); // DEBUGGING

		} else if (choice == 2) {

		} else if (choice == 3) {

		}
	}

	/*
	 * This method is a test case to see if the words and vector value are being
	 * read in correctly.
	 */
	public static void testCase() {
	    int counter = 0;
	    String filePath = "word-embeddings.txt";

	    // Call the readLineFromFile to read the file and parse the word embedding
	    Map<String, double[]> wordEmbeddings = FileParser.readLineFromFile(filePath);

	    // Print the word embeddings to verify if they are read correctly
	    for (Map.Entry<String, double[]> entry : wordEmbeddings.entrySet()) {
	        String word = entry.getKey(); // word is the key
	        double[] embeddings = entry.getValue(); // embeddings are the values

	        System.out.println("Word: " + word);
	        counter++; // Increment counter
	        System.out.print("Embeddings: ");
	        for (double embedding : embeddings) {
	            System.out.print(embedding + " ");
	        }
	        System.out.println("\n" + counter + " lines read in");
	    }
	}
}