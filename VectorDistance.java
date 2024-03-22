package ie.atu.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VectorDistance {
	private Map<String, double[]> wordEmbeddings;

	/*
	 * This method simply takes in the map as a param and initialises our local map
	 */
	public VectorDistance(Map<String, double[]> wordEmbeddings) {
		this.wordEmbeddings = wordEmbeddings;
	}

	/*
	 * This method will compare the word given by the user, to the words loaded into
	 * the map
	 */
    public void searchSimilarWord(String inputWord, String outputPath) {
        // Check if word entered exists in the map
        if (wordEmbeddings.containsKey(inputWord)) {
            // If so, get value (vectors) from that key
            double[] inputVector = wordEmbeddings.get(inputWord);

            // Stores the euclidean distance given by calculateEuclideanDistances()
            // User word will be compared to each word in the map
            Map<String, Double> euclideanDistances = calculateEuclideanDistances(inputVector);

            // Sort all the distances
            Map<String, Double> sortedDistances = sortByValue(euclideanDistances);

            // Retrieve the top 5
            // Output to the specified file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                int count = 0;
                // Loop through all distances until count hits 5
                for (Map.Entry<String, Double> entry : sortedDistances.entrySet()) {
                    if (count >= 5)
                        break;
                    String word = entry.getKey();
                    double euclideanDistance = entry.getValue();
                    // Write word and distance to the file
                    writer.write("Distance from " + word + ": " + euclideanDistance + "\n");
                    count++;
                }
                System.out.println("Top 5 words written to " + outputPath);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println("Word '" + inputWord + "' not found in word embeddings.");
        }
    }

	/*
	 * This method will compare the user word against all words in the map and store
	 * the distances
	 */
	private Map<String, Double> calculateEuclideanDistances(double[] inputVector) {
		Map<String, Double> euclideanDistances = new HashMap<>();

		// For each <String, double[]>, do the following
		for (Map.Entry<String, double[]> entry : wordEmbeddings.entrySet()) {
			String word = entry.getKey();
			double[] vector = entry.getValue();

			// This gets the vectors from the user word (previous method) and compares the
			// actual distance
			double distance = calculateEuclideanDistance(inputVector, vector);

			// Store the Euclidean distance in the map
			euclideanDistances.put(word, distance);
		}

		return euclideanDistances;
	}

	/*
	 * This method will return the euclidean distance between each word and the user
	 * word.
	 */
	private double calculateEuclideanDistance(double[] vector1, double[] vector2) {
		// Vectors must have the same length
		if (vector1.length != vector2.length) {
			throw new IllegalArgumentException("Vectors must have the same length");
		}

		// Calculation
		// Increment the sum of squared differences between corresponding elements of
		// the two vectors
		double sumSquaredDiff = 0.0;
		for (int i = 0; i < vector1.length; i++) {
			double diff = vector1[i] - vector2[i];
			sumSquaredDiff += diff * diff; // the distance is the sum of all squared differences
		}
		return Math.sqrt(sumSquaredDiff);
	}

	/*
	 * This method will sort the distances
	 * 
	 * I used this StackOverflow article to understand this method:
	 * https://stackoverflow.com/questions/22132103/sorting-a-map-by-value-in-java
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
