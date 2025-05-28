package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import engine.Game;
import engine.board.Board;
import engine.board.Cell;
import engine.board.SafeZone;
import exception.GameException;
import exception.SplitOutOfRangeException;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;
import model.card.standard.Ace;
import model.card.standard.King;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.player.Marble;
import model.player.Player;

public class MainController {
	@FXML private AnchorPane anchorPane;
	@FXML private Label playerName;
	@FXML private Label cpuName1;
	@FXML private Label cpuName2;
	@FXML private Label cpuName3;
	@FXML private Circle playerColour;
	@FXML private Circle cpuColour1;
	@FXML private Circle cpuColour2;
	@FXML private Circle cpuColour3;
	@FXML private GridPane boardGrid;
	@FXML private HBox handBox0;
	@FXML private HBox handBox1;
	@FXML private HBox handBox2;
	@FXML private HBox handBox3;
	@FXML private StackPane firepitCard;
	@FXML private Button playTurnBtn;
	@FXML private TextField splitDistance;
	@FXML private Button setSplitBtn;
	@FXML private Label setSplitLabel;
	@FXML private Pane alertPane;
	@FXML private Label alertLabel;
	@FXML private Button alertOkBtn;
	@FXML private Pane errPane;
	@FXML private Label errLabel;
	@FXML private TextField waitTime;
	@FXML private Button waitTimeBtn;
	@FXML private Label waitTimeLabel;
	
	private double CPU_WAIT_TIME = 5;
	private List<StackPane> trackCells; // UI track cells
	private ArrayList<List<StackPane>> safezones; // UI safezones
	private ArrayList<List<StackPane>> homezones; // UI homezones
	private ArrayList<List<StackPane>> playerCards; // UI player cards
	private ArrayList<List<Circle>> playerMarbles; // UI player marbles
	private Game game; // Game Engine
	private ArrayList<Player> players; // Players from game
	private String originalPlayerStyle; // original style of player name text
	private StackPane selectedCard; // UI Selected Card
	private ArrayList<Marble> selectedMarbles; // Game Selected Marbles
	private List<Circle> selectedMarblesUI; // UI Selected Marbles
	
