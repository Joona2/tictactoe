package tictactoe;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

/*
 * Program name: TicTacToe Controller
 * Programmer: Joona Huomo
 * Date: January 4th 2021
 * Description: Contains all the methods that are required for the game of TicTacToe to function
 */

//tictactoe controller
public class TicTacToeController {

	// linking all the buttons from the fxml file
	@FXML
	Button b00;
	@FXML
	Button b01;
	@FXML
	Button b02;
	@FXML
	Button b10;
	@FXML
	Button b11;
	@FXML
	Button b12;
	@FXML
	Button b20;
	@FXML
	Button b21;
	@FXML
	Button b22;

	// linking the gameboard and the scores from the fxml file
	@FXML
	GridPane gameBoard;
	@FXML
	private Label scoreOne;
	@FXML
	private Label scoreTwo;

	// making booleans that show if it is the first player's turn or not, and if a
	// button click is allowed
	private boolean isFirstPlayer = true;
	private boolean allowButtonClick = true;

	// creates boolean that tells if a text was set to a button
	boolean setText = false;

	// declare time variables
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
	LocalDateTime now = LocalDateTime.now();

	// creates a 2d array to store the buttons in, and log
	private Button[][] gameBoardArr = new Button[3][3];
	public String[] gameLog = new String[9];

	// creates count variable
	public int count = 0;

	// creates the secondary stage for pop-up windows
	static Stage secondaryStage;

