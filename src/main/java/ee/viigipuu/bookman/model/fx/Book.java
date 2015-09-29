package ee.viigipuu.bookman.model.fx;

import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/***
 * Model class for JavaFX
 *
 * @author raul
 */
public class Book {

	private StringProperty year;
	private StringProperty title;
	private StringProperty additionalInfo;
	private StringProperty author;
	private StringProperty publisher;
	private StringProperty yearPublished;
	private StringProperty yearFirstPublished;
	private StringProperty pageCount;
	private StringProperty quoteText;
	private StringProperty quotePage;
	private StringProperty sourceName;
	private StringProperty sourceUrl;

	public Book() {

		this(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)), "", "", "", "", "", "", "", "", "", "", "");
	}

	public Book(String year, String title, String additionalInfo, String author, String publisher, String yearPublished,
			String yearFirstPublished, String pageCount, String quoteText, String quotePage, String sourceName, String sourceUrl) {

		this.year = new SimpleStringProperty(year);
		this.title = new SimpleStringProperty(title);
		this.additionalInfo = new SimpleStringProperty(additionalInfo);
		this.author = new SimpleStringProperty(author);
		this.publisher = new SimpleStringProperty(publisher);
		this.yearPublished = new SimpleStringProperty(yearPublished);
		this.yearFirstPublished = new SimpleStringProperty(yearFirstPublished);
		this.pageCount = new SimpleStringProperty(pageCount);
		this.quoteText = new SimpleStringProperty(quoteText);
		this.quotePage = new SimpleStringProperty(quotePage);
		this.sourceName = new SimpleStringProperty(sourceName);
		this.sourceUrl = new SimpleStringProperty(sourceUrl);
	}

	public String getYear() { return year == null ? Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) : year.get(); }
	public void setYear(String year) { this.year.set(year); }
	public StringProperty yearProperty() { return year; }

	public String getTitle() { return title == null ? "" : title.get(); }
	public void setTitle(String title) { this.title.set(title); }
	public StringProperty titleProperty() { return title; }

	public String getAdditionalInfo() { return additionalInfo == null ? "" : additionalInfo.get(); }
	public void setAdditionalInfo(String additionalInfo) { this.additionalInfo.set(additionalInfo); }
	public StringProperty additionalInfoProperty() { return additionalInfo; }

	public String getAuthor() { return author == null ? "" : author.get(); }
	public void setAuthor(String author) { this.author.set(author); }
	public StringProperty authorProperty() { return author; }

	public String getPublisher() { return publisher == null ? "" : publisher.get(); }
	public void setPublisher(String publisher) { this.publisher.set(publisher); }
	public StringProperty publisherProperty() { return publisher; }

	public String getYearPublished() { return yearPublished == null ? "" : yearPublished.get(); }
	public void setYearPublished(String yearPublished) { this.yearPublished.set(yearPublished); }
	public StringProperty yearPublishedProperty() { return yearPublished; }

	public String getYearFirstPublished() { return yearFirstPublished == null ? "" : yearFirstPublished.get(); }
	public void setYearFirstPublished(String yearFirstPublished) { this.yearFirstPublished.set(yearFirstPublished); }
	public StringProperty yearFirstPublishedProperty() { return yearFirstPublished; }

	public String getPageCount() { return pageCount == null ? "" : pageCount.get(); }
	public void setPageCount(String pageCount) { this.pageCount.set(pageCount); }
	public StringProperty pageCountProperty() { return pageCount; }

	public String getQuoteText() { return quoteText == null ? "" : quoteText.get(); }
	public void setQuoteText(String quoteText) { this.quoteText.set(quoteText); }
	public StringProperty quoteTextProperty() { return quoteText; }

	public String getQuotePage() { return quotePage == null ? "" : quotePage.get(); }
	public void setQuotePage(String quotePage) { this.quotePage.set(quotePage); }
	public StringProperty quotePageProperty() { return quotePage; }

	public String getSourceName() { return sourceName == null ? "" : sourceName.get(); }
	public void setSourceName(String sourceName) { this.sourceName.set(sourceName); }
	public StringProperty sourceNameProperty() { return sourceName; }

	public String getSourceUrl() { return sourceUrl == null ? "" : sourceUrl.get(); }
	public void setSourceUrl(String sourceUrl) { this.sourceUrl.set(sourceUrl); }
	public StringProperty sourceUrlProperty() { return sourceUrl; }

	@Override
	public String toString() {

		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("Year: ").append(getYear()).append("\n");
		strBuilder.append("Title: ").append(getTitle()).append("\n");
		strBuilder.append("Additional info: ").append(getAdditionalInfo()).append("\n");
		strBuilder.append("Author: ").append(getAuthor()).append("\n");
		strBuilder.append("Publisher: ").append(getPublisher()).append("\n");
		strBuilder.append("Year published: ").append(getYearPublished()).append("\n");
		strBuilder.append("Year first published: ").append(getYearFirstPublished()).append("\n");
		strBuilder.append("Page count: ").append(getPageCount()).append("\n");
		strBuilder.append("Quote text: ").append(getQuoteText()).append("\n");
		strBuilder.append("Quote page: ").append(getQuotePage()).append("\n");
		strBuilder.append("Source name: ").append(getSourceName()).append("\n");
		strBuilder.append("Source url: ").append(getSourceUrl()).append("\n");

		return strBuilder.toString();
	}
}
