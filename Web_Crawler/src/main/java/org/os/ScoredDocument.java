package org.os;

/**
 * Represents a document along with its relevance score
**/
public class ScoredDocument
{
    public DocumentData doc;
    public double score;

    public ScoredDocument(DocumentData doc, double score)
    {
        this.doc = doc;
        this.score = score;
    }
}
