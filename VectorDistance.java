package ie.atu.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class containing the methods to calculate the vectors distances
 */
public class VectorDistance {
	// Map
	private Map<String, double[]> wordEmbeddings;

	/**
	 * This method takes in the map as a parameter and initialises our local map.
	 * This is our constructor.
	 * 
	 * @param wordEmbeddings - the map contaning all words and vectors.
	 * 
	 */
	public VectorDistance(Map<String, double[]> wordEmbeddings) {
		this.wordEmbeddings = wordEmbeddings;
	}

	/**
	 * This method will compare the word given by the user to the words loaded into
	 * the map.
	 * 
	 * Big-O Notation: O(n log n) -> This method loops through all the words in
	 * wordEmbeddings and calculates their Euclidean distances (O(n)) from the input
	 * word each time. The distances also need to be sorted by sortByValue, which
	 * again will loop through all distances, this sorting is the dominant operation
	 * (the one that takes longer), and it's O(n log n) as we're using a map.
	 * 
	 * @param inputWord  - the word entered by the user
	 * @param outputPath - the name of the output file.
	 * @param topN       - the amount of words to be printed
	 */
	public void searchSimilarWord(String inputWord, String outputPath, int topN) {
		// Check if word entered exists in the map
		if (wordEmbeddings.containsKey(inputWord)) {
			// If so, get value (vectors) for that key
			double[] inputVector = wordEmbeddings.get(inputWord);

			// Stores the distance given by calculateEuclideanDistances() in our map
			Map<String, Double> euclideanDistances = calculateAllEuclideanDistances(inputVector);

			// Sort all the distances
			Map<String, Double> sortedDistances = sortByValue(euclideanDistances);

			// Retrieve the top N words
			// Output to the specified file or default file
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
				System.out.print(ConsoleColour.BLUE_BOLD_BRIGHT);
				writer.write(topN + " closest words to " + inputWord.toUpperCase() + ":\n");
				int count = 0;
				// Loop through all distances until count hits topN
				for (Map.Entry<String, Double> entry : sortedDistances.entrySet()) {
					if (count == topN)
						break;

					// Get word and distance
					String word = entry.getKey();
					double euclideanDistance = entry.getValue();

					// Format the output
					String formattedDistance = new DecimalFormat("#.###").format(euclideanDistance);

					// Output word and distance to the file
					writer.write("Word: " + word + " - Distance: " + formattedDistance + "\n");
					count++;
				}
				System.out.print(ConsoleColour.BLUE_BOLD_BRIGHT);
				System.out.println(topN + " most similar words written to " + outputPath);
			} catch (IOException e) {
				System.out.print(ConsoleColour.RED_BOLD_BRIGHT);
				System.err.println("Error writing to file: " + e.getMessage());
			}
		} else {
			System.out.print(ConsoleColour.RED_BOLD_BRIGHT); // word not in the map
			System.out.println("Word '" + inputWord.toUpperCase() + "' not found.");
		}
	}

	/**
	 * This method will compare the word given by the user to the words loaded into
	 * the map, only this time we will reverse the map to ouput the least similar.
	 * 
	 * Big-O Notation: O(n log n) -> Similar to searchSimilarWord(), the time this
	 * method takes to run is also O(n log n) due to the dominant opearation being
	 * the sorting of the distances. The more distances we have the longer it will
	 * take, but using a map makes it faster.
	 * 
	 * @param inputWord  - the word entered by the user
	 * @param outputPath - the name of the output file.
	 * @param topN       - the amount of words to be printed
	 */
	public void searchDifferentWord(String inputWord, String outputPath, int topN) {
		// Check if word entered exists in the map
		if (wordEmbeddings.containsKey(inputWord)) {
			// If so, get value (vectors) for that key
			double[] inputVector = wordEmbeddings.get(inputWord);

			// Stores the distance given by calculateEuclideanDistances() in the map
			Map<String, Double> euclideanDistances = calculateAllEuclideanDistances(inputVector);

			// Sort all the distances
			Map<String, Double> sortedDistances = sortByValue(euclideanDistances);

			// Retrieve the top N words
			// Output to the specified file or default file
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
				System.out.print(ConsoleColour.BLUE_BOLD_BRIGHT);
				writer.write(topN + " farthest words from " + inputWord.toUpperCase() + ":\n");
				int count = 0;

				/**
				 * This reversion operation was done with the help of OpenAI's ChatGPT.
				 * 
				 * Get the reverse order comparator for sorting distances in ascending order, so
				 * the top results will be the least similar.
				 */
				Comparator<String> reverseComparator = (word1, word2) -> sortedDistances.get(word2)
						.compareTo(sortedDistances.get(word1));
				Map<String, Double> reverseSortedDistances = new TreeMap<>(reverseComparator);
				reverseSortedDistances.putAll(sortedDistances); // store sorted distances in map

				// Loop through all distances until count reaches n
				for (Map.Entry<String, Double> entry : reverseSortedDistances.entrySet()) {
					if (count == topN)
						break;

					String word = entry.getKey();
					double euclideanDistance = entry.getValue();

					// Format the Euclidean distance
					String formattedDistance = new DecimalFormat("#.###").format(euclideanDistance);
					// Write word and distance to the file
					writer.write("Word: " + word + " - Distance: " + formattedDistance + "\n");
					count++;
				}
				System.out.print(ConsoleColour.BLUE_BOLD_BRIGHT);
				System.out.println(topN + " least similar words written to " + outputPath);
			} catch (IOException e) {
				System.out.print(ConsoleColour.RED_BOLD_BRIGHT);
				System.err.println("Error writing to file: " + e.getMessage());
			}

		} else {
			System.out.print(ConsoleColour.RED_BOLD_BRIGHT);
			System.out.println("Word '" + inputWord.toUpperCase() + "' not found.");
		}
	}

	/**
	 * This method will compare the user word against all words in the map and store
	 * the distances.
	 * 
	 * Big-O Notation: O(n) -> This method loops through all words in the map and
	 * compares them against the word entered by the user. The more words, the
	 * longer it'll take. The second for loop won't square the calculation because
	 * it will only loop through 50 vector values for each word, regardless of how
	 * many words.
	 * 
	 * @param inputVector - an array with all of the vector distances
	 * 
	 * @return euclideanDistances - the distances map
	 */
	private Map<String, Double> calculateAllEuclideanDistances(double[] inputVector) {
		Map<String, Double> euclideanDistances = new HashMap<>();

		// For each <String, double[]> in the map, get the key and values
		for (Map.Entry<String, double[]> entry : wordEmbeddings.entrySet()) {
			String word = entry.getKey();
			double[] vector = entry.getValue();

			// Vectors must have the same length (this might be unnecessary)
			if (inputVector.length != vector.length) {
				throw new IllegalArgumentException("Vectors must have the same length");
			}

			// Euclidean distance - Get the sum of the squared differences for all vectors
			double sumSquaredDiff = 0.0;
			for (int i = 0; i < inputVector.length; i++) {
				double diff = inputVector[i] - vector[i];
				sumSquaredDiff += diff * diff; // the distance is the sum of all squared differences
			}

			// Store the Euclidean distance and key (word) in the map
			euclideanDistances.put(word, Math.sqrt(sumSquaredDiff));
		}
		return euclideanDistances;
	}

	/**
	 * This method will sort the distances from greatest to smallest.
	 * 
	 * I used this StackOverflow article to create this method:
	 * https://stackoverflow.com/questions/22132103/sorting-a-map-by-value-in-java
	 * 
	 * Big-O notation: O(n log n) -> Sorting the list using Collections.sort takes
	 * O(n log n). Then creating a new linkedHashMap takes O(n). Overall the running
	 * time is O(n log n) as sorting the entries is the dominant operation here.
	 * 
	 * @param map - a map with all the distances.
	 * 
	 * @return sortedMap - a sorted map of the distances.
	 */
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

		Map<K, V> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
