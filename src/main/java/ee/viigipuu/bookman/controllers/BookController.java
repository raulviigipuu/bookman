package ee.viigipuu.bookman.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import com.thoughtworks.xstream.XStream;

import ee.viigipuu.bookman.Main;
import ee.viigipuu.bookman.model.fx.Book;
import ee.viigipuu.bookman.model.xml.BookEntry;
import ee.viigipuu.bookman.model.xml.ReadingList;
import ee.viigipuu.bookman.model.xml.Year;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;

/***
 * Controller for main view
 *
 * @author raul
 */
public class BookController implements Initializable {

	@FXML
	private TableView<Book> bookTableView;
	@FXML
	private TableColumn<Book, String> yearColumn;
	@FXML
	private TableColumn<Book, String> titleColumn;
	@FXML
	private TableColumn<Book, String> additionalInfoColumn;
	@FXML
	private TableColumn<Book, String> authorColumn;
	@FXML
	private TableColumn<Book, String> publisherColumn;
	@FXML
	private TableColumn<Book, String> yearPublishedColumn;
	@FXML
	private TableColumn<Book, String> yearFirstPublishedColumn;
	@FXML
	private TableColumn<Book, String> pageCountColumn;

	@FXML
	private Label yearLabel;
	@FXML
	private Label titleLabel;
	@FXML
	private Label additionalInfoLabel;
	@FXML
	private Label authorLabel;
	@FXML
	private Label publisherLabel;
	@FXML
	private Label yearPublishedLabel;
	@FXML
	private Label yearFirstPublishedLabel;
	@FXML
	private Label pageCountLabel;
	@FXML
	private Label quoteTextLabel;
	@FXML
	private Label quotePageLabel;
	@FXML
	private Label sourceNameLabel;
	@FXML
	private Label sourceUrlLabel;

	private Main mainApp;
	private ObservableList<Book> bookListObservable;
	private ReadingList readingList;

	private static final String FILE_PATH = "./books.xml";
	private static final String JSON_FILE_NAME = "books.json";

	public BookController() {}

	public void setMainApp(Main mainApp) {

		this.mainApp = mainApp;
	}

