package org.os;

/**
 * Stores metadata about a term in the inverted index.
 * Holds document frequency, collection term frequency, and a posting list.
**/
public class TermData
{
    /// The number of documents that contain this term
    public int doc_freq = 0;

    /// The total number of times this term appears across all documents
    public int term_freq = 0;

    /// Head of the linked list of postings (documents containing the term)
    Posting pList = null;

    /// Pointer to the last posting in the list for quick insertion
    Posting last = null;

    public TermData(int df, int tf)
    {
        doc_freq = df;
        term_freq = tf;
    }

    /**
     * Checks whether the posting list already contains a posting for the given document ID.
     *
     * @param i Document ID to check.
     * @return True if the document is already in the posting list; false otherwise.
     */
    boolean postingListContains(int i)
    {
        boolean found = false;
        Posting p = pList;
        while (p != null)
        {
            if (p.docId == i)
            {
                return true;
            }
            p = p.next;
        }
        return found;
    }

    /**
     * Retrieves the document term frequency (dtf) for a specific document ID.
     *
     * @param i Document ID to look for.
     * @return Document term frequency if found; otherwise, 0.
     */
    int getPosting(int i)
    {
        int found = 0;
        Posting p = pList;
        while (p != null)
        {
            if (p.docId >= i)
            {
                if (p.docId == i)
                {
                    return p.dtf;
                }
                else
                {
                    return 0;
                }
            }
            p = p.next;
        }
        return found;
    }
}