Imagine if you were surfing a website without search engines, how long would it take to find contents you want? Search Engines matter because they essentially act as filters for the wealth of information available on the Internet, and allow users to quickly and easily find information that is value to them, without the need to wade through various irrelevant web pages.  Because of  many advantages of search engine, we have made a search engine for ASI Home website. Our search engine has two major functions. 

1.Crawling and Indexing
Node.js and cheerio, a JavaScript for HTML parser, are used to build a crawler.
The crawl process begins with a list of web pages or sitemaps provided by website owners. As the crawler visit these web pages, it looks for important information such as header and title. These information will be used to create an index file which will be used for search.  

2. Providing Answers
 To provide answers to users, lunr.js, a full-text search engine, is used to perform search on an index file which is built by using the crawler. It will return a ranked list of the results  which is matched by a search.  Each result has two properties:
- ref : the document reference. 
- score : a relative measure of how similar this document is to the query. The results are sorted by this property.

With the new search functionality on ASI Home, users will be able to look up the content faster than ever.