	// method that closes a window
	public void closeWindow(final ActionEvent evt) {
		final Node source = (Node) evt.getSource();
		final Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	// when the user clicks a button, this method will detect whether the user has
	// chosen to play singleplayer, or multiplayer, if they chose singleplayer,
	// then it will decide whether to use the hard mode or easy mode.
	public void buttonClickHandler(ActionEvent evt) {

		// makes a variable that is called buttonlabel
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();

		// if the buttonlabel of the button that the user tried to press on is X, or O,
		// it just leaves the method, and the user has to click something else
		if (buttonLabel.equals("X") || buttonLabel.equals("O")) {
			return;
		}

		// updates the values in the gameBoardArr array that stores all the buttons
		resetGameBoard();

		// if a button click is allowed
		if (allowButtonClick) {

			// if the user chose to play against the computer
			if (Variables.computer) {

				// if the user chose easy mode
				if (Variables.difficulty.equals("Easy")) {

					// runs the onButtonClick method, which will place an X on the button that was
					// clicked by the user
					onButtonClick(evt);

					// if there are 3 things in a row other than empty
					if (find3InARow()) {

						// player one gets a point and allow click is set to false, then it leaves the
						// method
						scoreOne.setText(Integer.toString((Integer.parseInt(scoreOne.getText()) + 1)));
						allowButtonClick = false;
						return;
					}

					// while loop that runs until it generates a valid button to place the mark on
					while (true) {

						// generates random numbers from 0-2 for the row and column
						int row = (int) (Math.random() * 3);
						int column = (int) (Math.random() * 3);

						// if there is a blank space at that position on the gameBoard, then it will set
						// that to O, and set firstplayer to true, and then exit the while loop
						if (gameBoardArr[row][column].getText() == "") {
							gameBoardArr[row][column].setText("O");
							isFirstPlayer = true;
							gameLog[count] = "O " + gameBoardArr[row][column].getId().substring(1);
							count++;
							break;
						}
					}

					// if there are 3 things in a row other than empty
					if (find3InARow()) {

						// player two gets a point, and allowbuttonclick is set to false
						scoreTwo.setText(Integer.toString((Integer.parseInt(scoreTwo.getText()) + 1)));
						allowButtonClick = false;
					}

					// if the user chose hard mode
				} else {

					// sets setText to false
					setText = false;

					// runs the onButtonClick method, which will place an X on the button that was
					// clicked by the user
					onButtonClick(evt);

					// if there are 3 things in a row other than empty
					if (find3InARow()) {

						// player one gets a point and allow click is set to false, then it leaves the
						// method
						scoreOne.setText(Integer.toString((Integer.parseInt(scoreOne.getText()) + 1)));
						allowButtonClick = false;
						return;
					}

					// runs hardmode bot for O, and X
					hardModeBot("O");
					hardModeBot("X");

					// creates a loop variable
					int loop = 0;

					// while text has not been set, it will keep creating new positions to place the
					// O until one that is blank is created, or until it has looped through 10000
					// times, since then there would be no empty spots, and the game would be over
					while (!setText) {

						// loop gets 1 added to it
						loop++;

						// generates random numbers from 0-2 for the row and column
						int row = (int) (Math.random() * 3);
						int column = (int) (Math.random() * 3);

						// if there is a blank space at that position on the gameBoard, then it will set
						// that to O, and set firstplayer to true, and then exit the while loop
						if (gameBoardArr[row][column].getText() == "") {
							gameBoardArr[row][column].setText("O");
							gameLog[count] = "O " + gameBoardArr[row][column].getId().substring(1);
							count++;
							isFirstPlayer = true;
							break;
						}

						// if the loop has run 10000 times, it will break out of it
						if (loop > 10000) {
							break;
						}
					}

					// if there are 3 things in a row other than empty
					if (find3InARow()) {

						// player two gets a point, and allowbuttonclick is set to false
						scoreTwo.setText(Integer.toString((Integer.parseInt(scoreTwo.getText()) + 1)));
						allowButtonClick = false;
					}

				}
			}

			// if the user chose to play against another player
			else {

				// runs the onButtonClick method, which will place an X on the button that was
				// clicked by the user if it is player 1's turn, then if it is player 2's turn,
				// it will place an O on the spot that was clicked
				onButtonClick(evt);

				// if there are 3 things in a row other than empty
				if (find3InARow()) {

					// if the last player to go was player one, then player one gets a point
					if (isFirstPlayer == false) {
						scoreOne.setText(Integer.toString((Integer.parseInt(scoreOne.getText()) + 1)));
					}

					// if the last player to go was player two, then player two gets a point
					else if (isFirstPlayer) {
						scoreTwo.setText(Integer.toString((Integer.parseInt(scoreTwo.getText()) + 1)));
					}

					// allowbuttonclick gets set to false
					allowButtonClick = false;
				}
			}
		}
	}

	public void hardModeBot(String str) {
		if (!setText) {
			for (int j = 0; j < 3; j++) {

				// isfirstplayer is set to true, since after this it will be the first player's
				// turn
				isFirstPlayer = true;

				// creates strings to hold the texts from the 3 buttons in that row
				String a = gameBoardArr[j][0].getText();
				String b = gameBoardArr[j][1].getText();
				String c = gameBoardArr[j][2].getText();

				// if any of the two buttons in that row are the same
				if (a.equals(b) || a.equals(c) || b.equals(c)) {

					// creates and resets values of tally, and index integers
					int tally = 0;
					int index = 69;

					// for the amount of buttons in that row
					for (int k = 0; k < 3; k++) {

						// if there is an O, in that position, then 1 gets added to tally
						if (gameBoardArr[j][k].getText().equals(str)) {
							tally++;
						}

						// if there is a blank space in that spot, then index gets set to k
						else if (gameBoardArr[j][k].getText().equals("")) {
							index = k;
						}
					}

					// if the tally equals 2, meaning that there are 2 O's, or X's in that row, and
					// the
					// index is not 69, meaning that there is an empty space, then it will place an
					// O on that empty spot, and then it will have 3 in a row and win
					if (tally == 2 && index != 69) {

						// if the spot is a blank space
						if (gameBoardArr[j][index].getText().equals("")) {

							// places an O on that button, sets setText to true, since a text was set, then
							// breaks out of the for loop
							gameBoardArr[j][index].setText("O");
							setText = true;
							gameLog[count] = "O " + gameBoardArr[j][index].getId().substring(1);
							count++;
							break;
						}
					}
				}

				// creates strings to hold the texts from the 3 buttons in that column
				a = gameBoardArr[0][j].getText();
				b = gameBoardArr[1][j].getText();
				c = gameBoardArr[2][j].getText();

				// if any of the two buttons in that column are the same
				if (a.equals(b) || a.equals(c) || b.equals(c)) {

					// creates and resets values of tally, and index integers
					int tally = 0;
					int index = 69;

					// for the amount of buttons in that column
					for (int k = 0; k < 3; k++) {

						// if there is an O, or X in that position, then 1 gets added to tally
						if (gameBoardArr[k][j].getText().equals(str)) {
							tally++;
						}

						// if there is a blank space in that spot, then index gets set to k
						else if (gameBoardArr[k][j].getText().equals("")) {
							index = k;
						}
					}

					// if the tally equals 2, meaning that there are 2 O's, or 2 X's in that column,
					// and the
					// index is not 69, meaning that there is an empty space, then it will place an
					// O on that empty spot, and then it will have 3 in a row and win
					if (tally == 2 && index != 69) {

						// if the spot is a blank space
						if (gameBoardArr[index][j].getText().equals("")) {

							// places an O on that button, sets setText to true, since a text was set, then
							// breaks out of the for loop
							gameBoardArr[index][j].setText("O");
							setText = true;
							gameLog[count] = "O " + gameBoardArr[index][j].getId().substring(1);
							count++;
							break;
						}
					}
				}

				// creates strings to hold the texts from the 3 numbers in that diagonal
				a = gameBoardArr[0][0].getText();
				b = gameBoardArr[1][1].getText();
				c = gameBoardArr[2][2].getText();

				// if any of the two buttons in that diagonal are the same
				if (a.equals(b) || a.equals(c) || b.equals(c)) {

					// creates and resets values of tally, and index integers
					int tally = 0;
					int index = 69;

					// for the amount of buttons in that diagonal
					for (int k = 0; k < 3; k++) {

						// if there is an O, or X in that position, then 1 gets added to tally
						if (gameBoardArr[k][k].getText().equals(str)) {
							tally++;
						}

						// if there is a blank space in that spot, then index gets set to k
						else if (gameBoardArr[k][k].getText().equals("")) {
							index = k;
						}
					}

					// if the tally equals 2, meaning that there are 2 O's, or 2 X's in that
					// diagonal, and the
					// index is not 69, meaning that there is an empty space, then it will place an
					// O on that empty spot, and then it will have 3 in a row and win
					if (tally == 2 && index != 69) {

						// if the spot is a blank space
						if (gameBoardArr[index][index].getText().equals("")) {

							// places an O on that button, sets setText to true, since a text was set, then
							// breaks out of the for loop
							gameBoardArr[index][index].setText("O");
							setText = true;
							gameLog[count] = "O " + gameBoardArr[index][index].getId().substring(1);
							count++;
							break;
						}
					}
				}

				// creates strings to hold the texts from the 3 numbers in that diagonal
				a = gameBoardArr[2][0].getText();
				b = gameBoardArr[1][1].getText();
				c = gameBoardArr[0][2].getText();

				// if any of the two buttons in that diagonal are the same
				if (a.equals(b) || a.equals(c) || b.equals(c)) {

					// creates and resets values of tally, and index integers
					int tally = 0;
					int index = 69;

					// for the amount of buttons in that diagonal
					for (int k = 0; k < 3; k++) {

						// if there is an O, or X in that position, then 1 gets added to tally
						if (gameBoardArr[2 - k][k].getText().equals(str)) {
							tally++;
						}

						// if there is a blank space in that spot, then index gets set to k
						else if (gameBoardArr[2 - k][k].getText().equals("")) {
							index = k;
						}
					}

					// if the tally equals 2, meaning that there are 2 O's, or 2 X's in that
					// diagonal, and the
					// index is not 69, meaning that there is an empty space, then it will place an
					// O on that empty spot, and then it will have 3 in a row and win
					if (tally == 2 && index != 69) {

						// if the spot is a blank space
						if (gameBoardArr[2 - index][index].getText().equals("")) {

							// places an O on that button, sets setText to true, since a text was set, then
							// breaks out of the for loop
							gameBoardArr[2 - index][index].setText("O");
							setText = true;
							gameLog[count] = "O " + gameBoardArr[2 - index][index].getId().substring(1);
							count++;
							break;
						}
					}
				}
			}
		}
	}

	// method that gets the button that the user has clicked, and places an X, or O
	// on it depending if they are the first or second player
	public void onButtonClick(ActionEvent evt) {

		// gets the button that was clicked, the text on that button, as well as the ID
		// of the button
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();
		String buttonID = clickedButton.getId().substring(1);

		// if there is nothing on the button, and it is the first player's turn, then
		// the button label gets set to X, and isfirstplayer gets set to false
		if ("".equals(buttonLabel) && isFirstPlayer) {
			clickedButton.setText("X");
			gameBoardArr[Character.getNumericValue(buttonID.charAt(0))][Character
					.getNumericValue(buttonID.charAt(1))] = clickedButton;
			isFirstPlayer = false;
			gameLog[count] = "X " + buttonID;
			count++;
		}

		// if there is nothing on the button, and it is not the first player's turn,
		// then the button label gets set to O, and isfirstplayer gets set to true
		else if ("".equals(buttonLabel) && !isFirstPlayer) {
			clickedButton.setText("O");
			gameBoardArr[Character.getNumericValue(buttonID.charAt(0))][Character
					.getNumericValue(buttonID.charAt(1))] = clickedButton;
			isFirstPlayer = true;
			gameLog[count] = "O " + buttonID;
			count++;
		}
	}

	// method that resets the gameboard array to the values of the buttons
	public void resetGameBoard() {
		gameBoardArr[0][0] = b00;
		gameBoardArr[0][1] = b01;
		gameBoardArr[0][2] = b02;
		gameBoardArr[1][0] = b10;
		gameBoardArr[1][1] = b11;
		gameBoardArr[1][2] = b12;
		gameBoardArr[2][0] = b20;
		gameBoardArr[2][1] = b21;
		gameBoardArr[2][2] = b22;

	}

	// method that handles clicks in the menu
	public void menuClickHandler(ActionEvent evt) throws IOException, InterruptedException {

		// gets the label of the thing that was clicked in the menu
		MenuItem clickedMenu = (MenuItem) evt.getTarget();
		String menuLabel = clickedMenu.getText();

		// if the label was play, then it resets the gameBoardArr, as well as the
		// gameboard
		if ("Play".equals(menuLabel)) {

			// count gets reset
			count = 0;

			// gameLog gets reset
			gameLog = new String[9];

			// gameboardarr gets reset
			gameBoardArr = new Button[3][3];

			// buttonclick is set to true
			allowButtonClick = true;

			// gets the buttons and sets the text on all of them to blank, as well as
			// removes the winning-button style
			ObservableList<Node> buttons = gameBoard.getChildren();

			buttons.forEach(btn -> {
				((Button) btn).setText("");

				btn.getStyleClass().remove("winning-button");
			});

			// sets isfirstplayer to true
			isFirstPlayer = true;
		}

		// if the menu label that was clicked is quit, then it closes the program
		else if ("Quit".equals(menuLabel)) {
			Platform.exit();
		}

		// if the menu label that was clicked is how to play, then it opens the how to
		// play window
		else if ("How to Play".equals(menuLabel)) {
			openHowToPlayWindow();
		}

		// if the menu label that was clicked is about, then it opens the about window
		else if ("About".equals(menuLabel)) {
			openAboutWindow();
		}

		// if the menu label that was clicked is save, then it saves the game log
		else if ("Save".equals(menuLabel)) {

			//creates a string that stores the time, which will be the name of the file
			String time = dtf.format(now).toString() + ".txt";

			// creates printWriter
			PrintWriter writer = new PrintWriter(new FileWriter(time));

			//for the amount of buttons, if stores each of them in a new line on the file
			for (int i = 0; i < 9; i++) {
				writer.println(gameLog[i]);
			}
			writer.close();
			openSaveWindow();
		}

		// if the menu label that was clicked is Load game,
		else if ("Load Game".equals(menuLabel)) {

			// gets the buttons and sets the text on all of them to blank, as well as
			// removes the winning-button style
			ObservableList<Node> buttons = gameBoard.getChildren();

			buttons.forEach(btn -> {
				((Button) btn).setText("");

				btn.getStyleClass().remove("winning-button");
			});
			resetGameBoard();

			// creates fileGameBoard array
			String fileGameBoard[] = new String[9];

			// creates string address
			String address = "";

			// for file choosing, creates file directory to select the file from
			JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
			int returnValue = jfc.showOpenDialog(null);

			// if approved file
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				address = selectedFile.getAbsolutePath();
			}

			// creates bufferedreader which reads stuff from a file
			BufferedReader input = new BufferedReader(new FileReader(address));

			// for the amount of buttons on the gameboard, stores them to the filegameboard
			// array
			for (int x = 0; x < 9; x++) {
				fileGameBoard[x] = input.readLine();
			}

			// closes input
			input.close();

			// for the amount of buttons on the gameboard, sets the buttons to what they are
			// in filegameboard
			for (int x = 0; x < 9; x++) {

				// tries to put all the values onto the gameboard, if there are any empty ones,
				// it just ignores them
				try {
					gameBoardArr[Character.getNumericValue(fileGameBoard[x].charAt(2))][Character
							.getNumericValue(fileGameBoard[x].charAt(3))]
									.setText(Character.toString(fileGameBoard[x].charAt(0)));
					find3InARow();
				} catch (Exception e) {
				}
			}

		}
	}

