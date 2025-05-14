package org.os;

/**
 * A simple implementation of the Porter Stemming Algorithm.
 * Reduces words to their base or root form (e.g., "running" → "run").
**/
public class Stemmer
{
    private char[] b;   // Buffer for word to be stemmed
    private int i;     // Index for end of the current word in buffer
    private int i_end; // Index for end of stemmed word
    private int j, k;  // General-purpose indexes for processing
    private static final int INC = 50; // Buffer increment size

    public Stemmer()
    {
        b = new char[INC];
        i = 0;
        i_end = 0;
    }

    /** Add a string to the buffer */
    public void addString(String s)
    {
        add(s.toCharArray(), s.length());
    }

    /** Add a character array to the buffer */
    public void add(char[] w, int wLen)
    {
        if (i + wLen >= b.length)
        {
            // Resize the buffer if necessary
            char[] new_b = new char[i + wLen + INC];
            System.arraycopy(b, 0, new_b, 0, i);
            b = new_b;
        }
        System.arraycopy(w, 0, b, i, wLen);
        i += wLen;
    }

    /** Return the current stemmed word as a String */
    @Override
    public String toString()
    {
        return new String(b, 0, i_end);
    }

    /** Test if a character at position i is a consonant */
    private boolean cons(int i)
    {
        return switch (b[i])
        {
            case 'a', 'e', 'i', 'o', 'u' -> false;
            case 'y' -> i == 0 || !cons(i - 1);
            default -> true;
        };
    }

    /** Measure the number of consonant sequences between 0 and j */
    private int m()
    {
        int n = 0;
        int i = 0;
        while (true)
        {
            if (i > j) return n;
            if (!cons(i)) break;
            i++;
        }
        i++;
        while (true)
        {
            while (true)
            {
                if (i > j) return n;
                if (cons(i)) break;
                i++;
            }
            i++;
            n++;
            while (true)
            {
                if (i > j) return n;
                if (!cons(i)) break;
                i++;
            }
            i++;
        }
    }

    /** Check if there is a vowel in the stem (0...j) */
    private boolean vowelInStem()
    {
        for (int i = 0; i <= j; i++)
        {
            if (!cons(i)) return true;
        }
        return false;
    }

    /** Check if there is a double consonant ending at position j */
    private boolean doublec(int j)
    {
        if (j < 1) return false;
        if (b[j] != b[j - 1]) return false;
        return cons(j);
    }

