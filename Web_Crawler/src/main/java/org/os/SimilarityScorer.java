package org.os;
import java.util.*;

/**
 * Scores and ranks documents based on the cosine similarity between a query vector and document vectors.
**/
public class SimilarityScorer
{
    private final List<DocumentData> documents;      // the set of crawled documents
    private final Map<Integer, Map<String, Double>> docVectors;     // Document vectors (TF-IDF vectors) for each document

    public SimilarityScorer(List<DocumentData> documents, Map<Integer, Map<String, Double>> docVectors)
    {
        this.documents = documents;
        this.docVectors = docVectors;
    }

    /**
     * Ranks the documents based on cosine similarity to the query vector.
     *
     * @param queryVector The TF-IDF vector of the user's query.
     * @return A list of scored documents, sorted by score in descending order.
     */
    public List<ScoredDocument> rankDocuments(Map<String, Double> queryVector)
    {
        List<ScoredDocument> scoredDocs = new ArrayList<>();

        // Compute the cosine similarity between the query vector and each document vector
        for (int docId = 0; docId < documents.size(); docId++)
        {
            Map<String, Double> docVector = docVectors.get(docId);    // Get the current document's vector
            double score = cosineSimilarity(queryVector, docVector);
            if (score > 0)   // Only consider documents with a positive similarity score
            {
                scoredDocs.add(new ScoredDocument(documents.get(docId), score));
            }
        }

        // Sort the scored documents by their similarity score in descending order
        scoredDocs.sort((a, b) -> Double.compare(b.score, a.score));
        return scoredDocs;
    }

    /**
     * Calculates the cosine similarity between two vectors.
     *
     * @param vec1 The first vector (Always the Query vector).
     * @param vec2 The second vector.
     * @return The cosine similarity between the two vectors.
     */
    private double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2)
    {
        // Find the intersection of terms between both vectors
        Set<String> allTerms = new HashSet<>(vec1.keySet());
        allTerms.retainAll(vec2.keySet());

        double dotProduct = 0.0;

        // Calculate the dot product of the two vectors
        for (String term : allTerms)
        {
            dotProduct += vec1.get(term) * vec2.get(term);
        }

        // Calculate the norms (magnitudes) of both vectors
        double norm1 = Math.sqrt(vec1.values().stream().mapToDouble(v -> v * v).sum());
        double norm2 = Math.sqrt(vec2.values().stream().mapToDouble(v -> v * v).sum());

        // Handle the case where a vector's norm is 0 to avoid division by zero
        if (norm1 == 0)
        {
            norm1 = 1.0;
        }
        if (norm2 == 0)
        {
            norm2 = 1.0;
        }

        // Return the cosine similarity, which is the dot product divided by the product of the norms
        return dotProduct / (norm1 * norm2);
    }
}