	// method that opens the how to play window
	private void openHowToPlayWindow() {
		try {
			// load the pop up
			Pane howTo = (Pane) FXMLLoader.load(getClass().getResource("HowToPlayMenu.fxml"));

			// create a new scene
			Scene howToScene = new Scene(howTo, 260, 300);

			// create new stage to put scene in
			secondaryStage = new Stage();
			secondaryStage.setScene(howToScene);
			secondaryStage.setResizable(false);
			secondaryStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method that opens the about menu
	private void openAboutWindow() {
		try {
			// load the pop up
			Pane about = (Pane) FXMLLoader.load(getClass().getResource("AboutMenu.fxml"));

			// create a new scene
			Scene aboutScene = new Scene(about, 260, 300);

			// create new stage to put scene in
			secondaryStage = new Stage();
			secondaryStage.setScene(aboutScene);
			secondaryStage.setResizable(false);
			secondaryStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method that opens the save window
	private void openSaveWindow() {
		try {
			// load the pop up
			Pane save = (Pane) FXMLLoader.load(getClass().getResource("SaveScreen.fxml"));

			// create a new scene
			Scene saveScene = new Scene(save, 150, 150);

			// create new stage to put scene in
			secondaryStage = new Stage();
			secondaryStage.setScene(saveScene);
			secondaryStage.setResizable(false);
			secondaryStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// checks if the 3 labels on the buttons are equal or not
	private boolean check3AreEqual(Button a, Button b, Button c) {

		// tries to compare the 3 different labels, catches nullpointer exception if any
		// button is empty, if 3 labels are the same it returns true, if not, then
		// returns false
		try {
			if (a.getText().equals(b.getText()) && b.getText().equals(c.getText())) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

	// method that finds 3 in a row
	private boolean find3InARow() {

		// for the amount of rows and columns in each row and column
		for (int i = 0; i < 3; i++) {

			// if the 3 elements in the column are the same, if they are, they get
			// highlighted, and true gets returned
			if (check3AreEqual(gameBoardArr[i][0], gameBoardArr[i][1], gameBoardArr[i][2])
					&& gameBoardArr[i][0].getText() != "") {
				highlightWinningCombo(gameBoardArr[i][0], gameBoardArr[i][1], gameBoardArr[i][2]);
				return true;
			}

			// if the 3 elements in the row are the same, if they are, they get highlighted,
			// and true gets returned
			else if (check3AreEqual(gameBoardArr[0][i], gameBoardArr[1][i], gameBoardArr[2][i])
					&& gameBoardArr[0][i].getText() != "") {
				highlightWinningCombo(gameBoardArr[0][i], gameBoardArr[1][i], gameBoardArr[2][i]);
				return true;
			}
		}

		// if the middle button on the gameboard is empty, then false gets returned
		if (gameBoardArr[1][1].getText() == "") {
			return false;
		}

		// if the 3 elements in the first diagonal are the same, if they are, they get
		// highlighted, and true gets returned
		if (check3AreEqual(gameBoardArr[0][0], gameBoardArr[1][1], gameBoardArr[2][2])) {
			highlightWinningCombo(gameBoardArr[0][0], gameBoardArr[1][1], gameBoardArr[2][2]);
			return true;
		}

		// else if the 3 elements in the second diagonal are the same, if they are, they
		// get highlighted, and true gets returned
		else if (check3AreEqual(gameBoardArr[0][2], gameBoardArr[1][1], gameBoardArr[2][0])) {
			highlightWinningCombo(gameBoardArr[0][2], gameBoardArr[1][1], gameBoardArr[2][0]);
			return true;
		}
		return false;
	}

	// method that highlights the 3 squares that won
	private void highlightWinningCombo(Button first, Button second, Button third) {

		// adds winning button style to the 3 buttons
		first.getStyleClass().add("winning-button");
		second.getStyleClass().add("winning-button");
		third.getStyleClass().add("winning-button");

	}
}
