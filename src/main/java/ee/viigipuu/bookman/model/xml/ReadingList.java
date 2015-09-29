package ee.viigipuu.bookman.model.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/***
 * Model class for xml file
 *
 * @author raul
 */
@XStreamAlias("readingList")
public class ReadingList {

	@XStreamImplicit
	private List<Year> yearList;

	public List<Year> getYearList() { return yearList; }
	public void setYearList(List<Year> yearList) { this.yearList = yearList; }
}
