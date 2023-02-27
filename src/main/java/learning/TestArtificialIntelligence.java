package learning;

import java.util.*;

public class TestArtificialIntelligence {

    // Map to store the transition probabilities between words
    private Map<String, List<String>> transitions;

    // Constructor
    public TestArtificialIntelligence() {
        transitions = new HashMap<>();
    }

    // Method to train the model based on a given corpus
    public void train(String corpus) {
        // Split the corpus into words
        String[] words = corpus.split("\\s+");

        // Loop through the words and populate the transitions map
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i];
            String nextWord = words[i+1];
            if (transitions.containsKey(currentWord)) {
                transitions.get(currentWord).add(nextWord);
            } else {
                List<String> nextWords = new ArrayList<>();
                nextWords.add(nextWord);
                transitions.put(currentWord, nextWords);
            }
        }
    }

    // Method to generate a story based on the trained model
    public String generateStory(int numSentences) {
        String story = "";
        // Choose a random starting word
        String currentWord = (String) transitions.keySet().toArray()[(int) (Math.random() * transitions.size())];
        for (int i = 0; i < numSentences; i++) {
            // Add the current word to the story
            story += currentWord + " ";
            // Choose the next word based on the transition probabilities
            List<String> possibleNextWords = transitions.get(currentWord);
            currentWord = possibleNextWords.get((int) (Math.random() * possibleNextWords.size()));
            // If the current word ends with a period, start a new sentence
            if (currentWord.endsWith(".")) {
                story += currentWord + " ";
                currentWord = (String) transitions.keySet().toArray()[(int) (Math.random() * transitions.size())];
                story += "\n\n";
            }
        }
        // Post-processing: replace words and remove redundancy
        story = story.replaceAll(" elle ", " la princesse ");
        story = story.replaceAll(" il ", " le prince ");
        story = story.replaceAll(" la princesse la princesse", " la princesse ");
        story = story.replaceAll(" le prince le prince", " le prince ");
        return story;
    }

    // Main method to run the program
    public static void main(String[] args) {
        TestArtificialIntelligence generator = new TestArtificialIntelligence();
        // Train the model on a corpus of French fairy tales
        String corpus = "Il était une fois, une belle princesse qui vivait dans un grand château. " +
                "Un jour, elle rencontra un prince charmant et ils tombèrent amoureux. " +
                "Mais un méchant sorcier les maudit et ils furent séparés. " +
                "La princesse partit à la recherche du prince et surmonta de nombreux obstacles avant de le retrouver. " +
                "Finalement, ils se marièrent et vécurent heureux pour toujours.";
        generator.train(corpus);
        // Generate a story with 5 sentences
        String story = generator.generateStory(12);
        System.out.println(story);
    }
}

