package ee.viigipuu.bookman.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No book Selected");
            alert.setContentText("Please select a book in the table.");

            alert.showAndWait();
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
	    	Alert alert = new Alert(AlertType.WARNING);
	    	alert.initOwner(mainApp.getPrimaryStage());
	    	alert.setTitle("No selection!");
	    	alert.setHeaderText("No book selected!");
	    	alert.setContentText("Select something in the table.");

	    	alert.showAndWait();
	    }
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
}
