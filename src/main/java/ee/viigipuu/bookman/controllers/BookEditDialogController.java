package ee.viigipuu.bookman.controllers;

import ee.viigipuu.bookman.model.fx.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/***
 * Controller for book edit form
 *
 * @author raul
 */
public class BookEditDialogController {

	@FXML
	private TextField yearTF;
	@FXML
	private TextField titleTF;
	@FXML
	private TextField additionalInfoTF;
	@FXML
	private TextField authorTF;
	@FXML
	private TextField publisherTF;
	@FXML
	private TextField yearPublishedTF;
	@FXML
	private TextField yearFirstPublishedTF;
	@FXML
	private TextField pageCountTF;
	@FXML
	private TextField quoteTextTF;
	@FXML
	private TextField quotePageTF;
	@FXML
	private TextField sourceNameTF;
	@FXML
	private TextField sourceUrlTF;


	private Stage dialogStage;
	private Book book;
    private boolean okClicked = false;

	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {}

    public void setDialogStage(Stage dialogStage) {

    	this.dialogStage = dialogStage;
    }

    // Filling the fields
    public void setBook(Book book) {

    	this.book = book;

    	yearTF.setText(book.getYear());
    	titleTF.setText(book.getTitle());
    	additionalInfoTF.setText(book.getAdditionalInfo());
    	authorTF.setText(book.getAuthor());
    	publisherTF.setText(book.getPublisher());
    	yearPublishedTF.setText(book.getYearPublished());
    	yearFirstPublishedTF.setText(book.getYearFirstPublished());
    	pageCountTF.setText(book.getPageCount());
    	quoteTextTF.setText(book.getQuoteText());
    	quotePageTF.setText(book.getQuotePage());
    	sourceNameTF.setText(book.getSourceName());
    	sourceUrlTF.setText(book.getSourceUrl());
    }

    public boolean isOkClicked() {

        return okClicked;
    }

    // Ok button clicked
    @FXML
	private void handleOK() {

    	if(inputValidate()) {

    		book.setYear(yearTF.getText());
    		book.setTitle(titleTF.getText());
    		book.setAdditionalInfo(additionalInfoTF.getText());
    		book.setAuthor(authorTF.getText());
    		book.setPublisher(publisherTF.getText());
    		book.setYearPublished(yearPublishedTF.getText());
    		book.setYearFirstPublished(yearFirstPublishedTF.getText());
    		book.setPageCount(pageCountTF.getText());
    		book.setQuoteText(quoteTextTF.getText());
    		book.setQuotePage(quotePageTF.getText());
    		book.setSourceName(sourceNameTF.getText());
    		book.setSourceUrl(sourceUrlTF.getText());

    		okClicked = true;
    		dialogStage.close();
    	} else {

	    	// Validation failed
	    	Alert alert = new Alert(AlertType.WARNING);
	    	alert.initOwner(dialogStage.getOwner());
	    	alert.setTitle("Validation failed!");
	    	alert.setHeaderText("Book validation failed!");
	    	alert.setContentText("Title and/or year missing!");

	    	alert.showAndWait();
    	}
    }

    // Cancel button clicked
    @FXML
	private void handleCancel() {

    	dialogStage.close();
    }

    // Form validation
    private boolean inputValidate() {

    	return (yearTF.getText() != null && yearTF.getText().length() == 4 && titleTF.getText() != null && titleTF.getText().length() > 0);
    }
}
