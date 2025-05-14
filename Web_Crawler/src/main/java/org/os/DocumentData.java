package org.os;

/**
   A simple class to represent the data of a crawled document.
**/
public class DocumentData
{
    /// The URL of the document (web page)
    public String url;

    /// The title of the document (web page title)
    public String title;

    /// The main text content extracted from the document
    public String text;

    public DocumentData(String url, String title, String text)
    {
        this.url = url;
        this.title = title;
        this.text = text;
    }
}