	/***
	 * Initial list is loaded and displayed
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty());
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
		additionalInfoColumn.setCellValueFactory(cellData -> cellData.getValue().additionalInfoProperty());
		authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
		publisherColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
		yearPublishedColumn.setCellValueFactory(cellData -> cellData.getValue().yearPublishedProperty());
		yearFirstPublishedColumn.setCellValueFactory(cellData -> cellData.getValue().yearFirstPublishedProperty());
		pageCountColumn.setCellValueFactory(cellData -> cellData.getValue().pageCountProperty());

		List<Book> bookList = booksFromXml();
		bookListObservable = FXCollections.observableArrayList(bookList);
		bookTableView.setItems(bookListObservable);

		// Initialize detail view with blank values
		showBookDetails(null);

		bookTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showBookDetails(newValue));
	}

	// File -> Exit
	@FXML
	private void handleExit() {

		System.exit(0);
	}

	// File -> New book entry
	@FXML
	private void handleNewBook() {

		Book tempBook = new Book();
		boolean okClicked = mainApp.showBookEditDialog(tempBook);
		if(okClicked) {

			bookListObservable.add(0, tempBook);
			booksToXml(bookListObservable);
			booksFromXml(); // To update this.readingList for JSON export
		}
	}

	// File -> Export to JSON
	@FXML
	private void handleJsonExport() {

		String jsonStr = "";
		if(this.readingList != null && this.readingList.getYearList() != null && this.readingList.getYearList().size() > 0) {

			jsonStr = constructJson();

			final DirectoryChooser directoryChooser = new DirectoryChooser();
			final File selectedDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());
			if(selectedDirectory != null) {

				selectedDirectory.getAbsolutePath();
			}
			File jsonFile = new File(selectedDirectory, JSON_FILE_NAME);
			writeStringToFile(jsonFile, jsonStr);
		} else {

			alert("Empty list!", "Nothing to export!", "There no items in list!");
		}
	}

	// Edit button clicked
	@FXML
	private void handleEditBook() {

		Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
		if(selectedBook != null) {

			boolean okClicked = mainApp.showBookEditDialog(selectedBook);
			if(okClicked) {

				showBookDetails(selectedBook);
				booksToXml(bookListObservable);
			}
		} else {

			// Nothing selected
			alert("No Selection", "No book Selected", "Please select a book in the table.");
		}
	}

	// Delete button clicked
	@FXML
	private void handleDeleteBook() {

		// Get selected item
	    int selectedIndex = bookTableView.getSelectionModel().getSelectedIndex();
	    if(selectedIndex >= 0) {

	    	bookTableView.getItems().remove(selectedIndex);
	    	booksToXml(bookListObservable);
	    } else {

	    	// Nothing selected
	    	alert("No selection!", "No book selected!", "Select something in the table.");
	    }
	}

	// Makes correctly formatted JSON string
	private String constructJson() {

		StringBuilder jsonBuilder = new StringBuilder();

		jsonBuilder.append("{\"readingList\": [\n\t{\n");

		List<Year> yearList = this.readingList.getYearList();
		for(int j = 0; j < yearList.size(); j++) {

			jsonBuilder.append("\t\t\"year\": \"").append(yearList.get(j).getYear()).append("\",\n");
			jsonBuilder.append("\t\t\"comment\": \"").append((yearList.get(j).getComment() != null) ? yearList.get(j).getComment() : "").append("\",\n");
			jsonBuilder.append("\t\t\"bookList\": [");
			List<BookEntry> bookList = yearList.get(j).getBookList();
			for(int i = 0; i < bookList.size(); i++) {

				jsonBuilder.append("{\n\t\t\t\"title\": \"").append(bookList.get(i).getTitle().replace("\"", "\\\"")).append("\",\n");
				jsonBuilder.append("\t\t\t\"additionalInfo\": \"").append(bookList.get(i).getAdditionalInfo().replace("\"", "\\\"")).append("\",\n");
				jsonBuilder.append("\t\t\t\"author\": \"").append(bookList.get(i).getAuthor().replace("\"", "\\\"")).append("\",\n");
				jsonBuilder.append("\t\t\t\"publisher\": \"").append(bookList.get(i).getPublisher()).append("\",\n");
				jsonBuilder.append("\t\t\t\"yearPublished\": \"").append(bookList.get(i).getYearPublished()).append("\",\n");
				jsonBuilder.append("\t\t\t\"yearFirstPublished\": \"").append(bookList.get(i).getYearFirstPublished()).append("\",\n");
				jsonBuilder.append("\t\t\t\"pageCount\": \"").append(bookList.get(i).getPageCount()).append("\",\n");
				if(bookList.get(i).getQuoteText() != null && bookList.get(i).getQuoteText().length() > 0) {

					jsonBuilder.append("\t\t\t\"quote\": {\n");
					jsonBuilder.append("\t\t\t\t\"quote\": \"").append(bookList.get(i).getQuoteText().replace("\"", "\\\"")).append("\",\n");
					jsonBuilder.append("\t\t\t\t\"page\": \"").append(bookList.get(i).getQuotePage()).append("\"\n");
					jsonBuilder.append("\t\t\t},\n");
				} else {
					jsonBuilder.append("\t\t\t\"quote\": \"").append(bookList.get(i).getQuoteText()).append("\",\n");
				}
				if(bookList.get(i).getSourceName() != null && bookList.get(i).getSourceName().length() > 0) {

					jsonBuilder.append("\t\t\t\"source\": {\n");
					jsonBuilder.append("\t\t\t\t\"name\": \"").append(bookList.get(i).getSourceName()).append("\",\n");
					jsonBuilder.append("\t\t\t\t\"url\": \"").append(bookList.get(i).getSourceUrl()).append("\"\n");
					// End of the list?
					if(i < (bookList.size() - 1)) {

						jsonBuilder.append("\t\t\t}\n\t\t},");
					} else {
						jsonBuilder.append("\t\t\t}\n\t\t}");
					}
				} else {
					jsonBuilder.append("\t\t\t\"source\": \"\"\n");
					// End of the list?
					if(i < (bookList.size() - 1)) {

						jsonBuilder.append("\t\t},");
					} else {
						jsonBuilder.append("\t\t}");
					}
				}
			}
			// End of the list?
			if(j < (yearList.size() - 1)) {

				jsonBuilder.append("]\n\t},{\n");
			} else {
				jsonBuilder.append("]\n\t}\n");
			}
		}
		jsonBuilder.append("]}\n");

		return jsonBuilder.toString();
	}

	// Display book details in main window
	private void showBookDetails(Book book) {

		if(book == null) {
			fillWithBlanks();
			return;
		}

		yearLabel.setText(book.getYear());
		titleLabel.setText(book.getTitle());
		additionalInfoLabel.setText(book.getAdditionalInfo());
		authorLabel.setText(book.getAuthor());
		publisherLabel.setText(book.getPublisher());
		yearPublishedLabel.setText(book.getYearPublished());
		yearFirstPublishedLabel.setText(book.getYearFirstPublished());
		pageCountLabel.setText(book.getPageCount());
		quoteTextLabel.setText(book.getQuoteText());
		quotePageLabel.setText(book.getQuotePage());
		sourceNameLabel.setText(book.getSourceName());
		sourceUrlLabel.setText(book.getSourceUrl());
	}

	// All book detail fields to blank (nothing is selected)
	private void fillWithBlanks() {

		yearLabel.setText("");
		titleLabel.setText("");
		additionalInfoLabel.setText("");
		authorLabel.setText("");
		publisherLabel.setText("");
		yearPublishedLabel.setText("");
		yearFirstPublishedLabel.setText("");
		pageCountLabel.setText("");
		quoteTextLabel.setText("");
		quotePageLabel.setText("");
		sourceNameLabel.setText("");
		sourceUrlLabel.setText("");
	}

	/***
	 * Display alert
	 *
	 * @param title
	 * @param header
	 * @param text
	 */
	private void alert(String title, String header, String text) {

		Alert alert = new Alert(AlertType.WARNING);

	    alert.initOwner(mainApp.getPrimaryStage());
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(text);

	    alert.showAndWait();
	}

