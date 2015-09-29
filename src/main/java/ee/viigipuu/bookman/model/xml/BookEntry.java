package ee.viigipuu.bookman.model.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/***
 * Model class for xml file
 *
 * @author raul
 */
@XStreamAlias("book")
public class BookEntry {

	@XStreamAlias("title")
	private String title;
	@XStreamAlias("additionalInfo")
	private String additionalInfo;
	@XStreamAlias("author")
	private String author;
	@XStreamAlias("publisher")
	private String publisher;
	@XStreamAlias("yearPublished")
	private String yearPublished;
	@XStreamAlias("yearFirstPublished")
	private String yearFirstPublished;
	@XStreamAlias("pageCount")
	private String pageCount;
	@XStreamAlias("quoteText")
	private String quoteText;
	@XStreamAlias("quotePage")
	private String quotePage;
	@XStreamAlias("sourceName")
	private String sourceName;
	@XStreamAlias("sourceUrl")
	private String sourceUrl;


	public BookEntry() {}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getAdditionalInfo() { return additionalInfo; }
	public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
	public String getAuthor() { return author; }
	public void setAuthor(String author) { this.author = author; }
	public String getPublisher() { return publisher; }
	public void setPublisher(String publisher) { this.publisher = publisher; }
	public String getYearPublished() { return yearPublished; }
	public void setYearPublished(String yearPublished) { this.yearPublished = yearPublished; }
	public String getYearFirstPublished() { return yearFirstPublished; }
	public void setYearFirstPublished(String yearFirstPublished) { this.yearFirstPublished = yearFirstPublished; }
	public String getPageCount() { return pageCount; }
	public void setPageCount(String pageCount) { this.pageCount = pageCount; }
	public String getQuoteText() { return quoteText; }
	public void setQuoteText(String quoteText) { this.quoteText = quoteText; }
	public String getQuotePage() { return quotePage; }
	public void setQuotePage(String quotePage) { this.quotePage = quotePage; }
	public String getSourceName() { return sourceName; }
	public void setSourceName(String sourceName) { this.sourceName = sourceName; }
	public String getSourceUrl() { return sourceUrl; }
	public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
}
