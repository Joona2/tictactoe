package tictactoe;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*
 * Program name: Computer Difficulty Controller
 * Programmer: Joona Huomo
 * Date: January 4th 2021
 * Description: Stores values for if the user chose to play against the computer or not, as well as what difficulty they chose
 */

public class ComputerDifficultyController {

	// changebuttonhandler
	public void changeSceneButtonHandlerTwo(ActionEvent evt) throws IOException {

		// Gets the label on the button
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();

		// Sets the difficulty to easy, or hard depending on which button was pressed,
		// and sets computer to true
		Variables.difficulty = buttonLabel;
		Variables.computer = true;

		// loads the main game screen
		BorderPane sceneTwoParent = (BorderPane) FXMLLoader.load(getClass().getResource("TicTacToe.fxml"));
		Scene sceneTwo = new Scene(sceneTwoParent, 300, 400);

		Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

		stage.setScene(sceneTwo);
		stage.show();
	}

}
