package org.os;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

/**
 * A simple web crawler that starts from seed Wikipedia URLs,
 * connect to pages, extracts text, and follows links to new pages.
**/
public class WebCrawler
{
    private static final int MAX_DOCS = 10;    // Maximum number of documents to crawl
    private static final String WIKI_PREFIX = "https://en.wikipedia.org/wiki/";    // Base URL prefix to ensure staying on Wikipedia

    private Set<String> visited = new HashSet<>();         // Keeps track of visited URLs to avoid revisiting
    private List<DocumentData> documents = new ArrayList<>();  // Stores the crawled documents

    /**
     * Starts crawling from the given seed URLs.
     * Using a BFS like Algorithm to visit the pages.
     *
     * @param seedUrls List of starting Wikipedia page URLs "Two URLs in our case".
     * @return List of crawled DocumentData objects containing URL, title, and text.
     */
    public List<DocumentData> crawl(List<String> seedUrls)
    {
        Queue<String> q = new LinkedList<>(seedUrls);   // BFS queue initialized with seed URLs

        while (!q.isEmpty() && documents.size() < MAX_DOCS)
        {
            String url = q.poll();
            if (visited.contains(url) || !url.startsWith(WIKI_PREFIX))
            {
                continue;   // Skip already visited URLs and non-Wikipedia links
            }

            try
            {
                // Fetch and parse the HTML page using Jsoup
                Document doc = Jsoup.connect(url).get();
                /*
                 * Select main paragraph content.
                 * used only the p elements because some sections in the main container that aren't relevant to the page content.
                 * same goes for the links.
                **/
                Elements main_content = doc.select(".mw-content-ltr.mw-parser-output p");
                visited.add(url);

                // Extract the page title
                String title = doc.select(".firstHeading.mw-first-heading").text();

                // Extract the main text content
                StringBuilder textBuilder = new StringBuilder();
                for (Element p : main_content)
                {
                    textBuilder.append(p.text()).append(" ");
                }
                String text = textBuilder.toString();

                // Add the document to the list
                documents.add(new DocumentData(url, title, text));

                // Extract all internal links from the page
                Elements links = main_content.select("p a[href]");
                for (Element link : links)
                {
                    String absHref = link.absUrl("href");
                    absHref = absHref.split("#")[0];     // Remove any URL fragment after #

                    // Only queue new, unvisited Wikipedia articles
                    if (absHref.startsWith(WIKI_PREFIX) && !visited.contains(absHref))
                    {
                        q.add(absHref);
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println("Failed to fetch: " + url + " - " + e.getMessage());
            }
        }

        return documents;
    }
}