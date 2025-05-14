package org.os;
import java.util.*;

/**
 * A class representing the Inverted Index data structure.
 * Maps terms to their document frequency, term frequency, and posting lists.
**/
public class InvertedIndex
{
    /// The main dictionary: maps terms (words) to their associated TermData
    private Map<String, TermData> index = new HashMap<>();

    /**
     * Adds the content of a document into the inverted index.
     *
     * @param docId   The unique ID of the document.
     * @param content The textual content of the document.
     **/
    public void addDocument(int docId, String content)
    {
        String[] words = content.toLowerCase().split("\\W+");

        for (String word : words)
        {
            if (word.isBlank() || word.length() <= 2 || StopWords.isStopWord(word))
            {
                continue;
            }

            word = stemWord(word);

            if (!index.containsKey(word))
            {
                index.put(word, new TermData(0, 0));
            }

            TermData termData = index.get(word);

            if (!termData.postingListContains(docId))
            {
                Posting newPost = new Posting(docId, 1);

                if (termData.pList == null)
                {
                    termData.pList = newPost;
                    termData.last = newPost;
                }
                else
                {
                    termData.last.next = newPost;
                    termData.last = newPost;
                }

                termData.doc_freq += 1;
            }
            else
            {
                Posting current = termData.pList;
                while (current != null)
                {
                    if (current.docId == docId)
                    {
                        current.dtf += 1;
                        break;
                    }
                    current = current.next;
                }
            }

            termData.term_freq += 1;
        }
    }

    /**
     * Prints a given posting list.
     *
     * @param p The head of the posting list to print.
    **/
    public void printPostingList(Posting p)
    {
        System.out.print("[");
        while (p != null)
        {
            System.out.print("(" + p.docId + ", tf=" + p.dtf + ")");
            if (p.next != null) System.out.print(", ");
            p = p.next;
        }
        System.out.println("]");
    }

    /**
     * Prints the entire inverted index dictionary and all posting lists.
    */
    public void PrintInvertedIndex()
    {
        Iterator it = index.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            TermData dd = (TermData) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "]       =--> ");
            printPostingList(dd.pList);
        }
        System.out.println("------------------------------------------------------");
        System.out.println("*** Number of terms = " + index.size());
    }

    /**
     * Returns the internal index map.
     *
     * @return The map of term to TermData.
    */
    public Map<String, TermData> getIndex()
    {
        return index;
    }

    /**
     * Stems a given word. (Currently returns the word without stemming for simplicity.)
     *
     * @param word The word to stem.
     * @return The stemmed version of the word.
    **/
    String stemWord(String word)
    {
//      return word;
        Stemmer s = new Stemmer();
        s.addString(word);
        s.stem();
        return s.toString();
    }
}
