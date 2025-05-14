package org.os;

/**
 * Represents a node in the posting list for a term in the inverted index.
 * Each Posting object holds information about a single document where the term appears.
**/
public class Posting
{
    public Posting next = null;
    int docId;
    int dtf = 1;

    Posting(int id, int t)
    {
        docId = id;
        dtf=t;
    }

    Posting(int id)
    {
        docId = id;
    }
}