	public void initGame(String username) throws IOException {
		game = new Game(username);
		
		// Getting player names
		players = game.getPlayers();
		
		// Initializing Player names
		playerName.setText(players.get(0).getName());
		cpuName1.setText(players.get(1).getName());
		cpuName2.setText(players.get(2).getName());
		cpuName3.setText(players.get(3).getName());
		
		// Initializing Player Colours
		playerColour.setFill(getColour(players.get(0).getColour()));
		cpuColour1.setFill(getColour(players.get(1).getColour()));
		cpuColour2.setFill(getColour(players.get(2).getColour()));
		cpuColour3.setFill(getColour(players.get(3).getColour()));
		
		// Initializing selected marbles
		selectedMarbles = new ArrayList<Marble>();
		selectedMarblesUI = new ArrayList<Circle>();
		
		// Removing the split distance prompt
		splitDistance.setVisible(false);
		setSplitBtn.setVisible(false);
		setSplitLabel.setVisible(false);
		splitDistance.setText(game.getBoard().getSplitDistance()+"");
		
		// Removing the split distance prompt
		waitTimeLabel.setVisible(false);
		waitTime.setText(CPU_WAIT_TIME+"");
		
		
		// Hiding the alert pane
		alertOkBtn.setVisible(false);
		alertPane.setVisible(false);
		errPane.setVisible(false);
		
		// Getting the trackcells, safezones and homezones by extracting them from the xfml using their styleClass
		trackCells = boardGrid.getChildren().stream()
				.filter(n -> n instanceof StackPane && n.getStyleClass().contains("track-cell"))
				.map(n -> (StackPane)n)
				.collect(Collectors.toList());

		// Sorts into intended order for easier access
		trackCells.sort(Comparator.comparingInt(p -> {
			String id = p.getId(); 
			return Integer.parseInt(id.replace("track",""));
		}));
		
		safezones = new ArrayList<List<StackPane>>(4);
		homezones = new ArrayList<List<StackPane>>(4);
		playerCards = new ArrayList<List<StackPane>>(4);
		playerMarbles = new ArrayList<List<Circle>>(4);
		for (int i = 0; i<4; i++) {
			safezones.add(null);
			homezones.add(null);
			playerCards.add(null);
			playerMarbles.add(null);
			
			String safezoneName = "safezone" + i;
			safezones.set(i, boardGrid.getChildren().stream()
					.filter(n -> n instanceof StackPane && n.getStyleClass().contains(safezoneName))
					.map(n -> (StackPane)n)
					.collect(Collectors.toList()));

			String safezoneName2 = "safezone" + (i+1);
			safezones.get(i).sort(Comparator.comparingInt(p -> {
				String id = p.getId(); 
				return Integer.parseInt(id.replace(safezoneName2,""));
			}));
			
			String homezoneName = "homezone" + i;
			homezones.set(i, boardGrid.getChildren().stream()
					.filter(n -> n instanceof StackPane && n.getStyleClass().contains(homezoneName))
					.map(n -> (StackPane)n)
					.collect(Collectors.toList()));
			
			String homezoneName2 = "homezone" + (i+1);
			homezones.get(i).sort(Comparator.comparingInt(p -> {
				String id = p.getId(); 
				return Integer.parseInt(id.replace(homezoneName2,""));
			}));
			
			HBox handBox;
			switch(i) {
			case 0: handBox = handBox0; break;
			case 1: handBox = handBox1; break;
			case 2: handBox = handBox2; break;
			case 3: handBox = handBox3; break;
			default: handBox = null; break;
			}
			
			// Getting all player cards
			String playerCardsName = "cards" + i;
			playerCards.set(i, handBox.getChildren().stream()
					.filter(n -> n instanceof StackPane && n.getStyleClass().contains(playerCardsName))
					.map(n -> (StackPane)n)
					.collect(Collectors.toList()));

			String playerCardsName2 = "card" + i;
			// Sorts into intended order for easier access
			playerCards.get(i).sort(Comparator.comparingInt(p -> {
				String id = p.getId(); 
				return Integer.parseInt(id.replace(playerCardsName2,""));
			}));
			
			// Getting the all player marbles
			List<StackPane> homePanes = homezones.get(i);
			
			playerMarbles.set(i, homePanes.stream()
					.map(n -> (Circle)n.getChildren().get(0))
					.collect(Collectors.toList()));

			String marbleName = "marble" + i;
			playerMarbles.get(i).sort(Comparator.comparingInt(p -> {
				String id = p.getId(); 
				return Integer.parseInt(id.replace(marbleName,""));
			}));
		}
		
		// Updating the current and next players
		originalPlayerStyle = playerName.getStyle();
		updateAll();
	}
	
	private void showError(String msg) {
		alertOkBtn.requestFocus();
		alertOkBtn.setVisible(true);
		errLabel.setText(msg);
		errPane.setVisible(true);
	}
	
	public void cardSelection(MouseEvent e) throws InterruptedException {
		String selectedStyle =  "-fx-effect: dropshadow( gaussian , gold , 15 , 0.5 , 0 , 0 ); -fx-translate-y: -15px;";
		StackPane curSelectedCard = (StackPane)(e.getSource());
		
		// Selecting card
		if (selectedCard != curSelectedCard) {
			for (StackPane card : playerCards.get(0)) {
				card.setStyle("");
			}
			
			curSelectedCard.setStyle(selectedStyle);
			selectedCard = curSelectedCard;
			
			if (((Label)(((VBox)(curSelectedCard.getChildren().get(2))).getChildren().get(0))).getText().equals("7")) {
				splitDistance.setVisible(true);
				setSplitBtn.setVisible(true);
				splitDistance.setText(game.getBoard().getSplitDistance()+"");
			} else {
				splitDistance.setVisible(false);
				setSplitBtn.setVisible(false);
				setSplitLabel.setVisible(false);
				splitDistance.setText(game.getBoard().getSplitDistance()+"");
			}
		} else { // Deselecting card
			for (StackPane card : playerCards.get(0)) {
				card.setStyle("");
			}
			selectedCard = null;
			splitDistance.setVisible(false);
			setSplitBtn.setVisible(false);
			setSplitLabel.setVisible(false);
			splitDistance.setText(game.getBoard().getSplitDistance()+"");
		}
	}
	
