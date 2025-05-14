package org.os;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class StopWords
{
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am",
            "an", "and", "any", "are", "as", "at", "be", "because", "been",
            "before", "being", "below", "between", "both", "but", "by",
            "can", "cannot", "could", "did", "do", "does", "doing", "down",
            "during", "each", "few", "for", "from", "further", "had", "has",
            "have", "having", "he", "her", "here", "hers", "herself", "him",
            "himself", "his", "how", "i", "if", "in", "into", "is", "it",
            "its", "itself", "just", "let's", "me", "more", "most", "my",
            "myself", "no", "nor", "not", "now", "of", "off", "on", "once",
            "only", "or", "other", "ought", "our", "ours", "ourselves", "out",
            "over", "own", "same", "she", "should", "so", "some", "such",
            "than", "that", "the", "their", "theirs", "them", "themselves",
            "then", "there", "these", "they", "this", "those", "through",
            "to", "too", "under", "until", "up", "very", "was", "we", "were",
            "what", "when", "where", "which", "while", "who", "whom", "why",
            "with", "will", "would", "yet", "you", "your", "yours",
            "yourself", "yourselves"
    ));

    public static boolean isStopWord(String word)
    {
        if (word == null || word.isEmpty())
        {
            return false;
        }
        return STOP_WORDS.contains(word);
    }
}
