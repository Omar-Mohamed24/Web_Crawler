package org.os;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        List<String> seeds = Arrays.asList(
        "https://en.wikipedia.org/wiki/List_of_pharaohs",
        "https://en.wikipedia.org/wiki/Pharaoh"
        );

        // 1. Crawl documents from the web
        WebCrawler crawler = new WebCrawler();
        List<DocumentData> docs = crawler.crawl(seeds);

        // 2. Build the inverted index
        InvertedIndex index = new InvertedIndex();
        int docId = 0;
        for (DocumentData doc : docs)
        {
            index.addDocument(docId, doc.text);
            docId++;
        }

        // 3. Compute TF-IDF vectors
        TFIDF_Calculator tfidfCalc = new TFIDF_Calculator(index, docs);

        // 4. Prepare query processor and scorer
        QueryProcessor processor = new QueryProcessor(index);
        SimilarityScorer scorer = new SimilarityScorer(docs, tfidfCalc.getAllVectors());

        System.out.println("Search Engine is ready! Type your query below.");
        System.out.println("Type 'exit' to quit.\n");

        while (true)
        {
            System.out.print("Enter your search query: ");
            String userQuery = scanner.nextLine();
            if (userQuery.equalsIgnoreCase("exit")) break;

            Map<String, Double> queryVector = processor.processQuery(userQuery, docs.size());
            List<ScoredDocument> rankedDocs = scorer.rankDocuments(queryVector);

            if (rankedDocs.isEmpty())
            {
                System.out.println("No relevant documents found.");
                continue;
            }

            System.out.println("\nTop Results:");
            int count = 0;
            for (ScoredDocument scored : rankedDocs)
            {
                if (count >= 10) break;
                System.out.printf("Title: %-40s | Score: %.4f\n", scored.doc.title, scored.score);
                System.out.println("URL: " + scored.doc.url);
                System.out.println("--------------------------------------------------");
                count++;
            }
        }
        System.out.println("Search session ended.");
    }
}

///       debug tfidf
//        List<DocumentData> documents = new ArrayList<>();
//        documents.add(new DocumentData("https://example.com/doc1", "Doc 1", "The quick brown fox jumps over the lazy dog"));
//        documents.add(new DocumentData("https://example.com/doc2", "Doc 2", "Never jump over the lazy dog quickly"));
//        documents.add(new DocumentData("https://example.com/doc3", "Doc 3", "A fast brown fox leaps high"));
//        InvertedIndex in = new InvertedIndex();
//        for (int i = 0; i < documents.size(); i++)
//        {
//            in.addDocument(i, documents.get(i).text);
//        }
//        TFIDF_Calculator tf = new TFIDF_Calculator(in, documents);
//        tf.print();

///       debug inverted_index
//        index.addDocument(0, "Pharaohs Pharaohs ruled ancient Egypt.");
//        index.addDocument(1, "The pharaoh was a king in ancient Egypt.");
//        index.addDocument(2, "Pyramids were built by pharaohs.");
//        index.PrintInvertedIndex();

///       debug crawler
//        System.out.println("Crawled " + docs.size() + " documents:");
//        for (DocumentData doc : docs)
//        {
//            System.out.println("Title: " + doc.title);
//            System.out.println("URL: " + doc.url);
//            System.out.println("Snippet: " + doc.text.substring(0, Math.min(1000, doc.text.length())) + "...");
//            System.out.println("----------\n");
//        }

///       test on small input for weights calculation and debug.
//        public static void main(String[] args)
//        {
//            Scanner scanner = new Scanner(System.in);
//
//            // 1. Hardcoded small documents
//            List<DocumentData> docs = new ArrayList<>();
//            docs.add(new DocumentData("http://doc1.com", "Doc1", "Document about ancient Egypt and Pharaohs."));
//            docs.add(new DocumentData("http://doc2.com", "Doc2", "Pharaohs ruled ancient lands and built pyramids."));
//            docs.add(new DocumentData("http://doc3.com", "Doc3", "Modern Egypt is famous for pyramids and tourism."));
//
//            // 2. Build the inverted index
//            InvertedIndex index = new InvertedIndex();
//            int docId = 0;
//            for (DocumentData doc : docs) {
//                index.addDocument(docId, doc.text);
//                docId++;
//            }
//
//            System.out.println("\n=== Inverted Index ===");
//            index.PrintInvertedIndex();
//
//            // 3. Compute TF-IDF vectors
//            TFIDF_Calculator tfidfCalc = new TFIDF_Calculator(index, docs);
//
//            // 4. Prepare query processor and scorer
//            QueryProcessor processor = new QueryProcessor(index);
//            SimilarityScorer scorer = new SimilarityScorer(docs, tfidfCalc.getAllVectors());
//
//            System.out.println("Doc1 Vector: " + tfidfCalc.getAllVectors().get(0));
//            System.out.println("Doc2 Vector: " + tfidfCalc.getAllVectors().get(1));
//            System.out.println("Doc3 Vector: " + tfidfCalc.getAllVectors().get(2));
//
//
//            System.out.println("\nSearch Engine is ready! Type your query below.");
//            System.out.println("Type 'exit' to quit.\n");
//
//            while (true) {
//                System.out.print("Enter your search query: ");
//                String userQuery = scanner.nextLine();
//                if (userQuery.equalsIgnoreCase("exit")) break;
//
//                // 5. Process query
//                Map<String, Double> queryVector = processor.processQuery(userQuery, docs.size());
//
//                System.out.println("Query Vector: " + queryVector);
//
//                // 6. Rank documents
//                List<ScoredDocument> rankedDocs = scorer.rankDocuments(queryVector);
//
//                // 7. Display results
//                if (rankedDocs.isEmpty()) {
//                    System.out.println("No relevant documents found.\n");
//                    continue;
//                }
//
//                System.out.println("\nTop Results:");
//                int count = 0;
//                for (ScoredDocument scored : rankedDocs) {
//                    if (count >= 10) break;
//                    System.out.printf("Title: %-30s | Score: %.4f\n", scored.doc.title, scored.score);
//                    System.out.println("URL: " + scored.doc.url);
//                    System.out.println("--------------------------------------------------");
//                    count++;
//                }
//            }
//            System.out.println("Search session ended.");
//        }