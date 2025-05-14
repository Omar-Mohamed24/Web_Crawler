package org.os;
import java.util.*;

/**
 * Calculates and stores the TF-IDF vectors for a set of documents.
 * Also computes and stores the norms of these vectors for later similarity calculations.
**/
public class TFIDF_Calculator
{
    /// Stores the TF-IDF vector for each document (docId -> (term -> tf-idf score))
    private final Map<Integer, Map<String, Double>> docVectors = new HashMap<>();

    /// Stores the Euclidean norm (length) of each document vector
    private final Map<Integer, Double> docNorms = new HashMap<>();

    /// The inverted index built from the documents
    private final InvertedIndex index;

    /// The list of document data
    private final List<DocumentData> documents;

    public TFIDF_Calculator(InvertedIndex index, List<DocumentData> documents)
    {
        this.index = index;
        this.documents = documents;
        compute();
    }

    /**
     * Computes the TF-IDF vector and norm for each document.
     */
    private void compute()
    {
        int totalDocs = documents.size();
        Map<String, TermData> invertedIndex = index.getIndex();

        for (int docId = 0; docId < totalDocs; docId++)
        {
            Map<String, Double> vector = new HashMap<>();
            double normSquared = 0.0;

            for (Map.Entry<String, TermData> entry : invertedIndex.entrySet())
            {
                String term = entry.getKey();
                TermData data = entry.getValue();

                int tf = data.getPosting(docId);
                if (tf == 0) continue;  // Skip terms not present in this document

                // Compute TF weight (1 + log10(tf))
                double tfWeight = 1 + Math.log10(tf);

                // Compute IDF (log10(totalDocs / doc_freq))
                double idf = Math.log10((double) totalDocs / data.doc_freq);

                // TF-IDF = TF weight * IDF
                double tfidf = tfWeight * idf;

                vector.put(term, tfidf);
                normSquared += tfidf * tfidf;
            }

            docVectors.put(docId, vector);
            docNorms.put(docId, Math.sqrt(normSquared));
        }
    }

    public Map<String, Double> getTFIDFVector(int docId)
    {
        return docVectors.get(docId);
    }

    public double getVectorNorm(int docId)
    {
        return docNorms.get(docId);
    }

    public Map<Integer, Map<String, Double>> getAllVectors()
    {
        return docVectors;
    }

    public Map<Integer, Double> getAllNorms()
    {
        return docNorms;
    }

    /**
     * Prints the TF-IDF vectors and their norms for debugging purposes.
     */
    public void print()
    {
        for (int docId = 0; docId < documents.size(); docId++)
        {
            System.out.println("Doc ID: " + docId + " (" + documents.get(docId).title + ")");
            Map<String, Double> vec = docVectors.get(docId);
            for (Map.Entry<String, Double> entry : vec.entrySet())
            {
                System.out.printf("   %s: %.4f\n", entry.getKey(), entry.getValue());
            }
            System.out.println("Norm: " + docNorms.get(docId));
            System.out.println("-----------------------------------------------------");
        }
    }
}
