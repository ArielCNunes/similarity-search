package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileParser {
	/**
	 * This method will take the line directly from the file, break it down, and add
	 * it to the map.
	 * 
	 * Big-O Notation: O(n) -> This method will always loop through all the lines in
	 * the .txt file, break up each line in 2. It does the same thing to all lines,
	 * so the more lines, the longer it takes.
	 * 
	 * @param the file path/file name.
	 * 
	 * @return the map containing the words and vectors.
	 */
	public static Map<String, double[]> readLineFromFile(String filePath) {
		Map<String, double[]> wordsAndVectorsMap = new HashMap<>(); // Map

		// Read in each line
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String lineFromFile;
			/*
			 * This will run in O(n) time. The more lines the longer it will take
			 */
			while ((lineFromFile = br.readLine()) != null) {
				// Line will be split whenever there is a comma followed by whitespace
				String[] lineArray = lineFromFile.split(",\\s*");

				// The first element is always going to be a word
				String word = lineArray[0];

				// All the other element are the vector values
				double[] vectors = new double[lineArray.length - 1];
				for (int i = 1; i < lineArray.length; i++) {
					vectors[i - 1] = Double.parseDouble(lineArray[i]);
				}

				// Put the word and its vector values into the map
				wordsAndVectorsMap.put(word, vectors);
			}
		} catch (IOException e) {
			e.printStackTrace(); // Handle exceptions
		}
		return wordsAndVectorsMap; // This method returns the actual map
	}
}
