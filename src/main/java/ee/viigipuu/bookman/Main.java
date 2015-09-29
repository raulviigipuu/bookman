package ee.viigipuu.bookman;

import java.io.IOException;

import ee.viigipuu.bookman.controllers.BookController;
import ee.viigipuu.bookman.controllers.BookEditDialogController;
import ee.viigipuu.bookman.model.fx.Book;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

//TODO: comment code
//TODO: README.md
//TODO: GUI-s sättida asjad nii et tekst oleks kogu aeg nähtaval
//TODO: recheck code structure also(XmlOps maybe?)

/***
 * Main class, starting UI
 *
 * @author raul
 */
public class Main extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {

		try {

			this.primaryStage = primaryStage;

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("Main.fxml"));
			loader.load();
			BookController bookController = loader.getController();
			bookController.setMainApp(this);
			Scene scene = new Scene(loader.getRoot(), 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Bookman");
			primaryStage.setScene(scene);
			primaryStage.setWidth(1000);
			primaryStage.setHeight(800);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() { return this.primaryStage; }

	public boolean showBookEditDialog(Book book) {

		try {

			// Load FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("BookEditDialog.fxml"));
			GridPane dialogForm = (GridPane) loader.load();

			// Set up stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Book");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(dialogForm);
			dialogStage.setScene(scene);

			// Set book to be edited
			BookEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setBook(book);

			// Show dialog
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch(IOException ioe) {

			ioe.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {

		launch(args);
	}
}
