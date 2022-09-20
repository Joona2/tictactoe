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
 * Program name: Start Screen Controller
 * Programmer: Joona Huomo
 * Date: January 4th 2021
 * Description: Decides which scene to move to next, depending on if the user chose to play against another player, or the computer
 */

public class StartScreenController {

	public void changeSceneButtonHandlerTwo(ActionEvent evt) throws IOException {

		// Stores the label of the button that was clicked
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();

		// if the label is PvP, it goes straight to the game board
		if (buttonLabel.equals("PvP")) {

			BorderPane sceneTwoParent = (BorderPane) FXMLLoader.load(getClass().getResource("TicTacToe.fxml"));
			Scene sceneTwo = new Scene(sceneTwoParent, 300, 400);

			Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

			stage.setScene(sceneTwo);
			stage.show();

		}

		// if the label is PvE, then it goes to the computer difficulty, and lets the
		// user choose it
		else if (buttonLabel.equals("PvE")) {

			BorderPane sceneTwoParent = (BorderPane) FXMLLoader.load(getClass().getResource("ComputerDifficulty.fxml"));
			Scene sceneTwo = new Scene(sceneTwoParent, 300, 400);

			Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

			stage.setScene(sceneTwo);
			stage.show();
		}

	}
}
