package org.os;
import java.util.*;

/**
 * Processes user queries and computes the TF-IDF vector for the query.
**/
public class QueryProcessor
{
    private final InvertedIndex index;    // The inverted index for the set of crawled documents

    public QueryProcessor(InvertedIndex index)
    {
        this.index = index;
    }

    /**
     * Processes the query and computes its TF-IDF vector.
     *
     * @param query The search query string input by the user.
     * @param totalDocs The total number of documents in the set of crawled documents.
     * @return A map containing terms and their corresponding TF-IDF values.
     */
    public Map<String, Double> processQuery(String query, int totalDocs)
    {
        Map<String, Integer> termFreq = new HashMap<>();    // Stores the frequency of terms in the query

        // Split the query into words and process each word
        String[] words = query.toLowerCase().split("\\W+");

        // Count the frequency of each term in the query, ignoring stop words and small words
        for (String word : words)
        {
            if (word.isBlank() || word.length() < 2 || StopWords.isStopWord(word))
            {
                continue;
            }

            word = stemWord(word);
            termFreq.put(word, termFreq.getOrDefault(word, 0) + 1);
        }

        Map<String, Double> queryTfidf = new HashMap<>();      // The resulting TF-IDF vector for the query

        for (Map.Entry<String, Integer> entry : termFreq.entrySet())
        {
            String term = entry.getKey();
            int tf = entry.getValue();      // Term Frequency (TF) of the term in the query
            double tfWeight = 1 + Math.log10(tf);     // TF weight, using log scaling

            // Document Frequency (DF) for the term in the index
            int df = 1;
            if (index.getIndex().containsKey(term))
            {
                df = index.getIndex().get(term).doc_freq;     // Get document frequency from the inverted index
            }

            double idf = Math.log10((double) totalDocs / df);

            queryTfidf.put(term, tfWeight * idf);
        }

        return queryTfidf;
    }

    /**
     * Stems a given word. (Currently returns the word without stemming for simplicity.)
     *
     * @param word The word to stem.
     * @return The stemmed version of the word.
     **/
    private String stemWord(String word)
    {
//        return word;
        Stemmer s = new Stemmer();
        s.addString(word);
        s.stem();
        return s.toString();
    }
}
