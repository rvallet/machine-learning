package learning;

import java.io.File;
import java.util.*;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectorsModel;
import org.deeplearning4j.models.paragraphvectors.PVDM;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class TestArtificialIntelligenceV2 {

    private WordVectors wordVectors;
    private ParagraphVectorsModel model;

    // Constructor
    public TestArtificialIntelligenceV2(String corpusPath, String wordVectorsPath) throws Exception {
        // Load the word vectors from a file
        wordVectors = WordVectorSerializer.loadStaticModel(new File(wordVectorsPath));
        // Create the iterator and preprocessor
        SentenceIterator iterator = new CollectionSentenceIterator(Arrays.asList(corpus.split("\n")));
        iterator.setPreProcessor(new SentencePreProcessor() {
            public String preProcess(String sentence) {
                return sentence.toLowerCase().replaceAll("[^\\w\\s]", "");
            }
        });
        // Create the tokenizer
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        // Create the paragraph vectors model
        model = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .batchSize(1000)
                .epochs(50)
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();
        // Train the model
        model.fit();
    }

    // Method to generate a story based on the trained model
    public String generateStory(int numSentences) {
        String story = "";
        INDArray vectorSum = Nd4j.zeros(1, wordVectors.getWordVector(wordVectors.vocab().wordAtIndex(0)).length());
        for (int i = 0; i < numSentences; i++) {
            String sentence = model.predict(vectorSum).getLabel();
            vectorSum.addi(wordVectors.getWordVectorMatrix(sentence));
            story += sentence + " ";
        }
        return story;
    }

    // Main method to run the program
    public static void main(String[] args) throws Exception {
        TestArtificialIntelligenceV2 generator = new TestArtificialIntelligenceV2("corpus.txt", "wordvectors.txt");
        // Generate a story with 5 sentences
        String story = generator.generateStory(5);
        System.out.println(story);
    }
}
