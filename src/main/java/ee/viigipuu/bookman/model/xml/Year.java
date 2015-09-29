package ee.viigipuu.bookman.model.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/***
 * Model class for xml file
 *
 * @author raul
 */
@XStreamAlias("year")
public class Year {

	@XStreamAlias("value")
	@XStreamAsAttribute
	private String year;
	@XStreamAlias("comment")
	@XStreamAsAttribute
	private String comment;
	@XStreamImplicit
	private List<BookEntry> bookList;

	public Year() {

		this.bookList = new ArrayList<BookEntry>();
	}

	public String getYear() { return year; }
	public void setYear(String year) { this.year = year; }
	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }
	public List<BookEntry> getBookList() { return bookList; }
	public void setBookList(List<BookEntry> bookList) { this.bookList = bookList; }
}