	// Read books from xml file
	private List<Book> booksFromXml() {

		XStream xstream = new XStream();
		xstream.processAnnotations(BookEntry.class);
		xstream.processAnnotations(Year.class);
		xstream.processAnnotations(ReadingList.class);

		List<Book> bookList;

		Path filePath = Paths.get(FILE_PATH);
		if( ! Files.exists(filePath)) {

			try {

				Files.createFile(filePath);
				return new ArrayList<Book>();
			} catch(IOException ioe) {

				ioe.printStackTrace();
			}

		}

		String xmlStr = readFileContent(filePath);
		if(xmlStr.length() == 0) return new ArrayList<Book>();

		this.readingList = (ReadingList)xstream.fromXML(xmlStr);
		bookList = new ArrayList<Book>();
		Book book;
		for(Year year: readingList.getYearList()) {

			for(BookEntry bookEntry: year.getBookList()) {

				book = new Book();
				book.setYear(year.getYear());
				book.setTitle(bookEntry.getTitle());
				book.setAdditionalInfo(bookEntry.getAdditionalInfo());
				book.setAuthor(bookEntry.getAuthor());
				book.setPublisher(bookEntry.getPublisher());
				book.setYearPublished(bookEntry.getYearPublished());
				book.setYearFirstPublished(bookEntry.getYearFirstPublished());
				book.setPageCount(bookEntry.getPageCount());
				book.setQuoteText(bookEntry.getQuoteText());
				book.setQuotePage(bookEntry.getQuotePage());
				book.setSourceName(bookEntry.getSourceName());
				book.setSourceUrl(bookEntry.getSourceUrl());
				bookList.add(book);
			}
		}

		return bookList;
	}

	// Write books to xml file
	private void booksToXml(List<Book> bookList) {

		XStream xstream = new XStream();
		xstream.processAnnotations(BookEntry.class);
		xstream.processAnnotations(Year.class);
		xstream.processAnnotations(ReadingList.class);

		ReadingList readingList = new ReadingList();

		SortedMap<String, Year> yearMap = new TreeMap<String, Year>(Collections.reverseOrder());
		Year curYear;
		BookEntry bookEntry;
		for(Book book : bookList) {

			if(yearMap.containsKey(book.getYear())) {

				curYear = yearMap.get(book.getYear());
			} else {
				curYear = new Year();
				curYear.setYear(book.getYear());
				yearMap.put(book.getYear(), curYear);
			}

			bookEntry = new BookEntry();
			bookEntry.setTitle(book.getTitle());
			bookEntry.setAdditionalInfo(book.getAdditionalInfo());
			bookEntry.setAuthor(book.getAuthor());
			bookEntry.setPublisher(book.getPublisher());
			bookEntry.setYearPublished(book.getYearPublished());
			bookEntry.setYearFirstPublished(book.getYearFirstPublished());
			bookEntry.setPageCount(book.getPageCount());
			bookEntry.setQuoteText(book.getQuoteText());
			bookEntry.setQuotePage(book.getQuotePage());
			bookEntry.setSourceName(book.getSourceName());
			bookEntry.setSourceUrl(book.getSourceUrl());

			curYear.getBookList().add(bookEntry);
		}

		readingList.setYearList(new ArrayList<Year>(yearMap.values()));


		try {

			File file = new File(FILE_PATH);

			if( ! file.exists()) {

				file.createNewFile();
			}

			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			xstream.toXML(readingList, bufWriter);
		} catch(IOException ioe) {

			ioe.printStackTrace();
		}
	}

	// Utility - read file contents to String
	public String readFileContent(Path filePath) {

		String fileContent = "";

		try {
			fileContent = new String(Files.readAllBytes(filePath));
		} catch(IOException ioe) {

			ioe.printStackTrace();
		}

		return fileContent;
	}

	// Write string to file
	public void writeStringToFile(File file, String content) {

		try {

			OutputStreamWriter outStreamWriter = new OutputStreamWriter(
					new FileOutputStream(file.getAbsoluteFile()), Charset.forName("UTF-8").newEncoder());
			outStreamWriter.write(content);
			outStreamWriter.flush();
			outStreamWriter.close();
		} catch(IOException ioe) {

			ioe.printStackTrace();
		}
	}
}
