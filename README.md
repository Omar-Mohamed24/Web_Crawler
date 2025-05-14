# 🔍 Java Search Engine

A simple search engine that crawls web pages, builds an inverted index, computes TF-IDF scores, and ranks search results using cosine similarity.

![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)
![Status](https://img.shields.io/badge/status-Completed-brightgreen.svg)

---

## 📌 Features

- 🌐 **Web Crawler** — Fetches text content from seed URLs using [Jsoup](https://jsoup.org/).
- 🧠 **Inverted Index** — Maps each stemmed word to documents and their term frequency.
- 📊 **TF-IDF Calculation** — Computes weighted term importance per document.
- 📈 **Cosine Similarity** — Ranks documents by their relevance to the query.
- 🧹 **Text Preprocessing** — Removes stop words and stems tokens for accuracy.

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- [Jsoup Library](https://jsoup.org/download)

Download the `.jar` file and place it in your project directory, or add it as a dependency via your build tool (e.g., Maven or Gradle).


---

## 🧠 How It Works

### 1. 🌍 Web Crawler

- Accepts a list of seed URLs.
- Uses Jsoup to fetch and parse HTML content.
- Extracts visible text while skipping scripts, styles, and metadata.
- Normalizes and extracts hyperlinks.

### 2. 🧱 Inverted Index Construction

- Tokenizes document text into lowercase words.
- Removes common stop words (e.g., "the", "is", "and").
- Applies Porter stemming (e.g., "pharaohs" → "pharaoh").
- Records each term’s frequency in a document and stores them in a hash map.

### 3. 📐 TF-IDF Vectorization

Each document is represented as a vector of term weights:

- **TF (Term Frequency)**:  
  `1 + log10(frequency of term in document)`

- **IDF (Inverse Document Frequency)**:  
  `log10(total documents / number of documents containing the term)`

- **TF-IDF Score**:  
  `TF * IDF`

### 4. 🔍 Query Processing

- Tokenize the input query.
- Remove stop words and apply stemming.
- Build the query vector using the same TF-IDF scheme.

### 5. 📈 Cosine Similarity Scoring

Cosine similarity is used to compute how close the query is to each document:

```math
cosine_similarity(q, d) = (q · d) / (||q|| * ||d||)
```

Where:

- `q · d` is the dot product.
- `||q||` and `||d||` are the vector magnitudes.
- Documents with higher cosine scores are considered more relevant.

---

## 💻 Example Session

```yaml
Search Engine is ready! Type your query below.
Type 'exit' to quit.

Enter your search query: pharaohs

Top Results:
Title: Doc1                           | Score: 0.3110
URL: http://doc1.com
--------------------------------------------------
Title: Doc2                           | Score: 0.1999
URL: http://doc2.com
--------------------------------------------------
```

---

## 🧪 Testing Without Crawling

You can test the search engine with mock data instead of real URLs:

```java
docs.add(new DocumentData("http://doc1.com", "Doc1", "Document about ancient Egypt and Pharaohs."));
docs.add(new DocumentData("http://doc2.com", "Doc2", "Pharaohs ruled ancient lands and built pyramids."));
docs.add(new DocumentData("http://doc3.com", "Doc3", "Modern Egypt is famous for pyramids and tourism."));
```

---

## 📋 Project Structure

```text
├── Main.java
├── WebCrawler.java
├── DocumentData.java
├── InvertedIndex.java
├── TermData.java
├── Posting.java
├── Stemmer.java
├── StopWords.java
├── TFIDF_Calculator.java
├── QueryProcessor.java
├── SimilarityScorer.java
└── ScoredDocument.java
```

---

## ❓ Troubleshooting

- **Query returns wrong scores**:  
  Ensure TF-IDF formulas match both for documents and queries.

- **URLs appear broken in console**:  
  The terminal might not highlight special characters like `()` in links. They are still valid.

- **No results found**:  
  Check that query terms exist in the index and aren’t filtered as stop words.

---
