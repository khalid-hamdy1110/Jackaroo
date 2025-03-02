package model.card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.card.standard.*;
import model.card.wild.*;
import engine.GameManager;
import engine.board.BoardManager;

public class Deck {
	private static final String CARDS_FILE = "Cards.csv";
	private static ArrayList<Card> cardsPool;
	
	public static void loadCardPool(BoardManager boardManager, GameManager gameManager) 
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE)); 
		String row; // To store the line read from the CSV file
		
		while ((row = br.readLine()) != null) { // Reads a line and stores it and checks if it is null
            String[] values = row.split(","); // Splitting the values into an array of Strings
            
            Card c;
            
            switch(values[0]) { // Choosing the type of card
            case "1":
            	c = new Ace(values[2], values[3], convertSuit(values[5]), boardManager, gameManager);
            case "13":
            	c = new King(values[2], values[3], convertSuit(values[5]), boardManager, gameManager);
            case "12":
            	c = new Queen(values[2], values[3], convertSuit(values[5]), boardManager, gameManager);
			case "10":
        	c = new Ten(values[2], values[3], convertSuit(values[5]), boardManager, gameManager);
			case "7":
            	c = new Seven(values[2], values[3], convertSuit(values[5]), boardManager, gameManager);
			case "5":
            	c = new Five(values[2], values[3], convertSuit(values[5]), boardManager, gameManager);
			case "4":
            	c = new Four(values[2], values[3], convertSuit(values[5]), boardManager, gameManager);
			case "14":
            	c = new Burner(values[2], values[3], boardManager, gameManager);
			case "15":
            	c = new Saver(values[2], values[3], boardManager, gameManager);
            default:
            	c = new Standard(values[2], values[3], Integer.parseInt(values[4]), convertSuit(values[5]), boardManager, gameManager);
           }
            
           for(int i = 0; i<Integer.parseInt(values[1]); i++) {
        	   cardsPool.add(c);
           }
        }
	}
	
	public static ArrayList<Card> drawCards() {
		ArrayList<Card> newPool = new ArrayList<Card>();
		return newPool;
	}
	
	public static Suit convertSuit(String s) {
		switch(s) {
		case "HEART":
			return Suit.HEART;
		case "DIAMOND":
			return Suit.DIAMOND;
		case "SPADE":
			return Suit.SPADE;
		default:
			return Suit.CLUB;
		}
	}
}