    /** Check if the word ends with consonant-vowel-consonant pattern */
    private boolean cvc(int i)
    {
        if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2)) return false;
        int ch = b[i];
        return !(ch == 'w' || ch == 'x' || ch == 'y');
    }

    /** Check if word ends with a given string and set j accordingly */
    private boolean ends(String s)
    {
        int l = s.length();
        int o = k - l + 1;
        if (o < 0) return false;
        for (int i = 0; i < l; i++)
        {
            if (b[o + i] != s.charAt(i)) return false;
        }
        j = k - l;
        return true;
    }

    /** Set ending of the word to a new given string */
    private void setTo(String s)
    {
        int l = s.length();
        int o = j + 1;
        for (int i = 0; i < l; i++)
        {
            b[o + i] = s.charAt(i);
        }
        k = j + l;
    }

    /** Replace the ending if m() > 0 */
    private void r(String s)
    {
        if (m() > 0) setTo(s);
    }

    /** Step 1: Deals with plurals and past participles */
    private void step1()
    {
        if (b[k] == 's')
        {
            if (ends("sses")) k -= 2;
            else if (ends("ies")) setTo("i");
            else if (b[k - 1] != 's') k--;
        }

        if (ends("eed"))
        {
            if (m() > 0) k--;
        }
        else if ((ends("ed") || ends("ing")) && vowelInStem())
        {
            k = j;
            if (ends("at")) setTo("ate");
            else if (ends("bl")) setTo("ble");
            else if (ends("iz")) setTo("ize");
            else if (doublec(k))
            {
                k--;
                int ch = b[k];
                if (ch == 'l' || ch == 's' || ch == 'z') k++;
            }
            else if (m() == 1 && cvc(k))
            {
                setTo("e");
            }
        }
    }

    /** Step 2: Turn terminal 'y' to 'i' if there is another vowel */
    private void step2()
    {
        if (ends("y") && vowelInStem())
        {
            b[k] = 'i';
        }
    }

    /** Step 3: Maps double suffixes to single ones */
    private void step3()
    {
        if (k == 0) return;
        switch (b[k - 1])
        {
            case 'a':
                if (ends("ational")) { r("ate"); break; }
                if (ends("tional")) { r("tion"); break; }
                break;
            case 'c':
                if (ends("enci")) { r("ence"); break; }
                if (ends("anci")) { r("ance"); break; }
                break;
            case 'e':
                if (ends("izer")) { r("ize"); break; }
                break;
            case 'l':
                if (ends("bli")) { r("ble"); break; }
                if (ends("alli")) { r("al"); break; }
                if (ends("entli")) { r("ent"); break; }
                if (ends("eli")) { r("e"); break; }
                if (ends("ousli")) { r("ous"); break; }
                break;
            case 'o':
                if (ends("ization")) { r("ize"); break; }
                if (ends("ation")) { r("ate"); break; }
                if (ends("ator")) { r("ate"); break; }
                break;
            case 's':
                if (ends("alism")) { r("al"); break; }
                if (ends("iveness")) { r("ive"); break; }
                if (ends("fulness")) { r("ful"); break; }
                if (ends("ousness")) { r("ous"); break; }
                break;
            case 't':
                if (ends("aliti")) { r("al"); break; }
                if (ends("iviti")) { r("ive"); break; }
                if (ends("biliti")) { r("ble"); break; }
                break;
            case 'g':
                if (ends("logi")) { r("log"); break; }
                break;
        }
    }

    /** Step 4: Deals with suffixes like -ic, -ful, -ness etc. */
    private void step4()
    {
        switch (b[k])
        {
            case 'e':
                if (ends("icate")) { r("ic"); break; }
                if (ends("ative")) { r(""); break; }
                if (ends("alize")) { r("al"); break; }
                break;
            case 'i':
                if (ends("iciti")) { r("ic"); break; }
                break;
            case 'l':
                if (ends("ical")) { r("ic"); break; }
                if (ends("ful")) { r(""); break; }
                break;
            case 's':
                if (ends("ness")) { r(""); break; }
                break;
        }
    }

    /** Step 5: Removes certain suffixes if m() > 1 */
    private void step5()
    {
        if (k == 0) return;
        switch (b[k - 1])
        {
            case 'a': if (ends("al")) break; return;
            case 'c': if (ends("ance") || ends("ence")) break; return;
            case 'e': if (ends("er")) break; return;
            case 'i': if (ends("ic")) break; return;
            case 'l': if (ends("able") || ends("ible")) break; return;
            case 'n': if (ends("ant") || ends("ement") || ends("ment") || ends("ent")) break; return;
            case 'o':
                if ((ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) || ends("ou")) break;
                return;
            case 's': if (ends("ism")) break; return;
            case 't': if (ends("ate") || ends("iti")) break; return;
            case 'u': if (ends("ous")) break; return;
            case 'v': if (ends("ive")) break; return;
            case 'z': if (ends("ize")) break; return;
            default: return;
        }
        if (m() > 1) k = j;
    }

    /** Step 6: Final clean up step */
    private void step6()
    {
        j = k;
        if (b[k] == 'e')
        {
            int a = m();
            if (a > 1 || (a == 1 && !cvc(k - 1))) k--;
        }

        if (b[k] == 'l' && doublec(k) && m() > 1)
        {
            k--;
        }
    }

    /** Stem the word placed into the Stemmer buffer */
    public void stem()
    {
        k = i - 1;
        if (k > 1)
        {
            step1();
            step2();
            step3();
            step4();
            step5();
            step6();
        }
        i_end = k + 1;
        i = 0;
    }
}