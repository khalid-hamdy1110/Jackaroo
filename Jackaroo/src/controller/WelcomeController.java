package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WelcomeController {
	@FXML TextField usernameTextField;
	@FXML Label errorMsg;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void play(ActionEvent e) throws IOException {
		
		String username = usernameTextField.getText();
		if (username != "") {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
			root = loader.load();
			
			MainController mainController = loader.getController();
			mainController.initGame(username);
			
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/styles.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} else {
			errorMsg.setText("* Must enter a username!");
		}
	}
}