	public void selectMarble(MouseEvent e) {
		String selectedStyle =  "-fx-effect: dropshadow( gaussian , gold , 15 , 0.5 , 0 , 0 );";
		Circle curSelectedMarble = (Circle)(e.getSource());
		Marble modelMarble = (Marble) curSelectedMarble.getUserData();
		
		// First checking if the marble is inside the selected marbles list
		Boolean inSelected = selectedMarbles.contains(modelMarble);
		
		if (inSelected) {
			selectedMarbles.remove(modelMarble);
			selectedMarblesUI.remove(curSelectedMarble);
			curSelectedMarble.setStyle("");
		} else {
			selectedMarbles.add(modelMarble);
			selectedMarblesUI.add(curSelectedMarble);
			curSelectedMarble.setStyle(selectedStyle);
		}
	}
	
	public Color getColour(Colour playerColour) {
		if (playerColour == Colour.RED)
			return Color.RED;
		else if (playerColour == Colour.BLUE)
			return Color.BLUE;
		else if (playerColour == Colour.GREEN)
			return Color.GREEN;
		else if (playerColour == Colour.YELLOW)
			return Color.YELLOW;
		return Color.BLACK;
	}
	
	public void updateCurrentNextPlayer() {

		// Resetting the player names colour
		playerName.setStyle(originalPlayerStyle);
		cpuName1.setStyle(originalPlayerStyle);
		cpuName2.setStyle(originalPlayerStyle);
		cpuName3.setStyle(originalPlayerStyle);
		
		String activePlayerStyle = "-fx-border-color: gold; " + "-fx-border-width: 4; " + "-fx-border-radius: 5; " + "-fx-padding: 2;";
		String nextPlayerStyle = "-fx-border-color: gold; " + "-fx-border-width: 2; " + "-fx-border-radius: 5; " + "-fx-padding: 2; " + "-fx-opacity: 0.5;";;
		
		// Finds the active player and next player and sets a style to them
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getColour() == game.getActivePlayerColour()) {
				switch(i) {
				case 0: playerName.setStyle(activePlayerStyle); break;
				case 1: cpuName1.setStyle(activePlayerStyle); break;
				case 2: cpuName2.setStyle(activePlayerStyle); break;
				case 3: cpuName3.setStyle(activePlayerStyle); break;
				}
			}
			if (players.get(i).getColour() == game.getNextPlayerColour()) {
				switch(i) {
				case 0: playerName.setStyle(nextPlayerStyle); break;
				case 1: cpuName1.setStyle(nextPlayerStyle); break;
				case 2: cpuName2.setStyle(nextPlayerStyle); break;
				case 3: cpuName3.setStyle(nextPlayerStyle); break;
				}
			}
		}
		
	}
	
	public void updatePlayerHands() {
		
		// Going over all players to update their hands
		for (int i = 0; i < players.size(); i++) {
			ArrayList<Card> hand = game.getPlayers().get(i).getHand();
			List<StackPane> cardHolder = playerCards.get(i);
			
			// Removing all the cards from the board
			for (StackPane card : cardHolder) {
				card.setVisible(false);
			}
			
			// Setting the labels for the human player and after the update only the remaining cards get shown
			for (int j = 0; j < hand.size(); j++) {
				Card card = hand.get(j);
				StackPane cardUI = cardHolder.get(j);
				
				// Updating the labels  of each human card
				if (i == 0) {
					VBox cardLabelHolder = (VBox) cardUI.getChildren().get(2);
					
					
					((Label)(cardLabelHolder.getChildren().get(0))).setText(card.getName().charAt(0)+"");
					if (card instanceof Standard) {
						
						if (((Standard) card).getRank() >= 2 && ((Standard) card).getRank() <= 10) {
							((Label)(cardLabelHolder.getChildren().get(0))).setText(((Standard) card).getRank()+"");
						}
						
						if (((Standard) card).getSuit().equals(Suit.HEART)) 
							((Label)(cardLabelHolder.getChildren().get(2))).setText("❤");
						else if (((Standard) card).getSuit().equals(Suit.DIAMOND))  
							((Label)(cardLabelHolder.getChildren().get(2))).setText("♦");
						else if (((Standard) card).getSuit().equals(Suit.SPADE)) 
							((Label)(cardLabelHolder.getChildren().get(2))).setText("♠");
						else if (((Standard) card).getSuit().equals(Suit.CLUB)) 
							((Label)(cardLabelHolder.getChildren().get(2))).setText("♣");
					} else {
						((Label)(cardLabelHolder.getChildren().get(0))).setText(card.getName());
						((Label)(cardLabelHolder.getChildren().get(0))).setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 0 0 0 6;");
						
						if (card.getName().equals("MarbleBurner"))
							((Label)(cardLabelHolder.getChildren().get(2))).setText("🔥");
						else
							((Label)(cardLabelHolder.getChildren().get(2))).setText("💧");
					}
					
					((Label)(cardLabelHolder.getChildren().get(1))).setText(card.getDescription());
					
					cardColourChanger((StackPane)cardUI, card);
				}
				
				cardUI.setVisible(true);
			}
		}
	}
	
	public void updateFirepit() {
		ArrayList<Card> firepit = game.getFirePit();
		
		// Updating the firepit if its empty then no card is displayed
		if (firepit.size() == 0) {
			firepitCard.setVisible(false);
		} else if (firepit.getLast() != null){
			
			VBox cardLabelHolder = (VBox) firepitCard.getChildren().get(2);
			Card card = firepit.getLast();
			
			((Label)(cardLabelHolder.getChildren().get(0))).setText(card.getName().charAt(0)+"");
			if (card instanceof Standard) {
				
				if (((Standard) card).getRank() >= 2 && ((Standard) card).getRank() <= 10) {
					((Label)(cardLabelHolder.getChildren().get(0))).setText(((Standard) card).getRank()+"");
				}
				
				if (((Standard) card).getSuit().equals(Suit.HEART))  
					((Label)(cardLabelHolder.getChildren().get(2))).setText("❤");
				else if (((Standard) card).getSuit().equals(Suit.DIAMOND))  
					((Label)(cardLabelHolder.getChildren().get(2))).setText("♦");
				else if (((Standard) card).getSuit().equals(Suit.SPADE)) 
					((Label)(cardLabelHolder.getChildren().get(2))).setText("♠");
				else if (((Standard) card).getSuit().equals(Suit.CLUB)) 
					((Label)(cardLabelHolder.getChildren().get(2))).setText("♣");
				
			} else {
				((Label)(cardLabelHolder.getChildren().get(0))).setText(card.getName());
				((Label)(cardLabelHolder.getChildren().get(0))).setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 0 0 0 6;");
				
				if (card.getName().equals("MarbleBurner"))
					((Label)(cardLabelHolder.getChildren().get(2))).setText("🔥");
				else
					((Label)(cardLabelHolder.getChildren().get(2))).setText("💧");
			}
			
			((Label)(cardLabelHolder.getChildren().get(1))).setText(card.getDescription());
			
			cardColourChanger(firepitCard, card);
			firepitCard.setVisible(true);
		}
	}
	
	// Updating the track, safezone and homezone cells on the board
	public void updateBoard() {
		// Clearing all marbles on the board
		for (StackPane cell : trackCells) {
			Circle circle = (Circle)cell.getChildren().get(0);
			circle.setVisible(false);
		}
		
		for (List<StackPane> safezone : safezones) {
			for (StackPane safezoneCell : safezone) {
				Circle circle = (Circle)safezoneCell.getChildren().get(0);
				circle.setVisible(false);
			}
		}
		
		for (List<StackPane> homezone : homezones) {
			for (StackPane homezoneCell : homezone) {
				Circle circle = (Circle)homezoneCell.getChildren().get(0);
				circle.setVisible(false);
			}
		}
		
		// Getting the running game's board and track
		Board board = game.getBoard();
		ArrayList<Cell> track = board.getTrack();
		
		// Updating the track
		for (int i = 0; i < track.size(); i++) {
			Marble marble = track.get(i).getMarble();
			
			if (marble != null) {
				StackPane cell = trackCells.get(i);
				Circle circle = (Circle) cell.getChildren().get(0);
				circle.setUserData(marble);
				circle.setFill(getColour(marble.getColour()));
				circle.setVisible(true);
			}
		}
		
		// Getting the board's safezones
		ArrayList<SafeZone> safezonesGame = board.getSafeZones();
		
		// Updating the safezones
		for (int i = 0; i < safezonesGame.size(); i++) {
			for (int j = 0; j < safezonesGame.get(i).getCells().size(); j++) {
				StackPane cell = safezones.get(i).get(j);
				Marble marble = safezonesGame.get(i).getCells().get(j).getMarble();
				
				if (marble != null) {
					Circle circle = (Circle) cell.getChildren().get(0);
					circle.setUserData(marble);
					circle.setFill(getColour(marble.getColour()));
					circle.setVisible(true);
				}
			}
		}
		
		// Getting the board's homezones
		ArrayList<ArrayList<Marble>> homezonesGame =  new ArrayList<ArrayList<Marble>>(4);
		homezonesGame.add(players.get(0).getMarbles());
		homezonesGame.add(players.get(1).getMarbles());
		homezonesGame.add(players.get(2).getMarbles());
		homezonesGame.add(players.get(3).getMarbles());
		
		// Updating the homezones
		for (int i = 0; i < homezonesGame.size(); i++) {
			for (int j = 0; j < homezonesGame.get(i).size(); j++) {
				StackPane cell = homezones.get(i).get(j);
				Marble marble = homezonesGame.get(i).get(j);
				
				if (marble != null) {
					Circle circle = (Circle) cell.getChildren().get(0);
					circle.setUserData(marble);
					circle.setFill(getColour(marble.getColour()));
					circle.setVisible(true);
				}
			}
		}
	}
	
	public void startTurn() {
		updateAll();
		
		if (game.checkWin() != null) 
			return;
		
		while (!game.canPlayTurn()) {
			game.endPlayerTurn();
			updateAll();
		}
		
		if (game.getActivePlayerColour() != players.get(0).getColour()) {
			// Disabling the play button
			playTurnBtn.setDisable(true);
			
			// Disabling the player cards
			for (StackPane card : playerCards.get(0))    
				card.setDisable(true);
			
			// Disabling the player Marbles
			for (StackPane cell : trackCells) {
				Circle circle = (Circle)cell.getChildren().get(0);
				circle.setDisable(true);
			}
			
			for (List<StackPane> safezone : safezones) {
				for (StackPane safezoneCell : safezone) {
					Circle circle = (Circle)safezoneCell.getChildren().get(0);
					circle.setDisable(true);
				}
			}
			
			PauseTransition pause = new PauseTransition(Duration.seconds(CPU_WAIT_TIME));
	    	pause.setOnFinished(evt -> playCPUTurn());
	    	pause.play();
		} else {
			// Enabling the play button
			playTurnBtn.setDisable(false);
			
			// Enabling the player cards
			for (StackPane card : playerCards.get(0))    
				card.setDisable(false);
			
			// Enabling the player marbles
			for (StackPane cell : trackCells) {
				Circle circle = (Circle)cell.getChildren().get(0);
				circle.setDisable(false);
			}
			
			for (List<StackPane> safezone : safezones) {
				for (StackPane safezoneCell : safezone) {
					Circle circle = (Circle)safezoneCell.getChildren().get(0);
					circle.setDisable(false);
				}
			}
		}
	}
	
	public void playTurn(ActionEvent e) {
		updateAll();
		Player currentPlayer = players.get(getActivePlayerFromColour(game.getActivePlayerColour()));
		
		// If not the players turn dont allow the input
		if (game.getActivePlayerColour() != players.get(0).getColour()) {
			showError("Not your turn!");
			return;
		}
		
		// If no card selected don't allow the play
		if (selectedCard == null) {
			showError("No card selected!");
			return;
		}
		
		try {
			
			// Finding the card in the game engine itself
			ArrayList<Card> currentPlayerHand = currentPlayer.getHand();
			Card curSelectedCard = null;
			for (int i = 0; i < currentPlayerHand.size(); i++) {
				if (i == Integer.parseInt(selectedCard.getId().substring(selectedCard.getId().length() - 1))) {
					curSelectedCard = currentPlayerHand.get(i);
				}
			}
			
			// Selecting the card in the engine
			game.selectCard(curSelectedCard);
			
			// Selecting all the marbles in the engine
			for (Marble curMarble : selectedMarbles) {
				game.selectMarble(curMarble);
			}
			
			// Playing the turn
			game.playPlayerTurn();
			updateAll();
			splitDistance.setVisible(false);
			setSplitBtn.setVisible(false);
			setSplitLabel.setVisible(false);
			splitDistance.setText(game.getBoard().getSplitDistance()+"");
			
			if (game.checkWin() != null) {
				alertLabel.setText("🎉 " + game.checkWin() + " won the game! 🎉");
				alertLabel.setTextFill(getColour(game.checkWin()));
				alertPane.setVisible(true);
				disableAllInputs();
			}
			
			if (game.getBoard().getTrap() != null) {
				alertLabel.setText(game.getBoard().getTrap()+ "'s marble landed on a trap! 💥💥💥");
				alertLabel.setTextFill(getColour(game.getBoard().getTrap()));
				alertPane.setVisible(true);
				PauseTransition pause = new PauseTransition(Duration.millis(2500));
		    	pause.setOnFinished(evt -> alertPane.setVisible(false));
		    	pause.play();
		    	game.getBoard().setTrap(null);
			}
			
			game.endPlayerTurn();	
		} catch (GameException err) {
			showError(err.getMessage());
			game.endPlayerTurn();
		}
		
		if (selectedCard != null) {
			selectedCard.setStyle("");
		}
		if (selectedMarblesUI != null) {
			for (Circle marble : selectedMarblesUI) {
				marble.setStyle("");
			}
		}
		
		selectedCard = null;
		selectedMarbles.clear();
		splitDistance.setVisible(false);
		setSplitBtn.setVisible(false);
		setSplitLabel.setVisible(false);
		splitDistance.setText(game.getBoard().getSplitDistance()+"");
		updateAll();
		startTurn();
	}
	
	public void playCPUTurn() {
		try {
			game.playPlayerTurn();
			updateAll();
			
			if (game.checkWin() != null) {
				alertLabel.setText("🎉 " + game.checkWin() + " won the game! 🎉");
				alertLabel.setTextFill(getColour(game.checkWin()));
				alertPane.setVisible(true);
				disableAllInputs();
			}
			
			if (game.getBoard().getTrap() != null) {
				alertLabel.setText(game.getBoard().getTrap() + "'s marble landed on a trap! 💥💥💥");
				alertLabel.setTextFill(getColour(game.getBoard().getTrap()));
				alertPane.setVisible(true);
				PauseTransition pause = new PauseTransition(Duration.millis(2500));
		    	pause.setOnFinished(evt -> alertPane.setVisible(false));
		    	pause.play();
		    	game.getBoard().setTrap(null);
			}
			
			game.endPlayerTurn();
		} catch (GameException err) {
			showError(err.getMessage());
			game.endPlayerTurn();
		}
		
		updateAll();
		startTurn();
	}
	
	public int getActivePlayerFromColour(Colour colour) {
		for (int i = 0; i < players.size(); i++) {
			if (colour == players.get(i).getColour()) {
				return i;
			}
		}
		return -1;
	}
	
	public void updateAll() {
		updateBoard();
		updatePlayerHands();
		updateFirepit();
		updateCurrentNextPlayer();
		
	}
	
	public void setSplitDistance(ActionEvent e) { 
		try {
			int distance = Integer.parseInt(splitDistance.getText());
			game.editSplitDistance(distance);
			setSplitLabel.setVisible(true);
			PauseTransition pause = new PauseTransition(Duration.millis(2000));
			pause.setOnFinished(event -> setSplitLabel.setVisible(false));
			pause.play();
		} catch (NumberFormatException err) {
			showError("Split Distance set is not an int.");
		} catch (SplitOutOfRangeException err) {
			showError(err.getMessage());
		}
	}
	
	public void disableAllInputs() {
		playTurnBtn.setDisable(true);
		
		for (StackPane card : playerCards.get(0))    
			card.setDisable(true);
		
		for (StackPane cell : trackCells) {
			Circle circle = (Circle)cell.getChildren().get(0);
			circle.setDisable(true);
		}
		
		for (List<StackPane> safezone : safezones) {
			for (StackPane safezoneCell : safezone) {
				Circle circle = (Circle)safezoneCell.getChildren().get(0);
				circle.setDisable(true);
			}
		}
		
		Stage stage = (Stage) anchorPane.getScene().getWindow();
		
		PauseTransition pause = new PauseTransition(Duration.millis(60000));
    	pause.setOnFinished(evt -> stage.close());
    	pause.play();
	}
	
	public void marbleFieldingShortcut(KeyEvent e) {
		if (e.getCode() == KeyCode.F && game.getActivePlayerColour() == players.get(0).getColour()) {
			for (Card card : players.get(0).getHand()) {
				if (card instanceof Ace || card instanceof King) {
					try {
						game.selectCard(card);
						
						game.playPlayerTurn();
						updateAll();
						game.endPlayerTurn();
					} catch (GameException err) {
						showError(err.getMessage());
						game.endPlayerTurn();
					}
					updateAll();
					startTurn();
					return;
				}
			}
			
			showError("No card available to use for fielding!");
		}
	}
	
	public void cardColourChanger(StackPane cardHolder, Card card) {
		Rectangle cardBorder = (Rectangle) cardHolder.getChildren().get(1);
		VBox cardLabels = (VBox) cardHolder.getChildren().get(2);
		
		if (card instanceof Standard) {
			Suit s = ((Standard) card).getSuit();
			
			Color c = (s == Suit.HEART || s == Suit.DIAMOND)? Color.web("#d20000") : Color.web("#000000");
			cardBorder.setStroke(c);
			((Label)cardLabels.getChildren().get(0)).setTextFill(c);
			((Label)cardLabels.getChildren().get(2)).setTextFill(c);
		} else {
			Color c = (card.getName().equals("MarbleBurner"))? Color.web("#ff5e23") : Color.web("#0701be");
			cardBorder.setStroke(c);
			((Label)cardLabels.getChildren().get(0)).setTextFill(c);
			((Label)cardLabels.getChildren().get(2)).setTextFill(c);
		}
	}
	
	public void alertOK(ActionEvent e) {
		alertOkBtn.setVisible(false);
		errPane.setVisible(false);
	}
	
	public void setCPUWait(ActionEvent e) {
		try {
			double time = Double.parseDouble(waitTime.getText());
			if (time >= 0 && time <= 10) {
				CPU_WAIT_TIME = time;
				waitTime.setText(CPU_WAIT_TIME+"");
				waitTimeLabel.setVisible(true);
				PauseTransition pause = new PauseTransition(Duration.millis(2000));
				pause.setOnFinished(event -> waitTimeLabel.setVisible(false));
				pause.play();
			} else {
				showError("Not a valid number for time! 0-10s");
				waitTime.setText(CPU_WAIT_TIME+"");
			}
		} catch (NumberFormatException err) {
			showError("Not a valid number for time! 0-10s");
			waitTime.setText(CPU_WAIT_TIME+"");
		}
	}
}