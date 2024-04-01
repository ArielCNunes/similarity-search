package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will make sure the words are parsed from a file into our map.
 */
public class FileParser {
	/**
	 * This method will read each line from the .txt file. For each line, it will
	 * break it into a String word and a double array vectors.
	 * 
	 * Big-O Notation: O(n) -> This method will loop through all lines, so the more
	 * lines the longer it'll take. There is another loop within each loop as well
	 * which is when we loop trough all 50 vector values, but that's also O(n).
	 * 
	 * @param filePath - the file path/name
	 * @return the map containing the words (keys) and vectors (values).
	 * @throws IOException - if an I/O error occurs while reading the file.
	 */
	public static Map<String, double[]> readLineFromFile(String filePath) throws IOException {
		// Map
		Map<String, double[]> wordsAndVectorsMap = new HashMap<>();

		// Count total lines in the file
		int totalLines = countLines(filePath);

		// Read in each line
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String lineFromFile;
			int currentLine = 0; // track current line
			while ((lineFromFile = br.readLine()) != null) {
				/**
				 * Line will be split whenever there is a comma followed by whitespace. That's
				 * how the file is layed out.
				 */
				String[] lineArray = lineFromFile.split(",\\s*");

				// The first element is always going to be a word
				String word = lineArray[0];

				// All the other elements are the vector values
				double[] vectors = new double[lineArray.length - 1];
				for (int i = 1; i < lineArray.length; i++) {
					vectors[i - 1] = Double.parseDouble(lineArray[i]);
				}

				// Put the word and its vector values into the map
				wordsAndVectorsMap.put(word, vectors);

				// Update progress bar each time we loop through one line
				currentLine++;
				Menu.printProgress(currentLine, totalLines);
			}
		} catch (IOException e) {
			throw new IOException("Error reading file: " + filePath, e);
		}
		return wordsAndVectorsMap;
	}

	/**
	 * Method to count total lines in the file.
	 * 
	 * Big-O Notation: O(n) -> This method again loops through all lines in the
	 * file.
	 * 
	 * @param filePath - the file path/name
	 * 
	 * @return lines - the amount of lines.
	 * 
	 * @throws IOException
	 */
	private static int countLines(String filePath) throws IOException {
		int lines = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			while (reader.readLine() != null) {
				lines++; // incremen by 1.
			}
		}
		return lines;
	}
}